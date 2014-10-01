package truffler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.IOException;

public class REPL {
    private static Form read() throws Exception {
        Console console = System.console();
        try {
            String data = console.readLine("~> ");
            return Reader.read(new ByteArrayInputStream(data.getBytes()));
        } catch (IOException e) {
            System.err.println("IO error trying to read: " + e);
            throw new Exception();
        }
    }

    public static void main(String[] args) {
        while (true) {
            try {
                Form form = read();
                Object output = form.eval();
                System.out.println(output);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
