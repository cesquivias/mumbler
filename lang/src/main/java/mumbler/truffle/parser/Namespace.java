package mumbler.truffle.parser;

import org.antlr.v4.runtime.misc.Pair;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

public class Namespace {
    /**
     * The name for the namespace at the top level of a file.
     */
    public static final String TOP_NS = "<top>";
    /**
     * The name of the global namespace that contains all predefined variables.
     */
    private static final String GLOBAL_NS = "<global>";

    private final String functionName;
    private final Namespace parent;
    private final FrameDescriptor frameDescriptor;

    public Namespace(FrameDescriptor frameDescriptor) {
        this.functionName = GLOBAL_NS;
        this.parent = null;
        this.frameDescriptor = frameDescriptor;
    }

    public Namespace(String name, Namespace parent) {
        this.functionName = name;
        this.parent = parent;
        this.frameDescriptor = new FrameDescriptor();
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public Namespace getParent() {
        return this.parent;
    }

    public FrameDescriptor getFrameDescriptor() {
        return this.frameDescriptor;
    }

    public FrameSlot addIdentifier(String id) {
        return this.frameDescriptor.addFrameSlot(id);
    }

    public Pair<Integer, FrameSlot> getIdentifier(String id) {
        int depth = 0;
        Namespace current = this;
        FrameSlot slot = current.frameDescriptor.findFrameSlot(id);
        while (slot == null) {
            depth++;
            current = current.parent;
            if (current == null) {
                return new Pair<>(-1, null);
            }
            slot = current.frameDescriptor.findFrameSlot(id);
        }
        if (current.parent == null) {
            return new Pair<>(-1, slot);
        }
        return new Pair<>(depth, slot);
    }
}