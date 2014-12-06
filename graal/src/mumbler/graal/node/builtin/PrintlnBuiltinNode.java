package mumbler.graal.node.builtin;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "println")
public abstract class PrintlnBuiltinNode extends BuiltinNode {
    @Specialization
    public long println(long value) {
        doPrint(value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(long value) {
        System.out.println(value);
    }

    @Specialization
    public Object println(Object value) {
        doPrint(value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(Object value) {
        System.out.println(value);
    }
}
