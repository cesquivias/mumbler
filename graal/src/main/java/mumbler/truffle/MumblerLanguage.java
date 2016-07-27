package mumbler.truffle;

import java.io.IOException;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.parser.Converter;
import mumbler.truffle.parser.IdentifierScanner.Namespace;
import mumbler.truffle.parser.Reader;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;

@TruffleLanguage.Registration(name = "Mumbler", version = "0.3",
    mimeType = MumblerLanguage.MIME_TYPE)
public class MumblerLanguage extends TruffleLanguage<Object> {
    public static final String MIME_TYPE = "application/x-mumbler";

    public static final MumblerLanguage INSTANCE = new MumblerLanguage();

    private MumblerLanguage() {
    }

    @Override
    protected Object createContext(TruffleLanguage.Env env) {
        return new Object();
    }

    @Override
    protected CallTarget parse(Source source, Node context, String... argumentNames) throws IOException {
        MumblerList<Object> sexp = Reader.read(source.getInputStream());
        Converter converter = new Converter();
        VirtualFrame topFrame = TruffleMumblerMain.createTopFrame(new FrameDescriptor());
        Namespace global = new Namespace(topFrame.getFrameDescriptor());
        MaterializedFrame globalFrame = topFrame.materialize();
        MumblerNode[] nodes = converter.convertSexp(sexp, global, globalFrame);
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
                nodes, frameDescriptor);
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
