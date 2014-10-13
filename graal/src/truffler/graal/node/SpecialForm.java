package truffler.graal.node;

import truffler.graal.Function;
import truffler.graal.env.Environment;

public abstract class SpecialForm extends Node {
    private static class DefineSpecialForm extends SpecialForm {
        public DefineSpecialForm(TrufflerListNode<Node> listNode) {
            super(listNode);
        }

        @Override
        public Object eval(Environment env) {
            SymbolNode sym = (SymbolNode) this.node.cdr.car; // 2nd element
            env.putValue(sym.name,
                    this.node.cdr.cdr.car.eval(env)); // 3rd element
            return null;
        }
    }

    private static class LambdaSpecialForm extends SpecialForm {
        public LambdaSpecialForm(TrufflerListNode<Node> listNode) {
            super(listNode);
        }

        @Override
        public Object eval(final Environment parentEnv) {
            @SuppressWarnings("unchecked")
            final TrufflerListNode<Node> formalParams =
            (TrufflerListNode<Node>) this.node.cdr.car;
            final TrufflerListNode<Node> body = this.node.cdr.cdr;
            return new Function() {
                @Override
                public Object apply(Object... args) {
                    Environment lambdaEnv = new Environment(parentEnv);
                    if (args.length != formalParams.length()) {
                        throw new RuntimeException(
                                "Wrong number of arguments. Expected: " +
                                        formalParams.length() + ". Got: " +
                                        args.length);
                    }

                    // Map parameter values to formal parameter names
                    int i = 0;
                    for (Node param : formalParams) {
                        SymbolNode paramSymbol = (SymbolNode) param;
                        lambdaEnv.putValue(paramSymbol.name, args[i]);
                        i++;
                    }

                    // Evaluate body
                    Object output = null;
                    for (Node node : body) {
                        output = node.eval(lambdaEnv);
                    }

                    return output;
                }
            };
        }
    }

    private static class IfSpecialForm extends SpecialForm {
        public IfSpecialForm(TrufflerListNode<Node> listNode) {
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

    private static class QuoteSpecialForm extends SpecialForm {
        public QuoteSpecialForm(TrufflerListNode<Node> listNode) {
            super(listNode);
        }

        @Override
        public Object eval(Environment env) {
            return this.node.cdr.car;
        }
    }

    protected final TrufflerListNode<Node> node;

    private SpecialForm(TrufflerListNode<Node> listNode) {
        this.node = listNode;
    }

    private static final SymbolNode DEFINE = new SymbolNode("define");
    private static final SymbolNode LAMBDA = new SymbolNode("lambda");
    private static final SymbolNode IF = new SymbolNode("if");
    private static final SymbolNode QUOTE = new SymbolNode("quote");

    public static Node check(TrufflerListNode<Node> l) {
        if (l == TrufflerListNode.EMPTY) {
            return l;
        } else if (l.car.equals(DEFINE)) {
            return new DefineSpecialForm(l);
        } else if (l.car.equals(LAMBDA)) {
            return new LambdaSpecialForm(l);
        } else if (l.car.equals(IF)) {
            return new IfSpecialForm(l);
        } else if (l.car.equals(QUOTE)) {
            return new QuoteSpecialForm(l);
        }
        return l;
    }
}
