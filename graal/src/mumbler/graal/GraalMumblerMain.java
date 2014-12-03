package mumbler.graal;

import java.io.Console;
import java.io.IOException;

public class GraalMumblerMain {
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
            // MumblerListNode<MumblerNode> nodes = Reader.read(new ByteArrayInputStream(data.getBytes()));

            // EVAL
            //            Object result = MumblerListNode.EMPTY;
            //            for (MumblerNode node : nodes) {
            //                result = node.eval(topEnv);
            //            }
            //
            //            // PRINT
            //            if (result != MumblerListNode.EMPTY) {
            //                System.out.println(result);
            //            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        //        Environment topEnv = Environment.getBaseEnvironment();
        //
        //        MumblerListNode<MumblerNode> nodes = Reader.read(new FileInputStream(filename));
        //        for (MumblerNode node : nodes) {
        //            node.eval(topEnv);
        //        }
    }
}
