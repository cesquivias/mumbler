package truffler;

public class SymbolForm implements Form {
    private final String name;

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
    public Object eval() {
        return null;
    }
}
