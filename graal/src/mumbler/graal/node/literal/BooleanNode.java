package mumbler.graal.node.literal;

import mumbler.graal.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BooleanNode extends MumblerNode {
    public final boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    @Override
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        return this.value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.value;
    }

    @Override
    public String toString() {
        if (this.value) {
            return "#t";
        } else {
            return "#f";
        }
    }
}
