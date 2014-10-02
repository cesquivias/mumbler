package truffler.simple;

import truffler.simple.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
