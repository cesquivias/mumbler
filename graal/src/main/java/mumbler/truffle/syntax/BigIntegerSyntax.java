package mumbler.truffle.syntax;

import java.math.BigInteger;

import com.oracle.truffle.api.source.SourceSection;

import mumbler.truffle.parser.Syntax;

public class BigIntegerSyntax extends Syntax<BigInteger> {
	public BigIntegerSyntax(BigInteger value, SourceSection source) {
		super(value, source);
	}
}
