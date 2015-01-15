package mumbler.truffle.node;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadArgumentNode extends MumblerNode {
    public final int argumentIndex;

    public ReadArgumentNode(int argumentIndex) {
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (!this.isArgumentIndexInRange(virtualFrame, this.argumentIndex)) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Not enough arguments passed");
        }
        return this.getArgument(virtualFrame, this.argumentIndex);
    }

    @Override
    public String toString() {
        return "(arg " + this.argumentIndex + ")";
    }
}
