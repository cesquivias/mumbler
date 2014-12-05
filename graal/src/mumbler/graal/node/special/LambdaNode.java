package mumbler.graal.node.special;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class LambdaNode extends MumblerNode {
    public final MumblerFunction function;

    public LambdaNode(MumblerFunction function) {
        this.function = function;
    }

    @Override
    public MumblerFunction executeMumblerFunction(VirtualFrame virtualFrame) {
        return this.function;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.function;
    }

    public static MumblerFunction createMumblerFunction(RootNode rootNode) {
        return new MumblerFunction(
                Truffle.getRuntime().createCallTarget(rootNode));
    }
}
