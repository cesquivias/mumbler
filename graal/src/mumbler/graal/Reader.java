package mumbler.graal;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

import mumbler.graal.node.BooleanNode;
import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.NumberNode;
import mumbler.graal.node.SpecialForm;
import mumbler.graal.node.SymbolNode;
import mumbler.graal.node.MumblerListNode;

public class Reader {
    public static MumblerListNode<MumblerNode> read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    private static MumblerListNode<MumblerNode> read(PushbackReader pstream)
            throws IOException {
        List<MumblerNode> nodes = new ArrayList<MumblerNode>();

        readWhitespace(pstream);
        char c = (char) pstream.read();
        while ((byte) c != -1) {
            pstream.unread(c);
            nodes.add(readNode(pstream));
            readWhitespace(pstream);
            c = (char) pstream.read();
        }

        return MumblerListNode.list(nodes);
    }

    public static MumblerNode readNode(PushbackReader pstream) throws IOException {
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

    private static MumblerNode readList(PushbackReader pstream) throws IOException {
        char paren = (char) pstream.read();
        assert paren == '(' : "Reading a list must start with '('";
        List<MumblerNode> list = new ArrayList<MumblerNode>();
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
        return SpecialForm.check(MumblerListNode.list(list));
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
