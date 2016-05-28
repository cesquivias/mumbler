package mumbler.truffle.node.read;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "globalFrame", type = MaterializedFrame.class)
public abstract class GlobalSymbolNode extends SymbolNode {

    public abstract MaterializedFrame getGlobalFrame();

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getGlobalFrame().getLong(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getGlobalFrame().getBoolean(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getGlobalFrame().getObject(this.getSlot());
    }

    @Specialization(contains = { "readLong", "readBoolean", "readObject", })
    protected Object read(VirtualFrame virtualFrame) {
        return this.getGlobalFrame().getValue(this.getSlot());
    }
}
