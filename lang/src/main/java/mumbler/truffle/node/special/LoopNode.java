package mumbler.truffle.node.special;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.call.InvokeNode;

@NodeInfo(shortName = "loop", description = "Repeats the function call forever")
public class LoopNode extends MumblerNode {
    @Child private com.oracle.truffle.api.nodes.LoopNode loopNode;
    private final String callString;

    public LoopNode(InvokeNode callNode) {
        this.callString = callNode.toString();
        this.loopNode = Truffle.getRuntime().createLoopNode(
                new LoopReapeatingNode(callNode));
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        this.loopNode.executeLoop(virtualFrame);
        return null;
    }

    @Override
    public String toString() {
        return '\u331e' + this.callString;
    }
}
