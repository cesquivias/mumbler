package mumbler.truffle.node.builtin.arithmetic;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "/")
@GenerateNodeFactory
public abstract class DivBuiltinNode extends BuiltinNode {
    @Specialization
    protected long divide(long value0, long value1) {
        return value0 / value1;
    }

    @Specialization
    protected BigInteger divide(BigInteger value0, BigInteger value1) {
        return value0.divide(value1);
    }
}
