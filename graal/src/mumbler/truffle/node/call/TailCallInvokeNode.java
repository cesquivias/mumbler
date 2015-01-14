package mumbler.truffle.node.call;

import java.util.Arrays;

import mumbler.truffle.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;

public class TailCallInvokeNode extends MumblerNode {
    @Child protected MumblerNode functionNode;
    @Children protected final MumblerNode[] argumentNodes;
    @Child protected DirectCallNode callNode;

    public TailCallInvokeNode(MumblerNode functionNode,
            MumblerNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
    }

    @Override
    public String toString() {
        return "(apply " + this.functionNode + " " +
                Arrays.toString(this.argumentNodes) + ")";
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        // TODO Auto-generated method stub
        return null;
    }
}
