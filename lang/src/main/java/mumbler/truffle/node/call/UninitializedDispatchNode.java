package mumbler.truffle.node.call;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

final public class UninitializedDispatchNode extends DispatchNode {
    @Override
    protected Object executeDispatch(VirtualFrame virtualFrame,
            CallTarget callTarget, Object[] arguments) {
        CompilerDirectives.transferToInterpreterAndInvalidate();

        Node cur = this;
        int size = 0;
        while (cur.getParent() instanceof DispatchNode) {
            cur = cur.getParent();
            size++;
        }
        InvokeNode invokeNode = (InvokeNode) cur.getParent();

        DispatchNode replacement;
        if (size < INLINE_CACHE_SIZE) {
            // There's still room in the cache. Add a new DirectDispatchNode.
            DispatchNode next = new UninitializedDispatchNode();
            replacement = new DirectDispatchNode(next, callTarget);
            this.replace(replacement);
        } else {
            replacement = new GenericDispatchNode();
            invokeNode.dispatchNode.replace(replacement);
        }

        // Call function with newly created dispatch node.
        return replacement.executeDispatch(virtualFrame, callTarget, arguments);
    }
}
