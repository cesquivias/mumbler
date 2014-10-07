package truffler.graal.node;

import com.oracle.truffle.api.frame.VirtualFrame;

public class AddNode extends BaseNode {
    @Children
    private final NumberNode[] args;

    public AddNode(NumberNode[] args) {
        this.args = args;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        long total = 0;
        for (NumberNode num : this.args) {
            total += (Long) num.execute(virtualFrame);
        }
        return total;
    }

}
