package mumbler.graal.node;

import mumbler.graal.Evaluatable;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(language = "Mumbler Language", description = "The abstract base node for all expressions")
public abstract class MumblerNode extends Node implements Evaluatable  {
}
