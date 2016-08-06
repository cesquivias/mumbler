package mumbler.truffle.node.literal;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.syntax.SymbolSyntax;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LiteralSymbolNode extends MumblerNode {
    public final MumblerSymbol symbol;

    public LiteralSymbolNode(SymbolSyntax syntax) {
        this.symbol = syntax.getValue();
        setSourceSection(syntax.getSourceSection());
    }

    @Override
    public MumblerSymbol executeMumblerSymbol(VirtualFrame virtualFrame) {
        return this.symbol;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        return this.symbol;
    }

    @Override
    public String toString() {
        return this.symbol.toString();
    }
}
