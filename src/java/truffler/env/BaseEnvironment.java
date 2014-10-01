package truffler.env;

import truffler.Fn;
import truffler.form.SymbolForm;

public class BaseEnvironment {
    private static abstract class BuiltinFn implements Fn {
        private final String name;

        public BuiltinFn(String name) {
            this.name = name;
        }

        @Override
        public Object eval(Environment env) {
            return this;
        }

        @Override
        public String toString() {
            return "<procedure: " + this.name;
        }
    }

    private static final Fn PLUS = new BuiltinFn("PLUS") {
            @Override
            public Object apply(Object... args) {
                long sum = 0;
                for (Object arg : args) {
                    sum += (Long) arg;
                }
                return sum;
            }
        };

    private static final Fn MINUS = new BuiltinFn("MINUS") {
            @Override
            public Object apply(Object... args) {
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

    public static Environment getBaseEnvironment() {
        Environment env = new Environment();
        env.putValue(new SymbolForm("+"), PLUS);
        env.putValue(new SymbolForm("-"), MINUS);
        return env;
    }
}
