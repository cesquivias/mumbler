package truffler.simple;

import static org.junit.Assert.assertEquals;
import static truffler.simple.node.TrufflerListNode.list;

import org.junit.Test;

import truffler.simple.node.Node;
import truffler.simple.node.NumberNode;
import truffler.simple.node.SymbolNode;
import truffler.simple.node.TrufflerListNode;

public class TrufflerListNodeTest {
    @Test
    public void testEmptyToString() {
        String expected = "()";
        assertEquals(expected, TrufflerListNode.EMPTY.toString());
    }

    @Test
    public void testNonEmptyToString() {
        String expected = "(1 'foo)";
        TrufflerListNode<Node> nodes = list(
                new NumberNode(1),
                new SymbolNode("foo"));
        assertEquals(expected, nodes.toString());
    }
}
