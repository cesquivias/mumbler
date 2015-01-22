package mumbler.truffle.parser;

import static mumbler.truffle.node.literal.BooleanNode.FALSE;
import static mumbler.truffle.node.literal.BooleanNode.TRUE;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.SymbolNode;
import mumbler.truffle.node.SymbolNodeGen;
import mumbler.truffle.node.call.InvokeNode;
import mumbler.truffle.node.literal.BigIntegerNode;
import mumbler.truffle.node.literal.BooleanNode;
import mumbler.truffle.node.literal.LiteralListNode;
import mumbler.truffle.node.literal.LiteralSymbolNode;
import mumbler.truffle.node.literal.LongNode;
import mumbler.truffle.node.special.DefineNode;
import mumbler.truffle.node.special.DefineNodeGen;
import mumbler.truffle.node.special.IfNode;
import mumbler.truffle.node.special.LambdaNode;
import mumbler.truffle.node.special.LambdaNodeGen;
import mumbler.truffle.node.special.QuoteNode;
import mumbler.truffle.node.special.QuoteNode.QuoteKind;
import mumbler.truffle.node.special.QuoteNodeGen;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

public class Reader extends MumblerBaseVisitor<MumblerNode> {
    private Stack<FrameDescriptor> frameDescriptors = new Stack<>();
    private MumblerNode[] body;

    private Reader(FrameDescriptor frameDescriptor) {
        this.frameDescriptors.push(frameDescriptor);
    }

    public static MumblerNode[] read(InputStream istream,
            FrameDescriptor frameDescriptor) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(istream);
        MumblerLexer lexer = new MumblerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MumblerParser parser = new MumblerParser(tokens);
        ParseTree tree = parser.file();
        Reader reader = new Reader(frameDescriptor);
        reader.visit(tree);
        return reader.body;
    }

    @Override
    public MumblerNode visitFile(MumblerParser.FileContext ctx) {
        return this.visit(ctx.forms());
    }

    @Override
    public MumblerNode visitForms(MumblerParser.FormsContext ctx) {
        this.body = ctx.form().stream()
                .map(child -> this.visit(child))
                .toArray(size -> new MumblerNode[size]);
        return null;
    }

    @Override
    public MumblerNode visitNumber(MumblerParser.NumberContext ctx) {
        try {
            return new LongNode(Long.valueOf(ctx.INT().getText()));
        } catch (NumberFormatException e) {
            return new BigIntegerNode(new BigInteger(ctx.INT().getText()));
        }
    }

    @Override
    public BooleanNode visitBool(MumblerParser.BoolContext ctx) {
        return "#t".equals(ctx.BOOLEAN().getText()) ? TRUE: FALSE;
    }

    @Override
    public SymbolNode visitSymbol(MumblerParser.SymbolContext ctx) {
        return SymbolNodeGen.create(
                this.frameDescriptors.peek().findOrAddFrameSlot(
                        ctx.SYMBOL().getText()));
    }

    @Override
    public MumblerNode visitList(MumblerParser.ListContext ctx) {
        List<ParseTree> children = ctx.forms().children;
        if (children == null) {
            return new LiteralListNode(MumblerList.EMPTY);
        }
        if (children.get(0) instanceof MumblerParser.SymbolContext) {
            MumblerParser.SymbolContext symCtx = (MumblerParser.SymbolContext)
                    children.get(0);
            switch (symCtx.getText()) {
            case "define":
                return this.toDefine(ctx);
            case "lambda":
                return this.toLambda(ctx);
            case "if":
                return this.toIf(ctx);
            case "quote":
                return this.toQuote(ctx);
            }
        }
        return this.toInvoke(ctx);
    }

    public DefineNode toDefine(MumblerParser.ListContext ctx) {
        assert ctx.forms().children.size() == 3;
        return DefineNodeGen.create(this.visit(ctx.forms().getChild(2)),
                this.frameDescriptors.peek().findOrAddFrameSlot(
                        ctx.forms().getChild(1).getText()));
    }

    public LambdaNode toLambda(MumblerParser.ListContext ctx) {
        List<ParseTree> children = ctx.forms().children;
        MumblerParser.ListContext args = (MumblerParser.ListContext)
                children.get(1);
        FrameDescriptor lambdaDescriptor = new FrameDescriptor();
        this.frameDescriptors.push(lambdaDescriptor);
        FrameSlot[] arguments = args.forms().children.stream()
                .map(child -> lambdaDescriptor.findOrAddFrameSlot(child.getText()))
                .toArray(size -> new FrameSlot[size]);
        MumblerNode[] bodyNodes = children.subList(2, children.size()).stream()
                .map(child -> this.visit(child))
                .toArray(size -> new MumblerNode[size]);
        return LambdaNodeGen.create(MumblerFunction.create(arguments, bodyNodes,
                this.frameDescriptors.pop()));
    }

    public InvokeNode toInvoke(MumblerParser.ListContext ctx) {
        List<ParseTree> children = ctx.forms().children;
        MumblerNode[] args = children.subList(1, children.size())
                .stream()
                .map(child -> this.visit(child))
                .toArray(size-> new MumblerNode[size]);
        return new InvokeNode(this.visit(children.get(0)), args);
    }

    public IfNode toIf(MumblerParser.ListContext ctx) {
        List<ParseTree> children = ctx.forms().children;
        return new IfNode(this.visit(children.get(1)),
                this.visit(children.get(2)),
                this.visit(children.get(3)));
    }

    private QuoteNode toQuote(MumblerParser.ListContext ctx) {
        List<ParseTree> children = ctx.forms().children;
        assert children.size() == 2;
        ParseTree value = children.get(1);
        if (value instanceof MumblerParser.SymbolContext) {
            return QuoteNodeGen.create(new LiteralSymbolNode(
                    new MumblerSymbol(value     .getText())),
                    QuoteKind.SYMBOL);
        } else if (value instanceof MumblerParser.NumberContext) {
            return QuoteNodeGen.create(this.visit(value), QuoteKind.LONG);
        } else if (value instanceof MumblerParser.BoolContext) {
            return QuoteNodeGen.create(this.visit(value), QuoteKind.BOOLEAN);
        } else if (value instanceof MumblerParser.ListContext){
            List<ParseTree> elements = ((MumblerParser.ListContext) value).forms().children;
            return QuoteNodeGen.create(new LiteralListNode(this.quoteList(elements)),
                    QuoteKind.LIST);
        } else {
            throw new IllegalArgumentException("Unknown quote type");
        }
    }

    private MumblerList<?> quoteList(List<ParseTree> children) {
        if (children == null) {
            return MumblerList.EMPTY;
        }
        List<Object> output = new ArrayList<>();
        for (ParseTree child : children) {
            if (child instanceof MumblerParser.NumberContext) {
                output.add(this.parseNumber(child.getText()));
            } else if (child instanceof MumblerParser.BoolContext) {
                output.add(this.parseBoolean(child.getText()));
            } else if (child instanceof MumblerParser.SymbolContext) {
                output.add(new MumblerSymbol(child.getText()));
            } else if (child instanceof MumblerParser.ListContext) {
                output.add(this.quoteList(
                        ((MumblerParser.ListContext) child).forms().children));
            } else {
                throw new IllegalArgumentException("Unknown quote type");
            }
        }
        return MumblerList.list(output);
    }

    private Number parseNumber(String text) {
        try {
            return Long.valueOf(text);
        } catch (NumberFormatException e) {
            return new BigInteger(text);
        }
    }

    private boolean parseBoolean(String text) {
        return "#t".equals(text);
    }
}
