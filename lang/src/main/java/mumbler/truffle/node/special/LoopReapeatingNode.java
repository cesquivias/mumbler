package mumbler.truffle.node.special;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;

import mumbler.truffle.node.call.InvokeNode;

public final class LoopReapeatingNode extends Node implements RepeatingNode {
    @Child private InvokeNode invokeNode;

    public LoopReapeatingNode(InvokeNode invokeNode) {
        this.invokeNode = invokeNode;
    }

    @Override
    public boolean executeRepeating(VirtualFrame virtualFrame) {
        this.invokeNode.execute(virtualFrame);
        return true;
    }

}
