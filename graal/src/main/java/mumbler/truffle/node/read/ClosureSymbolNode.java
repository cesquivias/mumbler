package mumbler.truffle.node.read;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

@NodeField(name = "depth", type = int.class)
public abstract class ClosureSymbolNode extends SymbolNode {

    /**
     * Functional interface to get right type out of {@link VirtualFrame}.
     */
    public static interface FrameGet<T> {
        public T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    public abstract int getDepth();

    @ExplodeLoop
    public <T> T readUpStack(FrameGet<T> getter, Frame frame)
            throws FrameSlotTypeException {

        Frame lookupFrame = frame;
        for (int i = 0; i < this.getDepth(); i++) {
            lookupFrame = getLexicalScope(lookupFrame);
        }
        return getter.get(lookupFrame, this.getSlot());
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

    @Specialization(contains = { "readLong", "readBoolean", "readObject" })
    protected Object read(VirtualFrame virtualFrame) {
        try {
            return this.readUpStack(Frame::getValue, virtualFrame);
        } catch (FrameSlotTypeException e) {
            // FrameSlotTypeException not thrown
        }
        return null;
    }
}
