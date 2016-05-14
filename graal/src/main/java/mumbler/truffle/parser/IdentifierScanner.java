package mumbler.truffle.parser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.misc.Pair;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

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

    public IdentifierScanner() {
        this.namespaces = new HashMap<>();
    }

    public Namespace getNamespace(MumblerList<?> list) {
        return this.namespaces.get(list);
    }

    public void scan(MumblerList<?> sexp) {
        this.currentNamespace = new Namespace(null);
        this.namespaces.put(null, this.currentNamespace);

        for (Object el : sexp) {
            if (el instanceof MumblerList) {
                scanList((MumblerList<?>) el);
            }
        }
    }

    private void scanList(MumblerList<?> list) {
        if (isLambda(list)) {
            this.currentNamespace = new Namespace(this.currentNamespace);
            this.namespaces.put(list, this.currentNamespace);
            this.scan(list);
            this.currentNamespace = this.currentNamespace.getParent();
        } else if (isDefine(list)) {
            MumblerSymbol sym = (MumblerSymbol) list.cdr().car();
            this.currentNamespace.addIdentifier(sym.name);
            this.scan(list);
        } else {
            this.scan(list);
        }
    }

    private boolean isLambda(MumblerList<?> list) {
        return isListOfSymbol(list, "lambda");
    }

    public boolean isDefine(MumblerList<?> list) {
        return isListOfSymbol(list, "define");
    }

    private static boolean isListOfSymbol(MumblerList<?> list, String text) {
        if (list.size() != 3) {
            return false;
        }
        if (!(list.car() instanceof MumblerSymbol)) {
            return false;
        }
        MumblerSymbol sym = (MumblerSymbol) list.car();
        if (!sym.name.equals(text)) {
            return false;
        }
        return true;
    }

    public static class Namespace {
        private final Namespace parent;
        private final FrameDescriptor frameDescriptor;

        public Namespace(Namespace parent) {
            this.parent = parent;
            this.frameDescriptor = new FrameDescriptor();
        }

        public Namespace getParent() {
            return this.parent;
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
            return new Pair<>(depth, slot);
        }
    }
}
