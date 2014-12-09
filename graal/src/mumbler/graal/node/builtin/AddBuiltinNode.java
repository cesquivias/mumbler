package mumbler.graal.node.builtin;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "+")
public abstract class AddBuiltinNode extends BuiltinNode {
    @Specialization
    public long add(long value0, long value1) {
        return value0 + value1;
    }
}
