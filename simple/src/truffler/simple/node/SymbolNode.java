package truffler.simple.node;

import truffler.simple.env.Environment;

public class SymbolNode extends Node {
    public final String name;

    public SymbolNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "'" + this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SymbolNode &&
                this.name.equals(((SymbolNode) other).name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public Object eval(Environment env) {
        return env.getValue(this.name);
    }
}
