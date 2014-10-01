package truffler;

import java.util.HashMap;

import truffler.form.SymbolForm;

public class Environment {
    private final HashMap<SymbolForm, Object> env =
        new HashMap<SymbolForm, Object>();

    public Environment() {}

    public Object getValue(SymbolForm sym) {
        return this.env.get(sym);
    }

    public void putValue(SymbolForm sym, Object value) {
        this.env.put(sym, value);
    }
}
