package mumbler.truffle.parser;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import mumbler.truffle.Converter;
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
        ANTLRInputStream input = new ANTLRInputStream(istream);
        MumblerLexer lexer = new MumblerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MumblerParser parser = new MumblerParser(tokens);
        ParseTree tree = parser.file();
        Reader reader = new Reader();
        MumblerNode[] nodes = StreamSupport.stream(
                ((MumblerList<?>) reader.visit(tree)).spliterator(), false)
                .map(obj -> Converter.convert(obj, descriptor))
                .toArray(size -> new MumblerNode[size]);
        return MumblerList.list(nodes);
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
}
