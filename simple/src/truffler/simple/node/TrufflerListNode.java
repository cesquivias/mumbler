package truffler.simple.node;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import truffler.simple.Function;
import truffler.simple.env.Environment;

public class TrufflerListNode extends Node implements Iterable<Node> {
    public static final TrufflerListNode EMPTY = new TrufflerListNode();

    public final Node car;
    public final TrufflerListNode cdr;

    private TrufflerListNode() {
        this.car = null;
        this.cdr = null;
    }

    private TrufflerListNode(Node car, TrufflerListNode cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public static TrufflerListNode list(Node... nodes) {
        return list(asList(nodes));
    }

    public static TrufflerListNode list(List<Node> nodes) {
        TrufflerListNode l = EMPTY;
        for (int i=nodes.size()-1; i>=0; i--) {
            l = l.cons(nodes.get(i));
        }
        return l;
    }

    public TrufflerListNode cons(Node node) {
        return new TrufflerListNode(node, this);
    }

    public long length() {
        if (this == EMPTY) {
            return 0;
        }

        long len = 1;
        TrufflerListNode l = this.cdr;
        while (l != EMPTY) {
            len++;
            l = l.cdr;
        }
        return len;
    }

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            private TrufflerListNode l = TrufflerListNode.this;

            @Override
            public boolean hasNext() {
                return this.l != EMPTY;
            }

            @Override
            public Node next() {
                if (this.l == EMPTY) {
                    throw new IllegalStateException("At end of list");
                }
                Node car = this.l.car;
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

        TrufflerListNode that = (TrufflerListNode) other;
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
        Node rest = this.cdr;
        while (rest != null && rest != EMPTY) {
            b.append(" ");
            if (rest instanceof TrufflerListNode) {
                TrufflerListNode l = (TrufflerListNode) rest;
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
        Function function = (Function) this.car.eval(env);

        List<Object> args = new ArrayList<Object>();
        for (Node node : this.cdr) {
            args.add(node.eval(env));
        }
        return function.apply(args.toArray());
    }
}
