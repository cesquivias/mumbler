package mumbler.graal.type;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.MumblerRootNode;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

public class MumblerFunction {
    public final RootCallTarget callTarget;
    private MaterializedFrame lexicalScope;

    public MumblerFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public MaterializedFrame getLexicalScope() {
        return this.lexicalScope;
    }

    public void setLexicalScope(MaterializedFrame lexicalScope) {
        this.lexicalScope = lexicalScope;
    }

    public static MumblerFunction create(FrameSlot[] arguments,
            MumblerNode[] bodyNodes, VirtualFrame currentFrame) {
        return new MumblerFunction(
                Truffle.getRuntime().createCallTarget(
                        MumblerRootNode.create(arguments, bodyNodes)));
    }
}
