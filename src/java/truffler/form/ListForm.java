package truffler.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import truffler.Environment;
import truffler.Fn;

public class ListForm implements Form, Iterable<Form> {
    public static final ListForm EMPTY = new ListForm();

    private final Form car;
    private final ListForm cdr;

    private ListForm() {
        this.car = null;
        this.cdr = null;
    }

    private ListForm(Form car, ListForm cdr) {
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

    public long length() {
        if (this == EMPTY) {
            return 0;
        }

        long len = 1;
        ListForm l = this.cdr;
        while (l != EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    public Iterator<Form> iterator() {
        return new Iterator<Form>() {
            private ListForm l = ListForm.this;

            @Override
            public boolean hasNext() {
                return this.l != EMPTY;
            }

            @Override
            public Form next() {
                if (this.l == EMPTY) {
                    throw new IllegalStateException("At end of list");
                }
                Form car = this.l.car;
                this.l = this.l.cdr;
                return car;
            }

            @Override
            public void remove() {
                throw new IllegalStateException("Iterator is immutable");
            }
        };
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
    public Object eval(Environment env) {
        Fn fn = (Fn) this.car.eval(env);

        List<Object> args = new ArrayList<Object>();
        for (Form form : this.cdr) {
            args.add(form.eval(env));
        }
        return fn.apply(args.toArray());
    }
}
