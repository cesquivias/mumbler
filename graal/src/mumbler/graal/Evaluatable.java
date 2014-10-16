package mumbler.graal;

import mumbler.graal.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
