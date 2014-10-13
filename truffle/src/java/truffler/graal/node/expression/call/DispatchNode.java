package truffler.graal.node.expression.call;

import truffler.graal.node.Function;

import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class DispatchNode {
    protected abstract Object execute(
            VirtualFrame frame, Function function, Object[] arguments);
}
