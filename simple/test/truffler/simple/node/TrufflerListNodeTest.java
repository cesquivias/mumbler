package truffler.simple.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static truffler.simple.node.TrufflerListNode.EMPTY;
import static truffler.simple.node.TrufflerListNode.list;

import org.junit.Test;

import truffler.simple.node.Node;
import truffler.simple.node.NumberNode;
import truffler.simple.node.SymbolNode;
import truffler.simple.node.TrufflerListNode;

public class TrufflerListNodeTest {
    @Test
    public void emptyToString() {
        String expected = "()";
        assertEquals(expected, TrufflerListNode.EMPTY.toString());
    }

    @Test
    public void nonEmptyToString() {
        String expected = "(1 'foo)";
        TrufflerListNode<Node> nodes = list(
                new NumberNode(1),
                new SymbolNode("foo"));
        assertEquals(expected, nodes.toString());
    }
    
    @Test
    public void listOfNumsToString() {
        String expect = "(1 23)";
        String actual = list(new NumberNode(1), new NumberNode(23)).toString();
        assertEquals(expect, actual);
    }
    
    @Test
    public void listOfListsToString() {
        String expect = "((1) ())";
        String actual = list(list(new NumberNode(1)), EMPTY).toString();
        assertEquals(expect, actual);
    }
    
    @Test
    public void differentLengthListsNotEqual() {
        assertNotEquals(list(new NumberNode(1), new NumberNode(2)),
                list(new NumberNode(1)));
    }
}
