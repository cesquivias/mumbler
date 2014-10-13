package truffler.graal.node.expression;

import truffler.graal.node.ExpressionNode;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
@NodeInfo(shortName = "+")
public abstract class AddNode extends ExpressionNode {

    @Specialization
    protected long add(long left, long right) {
        return ExactMath.addExact(left, right);
    }

    @Specialization(guards = "isString")
    protected String add(Object left, Object right) {
        return left.toString() + right.toString();
    }

    protected boolean isString(Object a, Object b) {
        return a instanceof String || b instanceof String;
    }
}
