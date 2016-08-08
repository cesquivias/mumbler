package mumbler.truffle.syntax;

import com.oracle.truffle.api.source.SourceSection;

import mumbler.truffle.parser.Syntax;

public class BooleanSyntax extends Syntax<Boolean> {
	public BooleanSyntax(boolean value, SourceSection source) {
		super(value, source);
	}
}
