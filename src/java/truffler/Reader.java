import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class Reader {
    public static Form read(InputStream istream) throws IOException {
        PushbackInputStream pstream = new PushbackInputStream(istream);
        pstream.mark(1);
        char c = (char) pstream.read();
        pstream.reset();
        if (c == '(') {
            return readList(pstream);
        } else if (Character.isDigit(c)) {
            return readNumber(pstream);
        } else {
            throw new IllegalArgumentException("Illegal character: " + c);
        }
    }

    public static Form readList(PushbackInputStream pstream) {
        // TODO : finish
        return null;
    }

    public static Form readNumber(PushbackInputStream pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        pstream.mark(1);
        char c = (char) pstream.read();
        while (Character.isDigit(c)) {
            b.append(c);
            pstream.mark(1);
            c = (char) pstream.read();
        }
        pstream.reset();
        return new NumberForm(Long.valueOf(b.toString(), 10));
    }
}
