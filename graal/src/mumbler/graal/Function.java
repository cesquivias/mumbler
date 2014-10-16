package mumbler.graal;

import mumbler.graal.env.Environment;

public abstract class Function implements Evaluatable {
    @Override
    public Object eval(Environment env) {
        return this;
    }

    public abstract Object apply(Object... args);
}
