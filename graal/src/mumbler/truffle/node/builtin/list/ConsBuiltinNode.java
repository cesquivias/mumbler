package mumbler.truffle.node.builtin.list;

import mumbler.truffle.node.builtin.BuiltinNode;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="cons")
@GenerateNodeFactory
public abstract class ConsBuiltinNode extends BuiltinNode {
    @Specialization
    @SuppressWarnings("unchecked")
    protected MumblerList<?> cons(Object value, MumblerList<?> list) {
        return ((MumblerList<Object>) list).cons(value);
    }
}
