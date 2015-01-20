package mumbler.truffle;

import static mumbler.truffle.node.builtin.BuiltinNode.createBuiltinFunction;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.StreamSupport;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.builtin.NowBuiltinNodeFactory;
import mumbler.truffle.node.builtin.PrintlnBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.AddBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.DivBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.ModBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.MulBuiltinNodeFactory;
import mumbler.truffle.node.builtin.arithmetic.SubBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CarBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.CdrBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ConsBuiltinNodeFactory;
import mumbler.truffle.node.builtin.list.ListBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.EqualBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.GreaterThanBuiltinNodeFactory;
import mumbler.truffle.node.builtin.relational.LessThanBuiltinNodeFactory;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class TruffleMumblerMain {
    public static void main(String[] args) throws IOException {
        assert args.length < 2 : "Mumbler only accepts 1 or 0 files";
        if (args.length == 0) {
            startREPL();
        } else {
            runMumbler(args[0]);
        }
    }

    private static void startREPL() throws IOException {
        VirtualFrame topFrame = createTopFrame(Reader.frameDescriptors.peek());

        Console console = System.console();
        while (true) {
            // READ
            String data = console.readLine("~> ");
            if (data == null) {
                // EOF sent
                break;
            }
            MumblerList<MumblerNode> nodes = Reader.read(
                new ByteArrayInputStream(data.getBytes()));

            // EVAL
            Object result = execute(nodes, topFrame);

            // PRINT
            if (result != MumblerList.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        VirtualFrame topFrame = createTopFrame(Reader.frameDescriptors.peek());
        MumblerList<MumblerNode> nodes = Reader.read(new FileInputStream(filename));
        execute(nodes, topFrame);
    }

    private static Object execute(MumblerList<MumblerNode> nodes,
            VirtualFrame topFrame) {
        FrameDescriptor frameDescriptor = topFrame.getFrameDescriptor();
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
            StreamSupport.stream(nodes.spliterator(), false)
            .toArray(size -> new MumblerNode[size]),
            frameDescriptor);

        return function.callTarget.call(new Object[] {topFrame.materialize()});
    }

    private static VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
            new Object[] {}, frameDescriptor);
        virtualFrame.setObject(frameDescriptor.addFrameSlot("println"),
            createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("+"),
            createBuiltinFunction(AddBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("-"),
            createBuiltinFunction(SubBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("*"),
            createBuiltinFunction(MulBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("/"),
            createBuiltinFunction(DivBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("%"),
            createBuiltinFunction(ModBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("="),
            createBuiltinFunction(EqualBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("<"),
            createBuiltinFunction(LessThanBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot(">"),
            createBuiltinFunction(GreaterThanBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("list"),
            createBuiltinFunction(ListBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cons"),
            createBuiltinFunction(ConsBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("car"),
            createBuiltinFunction(CarBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("cdr"),
            createBuiltinFunction(CdrBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("now"),
            createBuiltinFunction(NowBuiltinNodeFactory.getInstance(),
                new FrameDescriptor()));
        return virtualFrame;
    }
}
