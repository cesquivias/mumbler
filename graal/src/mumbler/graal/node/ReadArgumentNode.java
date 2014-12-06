package mumbler.graal.node;

import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadArgumentNode extends MumblerNode {
    public final int argumentIndex;

    public ReadArgumentNode(int argumentIndex) {
        this.argumentIndex = argumentIndex;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (!this.isArgumentIndexInRange(virtualFrame, this.argumentIndex)) {
            throw new IllegalArgumentException("Not enough arguments passed");
        }
        return this.getArgument(virtualFrame, this.argumentIndex);
    }
}
