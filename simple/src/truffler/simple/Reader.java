package truffler.simple;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

import truffler.simple.node.BooleanNode;
import truffler.simple.node.Node;
import truffler.simple.node.NumberNode;
import truffler.simple.node.SpecialForm;
import truffler.simple.node.SymbolNode;
import truffler.simple.node.TrufflerListNode;

public class Reader {
    public static TrufflerListNode<Node> read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    private static TrufflerListNode<Node> read(PushbackReader pstream)
            throws IOException {
        List<Node> nodes = new ArrayList<Node>();

        readWhitespace(pstream);
        char c = (char) pstream.read();
        while ((byte) c != -1) {
            pstream.unread(c);
            nodes.add(readNode(pstream));
            readWhitespace(pstream);
            c = (char) pstream.read();
        }

        return TrufflerListNode.list(nodes);
    }

    public static Node readNode(PushbackReader pstream) throws IOException {
        char c = (char) pstream.read();
        pstream.unread(c);
        if (c == '(') {
            return readList(pstream);
        } else if (Character.isDigit(c)) {
            return readNumber(pstream);
        } else if (c == '#') {
            return readBoolean(pstream);
        } else if (c == ')') {
            throw new IllegalArgumentException("Unmatched close paren");
        } else {
            return readSymbol(pstream);
        }
    }

    private static void readWhitespace(PushbackReader pstream)
            throws IOException {
        char c = (char) pstream.read();
        while (Character.isWhitespace(c)) {
            c = (char) pstream.read();
        }
        pstream.unread(c);
    }

    private static SymbolNode readSymbol(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (!(Character.isWhitespace(c) || (byte) c == -1 || c == '(' || c == ')')) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return new SymbolNode(b.toString());
    }

    private static Node readList(PushbackReader pstream) throws IOException {
        char paren = (char) pstream.read();
        assert paren == '(' : "Reading a list must start with '('";
        List<Node> list = new ArrayList<Node>();
        do {
            readWhitespace(pstream);
            char c = (char) pstream.read();

            if (c == ')') {
                // end of list
                break;
            } else if ((byte) c == -1) {
                throw new EOFException("EOF reached before closing of list");
            } else {
                pstream.unread(c);
                list.add(readNode(pstream));
            }
        } while (true);
        return SpecialForm.check(TrufflerListNode.list(list));
    }

    private static NumberNode readNumber(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (Character.isDigit(c)) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return new NumberNode(Long.valueOf(b.toString(), 10));
    }

    private static final SymbolNode TRUE_SYM = new SymbolNode("t");
    private static final SymbolNode FALSE_SYM = new SymbolNode("f");

    private static BooleanNode readBoolean(PushbackReader pstream)
            throws IOException {
        char hash = (char) pstream.read();
        assert hash == '#' : "Reading a boolean must start with '#'";

        SymbolNode sym = readSymbol(pstream);
        if (TRUE_SYM.equals(sym)) {
            return BooleanNode.TRUE;
        } else if (FALSE_SYM.equals(sym)) {
            return BooleanNode.FALSE;
        } else {
            throw new IllegalArgumentException("Unknown value: #" + sym.name);
        }
    }
}
