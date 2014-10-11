package truffler.simple;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;

import truffler.simple.env.Environment;
import truffler.simple.form.Form;
import truffler.simple.form.ListForm;

public class SimpleTrufflerMain {
    public static void main(String[] args) throws IOException {
        assert args.length < 2 : "SimpleTruffler only accepts 1 or 0 files";
        if (args.length == 0) {
            startREPL();
        } else {
            runTruffler(args[0]);
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
            ListForm nodes = Reader.read(new ByteArrayInputStream(data.getBytes()));

            // EVAL
            Object result = ListForm.EMPTY;
            for (Form node : nodes) {
                result = node.eval(topEnv);
            }

            // PRINT
            System.out.println(result);
        }
    }

    private static void runTruffler(String filename) throws IOException {
        Environment topEnv = Environment.getBaseEnvironment();

        ListForm nodes = Reader.read(new FileInputStream(filename));
        for (Form node : nodes) {
            node.eval(topEnv);
        }
    }
}
