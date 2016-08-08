package mumbler.truffle.node.literal;

import java.math.BigInteger;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.syntax.BigIntegerSyntax;

import com.oracle.truffle.api.frame.VirtualFrame;

public class BigIntegerNode extends MumblerNode {
    public final BigInteger value;

    public BigIntegerNode(BigIntegerSyntax number) {
        this.value = number.getValue();
        setSourceSection(number.getSourceSection());
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
