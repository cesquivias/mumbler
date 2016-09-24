package mumbler.truffle;

import java.util.ArrayList;
import java.util.List;

import mumbler.truffle.node.MumblerRootNode;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameInstanceVisitor;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

/**
 * The base exception for any runtime errors.
 *
 */
public class MumblerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MumblerException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        CompilerAsserts.neverPartOfCompilation();
        return fillInMumblerStackTrace(this);
    }

    public static Throwable fillInMumblerStackTrace(Throwable t) {
        final List<StackTraceElement> stackTrace = new ArrayList<>();
        Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Void>() {
            @Override
            public Void visitFrame(FrameInstance frame) {
                Node callNode = frame.getCallNode();
                if (callNode == null) {
                    return null;
                }
                RootNode root = callNode.getRootNode();

                /*
                 * There should be no RootNodes other than SLRootNodes on the stack. Just for the
                 * case if this would change.
                 */
                String methodName = "$unknownFunction";
                if (root instanceof MumblerRootNode) {
                    methodName = ((MumblerRootNode) root).name;
                }

                SourceSection sourceSection = callNode.getEncapsulatingSourceSection();
                Source source = sourceSection != null ? sourceSection.getSource() : null;
                String sourceName = source != null ? source.getName() : null;
                int lineNumber;
                try {
                    lineNumber = sourceSection != null ? sourceSection.getLineLocation().getLineNumber() : -1;
                } catch (UnsupportedOperationException e) {
                    /*
                     * SourceSection#getLineLocation() may throw an UnsupportedOperationException.
                     */
                    lineNumber = -1;
                }
                stackTrace.add(new StackTraceElement("mumbler", methodName, sourceName, lineNumber));
                return null;
            }
        });
        t.setStackTrace(stackTrace.toArray(new StackTraceElement[stackTrace.size()]));
        return t;
    }
}
