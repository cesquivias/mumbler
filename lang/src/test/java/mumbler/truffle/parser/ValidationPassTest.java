package mumbler.truffle.parser;

import static mumbler.truffle.parser.IdentifierScanner.Namespace.TOP_NS;

import java.io.IOException;

import mumbler.truffle.MumblerContext;
import mumbler.truffle.parser.IdentifierScanner.Namespace;
import mumbler.truffle.syntax.ListSyntax;

import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.source.Source;

public class ValidationPassTest {

    MumblerContext context;
    IdentifierScanner idScanner;
    Namespace fileNs;

    @Before
    public void setUp() throws Exception {
        context = new MumblerContext();
        fileNs = new Namespace(TOP_NS, context.getGlobalNamespace());
        idScanner = new IdentifierScanner(this.fileNs);
    }

    @Test(expected = MumblerReadException.class)
    public void exceptionThrownDefineTooManyArguments() {
        ListSyntax sexp = read("(define foo 1 bar)");
        idScanner.scan(sexp);
        ValidationPass.validate(sexp, idScanner.getNamespaceMap());
    }

    private static ListSyntax read(String str) {
        try {
            return Reader.read(Source.fromText(str, "<junit>"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
