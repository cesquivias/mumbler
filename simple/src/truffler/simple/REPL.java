package truffler.simple;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.EOFException;
import java.io.IOException;

import truffler.simple.env.BaseEnvironment;
import truffler.simple.env.Environment;
import truffler.simple.form.Form;
import truffler.simple.form.ListForm;

public class REPL {
    private static ListForm read() throws Exception {
        Console console = System.console();
        String data = console.readLine("~> ");
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
        Environment env = BaseEnvironment.getBaseEnvironment();
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
                e.printStackTrace();
            }
        }
    }
}
