package mumbler.truffle.parser;

import static mumbler.truffle.parser.MumblerReadException.throwReaderException;

import java.util.HashMap;
import java.util.Map;

import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.frame.FrameDescriptor;

/**
 * This class walks through the syntax objects to define all the namespaces
 * and the identifiers within. Special forms are also verified to be structured
 * correctly.
 * <p>
 * After the scan, a lambda's namespace can be fetched to find symbols'
 * appropriate {@link FrameDescriptor}.
 * <p>
 * The following special form properties are checked
 * <ol>
 * <li> <code>define</code> calls only contain 2 arguments.
 * <li> The first argument to <code>define</code> is a symbol.
 * <li> <code>lambda</code> calls have at least 3 arguments.
 * <li> The first argument to <code>lambda</code> must be a list of symbols.
 * <li> <code>if</calls> have exactly 3 arguments.
 * <li> <code>quote</code> quote calls have exactly 1 argument.
 * </ol>
 */
public class Analyzer extends SexpListener {
    // TODO : Key over ListSyntax instead of MumblerList
    private final Map<MumblerList<?>, Namespace> namespaces;
    private Namespace currentNamespace;

    public Analyzer(Namespace topNamespace) {
        this.namespaces = new HashMap<>();
        this.namespaces.put(null, topNamespace);
        this.currentNamespace = topNamespace;
    }

    public Map<MumblerList<?>, Namespace> getNamespaceMap() {
        return this.namespaces;
    }

    public Namespace getNamespace(MumblerList<?> list) {
        return this.namespaces.get(list);
    }

    @Override
    public void onDefine(ListSyntax syntax) {
        MumblerList<? extends Syntax<?>> list = syntax.getValue();
        if (list.size() != 3) {
            throwReaderException("define takes 2 arguments", syntax,
                    this.currentNamespace);
        }
        if (!(list.cdr().car() instanceof SymbolSyntax)) {
            throwReaderException("define first argument must be a symbol",
                    syntax, this.currentNamespace);
        }
        MumblerSymbol sym = (MumblerSymbol) list.cdr().car().getValue();
        this.currentNamespace.addIdentifier(sym.name);
    }

    @Override
    public void onLambda(ListSyntax syntax) {
        MumblerList<? extends Syntax<?>> list = syntax.getValue();
        if (list.size() < 3) {
            throwReaderException("lambda takes at least 3 arguments",
                    syntax, this.currentNamespace);
        }

        if (!(list.cdr().car() instanceof ListSyntax)) {
            throwReaderException("lambda second argument must be a list",
                    syntax, this.currentNamespace);;
        }

        this.currentNamespace = new Namespace(syntax.getName(), this.currentNamespace);
        this.namespaces.put(list, this.currentNamespace);
        this.addLambdaArguments((ListSyntax) list.cdr().car());
    }

    @Override
    public void onLambdaExit(ListSyntax syntax) {
        this.currentNamespace = this.currentNamespace.getParent();
    }

    @Override
    public void onIf(ListSyntax syntax) {
        if (syntax.getValue().size() != 4) {
            throwReaderException("if must have 3 arguments: test, then & else",
                    syntax, this.currentNamespace);
        }
    }

    @Override
    public void onQuote(ListSyntax syntax) {
        if (syntax.getValue().size() != 2) {
            throwReaderException("quote has only one argument", syntax,
                    this.currentNamespace);
        }
    }

    private void addLambdaArguments(ListSyntax argsListSyntax) {
        for (Syntax<?> syntax: argsListSyntax.getValue()) {
            if (syntax instanceof SymbolSyntax) {
                this.currentNamespace.addIdentifier(
                        ((SymbolSyntax) syntax).getValue().name);
            } else {
                throwReaderException("lambda argument must be a symbol",
                        argsListSyntax, this.currentNamespace);
            }
        }
    }
//
//    private boolean isLambda(MumblerList<? extends Syntax<?>> list) {
//        return isListOfSymbol(list, "lambda");
//    }
//
//    public boolean isDefine(MumblerList<? extends Syntax<?>> list) {
//        return isListOfSymbol(list, "define");
//    }
//
//    private static boolean isListOfSymbol(MumblerList<? extends Syntax<?>> list, String text) {
//        if (list.size() < 3) {
//            return false;
//        }
//        if (!(list.car() instanceof SymbolSyntax)) {
//            return false;
//        }
//        SymbolSyntax sym = (SymbolSyntax) list.car();
//        if (!sym.getValue().name.equals(text)) {
//            return false;
//        }
//        return true;
//    }
}
