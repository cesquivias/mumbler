package mumbler.truffle;

import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({long.class, boolean.class, MumblerFunction.class,
    MumblerSymbol.class, MumblerList.class})
public class MumblerTypes {

}
