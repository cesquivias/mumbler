package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class NumberNode extends MumblerNode {
    public final long number;

    public NumberNode(long number) {
        this.number = number;
    }

    @Override
    public long executeLong(VirtualFrame virtualFrame) {
        return this.number;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + this.number;
    }
}
