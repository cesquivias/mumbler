package truffler.simple.env;

import java.util.HashMap;

import truffler.simple.node.SymbolNode;

public class Environment {
    private final HashMap<SymbolNode, Object> env =
        new HashMap<SymbolNode, Object>();

    private final Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public Object getValue(SymbolNode sym) {
        if (this.env.containsKey(sym)) {
            return this.env.get(sym);
        } else if (this.parent != null) {
            return this.parent.getValue(sym);
        } else {
            return null;
        }
    }

    public void putValue(SymbolNode sym, Object value) {
        this.env.put(sym, value);
    }

    public static Environment getBaseEnvironment() {
        Environment env = new Environment();
        env.putValue(new SymbolNode("+"), BuiltinFn.PLUS);
        env.putValue(new SymbolNode("-"), BuiltinFn.MINUS);
        env.putValue(new SymbolNode("*"), BuiltinFn.MULT);
        env.putValue(new SymbolNode("/"), BuiltinFn.DIV);
        env.putValue(new SymbolNode("="), BuiltinFn.EQUALS);
        return env;
    }
}
