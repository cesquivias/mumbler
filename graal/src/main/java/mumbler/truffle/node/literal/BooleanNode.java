package mumbler.truffle.node.literal;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.syntax.BooleanSyntax;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BooleanNode extends MumblerNode {
    public final boolean value;

    public BooleanNode(BooleanSyntax bool) {
        this.value = bool.getValue();
        setSourceSection(bool.getSourceSection());
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
