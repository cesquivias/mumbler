package truffler.simple;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static truffler.simple.node.TrufflerListNode.list;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import truffler.simple.node.SymbolNode;
import truffler.simple.node.TrufflerListNode;

public class ReaderTest {

    private static InputStream toInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    @Test
    public void testReadListWithWhitespace() throws IOException {
        // a list of one element (a list)
        TrufflerListNode expected = list(asList(list(asList(
                new SymbolNode("foo"),
                new SymbolNode("bar")))));

        TrufflerListNode actual = Reader.read(
                toInputStream("   ( foo  bar  )   "));
        assertEquals(expected, actual);
    }

}
