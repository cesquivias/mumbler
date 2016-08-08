package mumbler.truffle.node.builtin.io;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "now")
@GenerateNodeFactory
public abstract class NowBuiltinNode extends BuiltinNode {
    @Specialization
    protected long now() {
        return System.currentTimeMillis();
    }
}
