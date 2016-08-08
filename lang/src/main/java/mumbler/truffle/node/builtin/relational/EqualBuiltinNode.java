package mumbler.truffle.node.builtin.relational;

import java.math.BigInteger;

import mumbler.truffle.node.builtin.BuiltinNode;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "=")
@GenerateNodeFactory
public abstract class EqualBuiltinNode extends BuiltinNode {
    @Specialization
    protected boolean equals(long value0, long value1) {
        return value0 == value1;
    }

    @Specialization
    protected boolean equals(BigInteger value0, BigInteger value1) {
        return value0.equals(value1);
    }

    @Specialization
    protected boolean equals(boolean value0, boolean value1) {
        return value0 == value1;
    }

    @Specialization
    protected boolean equals(MumblerList<?> value0, MumblerList<?> value1) {
        return value0.equals(value1);
    }

    @Specialization
    protected boolean equals(Object value0, boolean value1) {
        return value0.equals(value1);
    }
}
