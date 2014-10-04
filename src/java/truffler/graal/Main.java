package truffler.graal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import truffler.graal.form.ListForm;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.source.Source;

public class Main {
    public static void main(String[] args) throws IOException {
        Context context = new Context(new BufferedReader(
                new InputStreamReader(System.in)),
                System.out);

        //        Source source;
        //        if (args.length == 0) {
        //            source = Source.fromReader(new InputStreamReader(System.in),
        //                    "stdin");
        //        } else {
        //            source = Source.fromFileName(args[0]);
        //        }

        int repeats = 1;
        if (args.length >= 2) {
            repeats = Integer.parseInt(args[1]);
        }

        run(context, null, System.out, repeats);
    }

    /**
     * Parse and run the specified SL source. Factored out in a separate method
     * so that it can also be used by the unit test harness.
     */
    public static void run(Context context, Source source,
            PrintStream logOutput, int repeats) {
        if (logOutput != null) {
            logOutput.println("== running on " + Truffle.getRuntime().getName());
        }

        FileRootNode rootNode = context.getMainNode();

        long start = System.nanoTime();
        /* Call the main entry point, without any arguments. */
        try {
            Object result = rootNode.getCallTarget().call();
            if (result != ListForm.EMPTY) {
                context.getOutput().println(result);
            }
        } catch (UnsupportedSpecializationException ex) {
            //context.getOutput().println(formatTypeError(ex));
            context.getOutput().println(ex);
        }
        long end = System.nanoTime();

        if (logOutput != null && repeats > 1) {
            logOutput.println("== " + ((end - start) / 1000000) + " ms");
        }
    }
}
