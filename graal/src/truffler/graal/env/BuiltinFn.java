package truffler.graal.env;

import truffler.graal.Function;
import truffler.graal.node.TrufflerListNode;

abstract class BuiltinFn extends Function {
    static final Function EQUALS = new BuiltinFn("EQUALS") {
        @Override
        public Object apply(Object... args) {
            Long last = (Long) args[0];
            for (Object arg : args) {
                Long current = (Long) arg;
                if (!last.equals(current)) {
                    return false;
                } else {
                    last = current;
                }
            }
            return true;
        }
    };

    static final Function LESS_THAN = new BuiltinFn("LESS-THAN") {
        @Override
        public Object apply(Object... args) {
            assert args.length > 1;
            long num = (Long) args[args.length - 1];
            for (int i=args.length - 2; i>=0; i--) {
                long n = (Long) args[i];
                if (n >= num) {
                    return false;
                }
                num = n;
            }
            return true;
        }
    };

    static final Function GREATER_THAN = new BuiltinFn("GREATER-THAN") {
        @Override
        public Object apply(Object... args) {
            assert args.length > 1;
            long num = (Long) args[args.length - 1];
            for (int i=args.length - 2; i>=0; i--) {
                long n = (Long) args[i];
                if (n <= num) {
                    return false;
                }
                num = n;
            }
            return true;
        }
    };

    static final Function DIV = new BuiltinFn("DIV") {
        @Override
        public Object apply(Object... args) {
            if (args.length == 1) {
                return 1 / (Long) args[0];
            }
            long quotient = (Long) args[0];
            for (int i=1; i<args.length; i++) {
                quotient /= (Long) args[i];
            }
            return quotient;
        }
    };

    static final Function MULT = new BuiltinFn("MULT") {
        @Override
        public Object apply(Object... args) {
            long product = 1;
            for (Object arg : args) {
                product *= (Long) arg;
            }
            return product;
        }
    };

    static final Function MINUS = new BuiltinFn("MINUS") {
        @Override
        public Object apply(Object... args) {
            if (args.length < 1) {
                throw new RuntimeException(this.name + " requires an argument");
            }
            switch (args.length) {
            case 1:
                return -((Long) args[0]);
            default:
                long diff = (Long) args[0];
                for (int i=1; i<args.length; i++) {
                    diff -= (Long) args[i];
                }
                return diff;
            }
        }
    };

    static final Function PLUS = new BuiltinFn("PLUS") {
        @Override
        public Object apply(Object... args) {
            long sum = 0;
            for (Object arg : args) {
                sum += (Long) arg;
            }
            return sum;
        }
    };

    static final Function LIST = new BuiltinFn("list") {
        @Override
        public Object apply(Object... args) {
            return TrufflerListNode.list(args);
        }
    };

    static final Function CAR = new BuiltinFn("car") {
        @Override
        public Object apply(Object... args) {
            assert args.length == 1;
            return ((TrufflerListNode<?>) args[0]).car;
        }
    };

    static final Function CDR = new BuiltinFn("cdr") {
        @Override
        public Object apply(Object... args) {
            assert args.length == 1;
            return ((TrufflerListNode<?>) args[0]).cdr;
        }
    };

    static final Function PRINTLN = new BuiltinFn("println") {
        @Override
        public Object apply(Object... args) {
            for (Object arg : args) {
                System.out.println(arg);
            }
            return TrufflerListNode.EMPTY;
        }
    };

    static final Function NOW = new BuiltinFn("now") {
        @Override
        public Object apply(Object... args) {
            return System.currentTimeMillis();
        }
    };

    protected final String name;

    public BuiltinFn(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "<procedure: " + this.name;
    }
}