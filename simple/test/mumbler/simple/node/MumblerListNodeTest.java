package mumbler.simple.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static mumbler.simple.node.MumblerListNode.EMPTY;
import static mumbler.simple.node.MumblerListNode.list;

import org.junit.Test;

import mumbler.simple.node.Node;
import mumbler.simple.node.NumberNode;
import mumbler.simple.node.SymbolNode;
import mumbler.simple.node.MumblerListNode;

public class MumblerListNodeTest {
    @Test
    public void emptyToString() {
        String expected = "()";
        assertEquals(expected, MumblerListNode.EMPTY.toString());
    }

    @Test
    public void nonEmptyToString() {
        String expected = "(1 'foo)";
        MumblerListNode<Node> nodes = list(
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
