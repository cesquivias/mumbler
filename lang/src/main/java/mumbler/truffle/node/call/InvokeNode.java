package mumbler.truffle.node.call;

import java.util.Arrays;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.source.SourceSection;

public class InvokeNode extends MumblerNode {
    @Child protected MumblerNode functionNode;
    @Children protected final MumblerNode[] argumentNodes;
    @Child protected DispatchNode dispatchNode;

    public InvokeNode(MumblerNode functionNode, MumblerNode[] argumentNodes,
            SourceSection sourceSection) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.dispatchNode = new UninitializedDispatchNode();
        setSourceSection(sourceSection);
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        MumblerFunction function = this.evaluateFunction(virtualFrame);
        CompilerAsserts.compilationConstant(this.argumentNodes.length);
        CompilerAsserts.compilationConstant(this.isTail());

        Object[] argumentValues = new Object[this.argumentNodes.length + 1];
        argumentValues[0] = function.getLexicalScope();
        for (int i=0; i<this.argumentNodes.length; i++) {
            argumentValues[i+1] = this.argumentNodes[i].execute(virtualFrame);
        }

        if (this.isTail()) {
            throw new TailCallException(function.callTarget, argumentValues);
        } else {
            return this.call(virtualFrame, function.callTarget, argumentValues);
        }
    }

    public Object call(VirtualFrame virtualFrame, CallTarget callTarget,
            Object[] arguments) {
        while (true) {
            try {
                return this.dispatchNode.executeDispatch(virtualFrame,
                        callTarget, arguments);
            } catch (TailCallException e) {
                callTarget = e.callTarget;
                arguments = e.arguments;
            }
        }
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
