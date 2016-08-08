package mumbler.truffle.syntax;

import com.oracle.truffle.api.source.SourceSection;

import mumbler.truffle.parser.Syntax;

public class StringSyntax extends Syntax<String> {
	public StringSyntax(String value, SourceSection source) {
		super(value, source);
	}
}
