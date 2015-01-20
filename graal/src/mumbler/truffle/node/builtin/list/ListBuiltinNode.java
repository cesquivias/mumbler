package mumbler.truffle.node.builtin.list;

import mumbler.truffle.node.builtin.BuiltinNode;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

@GenerateNodeFactory
public abstract class ListBuiltinNode extends BuiltinNode {
    @Specialization
    protected MumblerList<Object> list(Object first, Object second) {
        return MumblerList.list(first, second);
    }
}
