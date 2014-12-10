package mumbler.truffle.type;

import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.List;

public class MumblerList<T extends Object> implements Iterable<T> {
    public static final MumblerList<?> EMPTY = new MumblerList<>();

    public final T car;
    public final MumblerList<T> cdr;

    private MumblerList() {
        this.car = null;
        this.cdr = null;
    }

    private MumblerList(T car, MumblerList<T> cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @SafeVarargs
    public static <T> MumblerList<T> list(T... objs) {
        return list(asList(objs));
    }

    public static <T> MumblerList<T> list(List<T> objs) {
        @SuppressWarnings("unchecked")
        MumblerList<T> l = (MumblerList<T>) EMPTY;
        for (int i=objs.size()-1; i>=0; i--) {
            l = l.cons(objs.get(i));
        }
        return l;
    }

    public MumblerList<T> cons(T node) {
        return new MumblerList<T>(node, this);
    }

    public long length() {
        if (this == EMPTY) {
            return 0;
        }

        long len = 1;
        MumblerList<T> l = this.cdr;
        while (l != EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private MumblerList<T> l = MumblerList.this;

            @Override
            public boolean hasNext() {
                return this.l != EMPTY;
            }

            @Override
            public T next() {
                if (this.l == EMPTY) {
                    throw new IllegalStateException("At end of list");
                }
                T car = this.l.car;
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
        if (!(other instanceof MumblerList)) {
            return false;
        }
        if (this == EMPTY && other == EMPTY) {
            return true;
        }

        MumblerList<?> that = (MumblerList<?>) other;
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
        MumblerList<T> rest = this.cdr;
        while (rest != null && rest != EMPTY) {
            b.append(" ");
            b.append(rest.car);
            rest = rest.cdr;
        }
        b.append(")");
        return b.toString();
    }
}
