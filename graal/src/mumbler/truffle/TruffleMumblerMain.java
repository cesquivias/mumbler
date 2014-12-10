package mumbler.truffle;

import static mumbler.truffle.node.builtin.BuiltinNode.createBuiltinFunction;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.StreamSupport;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.builtin.AddBuiltinNodeFactory;
import mumbler.truffle.node.builtin.NowBuiltinNodeFactory;
import mumbler.truffle.node.builtin.PrintlnBuiltinNodeFactory;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;

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
        //Environment topEnv = Environment.getBaseEnvironment();

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
            Object result = execute(nodes);

            // PRINT
            if (result != MumblerList.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        MumblerList<MumblerNode> nodes = Reader.read(new FileInputStream(filename));
        execute(nodes);
    }

    private static Object execute(MumblerList<MumblerNode> nodes) {
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
                StreamSupport.stream(nodes.spliterator(), false)
                .toArray(size -> new MumblerNode[size]));
        DirectCallNode directCallNode = Truffle.getRuntime()
                .createDirectCallNode(function.callTarget);
        return directCallNode.call(
                createTopFrame(Reader.frameDescriptors.peek()),
                new Object[] {});
    }

    private static VirtualFrame createTopFrame(FrameDescriptor frameDescriptor) {
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
                new Object[] {}, frameDescriptor);
        virtualFrame.setObject(frameDescriptor.addFrameSlot("+"),
                createBuiltinFunction(AddBuiltinNodeFactory.getInstance()));
        // more buitins ...
        virtualFrame.setObject(frameDescriptor.addFrameSlot("println"),
                createBuiltinFunction(PrintlnBuiltinNodeFactory.getInstance()));
        virtualFrame.setObject(frameDescriptor.addFrameSlot("now"),
                createBuiltinFunction(NowBuiltinNodeFactory.getInstance()));
        return virtualFrame;
    }
}
