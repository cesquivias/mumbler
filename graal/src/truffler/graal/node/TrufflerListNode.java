package truffler.graal.node;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import truffler.graal.Function;
import truffler.graal.env.Environment;

public class TrufflerListNode<T extends Object> extends Node implements Iterable<T> {
    public static final TrufflerListNode<?> EMPTY =
            new TrufflerListNode<>();

    public final T car;
    public final TrufflerListNode<T> cdr;

    private TrufflerListNode() {
        this.car = null;
        this.cdr = null;
    }

    private TrufflerListNode(T car, TrufflerListNode<T> cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @SafeVarargs
    public static <T> TrufflerListNode<T> list(T... objs) {
        return list(asList(objs));
    }

    public static <T> TrufflerListNode<T> list(List<T> objs) {
        @SuppressWarnings("unchecked")
        TrufflerListNode<T> l = (TrufflerListNode<T>) EMPTY;
        for (int i=objs.size()-1; i>=0; i--) {
            l = l.cons(objs.get(i));
        }
        return l;
    }

    public TrufflerListNode<T> cons(T node) {
        return new TrufflerListNode<T>(node, this);
    }

    public long length() {
        if (this == EMPTY) {
            return 0;
        }

        long len = 1;
        TrufflerListNode<T> l = this.cdr;
        while (l != EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private TrufflerListNode<T> l = TrufflerListNode.this;

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
        if (!(other instanceof TrufflerListNode)) {
            return false;
        }
        if (this == EMPTY && other == EMPTY) {
            return true;
        }

        TrufflerListNode<?> that = (TrufflerListNode<?>) other;
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
        TrufflerListNode<T> rest = this.cdr;
        while (rest != null && rest != EMPTY) {
            b.append(" ");
            b.append(rest.car);
            rest = rest.cdr;
        }
        b.append(")");
        return b.toString();
    }

    @Override
    public Object eval(Environment env) {
        Function function = (Function) ((Node) this.car).eval(env);

        @SuppressWarnings("unchecked")
        TrufflerListNode<Node> nodes = (TrufflerListNode<Node>) this;
        List<Object> args = new ArrayList<Object>();
        for (Node node : nodes.cdr) {
            args.add(node.eval(env));
        }
        return function.apply(args.toArray());
    }
}
