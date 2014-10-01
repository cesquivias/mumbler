package truffler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.EOFException;
import java.io.IOException;

import truffler.form.Form;
import truffler.form.ListForm;

public class REPL {
    private static ListForm read() throws Exception {
        Console console = System.console();
        String data = data = console.readLine("~> ");
        if (data == null) {
            throw new EOFException();
        }
        try {
            return Reader.read(new ByteArrayInputStream(data.getBytes()));
        } catch (IOException e) {
            System.err.println("IO error trying to read: " + e);
            throw new Exception();
        }
    }

    public static void main(String[] args) {
        Environment env = new Environment();
        while (true) {
            try {
                ListForm forms = read();
                Object output = null;
                for (Form form : forms) {
                    output = form.eval(env);
                }
                System.out.println(output);
            } catch (EOFException e) {
                return;
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
