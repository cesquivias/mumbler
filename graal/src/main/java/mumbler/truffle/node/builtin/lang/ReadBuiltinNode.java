package mumbler.truffle.node.builtin.lang;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import mumbler.truffle.MumblerException;
import mumbler.truffle.node.builtin.BuiltinNode;
import mumbler.truffle.parser.Reader;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="read")
@GenerateNodeFactory
public abstract class ReadBuiltinNode extends BuiltinNode {

    @Specialization
    public Object read(VirtualFrame virtualFrame, String str) {
        try {
            return Reader.readForm(new ByteArrayInputStream(
                    str.getBytes(Charset.defaultCharset())));
        } catch (IOException e) {
            throw new MumblerException(e.getMessage());
        }
    }
}
