package mumbler.truffle.node.builtin.lang;

import java.io.IOException;

import mumbler.truffle.MumblerException;
import mumbler.truffle.node.builtin.BuiltinNode;
import mumbler.truffle.parser.Reader;
import mumbler.truffle.parser.Syntax;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;

@NodeInfo(shortName="read")
@GenerateNodeFactory
public abstract class ReadBuiltinNode extends BuiltinNode {

    @Specialization
    public Syntax read(VirtualFrame virtualFrame, String str) {
        try {
        	Source source = Source.fromText(str, "<read>");
            return Reader.readForm(source);
        } catch (IOException e) {
            throw new MumblerException(e.getMessage());
        }
    }
}
