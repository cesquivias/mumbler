package truffler.graal.node;

import java.math.BigInteger;

import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({long.class, BigInteger.class, boolean.class, String.class, Function.class})
public class TrufflerTypes {
}
