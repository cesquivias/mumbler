package mumbler.truffle.node.read;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.frame.FrameSlot;

import mumbler.truffle.node.MumblerNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class SymbolNode extends MumblerNode {

    public abstract FrameSlot getSlot();

    @Override
    public String toString() {
        return "'" + this.getSlot().getIdentifier();
    }
}
