package mumbler.truffle.node.read;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class LocalSymbolNode extends SymbolNode {

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame)
            throws FrameSlotTypeException{
        return virtualFrame.getLong(getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame)
            throws FrameSlotTypeException{
        return virtualFrame.getBoolean(getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame)
            throws FrameSlotTypeException{
        return virtualFrame.getObject(getSlot());
    }

    @Specialization(contains = { "readLong", "readBoolean", "readObject" })
    protected Object read(VirtualFrame virtualFrame) {
        return virtualFrame.getValue(getSlot());
    }
}
