package mumbler.graal.node.builtin;

import mumbler.graal.node.MumblerNode;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments", type = MumblerNode[].class)
public abstract class BuiltinNode extends MumblerNode {
}
