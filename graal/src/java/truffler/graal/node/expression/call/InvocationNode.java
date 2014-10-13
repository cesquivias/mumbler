package truffler.graal.node.expression.call;

import truffler.graal.node.ExpressionNode;
import truffler.graal.node.Function;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "invoke")
public final class InvocationNode extends ExpressionNode {
    @Child protected ExpressionNode function;
    @Children protected ExpressionNode[] arguments;
    @Child protected DispatchNode dispatchNode;

    public InvocationNode(ExpressionNode function, ExpressionNode[] arguments) {
        this.function = function;
        this.arguments = arguments;
        this.dispatchNode = new GenericDispatchNode();
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        Function fn = this.evaluateFunction(virtualFrame);

        CompilerAsserts.compilationConstant(this.arguments.length);

        Object[] argumentValues = new Object[this.arguments.length];
        for (int i=0; i < this.arguments.length; i++) {
            argumentValues[i] = this.arguments[i].execute(virtualFrame);
        }
        return this.dispatchNode.execute(
                virtualFrame, fn, argumentValues);
    }

    private Function evaluateFunction(VirtualFrame virtualFrame) {
        try {
            return this.function.executeFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new UnsupportedSpecializationException(
                    this, new Node[] {this.function}, e.getResult());
        }
    }
}
