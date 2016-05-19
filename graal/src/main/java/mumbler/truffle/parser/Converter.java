package mumbler.truffle.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.antlr.v4.runtime.misc.Pair;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

import mumbler.truffle.MumblerException;
import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.SymbolNode;
import mumbler.truffle.node.SymbolNodeGen;
import mumbler.truffle.node.call.InvokeNode;
import mumbler.truffle.node.literal.BigIntegerNode;
import mumbler.truffle.node.literal.BooleanNode;
import mumbler.truffle.node.literal.LiteralListNode;
import mumbler.truffle.node.literal.LiteralSymbolNode;
import mumbler.truffle.node.literal.LongNode;
import mumbler.truffle.node.literal.StringNode;
import mumbler.truffle.node.special.DefineNode;
import mumbler.truffle.node.special.DefineNodeGen;
import mumbler.truffle.node.special.IfNode;
import mumbler.truffle.node.special.LambdaNode;
import mumbler.truffle.node.special.LambdaNodeGen;
import mumbler.truffle.node.special.QuoteNode;
import mumbler.truffle.node.special.QuoteNode.QuoteKind;
import mumbler.truffle.node.special.QuoteNodeGen;
import mumbler.truffle.parser.IdentifierScanner.Namespace;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

public class Converter {
    private IdentifierScanner idScanner;

    public MumblerNode[] convertSexp(MumblerList<Object> sexp,
            Namespace globalNs) {
        Namespace fileNamespace = new Namespace(globalNs);
        this.idScanner = new IdentifierScanner(fileNamespace);
        this.idScanner.scan(sexp);

        return StreamSupport.stream(sexp.spliterator(), false)
                .map(obj -> this.convert(obj, fileNamespace))
                .toArray(size -> new MumblerNode[size]);
    }

    public MumblerNode convert(Object obj, Namespace ns) {
        if (obj instanceof Long) {
            return convert((long) obj);
        } else if (obj instanceof BigInteger) {
            return convert((BigInteger) obj);
        } else if (obj instanceof Boolean) {
            return convert((boolean) obj);
        } else if (obj instanceof String) {
            return convert((String) obj);
        } else if (obj instanceof MumblerSymbol) {
            return convert((MumblerSymbol) obj, ns);
        } else if (obj instanceof MumblerList) {
            return convert((MumblerList<?>) obj, ns);
        } else {
            throw new MumblerException("Unknown type: " + obj.getClass());
        }
    }

    public static LongNode convert(long number) {
        return new LongNode(number);
    }

    public static BigIntegerNode convert(BigInteger number) {
        return new BigIntegerNode(number);
    }

    public static BooleanNode convert(boolean bool) {
        return bool ? BooleanNode.TRUE : BooleanNode.FALSE;
    }

    public static StringNode convert(String str) {
        return new StringNode(str);
    }

    public static SymbolNode convert(MumblerSymbol sym, Namespace ns) {
        Pair<Integer, FrameSlot> pair = ns.getIdentifier(sym.name);
        return SymbolNodeGen.create(pair.b, pair.a);
    }

    public MumblerNode convert(MumblerList<?> list, Namespace ns) {
        if (list == MumblerList.EMPTY || list.size() == 0) {
            return new LiteralListNode(MumblerList.EMPTY);
        }

        Object car = list.car();
        if (car instanceof MumblerSymbol) {
            switch (((MumblerSymbol) car).name) {
            case "define":
                return convertDefine(list, ns);
            case "lambda":
                return convertLambda(list, ns);
            case "if":
                return convertIf(list, ns);
            case "quote":
                return convertQuote(list, ns);
            }
        }
        return new InvokeNode(convert(list.car(), ns),
                StreamSupport.stream(list.cdr().spliterator(), false)
                .map(obj -> convert(obj, ns))
                .toArray(size -> new MumblerNode[size]));
    }

    private DefineNode convertDefine(MumblerList<?> list, Namespace ns) {
        MumblerSymbol sym = (MumblerSymbol) list.cdr().car();
        return DefineNodeGen.create(convert(list.cdr().cdr().car(), ns),
                ns.getIdentifier(sym.name).b);
    }

    @SuppressWarnings("unchecked")
    private LambdaNode convertLambda(MumblerList<?> list, Namespace ns) {
        Namespace lambdaNs = this.idScanner.getNamespace(list);
        List<FrameSlot> formalParameters = new ArrayList<>();
        for (MumblerSymbol arg : (MumblerList<MumblerSymbol>)
                list.cdr().car()) {
            formalParameters.add(convert(arg, lambdaNs).getSlot());
        }
        List<MumblerNode> bodyNodes = new ArrayList<>();
        for (Object body : list.cdr().cdr()) {
            bodyNodes.add(convert(body, lambdaNs));
        }
        bodyNodes.get(bodyNodes.size() - 1).setIsTail();

        MumblerFunction function = MumblerFunction.create(
                formalParameters.toArray(new FrameSlot[] {}),
                bodyNodes.toArray(new MumblerNode[] {}),
                lambdaNs.getFrameDescriptor());
        return LambdaNodeGen.create(function);
    }

    private IfNode convertIf(MumblerList<?> list, Namespace ns) {
        return new IfNode(convert(list.cdr().car(), ns),
                convert(list.cdr().cdr().car(), ns),
                convert(list.cdr().cdr().cdr().car(), ns));
    }

    private static QuoteNode convertQuote(MumblerList<?> list,
            Namespace ns) {
        Object value = list.cdr().car();
        MumblerNode node;
        QuoteKind kind;
        if (value instanceof Long) {
            kind = QuoteKind.LONG;
            node = convert((long) value);
        } else if (value instanceof Boolean) {
            kind = QuoteKind.BOOLEAN;
            node = convert((boolean) value);
        } else if (value instanceof String) {
            kind = QuoteKind.STRING;
            node = new StringNode((String) value);
        } else if (value instanceof MumblerSymbol) {
            kind = QuoteKind.SYMBOL;
            node = new LiteralSymbolNode((MumblerSymbol) value);
        } else if (value instanceof MumblerList) {
            kind = QuoteKind.LIST;
            node = new LiteralListNode((MumblerList<?>) value);
        } else {
            throw new MumblerException("Unknown quote type: " +
                    value.getClass());
        }
        return QuoteNodeGen.create(node, kind);
    }
}
