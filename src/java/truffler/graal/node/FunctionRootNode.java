package truffler.graal.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

public final class FunctionRootNode extends RootNode {
    @Child private ExpressionNode bodyNode;

    public FunctionRootNode(SourceSection sourceSection,
            FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
        super(sourceSection, frameDescriptor);
        this.bodyNode = bodyNode;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.bodyNode.execute(virtualFrame);
    }
}
