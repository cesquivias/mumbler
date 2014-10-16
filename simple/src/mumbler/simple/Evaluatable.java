package mumbler.simple;

import mumbler.simple.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
