package mumbler.truffle.parser;

import static mumbler.truffle.parser.Namespace.TOP_NS;

import java.io.IOException;

import mumbler.truffle.MumblerContext;
import mumbler.truffle.syntax.ListSyntax;

import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;

public class AnalyzerTest {

    MumblerContext context;
    Analyzer analyzer;
    Namespace fileNs;

    @Before
    public void setUp() throws Exception {
        context = new MumblerContext();
        fileNs = new Namespace(TOP_NS, context.getGlobalNamespace());
        analyzer = new Analyzer(this.fileNs);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnDefineTooManyArguments() {
        ListSyntax sexp = read("(define foo 1 bar)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnDefineTooFewArguments() {
        ListSyntax sexp = read("(define foo)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnDefineFirstArgIsNotSymbol() {
        ListSyntax sexp = read("(define 3 foo)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnLambdaTooFewArguments() {
        ListSyntax sexp = read("(lambda ())");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnLambdaNoArgList() {
        ListSyntax sexp = read("(lambda foo 1)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnLambdaArgNotSymbol() {
        ListSyntax sexp = read("(lambda (foo 3) 'foo)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnIfWrongNumberOfArguments() {
        ListSyntax sexp = read("(if foo bar)");

        analyzer.walk(sexp);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionOnQuoteWrontNumberOfArguments() {
        ListSyntax sexp = read("(quote foo bar)");

        analyzer.walk(sexp);
    }

    private static ListSyntax read(String str) {
        try {
            return Reader.read(Source.fromText(str, "<junit>"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
