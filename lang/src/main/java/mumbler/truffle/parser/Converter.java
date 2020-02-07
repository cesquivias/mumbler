package mumbler.truffle.parser;

import static mumbler.truffle.parser.MumblerReadException.throwReaderException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import mumbler.truffle.MumblerLanguage;
import org.antlr.v4.runtime.misc.Pair;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.SourceSection;

import mumbler.truffle.MumblerContext;
import mumbler.truffle.MumblerException;
import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.call.InvokeNode;
import mumbler.truffle.node.call.TCOInvokeNode;
import mumbler.truffle.node.literal.BigIntegerNode;
import mumbler.truffle.node.literal.BooleanNode;
import mumbler.truffle.node.literal.LiteralListNode;
import mumbler.truffle.node.literal.LiteralSymbolNode;
import mumbler.truffle.node.literal.LongNode;
import mumbler.truffle.node.literal.StringNode;
import mumbler.truffle.node.read.ClosureSymbolNodeGen;
import mumbler.truffle.node.read.GlobalSymbolNodeGen;
import mumbler.truffle.node.read.LocalSymbolNodeGen;
import mumbler.truffle.node.read.SymbolNode;
import mumbler.truffle.node.special.DefineNode;
import mumbler.truffle.node.special.DefineNodeGen;
import mumbler.truffle.node.special.IfNode;
import mumbler.truffle.node.special.LambdaNode;
import mumbler.truffle.node.special.LambdaNodeGen;
import mumbler.truffle.node.special.LoopNode;
import mumbler.truffle.node.special.QuoteNode;
import mumbler.truffle.node.special.QuoteNode.QuoteKind;
import mumbler.truffle.node.special.QuoteNodeGen;
import mumbler.truffle.syntax.BigIntegerSyntax;
import mumbler.truffle.syntax.BooleanSyntax;
import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.syntax.LongSyntax;
import mumbler.truffle.syntax.StringSyntax;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

public class Converter {
    private MumblerContext context;
    private Analyzer analyzer;
    private final boolean isTailCallOptimizationEnabled;
    private final MumblerLanguage lang;

    public Converter(MumblerLanguage lang, boolean tailCallOptimizationEnabled) {
        this.lang = lang;
        this.isTailCallOptimizationEnabled = tailCallOptimizationEnabled;
    }

    public MumblerNode[] convertSexp(MumblerContext context, ListSyntax sexp) {
        this.context = context;
        Namespace fileNamespace = new Namespace(Namespace.TOP_NS,
                this.context.getGlobalNamespace());
        this.analyzer = new Analyzer(fileNamespace);
        this.analyzer.walk(sexp);

        return StreamSupport.stream(sexp.getValue().spliterator(), false)
                .map(obj -> this.convert(obj, fileNamespace))
                .toArray(MumblerNode[]::new);
    }

    public MumblerNode convert(Syntax<?> syntax, Namespace ns) {
        if (syntax instanceof LongSyntax) {
            return convert((LongSyntax) syntax);
        } else if (syntax instanceof BigIntegerSyntax) {
            return convert((BigIntegerSyntax) syntax);
        } else if (syntax instanceof BooleanSyntax) {
            return convert((BooleanSyntax) syntax);
        } else if (syntax instanceof StringSyntax) {
            return convert((StringSyntax) syntax);
        } else if (syntax instanceof SymbolSyntax) {
            return convert((SymbolSyntax) syntax, ns);
        } else if (syntax instanceof ListSyntax) {
            return convert((ListSyntax) syntax, ns);
        } else {
            throw new MumblerException("Unknown type: " + syntax.getClass());
        }
    }

    public static LongNode convert(LongSyntax number) {
        return new LongNode(number);
    }

    public static BigIntegerNode convert(BigIntegerSyntax number) {
        return new BigIntegerNode(number);
    }

    public static BooleanNode convert(BooleanSyntax bool) {
        return new BooleanNode(bool);
    }

    public static StringNode convert(StringSyntax str) {
        return new StringNode(str);
    }

    public SymbolNode convert(SymbolSyntax syntax, Namespace ns) {
    	SymbolNode node;
    	MumblerSymbol sym = syntax.getValue();
        Pair<Integer, FrameSlot> pair = ns.getIdentifier(sym.name);
        if (pair.a == Namespace.LEVEL_UNDEFINED) {
            throwReaderException(sym.name + " undefined", syntax, ns);
            return null;
        } else if (pair.a == 0) {
            node = LocalSymbolNodeGen.create(pair.b);
        } else if (pair.a == Namespace.LEVEL_GLOBAL) {
            node = GlobalSymbolNodeGen.create(pair.b, this.context.getGlobalFrame());
        } else {
            node = ClosureSymbolNodeGen.create(pair.b, pair.a);
        }
        node.setSourceSection(syntax.getSourceSection());
        return node;
    }

    public MumblerNode convert(ListSyntax syntax, Namespace ns) {
        MumblerList<? extends Syntax<?>> list = syntax.getValue();
        if (list == MumblerList.EMPTY || list.size() == 0) {
            return new LiteralListNode(MumblerList.EMPTY);
        }

        Syntax<?> car = list.car();
        if (car instanceof SymbolSyntax) {
            MumblerSymbol sym = ((SymbolSyntax) car).getValue();
            switch (sym.name) {
            case "define":
                return convertDefine(syntax, ns);
            case "lambda":
                return convertLambda(syntax, ns);
            case "if":
                return convertIf(syntax, ns);
            case "quote":
                return convertQuote(syntax, ns);
            case "loop":
                return convertLoop(syntax, ns);
            }
        }
        return convertInvoke(list, syntax.getSourceSection(), ns);
    }

    private InvokeNode convertInvoke(MumblerList<? extends Syntax<?>> list,
            SourceSection sourceSection, Namespace ns) {

        MumblerNode functionNode = convert(list.car(), ns);
        MumblerNode[] arguments = StreamSupport
                .stream(list.cdr().spliterator(), false)
                .map(syn-> convert(syn, ns))
                .toArray(MumblerNode[]::new);
        if (isTailCallOptimizationEnabled) {
            return new TCOInvokeNode(functionNode, arguments, sourceSection);
        } else {
            return new InvokeNode(functionNode, arguments, sourceSection);
        }
    }

    private DefineNode convertDefine(ListSyntax syntax, Namespace ns) {
        MumblerList<? extends Syntax<?>> list = syntax.getValue();
        SymbolSyntax symSyntax = (SymbolSyntax) list.cdr().car();
        FrameSlot nameSlot = ns.getIdentifier(symSyntax.getValue().name).b;
        MumblerNode valueNode = convert(list.cdr().cdr().car(), ns);
        DefineNode node = DefineNodeGen.create(valueNode, nameSlot);
        node.setSourceSection(syntax.getSourceSection());
        if (valueNode instanceof LambdaNode) {
            // TODO : not good enough. if there's an error in the lambda,
            // the name won't be used. Have to pass name
            LambdaNode lambda = (LambdaNode) valueNode;
            lambda.setName(nameSlot.toString());
        }
        return node;
    }

    @SuppressWarnings("unchecked")
    private LambdaNode convertLambda(ListSyntax syntax, Namespace ns) {
    	MumblerList<? extends Syntax<?>> list = syntax.getValue();
        Namespace lambdaNs = this.analyzer.getNamespace(syntax);
        List<FrameSlot> formalParameters = new ArrayList<>();
        ListSyntax argsSyntax = (ListSyntax) list.cdr().car();
        for (SymbolSyntax arg : (MumblerList<SymbolSyntax>) argsSyntax.getValue()) {
            formalParameters.add(convert(arg, lambdaNs).getSlot());
        }
        List<MumblerNode> bodyNodes = new ArrayList<>();
        for (Syntax<?> body : list.cdr().cdr()) {
            bodyNodes.add(convert(body, lambdaNs));
        }

        bodyNodes.get(bodyNodes.size() - 1).setIsTail();

        MumblerFunction function = MumblerFunction.create(
                lang,
                formalParameters.toArray(new FrameSlot[] {}),
                bodyNodes.toArray(new MumblerNode[] {}),
                lambdaNs.getFrameDescriptor());
        LambdaNode node = LambdaNodeGen.create(function);
        node.setSourceSection(syntax.getSourceSection());
        return node;
    }

    private IfNode convertIf(ListSyntax syntax, Namespace ns) {
    	MumblerList<? extends Syntax<?>> list = syntax.getValue();
        return new IfNode(convert(list.cdr().car(), ns),
                convert(list.cdr().cdr().car(), ns),
                convert(list.cdr().cdr().cdr().car(), ns),
                syntax.getSourceSection());
    }

    private static QuoteNode convertQuote(ListSyntax syntax,
            Namespace ns) {
        MumblerList<? extends Syntax<?>> list = syntax.getValue();
        Syntax<?> value = list.cdr().car();
        MumblerNode node;
        QuoteKind kind;
        if (value instanceof LongSyntax) {
            kind = QuoteKind.LONG;
            node = convert((LongSyntax) value);
        } else if (value instanceof BooleanSyntax) {
            kind = QuoteKind.BOOLEAN;
            node = convert((BooleanSyntax) value);
        } else if (value instanceof StringSyntax) {
            kind = QuoteKind.STRING;
            node = new StringNode((StringSyntax) value);
        } else if (value instanceof SymbolSyntax) {
            kind = QuoteKind.SYMBOL;
            node = new LiteralSymbolNode((SymbolSyntax) value);
        } else if (value instanceof ListSyntax) {
            kind = QuoteKind.LIST;
            node = new LiteralListNode(((MumblerList<?>) value.strip()));
        } else {
            throw new MumblerException("Unknown quote type: " +
                    value.getClass());
        }
        return QuoteNodeGen.create(node, kind);
    }

    private LoopNode convertLoop(ListSyntax syntax, Namespace ns) {
        return new LoopNode(convertInvoke(
                syntax.getValue().cdr(), syntax.getSourceSection(), ns));
    }
}
