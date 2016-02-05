package mumbler.truffle.node.builtin.arithmetic;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "*")
@GenerateNodeFactory
public abstract class MulBuiltinNode extends BuiltinNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long multiply(long value0, long value1) {
        return ExactMath.multiplyExact(value0, value1);
    }

    @Specialization
    protected BigInteger multiply(BigInteger value0, BigInteger value1) {
        return value0.multiply(value1);
    }
}
