package mumbler.graal.type;

import com.oracle.truffle.api.RootCallTarget;

public class MumblerFunction {
    public final RootCallTarget callTarget;

    private MumblerFunction(RootCallTarget callTarget) {
        this.callTarget = callTarget;
    }
}
