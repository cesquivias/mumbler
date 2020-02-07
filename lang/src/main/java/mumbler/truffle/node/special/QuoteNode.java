package mumbler.truffle.node.special;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.special.QuoteNode.QuoteKind;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("literalNode")
@NodeField(name = "kind", type = QuoteKind.class)
public abstract class QuoteNode extends MumblerNode {
	public enum QuoteKind {
		LONG,
		BOOLEAN,
		STRING,
		SYMBOL,
		LIST
	}

	protected abstract QuoteKind getKind();

	@Specialization(guards = "isLongKind()")
	protected long quoteLong(VirtualFrame virtualFrame, long value) {
		return value;
	}

	@Specialization(guards = "isBooleanKind()")
	protected boolean quoteBoolean(VirtualFrame virtualFrame, boolean value) {
		return value;
	}

	@Specialization(replaces = {"quoteLong", "quoteBoolean"})
	protected Object quote(VirtualFrame virtualFrame, Object value) {
		return value;
	}

	protected boolean isLongKind() {
		return this.getKind() == QuoteKind.LONG;
	}

	protected boolean isBooleanKind() {
		return this.getKind() == QuoteKind.BOOLEAN;
	}
}

