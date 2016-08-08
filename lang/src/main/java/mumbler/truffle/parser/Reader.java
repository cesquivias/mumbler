package mumbler.truffle.parser;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import mumbler.truffle.syntax.BigIntegerSyntax;
import mumbler.truffle.syntax.BooleanSyntax;
import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.syntax.LongSyntax;
import mumbler.truffle.syntax.StringSyntax;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class Reader extends MumblerBaseVisitor<Syntax<?>> {
    public static ListSyntax read(Source source) throws IOException {
        return (ListSyntax) new Reader(source)
            .visit(createParseTree(source.getInputStream()));
    }

	public static Syntax<?> readForm(Source source) throws IOException {
        return ((ListSyntax) new Reader(source).visit(
                createParseTree(source.getInputStream())))
                .getValue()
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

    private final Source source;

    private Reader(Source source) {
        this.source = source;
    }

    @Override
    public ListSyntax visitFile(MumblerParser.FileContext ctx) {
        List<Syntax<?>> forms = new ArrayList<>();
        for (MumblerParser.FormContext form : ctx.form()) {
            forms.add(this.visit(form));
        }
        return new ListSyntax(MumblerList.list(forms),
                createSourceSection(ctx));
    }

    @Override
    public ListSyntax visitList(MumblerParser.ListContext ctx) {
        List<Syntax<?>> forms = new ArrayList<>();
        for (MumblerParser.FormContext form : ctx.form()) {
            forms.add(this.visit(form));
        }
        return new ListSyntax(MumblerList.list(forms),
                createSourceSection(ctx));
    }

    @Override
    public Syntax<?> visitNumber(MumblerParser.NumberContext ctx) {
        try {
        return new LongSyntax(Long.valueOf(ctx.getText(), 10),
                createSourceSection(ctx));
        } catch (NumberFormatException e) {
            return new BigIntegerSyntax(new BigInteger(ctx.getText()),
                    createSourceSection(ctx));
        }
    }

    private SourceSection createSourceSection(ParserRuleContext ctx) {
        return source.createSection(MumblerParser.VOCABULARY.getDisplayName(ctx.getRuleIndex()),
                ctx.start.getLine(),
                ctx.start.getCharPositionInLine(),
                ctx.stop.getStopIndex() - ctx.start.getStartIndex());
    }

    @Override
    public BooleanSyntax visitBool(MumblerParser.BoolContext ctx) {
        return new BooleanSyntax("#t".equals(ctx.getText()) ? true : false,
                createSourceSection(ctx));
    }

    @Override
    public SymbolSyntax visitSymbol(MumblerParser.SymbolContext ctx) {
        return new SymbolSyntax(new MumblerSymbol(ctx.getText()),
                createSourceSection(ctx));
    }

    @Override
    public ListSyntax visitQuote(MumblerParser.QuoteContext ctx) {
        return new ListSyntax(MumblerList.list(
                new SymbolSyntax(new MumblerSymbol("quote"), createSourceSection(ctx)),
                this.visit(ctx.form())),
                createSourceSection(ctx));
    }

    @Override
    public StringSyntax visitString(MumblerParser.StringContext ctx) {
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
        return new StringSyntax(b.toString(), createSourceSection(ctx));
    }
}
