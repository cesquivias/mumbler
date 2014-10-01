package truffler.env;

import java.util.HashMap;

import truffler.form.SymbolForm;

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
}
