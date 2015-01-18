package mumbler.truffle.node.builtin;

import java.math.BigInteger;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="<")
@GenerateNodeFactory
public abstract class LessThanBuiltinNode extends BuiltinNode {
    @Specialization
    protected boolean lessThan(long value0, long value1) {
        return value0 < value1;
    }

    @Specialization
    protected boolean lessThan(BigInteger value0, BigInteger value1) {
        return value0.compareTo(value1) < 0;
    }
}
