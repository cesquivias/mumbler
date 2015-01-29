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
		T__2=1, T__1=2, T__0=3, INT=4, BOOLEAN=5, STRING=6, SYMBOL=7, COMMENT=8, 
		WS=9;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'''", "'('", "')'", "INT", "BOOLEAN", "STRING", "SYMBOL", "COMMENT", 
		"WS"
	};
	public static final String[] ruleNames = {
		"T__2", "T__1", "T__0", "INT", "BOOLEAN", "STRING", "SYMBOL", "COMMENT", 
		"WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\13G\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\5\6\5\35\n\5\r\5\16\5\36\3\6\3\6\3\6\3\6\5\6%\n\6\3"+
		"\7\3\7\3\7\3\7\7\7+\n\7\f\7\16\7.\13\7\3\7\3\7\3\b\3\b\7\b\64\n\b\f\b"+
		"\16\b\67\13\b\3\t\3\t\7\t;\n\t\f\t\16\t>\13\t\3\t\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3<\2\13\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\3\2\7\3\2\62"+
		";\3\2$$\7\2\13\f\17\17\"\"$%)+\7\2\13\f\17\17\"\"$$)+\5\2\13\f\17\17\""+
		"\"L\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3"+
		"\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\3\25\3\2\2\2\5\27\3\2\2"+
		"\2\7\31\3\2\2\2\t\34\3\2\2\2\13$\3\2\2\2\r&\3\2\2\2\17\61\3\2\2\2\218"+
		"\3\2\2\2\23C\3\2\2\2\25\26\7)\2\2\26\4\3\2\2\2\27\30\7*\2\2\30\6\3\2\2"+
		"\2\31\32\7+\2\2\32\b\3\2\2\2\33\35\t\2\2\2\34\33\3\2\2\2\35\36\3\2\2\2"+
		"\36\34\3\2\2\2\36\37\3\2\2\2\37\n\3\2\2\2 !\7%\2\2!%\7h\2\2\"#\7%\2\2"+
		"#%\7v\2\2$ \3\2\2\2$\"\3\2\2\2%\f\3\2\2\2&,\7$\2\2\'+\n\3\2\2()\7^\2\2"+
		")+\7$\2\2*\'\3\2\2\2*(\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-/\3\2\2\2"+
		".,\3\2\2\2/\60\7$\2\2\60\16\3\2\2\2\61\65\n\4\2\2\62\64\n\5\2\2\63\62"+
		"\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\66\20\3\2\2\2\67\65"+
		"\3\2\2\28<\7=\2\29;\13\2\2\2:9\3\2\2\2;>\3\2\2\2<=\3\2\2\2<:\3\2\2\2="+
		"?\3\2\2\2><\3\2\2\2?@\7\f\2\2@A\3\2\2\2AB\b\t\2\2B\22\3\2\2\2CD\t\6\2"+
		"\2DE\3\2\2\2EF\b\n\2\2F\24\3\2\2\2\t\2\36$*,\65<\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}