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
        int depth = 0;
        while (cur.getParent() instanceof DispatchNode) {
            cur = cur.getParent();
            depth++;
        }
        InvokeNode invokeNode = (InvokeNode) cur.getParent();

        DispatchNode replacement;
        if (callTarget == null) {
            /* Corner case: the function is not defined, so report an error to
             * the user. */
            throw new RuntimeException("Call of undefined function");
        } else if (depth < INLINE_CACHE_SIZE) {
            /* Extend the inline cache. Allocate the new cache entry, and the
             * new end of the cache. */
            DispatchNode next = new UninitializedDispatchNode();
            replacement = new DirectDispatchNode(next, callTarget);
            /* Replace ourself with the new cache entry. */
            this.replace(replacement);
        } else {
            /* Cache size exceeded, fall back to a single generic dispatch
             * node. */
            replacement = new GenericDispatchNode();
            /* Replace the whole chain, not just ourself, with the new generic
             * node. */
            invokeNode.dispatchNode.replace(replacement);
        }

        /*
         * Execute the newly created node perform the actual dispatch. That
         * saves us from duplicating the actual call logic here.
         */
        return replacement.executeDispatch(virtualFrame, callTarget, arguments);
    }
}
