package mumbler.graal.node;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class SymbolNode extends MumblerNode {
    public abstract FrameSlot getSlot();

    public static interface FrameGet<T> {
        public T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    public <T> T readUpStack(FrameGet<T> getter, Frame frame)
            throws FrameSlotTypeException {
        T value = getter.get(frame, this.getSlot());
        while (value == null) {
            frame = this.getLexicalScope(frame);
            if (frame == null) {
                throw new RuntimeException("Unknown variable: " +
                        this.getSlot().getIdentifier());
            }
            value = getter.get(frame, this.getSlot());
        }
        return value;
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.readUpStack(Frame::getLong, virtualFrame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.readUpStack(Frame::getBoolean, virtualFrame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame)
            throws FrameSlotTypeException {
        return this.readUpStack(Frame::getObject, virtualFrame);
    }

    @Specialization(contains = {"readLong", "readBoolean", "readObject"})
    protected Object read(VirtualFrame virtualFrame) {
        try {
            return this.readUpStack(Frame::getValue, virtualFrame);
        } catch (FrameSlotTypeException e) {
            // FrameSlotTypeException not thrown
        }
        return null;
    }

    protected Frame getLexicalScope(Frame frame) {
        return (Frame) frame.getArguments()[0];
    }
}
