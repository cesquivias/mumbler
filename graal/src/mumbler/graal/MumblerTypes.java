package mumbler.graal;

import mumbler.graal.type.MumblerFunction;
import mumbler.graal.type.MumblerList;
import mumbler.graal.type.MumblerSymbol;

import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({long.class, boolean.class, MumblerFunction.class,
    MumblerSymbol.class, MumblerList.class})
public class MumblerTypes {

}
