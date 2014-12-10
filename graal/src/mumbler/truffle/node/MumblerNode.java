package mumbler.truffle.node;

import mumbler.truffle.MumblerTypes;
import mumbler.truffle.MumblerTypesGen;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@TypeSystemReference(MumblerTypes.class)
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

    protected boolean isArgumentIndexInRange(VirtualFrame virtualFrame,
            int index) {
        return (index + 1) < virtualFrame.getArguments().length;
    }

    protected Object getArgument(VirtualFrame virtualFrame, int index) {
        return virtualFrame.getArguments()[index + 1];
    }
}
