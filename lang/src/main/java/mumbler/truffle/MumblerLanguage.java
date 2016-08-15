package mumbler.truffle;

import java.io.IOException;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.parser.Converter;
import mumbler.truffle.parser.Reader;
import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.type.MumblerFunction;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

@TruffleLanguage.Registration(name = "Mumbler", version = "0.3",
    mimeType = MumblerLanguage.MIME_TYPE)
public class MumblerLanguage extends TruffleLanguage<Object> {
    public static final String MIME_TYPE = "application/x-mumbler";

    public static final MumblerLanguage INSTANCE = new MumblerLanguage();

    private MumblerLanguage() {
    }

    @Override
    protected Object createContext(TruffleLanguage.Env env) {
        return new MumblerContext();
    }

    @Override
    protected CallTarget parse(Source source, Node node, String... argumentNames) throws IOException {
        MumblerContext context = new MumblerContext();
        ListSyntax sexp = Reader.read(source);
        Converter converter = new Converter();
        MumblerNode[] nodes = converter.convertSexp(context, sexp);
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
                nodes, context.getGlobalFrame().getFrameDescriptor());
        return function.callTarget;
    }

    @Override
    protected Object evalInContext(Source code, Node node, MaterializedFrame frame) throws IOException {
        throw new IllegalStateException("evalInContext not supported in this language: Python");
    }

    @Override
    protected Object findExportedSymbol(Object arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object getLanguageGlobal(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean isObjectOfLanguage(Object obj) {
        return false;
    }

}
