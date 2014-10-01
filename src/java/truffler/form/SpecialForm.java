package truffler.form;

import java.util.HashMap;
import java.util.Map;

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

    protected final ListForm form;

    private SpecialForm(ListForm form) {
        this.form = form;
    }

    public static Form check(ListForm l) {
        if (l.car.equals(new SymbolForm("define"))) {
            return new DefineSpecialForm(l);
        }
        return l;
    }
}
