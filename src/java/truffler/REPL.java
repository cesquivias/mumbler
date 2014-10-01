package truffler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.IOException;

public class REPL {
    private static Reader reader;

    private static Form read() throws Exception {
        Console console = System.console();
        System.out.println("console? " + console);
        try {
            String data = console.readLine("~> ");
            System.out.println(data);
            return reader.read(new ByteArrayInputStream(data.getBytes()));
        } catch (IOException e) {
            System.err.println("IO error trying to read: " + e);
            throw new Exception();
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                Form form = read();
                Object output = form.eval();
                System.out.println(output);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
