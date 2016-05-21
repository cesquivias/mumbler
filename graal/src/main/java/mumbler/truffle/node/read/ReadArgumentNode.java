package mumbler.truffle.node.read;

import mumbler.truffle.MumblerException;
import mumbler.truffle.node.MumblerNode;

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
            throw new MumblerException("Not enough arguments passed. Missing " +
                    this.argumentIndex + 1 + "th argument");
        }
        return this.getArgument(virtualFrame, this.argumentIndex);
    }

    @Override
    public String toString() {
        return "(arg " + this.argumentIndex + ")";
    }
}
