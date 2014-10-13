package truffler.graal.node;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.nodes.RootNode;

public class Function {
    private RootCallTarget rootTarget;

    public Function(RootNode rootNode) {
        this.rootTarget = Truffle.getRuntime().createCallTarget(rootNode);
    }

    public CallTarget getCallTarget() {
        return this.rootTarget;
    }
}
