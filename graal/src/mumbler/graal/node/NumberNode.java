package mumbler.graal.node;

import mumbler.graal.env.Environment;

public class NumberNode extends MumblerNode {
    private final long num;

    public NumberNode(long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return Long.toString(this.num);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NumberNode &&
            this.num == ((NumberNode) other).num;
    }

    @Override
    public Object eval(Environment env) {
        return new Long(this.num);
    }
}
