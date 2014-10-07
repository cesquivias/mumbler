package truffler.graal.node;

import com.oracle.truffle.api.frame.VirtualFrame;

public class NumberNode extends BaseNode {
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
