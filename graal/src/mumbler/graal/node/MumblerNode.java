package mumbler.graal.node;

import mumbler.graal.MumblerTypesGen;
import mumbler.graal.type.MumblerFunction;
import mumbler.graal.type.MumblerList;
import mumbler.graal.type.MumblerSymbol;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(language = "Mumbler Language", description = "The abstract base node for all expressions")
public abstract class MumblerNode extends Node {

    public abstract Object execute(VirtualFrame virtualFrame);

    public long executeLong(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.MUMBLERTYPES.expectLong(
                this.execute(virtualFrame));
    }

    public boolean executeBoolean(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.MUMBLERTYPES.expectBoolean(
                this.execute(virtualFrame));
    }

    public MumblerSymbol executeMumblerSymbol(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.MUMBLERTYPES.expectMumblerSymbol(
                this.execute(virtualFrame));
    }

    public MumblerFunction executeMumblerFunction(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.MUMBLERTYPES.expectMumblerFunction(
                this.execute(virtualFrame));

    }

    public MumblerList<?> executeMumblerList(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.MUMBLERTYPES.expectMumblerList(
                this.execute(virtualFrame));
    }
}
