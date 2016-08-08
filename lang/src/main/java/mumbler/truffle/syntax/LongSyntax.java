package mumbler.truffle.syntax;

import mumbler.truffle.parser.Syntax;

import com.oracle.truffle.api.source.SourceSection;


public class LongSyntax extends Syntax<Long> {
	public LongSyntax(long value, SourceSection source) {
		super(value, source);
	}
}
