package mumbler.truffle.node;

import java.math.BigInteger;

import mumbler.truffle.MumblerTypes;
import mumbler.truffle.MumblerTypesGen;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@TypeSystemReference(MumblerTypes.class)
@NodeInfo(language = "Mumbler Language", description = "The abstract base node for all expressions")
public abstract class MumblerNode extends Node {
    @CompilationFinal
    private boolean isTail = false;

    public boolean isTail() {
        return this.isTail;
    }

    public void setIsTail() {
        this.isTail = true;
    }

    public abstract Object execute(VirtualFrame virtualFrame);

    public long executeLong(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectLong(this.execute(virtualFrame));
    }

    public boolean executeBoolean(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectBoolean(this.execute(virtualFrame));
    }

    public BigInteger executeBigInteger(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectBigInteger(this.execute(virtualFrame));
    }

    public MumblerSymbol executeMumblerSymbol(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectMumblerSymbol(this.execute(virtualFrame));
    }

    public MumblerFunction executeMumblerFunction(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectMumblerFunction(
                this.execute(virtualFrame));
    }

    public MumblerList<?> executeMumblerList(VirtualFrame virtualFrame)
            throws UnexpectedResultException {
        return MumblerTypesGen.expectMumblerList(this.execute(virtualFrame));
    }

    public String executeString(VirtualFrame virtualFrame)
            throws UnexpectedResultException{
        return MumblerTypesGen.expectString(this.execute(virtualFrame));
    }

    protected boolean isArgumentIndexInRange(VirtualFrame virtualFrame,
            int index) {
        return (index + 1) < virtualFrame.getArguments().length;
    }

    protected Object getArgument(VirtualFrame virtualFrame, int index) {
        return virtualFrame.getArguments()[index + 1];
    }

    protected static MaterializedFrame getLexicalScope(Frame frame) {
        Object[] args = frame.getArguments();
        if (args.length > 0) {
            return (MaterializedFrame) frame.getArguments()[0];
        } else {
            return null;
        }
    }
}
