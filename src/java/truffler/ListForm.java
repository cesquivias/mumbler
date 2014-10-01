package truffler;

public class ListForm implements Form {
    public static final ListForm EMPTY = new ListForm();

    private final Form car;
    private final Form cdr;

    private ListForm() {
        this.car = null;
        this.cdr = null;
    }

    private ListForm(Form car, Form cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public ListForm cons(Form form) {
        return new ListForm(form, this);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ListForm)) {
            return false;
        }
        if (this == EMPTY && other == EMPTY) {
            return true;
        }

        ListForm l = (ListForm) other;
        return this.car.equals(l.car) && this.cdr.equals(l.cdr);
    }
    
    @Override
    public String toString() {
        if (this == EMPTY) {
            return "()";
        }

        StringBuilder b = new StringBuilder("(" + this.car);
        Form rest = this.cdr;
        while (rest != null && rest != EMPTY) {
            b.append(" ");
            if (rest instanceof ListForm) {
                ListForm l = (ListForm) rest;
                b.append(l.car);
                rest = l.cdr;
            } else {
                b.append(rest);
                rest = null;
            }
        }
        b.append(")");
        return b.toString();
    }

    @Override
    public Object eval() {
        return null;
    }
}
