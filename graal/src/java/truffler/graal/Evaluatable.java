package truffler.graal;

import truffler.graal.env.Environment;

public interface Evaluatable {
    public Object eval(Environment env);
}
