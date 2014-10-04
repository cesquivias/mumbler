package truffler.graal;

import truffler.graal.form.ListForm;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class FileRootNode extends RootNode {

    private final Context context;

    public FileRootNode(Context context, RootNode rootNode) {
        this.context = context;
        this.setCallTarget(Truffle.getRuntime().createCallTarget(rootNode));
    }

    @Override
    public Object execute(VirtualFrame arg0) {
        // TODO : get stdout from Context
        System.out.println("Hello world!");
        return ListForm.EMPTY;
    }

}
