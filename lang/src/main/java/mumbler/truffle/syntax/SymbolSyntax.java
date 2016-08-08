package mumbler.truffle.syntax;

import com.oracle.truffle.api.source.SourceSection;

import mumbler.truffle.parser.Syntax;
import mumbler.truffle.type.MumblerSymbol;

public class SymbolSyntax extends Syntax<MumblerSymbol> {
	public SymbolSyntax(MumblerSymbol value, SourceSection source) {
		super(value, source);
	}
}
