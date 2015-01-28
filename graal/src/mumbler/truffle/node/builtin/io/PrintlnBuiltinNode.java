package mumbler.truffle.node.builtin.io;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "println")
@GenerateNodeFactory
public abstract class PrintlnBuiltinNode extends BuiltinNode {
    @Specialization
    public long println(long value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public boolean println(boolean value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public Object println(Object value) {
        System.out.println(value);
        return value;
    }
}
