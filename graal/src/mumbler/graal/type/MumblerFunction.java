package mumbler.graal.type;

import com.oracle.truffle.api.RootCallTarget;

public class MumblerFunction {
    public final RootCallTarget callTarget;

    public MumblerFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }
}
