package mumbler.truffle.syntax;

import java.util.ArrayList;
import java.util.List;

import mumbler.truffle.parser.Syntax;
import mumbler.truffle.type.MumblerList;

import com.oracle.truffle.api.source.SourceSection;

public class ListSyntax extends Syntax<MumblerList<? extends Syntax<?>>> {
    public ListSyntax(MumblerList<? extends Syntax<?>> value,
            SourceSection sourceSection) {
        super(value, sourceSection);
    }

    @Override
    public Object strip() {
        List<Object> list = new ArrayList<Object>();
        for (Syntax<? extends Object> syntax : getValue()) {
            list.add(syntax.strip());
        }
        return MumblerList.list(list);
    }
}
