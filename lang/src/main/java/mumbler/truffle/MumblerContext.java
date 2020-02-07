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
import mumbler.truffle.parser.Namespace;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

public class MumblerContext {
    private final FrameDescriptor globalFrameDescriptor;
    private final Namespace globalNamespace;
    private final MaterializedFrame globalFrame;
    private final MumblerLanguage lang;

    public MumblerContext() {
        this(null);
    }

    public MumblerContext(MumblerLanguage lang) {
        this.globalFrameDescriptor = new FrameDescriptor();
        this.globalNamespace = new Namespace(this.globalFrameDescriptor);
        this.globalFrame = this.initGlobalFrame(lang);
        this.lang = lang;
    }

    private MaterializedFrame initGlobalFrame(MumblerLanguage lang) {
        VirtualFrame frame = Truffle.getRuntime().createVirtualFrame(null,
                this.globalFrameDescriptor);
        addGlobalFunctions(lang, frame);
        return frame.materialize();
    }

    private static void addGlobalFunctions(MumblerLanguage lang, VirtualFrame virtualFrame) {
        FrameDescriptor frameDescriptor = virtualFrame.getFrameDescriptor();
        virtualFrame.setObject(frameDescriptor.addFrameSlot("println"),
                createBuiltinFunction(lang, PrintlnBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("+"),
                createBuiltinFunction(lang, AddBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("-"),
                createBuiltinFunction(lang, SubBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("*"),
                createBuiltinFunction(lang, MulBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("/"),
                createBuiltinFunction(lang, DivBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("%"),
                createBuiltinFunction(lang, ModBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("="),
                createBuiltinFunction(lang, EqualBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("<"),
                createBuiltinFunction(lang, LessThanBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot(">"),
                createBuiltinFunction(lang, GreaterThanBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("list"),
                createBuiltinFunction(lang, ListBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cons"),
                createBuiltinFunction(lang, ConsBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("car"),
                createBuiltinFunction(lang, CarBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cdr"),
                createBuiltinFunction(lang, CdrBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("now"),
                createBuiltinFunction(lang, NowBuiltinNodeFactory.getInstance(),
                        virtualFrame));
//        virtualFrame.setObject(frameDescriptor.addFrameSlot("eval"),
//                createBuiltinFunction(EvalBuiltinNodeFactory.getInstance(),
//                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("read"),
                createBuiltinFunction(lang, ReadBuiltinNodeFactory.getInstance(),
                        virtualFrame));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("sleep"),
                createBuiltinFunction(lang, SleepBuiltinNodeFactory.getInstance(),
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
