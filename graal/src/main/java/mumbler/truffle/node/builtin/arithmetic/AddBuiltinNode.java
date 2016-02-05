package mumbler.truffle.node.builtin.arithmetic;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "+")
@GenerateNodeFactory
public abstract class AddBuiltinNode extends BuiltinNode {
    @Specialization(rewriteOn = ArithmeticException.class)
    public long add(long value0, long value1) {
        return ExactMath.addExact(value0, value1);
    }

    @Specialization
    protected BigInteger add(BigInteger value0, BigInteger value1) {
        return value0.add(value1);
    }
}
