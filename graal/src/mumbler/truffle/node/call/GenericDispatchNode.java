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

        CallTarget currentCallTarget = callTarget;
        Object[] arguments = argumentValues;
        do {
            try {
                return this.callNode.call(virtualFrame, currentCallTarget,
                        arguments);
            } catch (TailCallException e) {
                currentCallTarget = e.callTarget;
                arguments = e.arguments;
            }
        } while(true);
    }
}
