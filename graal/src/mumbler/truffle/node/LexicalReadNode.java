package mumbler.truffle.node;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeFields(value={
        @NodeField(name = "scope", type = MaterializedFrame.class),
        @NodeField(name = "slot", type = FrameSlot.class)
})
public abstract class LexicalReadNode extends MumblerNode {
    protected abstract MaterializedFrame getScope();
    protected abstract FrameSlot getSlot();

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getScope().getLong(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getScope().getBoolean(this.getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.getScope().getObject(this.getSlot());
    }

    @Specialization(contains = {"readLong", "readBoolean", "readObject"})
    public Object read(VirtualFrame virtualFrame) {
        return this.getScope().getValue(this.getSlot());
    }

}
