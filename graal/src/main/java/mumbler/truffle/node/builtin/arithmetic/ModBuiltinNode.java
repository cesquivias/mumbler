package mumbler.truffle.node.builtin.arithmetic;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "%")
@GenerateNodeFactory
public abstract class ModBuiltinNode extends BuiltinNode {
    @Specialization
    protected long modulo(long value0, long value1) {
        return value0 % value1;
    }

    @Specialization
    protected BigInteger modulo(BigInteger value0, BigInteger value1) {
        return value0.mod(value1);
    }
}
