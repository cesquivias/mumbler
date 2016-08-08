package mumbler.truffle.node.builtin;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.MumblerRootNode;
import mumbler.truffle.node.read.ReadArgumentNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "arguments", type = MumblerNode[].class)
public abstract class BuiltinNode extends MumblerNode {
    public static MumblerFunction createBuiltinFunction(
            NodeFactory<? extends BuiltinNode> factory,
            VirtualFrame outerFrame) {
        int argumentCount = factory.getExecutionSignature().size();
        MumblerNode[] argumentNodes = new MumblerNode[argumentCount];
        for (int i=0; i<argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }
        BuiltinNode node = factory.createNode((Object) argumentNodes);
        return new MumblerFunction(Truffle.getRuntime().createCallTarget(
                new MumblerRootNode(new MumblerNode[] {node},
                        new FrameDescriptor())));
    }
}
