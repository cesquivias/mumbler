package mumbler.truffle.node.literal;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.syntax.LongSyntax;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LongNode extends MumblerNode {
    public final long number;

    public LongNode(LongSyntax syntax) {
        this.number = syntax.getValue();
        setSourceSection(syntax.getSourceSection());
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
