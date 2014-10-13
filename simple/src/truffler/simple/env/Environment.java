package truffler.simple.env;

import java.util.HashMap;

public class Environment {
    private final HashMap<String, Object> env = new HashMap<String, Object>();

    private final Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public Object getValue(String name) {
        if (this.env.containsKey(name)) {
            return this.env.get(name);
        } else if (this.parent != null) {
            return this.parent.getValue(name);
        } else {
            throw new RuntimeException("No variable: " + name);
        }
    }

    public void putValue(String name, Object value) {
        this.env.put(name, value);
    }

    public static Environment getBaseEnvironment() {
        Environment env = new Environment();
        env.putValue("+", BuiltinFn.PLUS);
        env.putValue("-", BuiltinFn.MINUS);
        env.putValue("*", BuiltinFn.MULT);
        env.putValue("/", BuiltinFn.DIV);
        env.putValue("=", BuiltinFn.EQUALS);
        env.putValue("<", BuiltinFn.LESS_THAN);
        env.putValue(">", BuiltinFn.GREATER_THAN);
        env.putValue("list", BuiltinFn.LIST);
        env.putValue("car", BuiltinFn.CAR);
        env.putValue("cdr", BuiltinFn.CDR);
        env.putValue("println", BuiltinFn.PRINTLN);
        return env;
    }
}
