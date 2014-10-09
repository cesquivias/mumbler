package truffler.graal;

import java.io.BufferedReader;
import java.io.PrintStream;

import truffler.graal.node.ExpressionNode;
import truffler.graal.node.expression.AddNode;
import truffler.graal.node.expression.AddNodeFactory;
import truffler.graal.node.expression.NumberNode;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrument.SourceCallback;
import com.oracle.truffle.api.nodes.RootNode;

public class Context extends ExecutionContext {

    private SourceCallback sourceCallback;
    private final PrintStream out;

    public Context(BufferedReader bufferedReader, PrintStream out) {
        this.out = out;
    }

    @Override
    public String getLanguageShortName() {
        return "truffler";
    }

    @Override
    protected void setSourceCallback(SourceCallback sourceCallback) {
        this.sourceCallback = sourceCallback;
    }

    public SourceCallback getSourceCallback() {
        return this.sourceCallback;
    }

    public PrintStream getOutput() {
        return this.out;
    }

    public FileRootNode getMainNode() {
        final AddNode adding =  AddNodeFactory.create(
                new NumberNode(2),
                new NumberNode(3));
        FrameDescriptor frameDescriptor = new FrameDescriptor();
        RootNode rootNode = new RootNode(null, frameDescriptor) {
            @Child private ExpressionNode bodyNode = adding;

            @Override
            public Object execute(VirtualFrame frame) {
                return adding.execute(frame);
            }
        };
        return new FileRootNode(this, rootNode);
    }
}
