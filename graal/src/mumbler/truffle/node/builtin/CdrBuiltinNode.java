package mumbler.truffle.node.builtin;

import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="cdr")
@GenerateNodeFactory
public abstract class CdrBuiltinNode extends BuiltinNode {
    @Specialization
    protected MumblerList<?> cdr(MumblerList<?> list) {
        return list.cdr();
    }
}
