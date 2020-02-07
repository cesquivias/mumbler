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

import static mumbler.truffle.MumblerLanguage.ID;

@TruffleLanguage.Registration(id = ID, name = "Mumbler", version = "0.3",
    characterMimeTypes = MumblerLanguage.MIME_TYPE, defaultMimeType = MumblerLanguage.MIME_TYPE)
public class MumblerLanguage extends TruffleLanguage<Object> {
    public static final String ID = "mumbler";
    public static final String MIME_TYPE = "application/x-mumbler";

    private static final boolean TAIL_CALL_OPTIMIZATION_ENABLED = false;

    public MumblerLanguage() {
    }

    @Override
    protected Object createContext(TruffleLanguage.Env env) {
        return new MumblerContext(this);
    }

    @Override
    protected CallTarget parse(ParsingRequest parsingRequest) throws IOException {
        MumblerContext context = new MumblerContext(this);
        ListSyntax sexp = Reader.read(parsingRequest.getSource());
        Converter converter = new Converter(this, TAIL_CALL_OPTIMIZATION_ENABLED);
        MumblerNode[] nodes = converter.convertSexp(context, sexp);
        MumblerFunction function = MumblerFunction.create(this, new FrameSlot[] {},
                nodes, context.getGlobalFrame().getFrameDescriptor());
        return function.callTarget;
    }

    @Override
    protected boolean isObjectOfLanguage(Object obj) {
        return false;
    }

}
