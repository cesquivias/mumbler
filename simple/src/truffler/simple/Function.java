package truffler.simple;

public interface Function extends Evaluatable {
    public Object apply(Object... args);
}
