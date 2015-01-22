// Generated from Mumbler.g4 by ANTLR 4.2
package mumbler.truffle.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MumblerParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MumblerVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MumblerParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull MumblerParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link MumblerParser#symbol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbol(@NotNull MumblerParser.SymbolContext ctx);

	/**
	 * Visit a parse tree produced by {@link MumblerParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(@NotNull MumblerParser.FileContext ctx);

	/**
	 * Visit a parse tree produced by {@link MumblerParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(@NotNull MumblerParser.BoolContext ctx);

	/**
	 * Visit a parse tree produced by {@link MumblerParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(@NotNull MumblerParser.ListContext ctx);

	/**
	 * Visit a parse tree produced by {@link MumblerParser#forms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForms(@NotNull MumblerParser.FormsContext ctx);
}