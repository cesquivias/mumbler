package truffler.graal.node;

import java.math.BigInteger;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@TypeSystemReference(TrufflerTypes.class)
@NodeInfo(description = "Abstract base node for all expressions")
public abstract class ExpressionNode extends Node {
    public abstract Object execute(VirtualFrame virtualFrame);

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return TrufflerTypesGen.TRUFFLERTYPES.expectLong(this.execute(frame));
    }

    public BigInteger executeBigInteger(VirtualFrame frame) throws UnexpectedResultException {
        return TrufflerTypesGen.TRUFFLERTYPES.expectBigInteger(this.execute(frame));
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return TrufflerTypesGen.TRUFFLERTYPES.expectBoolean(this.execute(frame));
    }

    public String executeString(VirtualFrame frame) throws UnexpectedResultException {
        return TrufflerTypesGen.TRUFFLERTYPES.expectString(this.execute(frame));
    }

    public Function executeFunction(VirtualFrame frame) throws UnexpectedResultException {
        return TrufflerTypesGen.TRUFFLERTYPES.expectFunction(this.execute(frame));
    }
}
