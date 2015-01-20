package mumbler.truffle.node.builtin;

import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="car")
@GenerateNodeFactory
public abstract class CarBuiltinNode extends BuiltinNode {
    @Specialization
    protected Object car(MumblerList<?> list) {
        return list.car();
    }
}
