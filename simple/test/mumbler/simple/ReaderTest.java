package mumbler.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static mumbler.simple.Reader.read;
import static mumbler.simple.node.MumblerListNode.EMPTY;
import static mumbler.simple.node.MumblerListNode.list;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import mumbler.simple.node.BooleanNode;
import mumbler.simple.node.NumberNode;
import mumbler.simple.node.SpecialForm;
import mumbler.simple.node.SymbolNode;
import mumbler.simple.node.MumblerListNode;

public class ReaderTest {

    private static InputStream toInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    @Test
    public void testReadListWithWhitespace() throws IOException {
        // a list of one element (a list)
        MumblerListNode<?> expected = list(list(
                new SymbolNode("foo"),
                new SymbolNode("bar")));

        MumblerListNode<?> actual = read(
                toInputStream("   ( foo  bar  )   "));
        assertEquals(expected, actual);
    }

    @Test
    public void testSpecialFormIsConverted() throws IOException {
        MumblerListNode<?> actual = read(
                toInputStream("(define x 1)"));

        assert actual.car instanceof SpecialForm;
    }
    
    @Test
    public void readNumber() throws IOException {
        MumblerListNode<?> expect = list(new NumberNode(1234));
        MumblerListNode<?> actual = read(toInputStream("1234"));
        assertEquals(expect, actual);
    }

    @Test
    public void readEmptyList() throws IOException {
        MumblerListNode<?> expect = list(EMPTY);
        MumblerListNode<?> actual = read(toInputStream("()"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readList() throws IOException {
        MumblerListNode<?> expect = list(list(
                new NumberNode(123),
                new NumberNode(5)));
        MumblerListNode<?> actual = read(toInputStream("(123 5)"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readWhitespace() throws IOException {
        MumblerListNode<?> expect = list(EMPTY);
        MumblerListNode<?> actual = read(toInputStream("   (    )    "));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readSymbol() throws IOException {
        MumblerListNode<?> expect = list(new SymbolNode("foo"));
        MumblerListNode<?> actual = read(toInputStream("foo"));
        assertEquals(expect, actual);
    }
    
    @Test
    public void readMultipleForms() throws IOException {
        MumblerListNode<?> expect = list(new SymbolNode("foo"),
                list(new NumberNode(12), new NumberNode(3)));
        MumblerListNode<?> actual = read(toInputStream("foo (12 3)"));
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
        MumblerListNode<?> expect = list(BooleanNode.TRUE, BooleanNode.FALSE);
        MumblerListNode<?> actual = read(toInputStream("#t #f"));
        assertEquals(expect, actual);
    }
}
