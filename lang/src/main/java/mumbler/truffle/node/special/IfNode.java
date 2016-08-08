package mumbler.truffle.node.special;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.source.SourceSection;

public class IfNode extends MumblerNode {
    @Child private MumblerNode testNode;
    @Child private MumblerNode thenNode;
    @Child private MumblerNode elseNode;

    private final ConditionProfile conditionProfile =
            ConditionProfile.createBinaryProfile();

    public IfNode(MumblerNode testNode, MumblerNode thenNode,
            MumblerNode elseNode, SourceSection sourceSection) {
        this.testNode = testNode;
        this.thenNode = thenNode;
        this.elseNode = elseNode;
        setSourceSection(sourceSection);
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        if (this.conditionProfile.profile(this.testResult(virtualFrame))) {
            return this.thenNode.execute(virtualFrame);
        } else {
            return this.elseNode.execute(virtualFrame);
        }
    }

    @Override
    public void setIsTail() {
        super.setIsTail();
        this.thenNode.setIsTail();
        this.elseNode.setIsTail();
    }

    private boolean testResult(VirtualFrame virtualFrame) {
        try {
            return this.testNode.executeBoolean(virtualFrame);
        } catch (UnexpectedResultException e) {
            Object result = this.testNode.execute(virtualFrame);
            return result != MumblerList.EMPTY;
        }
    }

    @Override
    public String toString() {
        return "(if " + this.testNode + " "  + this.elseNode + ")";
    }
}
