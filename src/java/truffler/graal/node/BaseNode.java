package truffler.graal.node;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class BaseNode extends Node {
    public abstract Object execute(VirtualFrame virtualFrame);
}
