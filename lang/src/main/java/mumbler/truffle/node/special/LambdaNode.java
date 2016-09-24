package mumbler.truffle.node.special;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.MumblerRootNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "function", type = MumblerFunction.class)
public abstract class LambdaNode extends MumblerNode {
    public abstract MumblerFunction getFunction();

    private boolean scopeSet = false;

    @Specialization(guards = "isScopeSet()")
    public MumblerFunction getScopedFunction(VirtualFrame virtualFrame) {
        return this.getFunction();
    }

    @Specialization(contains = { "getScopedFunction" })
    public Object getMumblerFunction(VirtualFrame virtualFrame) {
        MumblerFunction function = this.getFunction();
        function.setLexicalScope(virtualFrame.materialize());
        return function;
    }

    protected boolean isScopeSet() {
        return this.scopeSet;
    }

    /**
     * The lambda expression is being set to a variable. Give the lambda the
     * name of the variable. Don't overwrite lambdas with existing names.
     *
     * @param name The name for the lambda.
     */
    public void setName(String name) {
        ((MumblerRootNode) this.getFunction().callTarget.getRootNode())
            .setName(name);
    }
}
