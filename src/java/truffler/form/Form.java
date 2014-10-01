package truffler.form;

import truffler.Environment;

public interface Form {
    public Object eval(Environment env);
}
