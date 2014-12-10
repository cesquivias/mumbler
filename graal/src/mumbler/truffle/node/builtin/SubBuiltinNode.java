package mumbler.truffle.node.builtin;

import com.oracle.truffle.api.dsl.Specialization;

public abstract class SubBuiltinNode extends BuiltinNode {
    @Specialization
    protected long minus(long value0, long value1) {
        return value0 - value1;
    }
}
