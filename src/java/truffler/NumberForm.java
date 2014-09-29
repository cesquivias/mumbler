public class NumberForm implements Form {
    private final long num;

    public NumberForm(long num) {
        this.num = num;
    }

    @Override
    public Object eval() {
        return new Long(this.num);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NumberForm &&
            this.num == ((NumberForm) other).num;
    }
}
