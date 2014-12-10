package mumbler.truffle.node.builtin;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "now")
public abstract class NowBuiltinNode extends BuiltinNode {
    @Specialization
    protected long now() {
        return System.currentTimeMillis();
    }
}
