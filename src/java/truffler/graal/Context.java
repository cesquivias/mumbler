package truffler.graal;

import java.io.BufferedReader;
import java.io.PrintStream;

import truffler.graal.form.ListForm;
import truffler.graal.form.SpecialForm.LambdaSpecialForm;

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

    public LambdaSpecialForm getFunctionRegistry() {
        // TODO Auto-generated method stub
        return null;
    }

    public PrintStream getOutput() {
        return this.out;
    }

    public FileRootNode getMainNode() {
        FrameDescriptor frameDescriptor = new FrameDescriptor();
        RootNode rootNode = new RootNode(null, frameDescriptor) {
            @Override
            public Object execute(VirtualFrame frame) {
                System.out.println("Hello world!");
                return ListForm.EMPTY;
            }
        };
        return new FileRootNode(this, rootNode);
    }
}
