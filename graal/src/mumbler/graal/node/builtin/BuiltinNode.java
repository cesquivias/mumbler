package mumbler.graal.node.builtin;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.MumblerRootNode;
import mumbler.graal.node.ReadArgumentNode;
import mumbler.graal.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeFactory;

@NodeChild(value = "arguments", type = MumblerNode[].class)
public abstract class BuiltinNode extends MumblerNode {
    public static MumblerFunction createBuiltinFunction(
            NodeFactory<? extends BuiltinNode> factory) {
        int argumentCount = factory.getExecutionSignature().size();
        MumblerNode[] argumentNodes = new MumblerNode[argumentCount];
        for (int i=0; i<argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }
        BuiltinNode node = factory.createNode((Object) argumentNodes);
        return new MumblerFunction(Truffle.getRuntime().createCallTarget(
                new MumblerRootNode(new MumblerNode[] {node})));
    }
}
