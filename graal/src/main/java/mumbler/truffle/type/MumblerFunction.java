package mumbler.truffle.type;

import mumbler.truffle.TruffleMumblerMain;
import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.MumblerRootNode;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;

public class MumblerFunction {
    public final RootCallTarget callTarget;
    private MaterializedFrame lexicalScope;

    public MumblerFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }

    public MaterializedFrame getLexicalScope() {
        // TODO: Hack. We should just return null if we don't have a scope.
        return this.lexicalScope == null ? TruffleMumblerMain.globalScope :
            this.lexicalScope;
    }

    public void setLexicalScope(MaterializedFrame lexicalScope) {
        this.lexicalScope = lexicalScope;
    }

    public static MumblerFunction create(FrameSlot[] arguments,
            MumblerNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        return new MumblerFunction(
                Truffle.getRuntime().createCallTarget(
                        MumblerRootNode.create(arguments, bodyNodes, frameDescriptor)));
    }
}
