package mumbler.truffle.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import mumbler.truffle.node.MumblerNode;
import mumbler.truffle.node.SymbolNode;
import mumbler.truffle.node.SymbolNodeGen;
import mumbler.truffle.node.call.InvokeNode;
import mumbler.truffle.node.literal.BigIntegerNode;
import mumbler.truffle.node.literal.BooleanNode;
import mumbler.truffle.node.literal.LiteralListNode;
import mumbler.truffle.node.literal.LiteralSymbolNode;
import mumbler.truffle.node.literal.LongNode;
import mumbler.truffle.node.special.DefineNode;
import mumbler.truffle.node.special.DefineNodeGen;
import mumbler.truffle.node.special.IfNode;
import mumbler.truffle.node.special.LambdaNode;
import mumbler.truffle.node.special.LambdaNodeGen;
import mumbler.truffle.node.special.QuoteNode;
import mumbler.truffle.node.special.QuoteNode.QuoteKind;
import mumbler.truffle.node.special.QuoteNodeGen;
import mumbler.truffle.type.MumblerFunction;
import mumbler.truffle.type.MumblerList;
import mumbler.truffle.type.MumblerSymbol;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;

public class Converter {
    public static MumblerNode convert(Object obj, FrameDescriptor desc) {
        if (obj instanceof Long) {
            return convert((long) obj, desc);
        } else if (obj instanceof BigInteger) {
            return convert((BigInteger) obj, desc);
        } else if (obj instanceof Boolean) {
            return convert((boolean) obj, desc);
        } else if (obj instanceof MumblerSymbol) {
            return convert((MumblerSymbol) obj, desc);
        } else if (obj instanceof MumblerList) {
            return convert((MumblerList<?>) obj, desc);
        } else {
            throw new IllegalArgumentException("Unknown type: " +
                    obj.getClass());
        }
    }

    public static LongNode convert(long number, FrameDescriptor desc) {
        return new LongNode(number);
    }

    public static BigIntegerNode convert(BigInteger number,
            FrameDescriptor desc) {
        return new BigIntegerNode(number);
    }

    public static BooleanNode convert(boolean bool, FrameDescriptor desc) {
        return bool ? BooleanNode.TRUE : BooleanNode.FALSE;
    }

    public static SymbolNode convert(MumblerSymbol sym, FrameDescriptor desc) {
        return SymbolNodeGen.create(desc.findOrAddFrameSlot(sym.name));
    }

    public static MumblerNode convert(MumblerList<?> list,
            FrameDescriptor desc) {
        if (list == MumblerList.EMPTY || list.length() == 0) {
            return new LiteralListNode(MumblerList.EMPTY);
        }

        Object car = list.car();
        if (car instanceof MumblerSymbol) {
            switch (((MumblerSymbol) car).name) {
            case "define":
                return convertDefine(list, desc);
            case "lambda":
                return convertLambda(list, desc);
            case "if":
                return convertIf(list, desc);
            case "quote":
                return convertQuote(list, desc);
            }
        }
        return new InvokeNode(convert(list.car(), desc),
                StreamSupport.stream(list.cdr().spliterator(), false)
                .map(obj -> convert(obj, desc))
                .toArray(size -> new MumblerNode[size]));
    }

    private static DefineNode convertDefine(MumblerList<?> list,
            FrameDescriptor desc) {
        MumblerSymbol sym = (MumblerSymbol) list.cdr().car();
        return DefineNodeGen.create(convert(list.cdr().cdr().car(), desc),
                desc.findOrAddFrameSlot(sym.name));
    }

    @SuppressWarnings("unchecked")
    private static LambdaNode convertLambda(MumblerList<?> list,
            FrameDescriptor desc) {
        FrameDescriptor lambdaEnv = new FrameDescriptor();
        List<FrameSlot> formalParameters = new ArrayList<>();
        for (MumblerSymbol arg : (MumblerList<MumblerSymbol>)
                list.cdr().car()) {
            formalParameters.add(convert(arg, lambdaEnv).getSlot());
        }
        List<MumblerNode> bodyNodes = new ArrayList<>();
        for (Object body : list.cdr().cdr()) {
            bodyNodes.add(convert(body, lambdaEnv));
        }
        bodyNodes.get(bodyNodes.size() - 1).setIsTail();

        MumblerFunction function = MumblerFunction.create(
                formalParameters.toArray(new FrameSlot[] {}),
                bodyNodes.toArray(new MumblerNode[] {}),
                lambdaEnv);
        return LambdaNodeGen.create(function);
    }

    private static IfNode convertIf(MumblerList<?> list, FrameDescriptor desc) {
        return new IfNode(convert(list.cdr().car(), desc),
                convert(list.cdr().cdr().car(), desc),
                convert(list.cdr().cdr().cdr().car(), desc));
    }

    private static QuoteNode convertQuote(MumblerList<?> list,
            FrameDescriptor desc) {
        Object value = list.cdr().car();
        MumblerNode node;
        QuoteKind kind;
        if (value instanceof Long) {
            kind = QuoteKind.LONG;
            node = convert((long) value, desc);
        } else if (value instanceof Boolean) {
            kind = QuoteKind.BOOLEAN;
            node = convert((boolean) value, desc);
        } else if (value instanceof MumblerSymbol) {
            kind = QuoteKind.SYMBOL;
            node = new LiteralSymbolNode((MumblerSymbol) value);
        } else if (value instanceof MumblerList) {
            kind = QuoteKind.LIST;
            node = new LiteralListNode((MumblerList<?>) value);
        } else {
            throw new IllegalArgumentException("Unknown quote type: " +
                    value.getClass());
        }
        return QuoteNodeGen.create(node, kind);
    }
}
