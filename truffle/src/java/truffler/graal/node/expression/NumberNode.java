package truffler.graal.node.expression;

import truffler.graal.node.ExpressionNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class NumberNode extends ExpressionNode {
    private final long value;

    public NumberNode(long value) {
        super();
        this.value = value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.value;
    }
}
