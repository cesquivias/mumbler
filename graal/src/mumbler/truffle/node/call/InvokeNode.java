package mumbler.truffle.node.call;

import java.util.Arrays;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class InvokeNode extends MumblerNode {
    @Child protected MumblerNode functionNode;
    @Children protected final MumblerNode[] argumentNodes;
    @Child protected DirectCallNode callNode;

    public InvokeNode(MumblerNode functionNode, MumblerNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        MumblerFunction function = this.evaluateFunction(virtualFrame);
        //CompilerAsserts.compilationConstant(this.argumentNodes.length);

        Object[] argumentValues = new Object[this.argumentNodes.length + 1];
        argumentValues[0] = function.getLexicalScope();
        for (int i=0; i<this.argumentNodes.length; i++) {
            argumentValues[i+1] = this.argumentNodes[i].execute(virtualFrame);
        }

        if (this.callNode == null)  {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.callNode = this.insert(Truffle.getRuntime().createDirectCallNode(function.callTarget));
        }

        if (function.callTarget != this.callNode.getCallTarget()) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedOperationException("need to implement a proper inline cache.");
        }

        return this.callNode.call(virtualFrame, argumentValues);
    }

    private MumblerFunction evaluateFunction(VirtualFrame virtualFrame) {
        try {
            return this.functionNode.executeMumblerFunction(virtualFrame);
        } catch (UnexpectedResultException e) {
            throw new UnsupportedSpecializationException(this,
                    new Node[] {this.functionNode}, e);
        }
    }

    @Override
    public String toString() {
        return "(apply " + this.functionNode + " " +
                Arrays.toString(this.argumentNodes) + ")";
    }
}
