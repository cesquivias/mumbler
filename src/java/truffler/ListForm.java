package truffler;

import java.util.List;

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

    public static ListForm list(List<Form> forms) {
        ListForm l = EMPTY;
        for (int i=forms.size()-1; i>=0; i--) {
            l = l.cons(forms.get(i));
        }
        return l;
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

        ListForm that = (ListForm) other;
        if (this.cdr == EMPTY && that.cdr != EMPTY) {
            return false;
        }
        return this.car.equals(that.car) && this.cdr.equals(that.cdr);
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
