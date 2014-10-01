package truffler.form;

import truffler.Environment;

public class NumberForm implements Form {
    private final long num;

    public NumberForm(long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return Long.toString(this.num);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NumberForm &&
            this.num == ((NumberForm) other).num;
    }

    @Override
    public Object eval(Environment env) {
        return new Long(this.num);
    }
}
