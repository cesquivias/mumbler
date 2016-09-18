package mumbler.truffle.node.call;

import mumbler.truffle.node.MumblerNode;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

public class TCOInvokeNode extends InvokeNode {
    public TCOInvokeNode(MumblerNode functionNode, MumblerNode[] argumentNodes,
            SourceSection sourceSection) {
        super(functionNode, argumentNodes, sourceSection);
    }

    @Override
    protected Object call(VirtualFrame virtualFrame, CallTarget callTarget,
            Object[] arguments) {
        if (this.isTail()) {
            throw new TailCallException(callTarget, arguments);
        }
        while (true) {
            try {
                return super.call(virtualFrame, callTarget, arguments);
            } catch (TailCallException e) {
                callTarget = e.callTarget;
                arguments = e.arguments;
            }
        }
    }
}
