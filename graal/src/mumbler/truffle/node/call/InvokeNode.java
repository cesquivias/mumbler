package mumbler.truffle.node.call;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class InvokeNode extends MumblerNode {
    @Child protected MumblerNode functionNode;
    @Children protected final MumblerNode[] argumentNodes;
    @Child protected DispatchNode dispatchNode;

    public InvokeNode(MumblerNode functionNode, MumblerNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.dispatchNode = new DispatchNode();
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        MumblerFunction function = this.evaluateFunction(virtualFrame);
        CompilerAsserts.compilationConstant(this.argumentNodes.length);

        Object[] argumentValues = new Object[this.argumentNodes.length + 1];
        argumentValues[0] = function.getLexicalScope();
        for (int i=1; i<=argumentValues.length; i++) {
            argumentValues[i] = this.argumentNodes[i-1].execute(virtualFrame);
        }

        return this.dispatchNode.executeDispatch(virtualFrame, function,
                argumentValues);
    }

    private MumblerFunction evaluateFunction(VirtualFrame virtualFrame) {
        try {
            return this.functionNode.executeMumblerFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new UnsupportedSpecializationException(this,
                    new Node[] {this.functionNode}, e);
        }
    }
}
