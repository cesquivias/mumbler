package truffler;

import truffler.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
