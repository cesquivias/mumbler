package truffler.simple.env;

import java.util.HashMap;

import truffler.simple.form.SymbolForm;

public class Environment {
    private final HashMap<SymbolForm, Object> env =
        new HashMap<SymbolForm, Object>();

    private final Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public Object getValue(SymbolForm sym) {
        if (this.env.containsKey(sym)) {
            return this.env.get(sym);
        } else if (this.parent != null) {
            return this.parent.getValue(sym);
        } else {
            return null;
        }
    }

    public void putValue(SymbolForm sym, Object value) {
        this.env.put(sym, value);
    }

    public static Environment getBaseEnvironment() {
        Environment env = new Environment();
        env.putValue(new SymbolForm("+"), BuiltinFn.PLUS);
        env.putValue(new SymbolForm("-"), BuiltinFn.MINUS);
        env.putValue(new SymbolForm("*"), BuiltinFn.MULT);
        env.putValue(new SymbolForm("/"), BuiltinFn.DIV);
        env.putValue(new SymbolForm("="), BuiltinFn.EQUALS);
        return env;
    }
}
