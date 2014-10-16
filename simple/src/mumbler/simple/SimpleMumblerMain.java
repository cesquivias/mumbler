package mumbler.simple;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;

import mumbler.simple.env.Environment;
import mumbler.simple.node.Node;
import mumbler.simple.node.MumblerListNode;

public class SimpleMumblerMain {
    public static void main(String[] args) throws IOException {
        assert args.length < 2 : "SimpleMumbler only accepts 1 or 0 files";
        if (args.length == 0) {
            startREPL();
        } else {
            runMumbler(args[0]);
        }
    }

    private static void startREPL() throws IOException {
        Environment topEnv = Environment.getBaseEnvironment();

        Console console = System.console();
        while (true) {
            // READ
            String data = console.readLine("~> ");
            if (data == null) {
                // EOF sent
                break;
            }
            MumblerListNode<Node> nodes = Reader.read(new ByteArrayInputStream(data.getBytes()));

            // EVAL
            Object result = MumblerListNode.EMPTY;
            for (Node node : nodes) {
                result = node.eval(topEnv);
            }

            // PRINT
            if (result != MumblerListNode.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        Environment topEnv = Environment.getBaseEnvironment();

        MumblerListNode<Node> nodes = Reader.read(new FileInputStream(filename));
        for (Node node : nodes) {
            node.eval(topEnv);
        }
    }
}
