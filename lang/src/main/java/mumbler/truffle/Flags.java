package mumbler.truffle;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Flags {
    @Parameter(description = "Mumbler script")
    List<String> scripts = new ArrayList<>();

    @Parameter(names = "--tco", description = "Enable tail call optimization",
            arity = 1)
    boolean tailCallOptimizationEnabled = true;

    @Parameter(names = "--help", help = true)
    boolean help = false;
}
