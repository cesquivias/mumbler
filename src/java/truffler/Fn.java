package truffler;

public interface Fn extends Evaluatable {
    public Object apply(Object... args);
}
