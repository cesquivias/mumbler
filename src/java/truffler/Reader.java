package truffler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;

public class Reader {
    public static Form read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    public static Form read(PushbackReader pstream) throws IOException {
        readWhitespace(pstream);
        char c = (char) pstream.read();
        if (c == '(') {
            return readList(pstream);
        } else if (Character.isDigit(c)) {
            pstream.unread(c);
            return readNumber(pstream);
        } else {
            pstream.unread(c);
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

    private static SymbolForm readSymbol(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (!(Character.isWhitespace(c) || (byte) c == -1
                        || c == '(' || c == ')')) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return new SymbolForm(b.toString());
    }

    private static ListForm readList(PushbackReader pstream)
            throws IOException {
        // open paren is already read
        ListForm list = ListForm.EMPTY;
        readWhitespace(pstream);
        char c = (char) pstream.read();
        while (true) {
            if (Character.isWhitespace(c)) {
                // pass
            } else if (c == ')') {
                // end of list
                break;
            } else {
                pstream.unread(c);
                list = list.cons(read(pstream));
            }
            c = (char) pstream.read();
        }
        return list;
    }

    private static NumberForm readNumber(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (Character.isDigit(c)) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return new NumberForm(Long.valueOf(b.toString(), 10));
    }
}
