package mumbler.truffle.node.call;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ControlFlowException;
import com.oracle.truffle.api.nodes.DirectCallNode;

public class TailCallException extends ControlFlowException {
    private static final long serialVersionUID = 1L;

    private final DirectCallNode callNode;
    private final Object[] arguments;
    public boolean outOfFunction = false;

    public TailCallException(DirectCallNode callNode, Object[] arguments) {
        this.callNode = callNode;
        this.arguments = arguments;
    }

    public Object call(VirtualFrame virtualFrame) {
        TailCallException tailCall = null;
        try {
            return this.callNode.call(virtualFrame, this.arguments);
        } catch (TailCallException e) {
            tailCall = e;
        }
        do {
            try {
                return tailCall.callNode.call(virtualFrame, tailCall.arguments);
            } catch (TailCallException e) {
                tailCall = e;
            }
        } while (true);
    }
}
