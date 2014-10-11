package truffler.simple.node;

import truffler.simple.Fn;
import truffler.simple.env.Environment;

public abstract class SpecialNode extends Node {
    private static class DefineSpecialForm extends SpecialNode {
        public DefineSpecialForm(TrufflerListNode listNode) {
            super(listNode);
        }

        @Override
        public Object eval(Environment env) {
            SymbolNode name = (SymbolNode) this.node.cdr.car; // 2nd element
            env.putValue(name, this.node.cdr.cdr.car.eval(env)); // 3rd element
            return null;
        }
    }
    
    private static class LambdaSpecialForm extends SpecialNode {
        public LambdaSpecialForm(TrufflerListNode listNode) {
            super(listNode);
        }

        @Override
        public Object eval(final Environment env) {
            final TrufflerListNode formalParams = (TrufflerListNode) this.node.cdr.car;
            final TrufflerListNode body = this.node.cdr.cdr;
            return new Fn() {
                @Override
                public Object eval(Environment env) {
                    return this;
                }

                @Override
                public Object apply(Object... args) {
                    Environment lambdaEnv = new Environment(env);
                    if (args.length != formalParams.length()) {
                        throw new IllegalArgumentException(
                                "Wrong number of arguments. Expected: " +
                                formalParams.length() + ". Got: " +
                                args.length);
                    }
                    // map parameter values to formal parameter names
                    int i = 0;
                    for (Node param : formalParams) {
                        SymbolNode paramName = (SymbolNode) param;
                        lambdaEnv.putValue(paramName, args[i]);
                        i++;
                    }

                    // execute body
                    Object output = null;
                    for (Node node : body) {
                        output = node.eval(lambdaEnv);
                    }

                    return output;
                }
            };
        }
    }

    private static class IfSpecialForm extends SpecialNode {
        public IfSpecialForm(TrufflerListNode listNode) {
            super(listNode);
        }

        @Override
        public Object eval(Environment env) {
            Node testNode = this.node.cdr.car;
            Node thenNode = this.node.cdr.cdr.car;
            Node elseNode = this.node.cdr.cdr.cdr.car;

            Object result = testNode.eval(env);
            if (result == TrufflerListNode.EMPTY || Boolean.FALSE == result) {
                return elseNode.eval(env);
            } else {
                return thenNode.eval(env);
            }
        }
    }

    private static class QuoteSpecialForm extends SpecialNode {
        public QuoteSpecialForm(TrufflerListNode listNode) {
            super(listNode);
        }

        @Override
        public Object eval(Environment env) {
            return this.node.cdr.car;
        }
    }

    protected final TrufflerListNode node;

    private SpecialNode(TrufflerListNode listNode) {
        this.node = listNode;
    }

    public static Node check(TrufflerListNode l) {
        if (l == TrufflerListNode.EMPTY) {
            return l;
        } else if (l.car.equals(new SymbolNode("define"))) {
            return new DefineSpecialForm(l);
        } else if (l.car.equals(new SymbolNode("lambda"))) {
            return new LambdaSpecialForm(l);
        } else if (l.car.equals(new SymbolNode("if"))) {
            return new IfSpecialForm(l);
        } else if (l.car.equals(new SymbolNode("quote"))) {
            return new QuoteSpecialForm(l);
        }
        return l;
    }
}
