package mumbler.graal.node;

import com.oracle.truffle.api.CompilerAsserts;
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
}
