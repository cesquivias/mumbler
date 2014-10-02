package truffler.simple.form;

import truffler.simple.env.Environment;

public class SymbolForm extends Form {
    public final String name;

    public SymbolForm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "'" + this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SymbolForm &&
            this.name.equals(((SymbolForm) other).name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public Object eval(Environment env) {
        return env.getValue(this);
    }
}
