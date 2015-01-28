// Generated from Mumbler.g4 by ANTLR 4.2
package mumbler.truffle.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MumblerLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, INT=3, SYMBOL=4, BOOLEAN=5, COMMENT=6, WS=7;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'('", "')'", "INT", "SYMBOL", "BOOLEAN", "COMMENT", "WS"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "INT", "SYMBOL", "BOOLEAN", "COMMENT", "WS"
	};


	public MumblerLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mumbler.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\t\66\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\3\3\3\3\4\6"+
		"\4\27\n\4\r\4\16\4\30\3\5\3\5\7\5\35\n\5\f\5\16\5 \13\5\3\6\3\6\3\6\3"+
		"\6\5\6&\n\6\3\7\3\7\7\7*\n\7\f\7\16\7-\13\7\3\7\3\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3+\2\t\3\3\5\4\7\5\t\6\13\7\r\b\17\t\3\2\6\3\2\62;\7\2\13\f\17"+
		"\17\"\"%%*+\6\2\13\f\17\17\"\"*+\5\2\13\f\17\17\"\"9\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\3"+
		"\21\3\2\2\2\5\23\3\2\2\2\7\26\3\2\2\2\t\32\3\2\2\2\13%\3\2\2\2\r\'\3\2"+
		"\2\2\17\62\3\2\2\2\21\22\7*\2\2\22\4\3\2\2\2\23\24\7+\2\2\24\6\3\2\2\2"+
		"\25\27\t\2\2\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2"+
		"\31\b\3\2\2\2\32\36\n\3\2\2\33\35\n\4\2\2\34\33\3\2\2\2\35 \3\2\2\2\36"+
		"\34\3\2\2\2\36\37\3\2\2\2\37\n\3\2\2\2 \36\3\2\2\2!\"\7%\2\2\"&\7h\2\2"+
		"#$\7%\2\2$&\7v\2\2%!\3\2\2\2%#\3\2\2\2&\f\3\2\2\2\'+\7=\2\2(*\13\2\2\2"+
		")(\3\2\2\2*-\3\2\2\2+,\3\2\2\2+)\3\2\2\2,.\3\2\2\2-+\3\2\2\2./\7\f\2\2"+
		"/\60\3\2\2\2\60\61\b\7\2\2\61\16\3\2\2\2\62\63\t\5\2\2\63\64\3\2\2\2\64"+
		"\65\b\b\2\2\65\20\3\2\2\2\7\2\30\36%+\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}