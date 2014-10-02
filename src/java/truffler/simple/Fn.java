package truffler.simple;

public interface Fn extends Evaluatable {
    public Object apply(Object... args);
}
