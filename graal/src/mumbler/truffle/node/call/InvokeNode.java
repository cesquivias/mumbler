package mumbler.truffle.node.call;

import java.util.Arrays;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class InvokeNode extends MumblerNode {
    @Child protected MumblerNode functionNode;
    @Children protected final MumblerNode[] argumentNodes;
    @Child protected IndirectCallNode callNode;

    public InvokeNode(MumblerNode functionNode, MumblerNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.callNode = Truffle.getRuntime().createIndirectCallNode();
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

        return this.callNode.call(virtualFrame, function.callTarget,
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

    @Override
    public String toString() {
        return "(apply " + this.functionNode + " " +
                Arrays.toString(this.argumentNodes) + ")";
    }
}
