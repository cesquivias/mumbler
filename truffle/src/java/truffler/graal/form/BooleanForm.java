package truffler.graal.form;

import truffler.graal.env.Environment;

public class BooleanForm extends Form {

    public static final BooleanForm TRUE = new BooleanForm(Boolean.TRUE);
    public static final BooleanForm FALSE = new BooleanForm(Boolean.FALSE);

    private final Boolean value;

    private BooleanForm(Boolean value) {
        this.value = value;
    }

    @Override
    public Object eval(Environment env) {
        return this.value;
    }
}
