package mumbler.truffle.node.literal;

import java.math.BigInteger;

import mumbler.truffle.node.MumblerNode;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BigIntegerNode extends MumblerNode {
    public final BigInteger value;

    public BigIntegerNode(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger executeBigInteger(VirtualFrame virtualFrame) {
        return this.value;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
