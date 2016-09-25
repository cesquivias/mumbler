package mumbler.truffle.parser;

import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerList;

/**
 * A simple walk through the syntax objects that is created by {@link Reader}.
 * Override the on- methods to be alerted when different types are encountered.
 * Convenience methods for special forms are included.
 *
 */
public abstract class SexpListener {
    private static enum ListType {
        LIST,
        DEFINE,
        LAMBDA,
        IF,
        QUOTE;
    }

    private final Syntax<?> topSexp;

    public SexpListener(Syntax<?> sexp) {
        this.topSexp = sexp;
    }

    public void walk() {
        walk(this.topSexp);
    }

    private void walk(Syntax<?> sexp) {
        if (sexp instanceof ListSyntax) {
            ListSyntax list = (ListSyntax) sexp;
            onList(list);

            ListType listType = ListType.LIST;
            if (isListOf("define", list)) {
                onDefine(list);
                listType = ListType.DEFINE;
            } else if (isListOf("lambda", list)) {
                onLambda(list);
                listType = ListType.LAMBDA;
            } else if (isListOf("if", list)) {
                onIf(list);
                listType = ListType.IF;
            } else if (isListOf("quote", list)) {
                onQuote(list);
                listType = ListType.QUOTE;
            }

            for (Syntax<?> sub : list.getValue()) {
                walk(sub);
            }

            switch (listType) {
            case DEFINE:
                onDefineExit(list);
                break;
            case LAMBDA:
                onLambdaExit(list);
                break;
            case IF:
                onIfExit(list);
                break;
            case QUOTE:
                onQuoteExit(list);
                break;
            default:
                // do nothing;
                break;
            }
        } else if (sexp instanceof SymbolSyntax) {
            onSymbol((SymbolSyntax) sexp);
        }
    }

    private boolean isListOf(String name, ListSyntax listSyntax) {
        MumblerList<? extends Syntax<?>> list = listSyntax.getValue();
        return list != MumblerList.EMPTY && list.car() != null
                && list.car() instanceof SymbolSyntax
                && name.equals(((SymbolSyntax) list.car()).getValue().name);
    }

    public void onList(ListSyntax syntax) {}

    public void onDefine(ListSyntax syntax) {}

    public void onDefineExit(ListSyntax syntax) {}

    public void onLambda(ListSyntax syntax) {}

    public void onLambdaExit(ListSyntax syntax) {}

    public void onIf(ListSyntax syntax) {}

    public void onIfExit(ListSyntax syntax) {}

    public void onQuote(ListSyntax syntax) {}

    public void onQuoteExit(ListSyntax syntax) {}

    public void onSymbol(SymbolSyntax syntax) {}
}
