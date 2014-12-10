package mumbler.truffle.node.literal;

import mumbler.truffle.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BooleanNode extends MumblerNode {
    public static final BooleanNode TRUE = new BooleanNode(true);
    public static final BooleanNode FALSE = new BooleanNode(false);

    public final boolean value;

    private BooleanNode(boolean value) {
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
