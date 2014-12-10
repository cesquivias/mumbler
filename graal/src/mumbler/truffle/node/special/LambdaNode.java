package mumbler.truffle.node.special;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "function", type = MumblerFunction.class)
public abstract class LambdaNode extends MumblerNode {
    public abstract MumblerFunction getFunction();

    private boolean scopeSet = false;

    @Specialization(guards = "isScopeSet")
    public MumblerFunction getScopedFunction(VirtualFrame virtualFrame) {
        return this.getFunction();
    }

    @Specialization(contains = {"getScopedFunction"})
    public Object getMumblerFunction(VirtualFrame virtualFrame) {
        MumblerFunction function = this.getFunction();
        function.setLexicalScope(virtualFrame.materialize());
        return function;
    }

    protected boolean isScopeSet() {
        return this.scopeSet;
    }
}
