package truffler.form;

import java.util.HashMap;
import java.util.Map;

import truffler.Fn;
import truffler.env.Environment;

public abstract class SpecialForm extends Form {
    private static class DefineSpecialForm extends SpecialForm {
        public DefineSpecialForm(ListForm form) {
            super(form);
        }

        @Override
        public Object eval(Environment env) {
            SymbolForm name = (SymbolForm) this.form.cdr.car; // 2nd element
            env.putValue(name, this.form.cdr.cdr.car.eval(env)); // 3rd element
            return null;
        }
    }
    
    private static class LambdaSpecialForm extends SpecialForm {
        public LambdaSpecialForm(ListForm form) {
            super(form);
        }

        @Override
        public Object eval(Environment env) {
            final Environment lambdaEnv = new Environment(env);
            final ListForm formalParams = (ListForm) this.form.cdr.car;
            final ListForm body = this.form.cdr.cdr;
            return new Fn() {
                @Override
                public Object eval(Environment env) {
                    return this;
                }

                @Override
                public Object apply(Object... args) {
                    if (args.length != formalParams.length()) {
                        throw new IllegalArgumentException(
                                "Wrong number of arguments. Expected: " +
                                formalParams.length() + ". Got: " +
                                args.length);
                    }
                    // map parameter values to formal parameter names
                    int i = 0;
                    for (Form param : formalParams) {
                        SymbolForm paramName = (SymbolForm) param;
                        lambdaEnv.putValue(paramName, args[i]);
                        i++;
                    }

                    // execute body
                    Object output = null;
                    for (Form form : body) {
                        output = form.eval(lambdaEnv);
                    }

                    return output;
                }
            };
        }
    }

    private static class IfSpecialForm extends SpecialForm {
        public IfSpecialForm(ListForm form) {
            super(form);
        }

        @Override
        public Object eval(Environment env) {
            Form test = this.form.cdr.car;
            Form thenForm = this.form.cdr.cdr.car;
            Form elseForm = this.form.cdr.cdr.cdr.car;

            Object result = test.eval(env);
            if (result == ListForm.EMPTY || Boolean.FALSE == result) {
                return elseForm.eval(env);
            } else {
                return thenForm.eval(env);
            }
        }
    }

    private static class QuoteSpecialForm extends SpecialForm {
        public QuoteSpecialForm(ListForm form) {
            super(form);
        }

        @Override
        public Object eval(Environment env) {
            return this.form.cdr.car;
        }
    }

    protected final ListForm form;

    private SpecialForm(ListForm form) {
        this.form = form;
    }

    public static Form check(ListForm l) {
        if (l == ListForm.EMPTY) {
            return l;
        } else if (l.car.equals(new SymbolForm("define"))) {
            return new DefineSpecialForm(l);
        } else if (l.car.equals(new SymbolForm("lambda"))) {
            return new LambdaSpecialForm(l);
        } else if (l.car.equals(new SymbolForm("if"))) {
            return new IfSpecialForm(l);
        } else if (l.car.equals(new SymbolForm("quote"))) {
            return new QuoteSpecialForm(l);
        }
        return l;
    }
}
