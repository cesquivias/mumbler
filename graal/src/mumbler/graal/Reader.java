package mumbler.graal;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.literal.NumberNode;
import mumbler.graal.type.MumblerList;
import mumbler.graal.type.MumblerSymbol;

public class Reader {
    public static MumblerList<MumblerNode> read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    private static MumblerList<MumblerNode> read(PushbackReader pstream)
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

        return MumblerList.list(nodes);
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

    private static MumblerNode readSymbol(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (!(Character.isWhitespace(c) || (byte) c == -1 || c == '(' || c == ')')) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        //return new MumblerSymbol(b.toString());
        return null;
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
        //return SpecialForm.check(MumblerList.list(list));
        return null;
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

    private static final MumblerSymbol TRUE_SYM = new MumblerSymbol("t");
    private static final MumblerSymbol FALSE_SYM = new MumblerSymbol("f");

    private static MumblerNode readBoolean(PushbackReader pstream)
            throws IOException {
        char hash = (char) pstream.read();
        assert hash == '#' : "Reading a boolean must start with '#'";

        //MumblerSymbol sym = readSymbol(pstream);
        MumblerSymbol sym = null; // TODO: remove
        if (TRUE_SYM.equals(sym)) {
            //return BooleanNode.TRUE;
            return null;
        } else if (FALSE_SYM.equals(sym)) {
            // return BooleanNode.FALSE;
            return null;
        } else {
            throw new IllegalArgumentException("Unknown value: #" + sym.name);
        }
    }
}
