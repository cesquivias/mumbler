package mumbler.truffle;

import static mumbler.truffle.node.builtin.BuiltinNode.createBuiltinFunction;
import mumbler.truffle.node.builtin.arithmetic.AddBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.DivBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.ModBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.MulBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.SubBuiltinNodeFactory;
import mumbler.truffle.node.builtin.io.NowBuiltinNodeFactory;
import mumbler.truffle.node.builtin.io.PrintlnBuiltinNodeFactory;
import mumbler.truffle.node.builtin.io.SleepBuiltinNodeFactory;
import mumbler.truffle.node.builtin.lang.ReadBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CarBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CdrBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ConsBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ListBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.EqualBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.GreaterThanBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.LessThanBuiltinNodeFactory;
import mumbler.truffle.parser.IdentifierScanner.Namespace;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

public class MumblerContext {
    private final FrameDescriptor globalFrameDescriptor;
    private final Namespace globalNamespace;
    private final MaterializedFrame globalFrame;

    public MumblerContext() {
        this.globalFrameDescriptor = new FrameDescriptor();
        this.globalNamespace = new Namespace(this.globalFrameDescriptor);
        this.globalFrame = this.initGlobalFrame();
    }

    private MaterializedFrame initGlobalFrame() {
        VirtualFrame frame = Truffle.getRuntime().createVirtualFrame(null,
                this.globalFrameDescriptor);
        addGlobalFunctions(frame);
        return frame.materialize();
    }

    private static void addGlobalFunctions(VirtualFrame virtualFrame) {
        FrameDescriptor frameDescriptor = virtualFrame.getFrameDescriptor();
        virtualFrame.setObject(frameDescriptor.addFrameSlot("println"),
                createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("+"),
                createBuiltinFunction(AddBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("-"),
                createBuiltinFunction(SubBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("*"),
                createBuiltinFunction(MulBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("/"),
                createBuiltinFunction(DivBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("%"),
                createBuiltinFunction(ModBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("="),
                createBuiltinFunction(EqualBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("<"),
                createBuiltinFunction(LessThanBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot(">"),
                createBuiltinFunction(GreaterThanBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("list"),
                createBuiltinFunction(ListBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cons"),
                createBuiltinFunction(ConsBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("car"),
                createBuiltinFunction(CarBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cdr"),
                createBuiltinFunction(CdrBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("now"),
                createBuiltinFunction(NowBuiltinNodeFactory.getInstance(),
                        virtualFrame));
//        virtualFrame.setObject(frameDescriptor.addFrameSlot("eval"),
//                createBuiltinFunction(EvalBuiltinNodeFactory.getInstance(),
//                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("read"),
                createBuiltinFunction(ReadBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("sleep"),
                createBuiltinFunction(SleepBuiltinNodeFactory.getInstance(),
                        virtualFrame));
    }

    /**
     * @return A {@link MaterializedFrame} on the heap that contains all global
     * values.
     */
    public MaterializedFrame getGlobalFrame() {
        return this.globalFrame;
    }

    public Namespace getGlobalNamespace() {
        return this.globalNamespace;
    }
}
