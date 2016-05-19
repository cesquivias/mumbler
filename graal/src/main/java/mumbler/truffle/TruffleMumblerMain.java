package mumbler.truffle;

import static mumbler.truffle.node.builtin.BuiltinNode.createBuiltinFunction;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.builtin.arithmetic.AddBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.DivBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.ModBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.MulBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.SubBuiltinNodeFactory;
import mumbler.truffle.node.builtin.io.NowBuiltinNodeFactory;
import mumbler.truffle.node.builtin.io.PrintlnBuiltinNodeFactory;
import mumbler.truffle.node.builtin.lang.ReadBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CarBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CdrBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ConsBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ListBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.EqualBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.GreaterThanBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.LessThanBuiltinNodeFactory;
import mumbler.truffle.parser.Converter;
import mumbler.truffle.parser.IdentifierScanner.Namespace;
import mumbler.truffle.parser.Reader;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;

public class TruffleMumblerMain {
    public static MaterializedFrame globalScope;

    public static void main(String[] args) throws IOException {
        assert args.length < 2 : "Mumbler only accepts 1 or 0 files";
        if (args.length == 0) {
            startREPL();
        } else {
            runMumbler(args[0]);
        }
    }

    private static void startREPL() throws IOException {
        VirtualFrame topFrame = createTopFrame(new FrameDescriptor());

        Console console = System.console();
        while (true) {
            // READ
            String data = console.readLine("~> ");
            if (data == null) {
                // EOF sent
                break;
            }
            Namespace global = new Namespace(topFrame.getFrameDescriptor());
            MumblerList<Object> sexp = Reader.read(
                    new ByteArrayInputStream(data.getBytes()));
            Converter converter = new Converter();
            MumblerNode[] nodes = converter.convertSexp(sexp, global);

            // EVAL
            Object result = execute(nodes, topFrame);

            // PRINT
            if (result != MumblerList.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        VirtualFrame topFrame = createTopFrame(new FrameDescriptor());
        Namespace global = new Namespace(topFrame.getFrameDescriptor());
        MumblerList<Object> sexp = Reader.read(new FileInputStream(filename));
        Converter converter = new Converter();
        MumblerNode[] nodes = converter.convertSexp(sexp, global);
        execute(nodes, topFrame);
    }

    private static Object execute(MumblerNode[] nodes, VirtualFrame topFrame) {
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
                nodes, frameDescriptor);

        globalScope = topFrame.materialize();

        return function.callTarget.call(new Object[] {globalScope});
    }

    private static VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
                new Object[] {}, frameDescriptor);
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
        return virtualFrame;
    }
}
