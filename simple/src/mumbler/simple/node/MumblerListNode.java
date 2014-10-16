package mumbler.simple.node;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mumbler.simple.Function;
import mumbler.simple.env.Environment;

public class MumblerListNode<T extends Object> extends Node implements Iterable<T> {
    public static final MumblerListNode<?> EMPTY =
            new MumblerListNode<>();

    public final T car;
    public final MumblerListNode<T> cdr;

    private MumblerListNode() {
        this.car = null;
        this.cdr = null;
    }

    private MumblerListNode(T car, MumblerListNode<T> cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @SafeVarargs
    public static <T> MumblerListNode<T> list(T... objs) {
        return list(asList(objs));
    }

    public static <T> MumblerListNode<T> list(List<T> objs) {
        @SuppressWarnings("unchecked")
        MumblerListNode<T> l = (MumblerListNode<T>) EMPTY;
        for (int i=objs.size()-1; i>=0; i--) {
            l = l.cons(objs.get(i));
        }
        return l;
    }

    public MumblerListNode<T> cons(T node) {
        return new MumblerListNode<T>(node, this);
    }

    public long length() {
        if (this == EMPTY) {
            return 0;
        }

        long len = 1;
        MumblerListNode<T> l = this.cdr;
        while (l != EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private MumblerListNode<T> l = MumblerListNode.this;

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
        if (!(other instanceof MumblerListNode)) {
            return false;
        }
        if (this == EMPTY && other == EMPTY) {
            return true;
        }

        MumblerListNode<?> that = (MumblerListNode<?>) other;
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
        MumblerListNode<T> rest = this.cdr;
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
        MumblerListNode<Node> nodes = (MumblerListNode<Node>) this;
        List<Object> args = new ArrayList<Object>();
        for (Node node : nodes.cdr) {
            args.add(node.eval(env));
        }
        return function.apply(args.toArray());
    }
}
