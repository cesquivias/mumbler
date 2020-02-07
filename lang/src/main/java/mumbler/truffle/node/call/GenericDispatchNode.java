package mumbler.truffle.node.call;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.IndirectCallNode;

public class GenericDispatchNode extends DispatchNode {
    @Child private IndirectCallNode callNode = Truffle.getRuntime()
            .createIndirectCallNode();

    @Override
    protected Object executeDispatch(VirtualFrame virtualFrame,
            CallTarget callTarget, Object[] argumentValues) {
        return this.callNode.call(callTarget, argumentValues);
    }
}
