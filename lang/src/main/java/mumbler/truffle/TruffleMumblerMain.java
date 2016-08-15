package mumbler.truffle;

import java.io.Console;
import java.io.IOException;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.parser.Converter;
import mumbler.truffle.parser.Reader;
import mumbler.truffle.syntax.ListSyntax;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.source.Source;

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
        Console console = System.console();
        while (true) {
            // READ
            String data = console.readLine("~> ");
            if (data == null) {
                // EOF sent
                break;
            }
            MumblerContext context = new MumblerContext();
            Source source = Source.fromText(data, "<console>");
            ListSyntax sexp = Reader.read(source);
            // TODO : replace with MumblerLanguage#parse
            Converter converter = new Converter();
            MumblerNode[] nodes = converter.convertSexp(context, sexp);

            // EVAL
            Object result = execute(nodes, context.getGlobalFrame());

            // PRINT
            if (result != MumblerList.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        Source source = Source.fromFileName(filename);
        MumblerContext context = new MumblerContext();
        ListSyntax sexp = Reader.read(source);
        // TODO : replace with MumblerLanguage#parse
        Converter converter = new Converter();
        MumblerNode[] nodes = converter.convertSexp(context, sexp);
        execute(nodes, context.getGlobalFrame());
    }

    private static Object execute(MumblerNode[] nodes, MaterializedFrame globalFrame) {
        MumblerFunction function = MumblerFunction.create(new FrameSlot[] {},
                nodes, globalFrame.getFrameDescriptor());

        return function.callTarget.call(new Object[] {globalFrame});
    }
}
