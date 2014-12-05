package mumbler.graal.node.call;

import mumbler.graal.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

public class DispatchNode extends Node {
    @Child private IndirectCallNode callNode = Truffle.getRuntime()
            .createIndirectCallNode();

    public Object executeDispatch(VirtualFrame virtualFrame,
            MumblerFunction function, Object[] arguments) {
        return this.callNode.call(virtualFrame, function.callTarget, arguments);
    }
}