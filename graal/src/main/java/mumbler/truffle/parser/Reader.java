package mumbler.truffle.parser;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.oracle.truffle.api.frame.FrameDescriptor;

public class Reader extends MumblerBaseVisitor<Object> {
    public static MumblerList<MumblerNode> read(InputStream istream,
            FrameDescriptor descriptor) throws IOException {
        MumblerNode[] nodes = StreamSupport.stream(
                ((MumblerList<?>) new Reader().visit(createParseTree(istream)))
                .spliterator(), false)
                .map(obj -> Converter.convert(obj, descriptor))
                .toArray(size -> new MumblerNode[size]);
        return MumblerList.list(nodes);
    }

    public static Object readForm(InputStream istream) throws IOException {
        return ((MumblerList<?>) new Reader().visit(createParseTree(istream)))
                .car();
    }

    private static ParseTree createParseTree(InputStream istream)
            throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(istream);
        MumblerLexer lexer = new MumblerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MumblerParser parser = new MumblerParser(tokens);
        return parser.file();
    }

    @Override
    public MumblerList<Object> visitFile(MumblerParser.FileContext ctx) {
        List<Object> forms = new ArrayList<>();
        for (MumblerParser.FormContext form : ctx.form()) {
            forms.add(this.visit(form));
        }
        return MumblerList.list(forms);
    }

    @Override
    public MumblerList<Object> visitList(MumblerParser.ListContext ctx) {
        List<Object> forms = new ArrayList<>();
        for (MumblerParser.FormContext form : ctx.form()) {
            forms.add(this.visit(form));
        }
        return MumblerList.list(forms);
    }

    @Override
    public Number visitNumber(MumblerParser.NumberContext ctx) {
        try {
            return Long.valueOf(ctx.getText());
        } catch (NumberFormatException e) {
            return new BigInteger(ctx.getText());
        }
    }

    @Override
    public Boolean visitBool(MumblerParser.BoolContext ctx) {
        return "#t".equals(ctx.getText()) ? true : false;
    }

    @Override
    public MumblerSymbol visitSymbol(MumblerParser.SymbolContext ctx) {
        return new MumblerSymbol(ctx.getText());
    }

    @Override
    public Object visitQuote(MumblerParser.QuoteContext ctx) {
        return MumblerList.list(new MumblerSymbol("quote"),
                this.visit(ctx.form()));
    }

    @Override
    public String visitString(MumblerParser.StringContext ctx) {
        String text = ctx.getText();
        StringBuilder b = new StringBuilder();
        for (int i=1; i<text.length()-1; i++) {
            char c = text.charAt(i);
            if (c == '\\') {
                char next = text.charAt(i + 1);
                i++;
                switch (next) {
                case '\\':
                    b.append('\\');
                    break;
                case '"':
                    b.append('"');
                    break;
                case 'n':
                    b.append('\n');
                    break;
                case 'r':
                    b.append('\r');
                    break;
                case 't':
                    b.append('\t');
                    break;
                case 'f':
                    b.append('\f');
                    break;
                case 'b':
                    b.deleteCharAt(b.length() -1);
                    break;
                default:
                    b.append(next);
                    break;
                }
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }
}
