package mumbler.truffle.type;

public class MumblerSymbol {
    public final String name;

    public MumblerSymbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
