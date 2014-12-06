package mumbler.graal.node;

import mumbler.graal.node.special.DefineNodeFactory;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class MumblerRootNode extends RootNode {
    @Children private final MumblerNode[] bodyNodes;

    public MumblerRootNode(MumblerNode[] bodyNodes) {
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        int secondToLast = this.bodyNodes.length - 1;
        CompilerAsserts.compilationConstant(secondToLast);
        for (int i=0; i<secondToLast; i++) {
            this.bodyNodes[i].execute(virtualFrame);
        }
        return this.bodyNodes[secondToLast].execute(virtualFrame);
    }

    public static MumblerRootNode create(FrameSlot[] argumentNames,
            MumblerNode[] bodyNodes) {
        MumblerNode[] allNodes = new MumblerNode[argumentNames.length
                                                 + bodyNodes.length];
        for (int i=0; i<argumentNames.length; i++) {
            allNodes[i] = DefineNodeFactory.create(
                    new ReadArgumentNode(i), argumentNames[i]);
        }
        System.arraycopy(bodyNodes, bodyNodes.length, allNodes,
                argumentNames.length, bodyNodes.length);
        return new MumblerRootNode(bodyNodes);
    }
}
