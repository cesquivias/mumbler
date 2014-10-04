package truffler.graal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import truffler.graal.form.ListForm;
import truffler.graal.form.SpecialForm.LambdaSpecialForm;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.instrument.SourceCallback;
import com.oracle.truffle.api.source.Source;

public class Main {
    public static void main(String[] args) throws IOException {
        Context context = new Context(new BufferedReader(new InputStreamReader(
                System.in)), System.out);

        Source source;
        if (args.length == 0) {
            source = Source.fromReader(new InputStreamReader(System.in),
                    "stdin");
        } else {
            source = Source.fromFileName(args[0]);
        }

        int repeats = 1;
        if (args.length >= 2) {
            repeats = Integer.parseInt(args[1]);
        }

        run(context, source, System.out, repeats);
    }

    /**
     * Parse and run the specified SL source. Factored out in a separate method
     * so that it can also be used by the unit test harness.
     */
    public static void run(Context context, Source source,
            PrintStream logOutput, int repeats) {
        if (logOutput != null) {
            logOutput
            .println("== running on " + Truffle.getRuntime().getName());
            // logOutput.println("Source = " + source.getCode());
        }

        final SourceCallback sourceCallback = context.getSourceCallback();

        /* Parse the SL source file. */
        if (sourceCallback != null) {
            sourceCallback.startLoading(source);
        }
        Reader.readSource(context, source);
        if (sourceCallback != null) {
            sourceCallback.endLoading(source);
        }
        /*
         * Lookup our main entry point, which is per definition always named
         * "main".
         */
        // SLFunction main = context.getFunctionRegistry().lookup("main");
        LambdaSpecialForm main = context.getFunctionRegistry();
        if (main.getCallTarget() == null) {
            throw new TException(
                    "No function main() defined in SL source file.");
        }

        /* Change to true if you want to see the AST on the console. */
        boolean printASTToLog = false;
        /*
         * Change to true if you want to see source attribution for the AST to
         * the console
         */
        boolean printSourceAttributionToLog = false;
        /* Change to dump the AST to IGV over the network. */
        boolean dumpASTToIGV = false;

        // printScript("before execution", context, logOutput, printASTToLog, printSourceAttributionToLog, dumpASTToIGV);
        try {
            for (int i = 0; i < repeats; i++) {
                long start = System.nanoTime();
                /* Call the main entry point, without any arguments. */
                try {
                    Object result = main.getCallTarget().call();
                    if (result != ListForm.EMPTY) {
                        context.getOutput().println(result);
                    }
                } catch (UnsupportedSpecializationException ex) {
                    //context.getOutput().println(formatTypeError(ex));
                    context.getOutput().println(ex);
                }
                long end = System.nanoTime();

                if (logOutput != null && repeats > 1) {
                    logOutput.println("== iteration " + (i + 1) + ": "
                            + ((end - start) / 1000000) + " ms");
                }
            }

        } finally {
            // printScript("after execution", context, logOutput, printASTToLog, printSourceAttributionToLog, dumpASTToIGV);
        }
        return;
    }

}
