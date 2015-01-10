package mumbler.truffle;

import java.math.BigInteger;

import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({long.class, boolean.class, BigInteger.class, MumblerFunction.class,
    MumblerSymbol.class, MumblerList.class})
public class MumblerTypes {
    @ImplicitCast
    public BigInteger castBigInteger(long value) {
        return BigInteger.valueOf(value);
    }
}
