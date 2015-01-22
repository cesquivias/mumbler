// Generated from Mumbler.g4 by ANTLR 4.2
package mumbler.truffle.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MumblerParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, INT=3, SYMBOL=4, BOOLEAN=5, COMMENT=6, WS=7;
	public static final String[] tokenNames = {
		"<INVALID>", "'('", "')'", "INT", "SYMBOL", "BOOLEAN", "COMMENT", "WS"
	};
	public static final int
		RULE_file = 0, RULE_forms = 1, RULE_form = 2;
	public static final String[] ruleNames = {
		"file", "forms", "form"
	};

	@Override
	public String getGrammarFileName() { return "Mumbler.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MumblerParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FileContext extends ParserRuleContext {
		public FormsContext forms() {
			return getRuleContext(FormsContext.class,0);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6); forms();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormsContext extends ParserRuleContext {
		public FormContext form(int i) {
			return getRuleContext(FormContext.class,i);
		}
		public List<FormContext> form() {
			return getRuleContexts(FormContext.class);
		}
		public FormsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forms; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitForms(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormsContext forms() throws RecognitionException {
		FormsContext _localctx = new FormsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_forms);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << INT) | (1L << SYMBOL) | (1L << BOOLEAN))) != 0)) {
				{
				{
				setState(8); form();
				}
				}
				setState(13);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormContext extends ParserRuleContext {
		public FormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_form; }
	 
		public FormContext() { }
		public void copyFrom(FormContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NumberContext extends FormContext {
		public TerminalNode INT() { return getToken(MumblerParser.INT, 0); }
		public NumberContext(FormContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SymbolContext extends FormContext {
		public TerminalNode SYMBOL() { return getToken(MumblerParser.SYMBOL, 0); }
		public SymbolContext(FormContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitSymbol(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolContext extends FormContext {
		public TerminalNode BOOLEAN() { return getToken(MumblerParser.BOOLEAN, 0); }
		public BoolContext(FormContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListContext extends FormContext {
		public FormsContext forms() {
			return getRuleContext(FormsContext.class,0);
		}
		public ListContext(FormContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MumblerVisitor ) return ((MumblerVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormContext form() throws RecognitionException {
		FormContext _localctx = new FormContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_form);
		try {
			setState(21);
			switch (_input.LA(1)) {
			case 1:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(14); match(1);
				setState(15); forms();
				setState(16); match(2);
				}
				break;
			case INT:
				_localctx = new NumberContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(18); match(INT);
				}
				break;
			case SYMBOL:
				_localctx = new SymbolContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(19); match(SYMBOL);
				}
				break;
			case BOOLEAN:
				_localctx = new BoolContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(20); match(BOOLEAN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\t\32\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\3\7\3\f\n\3\f\3\16\3\17\13\3\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4\30\n\4\3\4\2\2\5\2\4\6\2\2\32\2\b\3\2\2\2\4\r\3\2\2\2\6"+
		"\27\3\2\2\2\b\t\5\4\3\2\t\3\3\2\2\2\n\f\5\6\4\2\13\n\3\2\2\2\f\17\3\2"+
		"\2\2\r\13\3\2\2\2\r\16\3\2\2\2\16\5\3\2\2\2\17\r\3\2\2\2\20\21\7\3\2\2"+
		"\21\22\5\4\3\2\22\23\7\4\2\2\23\30\3\2\2\2\24\30\7\5\2\2\25\30\7\6\2\2"+
		"\26\30\7\7\2\2\27\20\3\2\2\2\27\24\3\2\2\2\27\25\3\2\2\2\27\26\3\2\2\2"+
		"\30\7\3\2\2\2\4\r\27";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}