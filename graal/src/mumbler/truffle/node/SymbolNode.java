package mumbler.truffle.node;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class SymbolNode extends MumblerNode {

    public abstract FrameSlot getSlot();

    @CompilationFinal
    FrameSlot resolvedSlot;
    @CompilationFinal
    int lookupDepth;

    public static interface FrameGet<T> {
        public T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    @ExplodeLoop
    public <T> T readUpStack(FrameGet<T> getter, Frame frame)
            throws FrameSlotTypeException {
        if (this.resolvedSlot == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.resolveSlot(getter, frame);
        }

        Frame lookupFrame = frame;
        for (int i = 0; i < this.lookupDepth; i++) {
            lookupFrame = this.getLexicalScope(lookupFrame);
        }
        return getter.get(lookupFrame, this.resolvedSlot);
    }

    private <T> void resolveSlot(FrameGet<T> getter, Frame frame)
            throws FrameSlotTypeException {

        FrameSlot slot = this.getSlot();
        int depth = 0;
        Object identifier = slot.getIdentifier();
        T value = getter.get(frame, slot);
        while (value == null) {
            depth++;
            frame = this.getLexicalScope(frame);
            if (frame == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new RuntimeException("Unknown variable: "
                        + this.getSlot().getIdentifier());
            }
            FrameDescriptor desc = frame.getFrameDescriptor();
            slot = desc.findFrameSlot(identifier);
            if (slot != null) {
                value = getter.get(frame, slot);
            }
        }
        this.lookupDepth = depth;
        this.resolvedSlot = slot;
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

    protected Frame getLexicalScope(Frame frame) {
        Object[] args = frame.getArguments();
        if (args.length > 0) {
            return (Frame) frame.getArguments()[0];
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "'" + this.getSlot().getIdentifier();
    }
}
