package truffler.graal;

public interface Fn extends Evaluatable {
    public Object apply(Object... args);
}
