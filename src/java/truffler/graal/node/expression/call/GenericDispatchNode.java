package truffler.graal.node.expression.call;

import truffler.graal.node.Function;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node.Child;

final class GenericDispatchNode extends DispatchNode {

    @Child private IndirectCallNode callNode = Truffle.getRuntime().createIndirectCallNode();

    @Override
    protected Object execute(VirtualFrame frame, Function function,
            Object[] arguments) {
        return this.callNode.call(frame, function.getCallTarget(), arguments);
    }

}
