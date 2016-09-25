package mumbler.truffle.parser;

import static mumbler.truffle.parser.MumblerReadException.throwReaderException;

import java.util.Map;
import java.util.Stack;

import mumbler.truffle.parser.IdentifierScanner.Namespace;
import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.type.MumblerList;

/**
 * This class passes through the s-expression structure created by {@link Reader}
 * to make sure that all forms follow a few basic rules.
 *
 * <ol>
 * <li> <code>define</code> calls only contain 2 arguments.
 * <li> The first argument to <code>define</code> is a symbol.
 * <li> <code>lambda</code> calls have at least 3 arguments.
 * <li> The first argument to <code>lambda</code> must be a list of symbols.
 * <li> <code>if</calls> have exactly 3 arguments.
 * <li> <code>quote</code> quote calls have exactly 1 argument.
 * </ol>
 */
public class ValidationPass {
    public static void validate(Syntax<?> syntax,
            Map<MumblerList<?>, Namespace> nsMap) {
        Stack<Namespace> ns = new Stack<>();
        ns.push(nsMap.get(null));

        new SexpListener(syntax) {

            @Override
            public void onLambda(ListSyntax syntax) {
                ns.push(nsMap.get(syntax.getValue()));
            }

            @Override
            public void onLambdaExit(ListSyntax syntax) {
                ns.pop();
            }

            @Override
            public void onDefine(ListSyntax syntax) {
                MumblerList<? extends Syntax<?>> list = syntax.getValue();
                if (list.size() != 3) {
                    throwReaderException("Define takes 2 arguments", syntax,
                            ns.peek());
                }
            }
        }.walk();
    }
}
