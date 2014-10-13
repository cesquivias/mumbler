package truffler.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static truffler.simple.Reader.read;
import static truffler.simple.node.TrufflerListNode.EMPTY;
import static truffler.simple.node.TrufflerListNode.list;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import truffler.simple.node.BooleanNode;
import truffler.simple.node.NumberNode;
import truffler.simple.node.SpecialForm;
import truffler.simple.node.SymbolNode;
import truffler.simple.node.TrufflerListNode;

public class ReaderTest {

    private static InputStream toInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    @Test
    public void testReadListWithWhitespace() throws IOException {
        // a list of one element (a list)
        TrufflerListNode<?> expected = list(list(
                new SymbolNode("foo"),
                new SymbolNode("bar")));

        TrufflerListNode<?> actual = read(
                toInputStream("   ( foo  bar  )   "));
        assertEquals(expected, actual);
    }

    @Test
    public void testSpecialFormIsConverted() throws IOException {
        TrufflerListNode<?> actual = read(
                toInputStream("(define x 1)"));

        assert actual.car instanceof SpecialForm;
    }
    
    @Test
    public void readNumber() throws IOException {
        TrufflerListNode<?> expect = list(new NumberNode(1234));
        TrufflerListNode<?> actual = read(toInputStream("1234"));
        assertEquals(expect, actual);
    }

    @Test
    public void readEmptyList() throws IOException {
        TrufflerListNode<?> expect = list(EMPTY);
        TrufflerListNode<?> actual = read(toInputStream("()"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readList() throws IOException {
        TrufflerListNode<?> expect = list(list(
                new NumberNode(123),
                new NumberNode(5)));
        TrufflerListNode<?> actual = read(toInputStream("(123 5)"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readWhitespace() throws IOException {
        TrufflerListNode<?> expect = list(EMPTY);
        TrufflerListNode<?> actual = read(toInputStream("   (    )    "));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readSymbol() throws IOException {
        TrufflerListNode<?> expect = list(new SymbolNode("foo"));
        TrufflerListNode<?> actual = read(toInputStream("foo"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readMultipleForms() throws IOException {
        TrufflerListNode<?> expect = list(new SymbolNode("foo"),
                list(new NumberNode(12), new NumberNode(3)));
        TrufflerListNode<?> actual = read(toInputStream("foo (12 3)"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readUnmatchedEndParen() throws IOException {
        try {
            read(toInputStream(")"));
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void readTrueFalse() throws IOException {
        TrufflerListNode<?> expect = list(BooleanNode.TRUE, BooleanNode.FALSE);
        TrufflerListNode<?> actual = read(toInputStream("#t #f"));
        assertEquals(expect, actual);
    }
}
