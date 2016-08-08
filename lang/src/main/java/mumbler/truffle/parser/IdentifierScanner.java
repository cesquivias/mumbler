package mumbler.truffle.parser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.misc.Pair;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

/**
 * The pass through the Mumbler AST to find all declarations of identifiers
 * in every namespace (lambda).
 * <p>
 * After the scan, a lambda's namespace can be fetched to find symbols'
 * appropriate {@link FrameDescriptor}.
 */
public class IdentifierScanner {
    private final Map<MumblerList<?>, Namespace> namespaces;
    private Namespace currentNamespace;

    public IdentifierScanner(Namespace topNamespace) {
        this.namespaces = new HashMap<>();
        this.namespaces.put(null, topNamespace);
        this.currentNamespace = topNamespace;
    }

    public Namespace getNamespace(MumblerList<?> list) {
        return this.namespaces.get(list);
    }

    public void scan(ListSyntax sexp) {
        for (Syntax<? extends Object> syntax : sexp.getValue()) {
            if (syntax instanceof ListSyntax) {
                scanList((ListSyntax) syntax);
            }
        }
    }

    private void scanList(ListSyntax syntax) {
    	MumblerList<? extends Syntax<?>> list = syntax.getValue();
        if (isLambda(list)) {
            this.currentNamespace = new Namespace(this.currentNamespace);
            this.namespaces.put(list, this.currentNamespace);
            ListSyntax argsSyntax = (ListSyntax) list.cdr().car();
            @SuppressWarnings("unchecked")
            MumblerList<MumblerSymbol> formalArgs = (MumblerList<MumblerSymbol>)
                    argsSyntax.strip();
            this.addLambdaArguments(formalArgs);
            this.scan(syntax);
            this.currentNamespace = this.currentNamespace.getParent();
        } else if (isDefine(list)) {
            MumblerSymbol sym = (MumblerSymbol) list.cdr().car().getValue();
            this.currentNamespace.addIdentifier(sym.name);
            this.scan(syntax);
        } else {
            this.scan(syntax);
        }
    }

    private void addLambdaArguments(MumblerList<MumblerSymbol> formalArgs) {
        for (MumblerSymbol sym : formalArgs) {
            this.currentNamespace.addIdentifier(sym.name);
        }
    }

    private boolean isLambda(MumblerList<? extends Syntax<?>> list) {
        return isListOfSymbol(list, "lambda");
    }

    public boolean isDefine(MumblerList<? extends Syntax<?>> list) {
        return isListOfSymbol(list, "define");
    }

    private static boolean isListOfSymbol(MumblerList<? extends Syntax<?>> list, String text) {
        if (list.size() < 3) {
            return false;
        }
        if (!(list.car() instanceof SymbolSyntax)) {
            return false;
        }
        SymbolSyntax sym = (SymbolSyntax) list.car();
        if (!sym.getValue().name.equals(text)) {
            return false;
        }
        return true;
    }

    public static class Namespace {
        private final Namespace parent;
        private final FrameDescriptor frameDescriptor;

        public Namespace(FrameDescriptor frameDescriptor) {
            this.parent = null;
            this.frameDescriptor = frameDescriptor;
        }

        public Namespace(Namespace parent) {
            this.parent = parent;
            this.frameDescriptor = new FrameDescriptor();
        }

        public Namespace getParent() {
            return this.parent;
        }

        public FrameDescriptor getFrameDescriptor() {
            return this.frameDescriptor;
        }

        public FrameSlot addIdentifier(String id) {
            return this.frameDescriptor.addFrameSlot(id);
        }

        public Pair<Integer, FrameSlot> getIdentifier(String id) {
            int depth = 0;
            Namespace current = this;
            FrameSlot slot = current.frameDescriptor.findFrameSlot(id);
            while (slot == null) {
                depth++;
                current = current.parent;
                if (current == null) {
                    return new Pair<>(-1, null);
                }
                slot = current.frameDescriptor.findFrameSlot(id);
            }
            if (current.parent == null) {
                return new Pair<>(-1, slot);
            }
            return new Pair<>(depth, slot);
        }
    }
}
