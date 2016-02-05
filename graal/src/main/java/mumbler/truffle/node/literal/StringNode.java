package mumbler.truffle.node.literal;

import mumbler.truffle.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class StringNode extends MumblerNode {
    public final String str;

    public StringNode(String str) {
        this.str = str;
    }

    @Override
    public String executeString(VirtualFrame virtualFrame) {
        return this.str;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.str;
    }

}
