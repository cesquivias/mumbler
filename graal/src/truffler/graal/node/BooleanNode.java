package truffler.graal.node;

import truffler.graal.env.Environment;

public class BooleanNode extends Node {

    public static final BooleanNode TRUE = new BooleanNode(Boolean.TRUE);
    public static final BooleanNode FALSE = new BooleanNode(Boolean.FALSE);

    private final Boolean value;

    private BooleanNode(Boolean value) {
        this.value = value;
    }

    @Override
    public Object eval(Environment env) {
        return this.value;
    }
}
