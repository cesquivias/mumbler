package mumbler.truffle.node.builtin.relational;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName=">")
@GenerateNodeFactory
public abstract class GreaterThanBuiltinNode extends BuiltinNode {
    @Specialization
    protected boolean greaterThan(long value0, long value1) {
        return value0 > value1;
    }

    @Specialization
    protected boolean greaterThan(BigInteger value0, BigInteger value1) {
        return value0.compareTo(value1) > 0;
    }
}
