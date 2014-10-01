package truffler;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    public static ListForm read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    private static ListForm read(PushbackReader pstream) throws IOException {
        List<Form> forms = new ArrayList<Form>();

        readWhitespace(pstream);
        char c = (char) pstream.read();
        while ((byte) c != -1) {
            pstream.unread(c);
            forms.add(readForm(pstream));
            readWhitespace(pstream);
            c = (char) pstream.read();
        }

        return ListForm.list(forms);
    }

    public static Form readForm(PushbackReader pstream) throws IOException {
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
        List<Form> list = new ArrayList<Form>();
        readWhitespace(pstream);
        char c = (char) pstream.read();
        while (true) {
            if (Character.isWhitespace(c)) {
                // pass
            } else if (c == ')') {
                // end of list
                break;
            } else if ((byte) c == -1) {
                throw new EOFException("EOF reached before closing of list");
            } else {
                pstream.unread(c);
                list.add(readForm(pstream));
            }
            c = (char) pstream.read();
        }
        return ListForm.list(list);
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
