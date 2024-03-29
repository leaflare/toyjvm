package vjvm.runtime.class_.attribute;

import lombok.SneakyThrows;
import lombok.var;
import vjvm.runtime.class_.ConstantPool;
import vjvm.runtime.class_.constant.*;
import vjvm.error.UnimplementedError;

import java.io.DataInput;

public class ConstantValue extends Attribute{

    private final int index;
    private final ConstantPool constantPool;


    @SneakyThrows
    ConstantValue(DataInput input, ConstantPool constantPool) {
        index = input.readUnsignedShort();
        this.constantPool = constantPool;
    }

    Object getValue() {
        // TODO

        var value = constantPool.constant(index);
        if (value instanceof LongConstant) {
            return ((LongConstant) value).value();
        } else if (value instanceof FloatConstant) {
            return ((FloatConstant) value).value();
        } else if (value instanceof DoubleConstant) {
            return ((DoubleConstant) value).value();
        } else if (value instanceof IntegerConstant) {
            return ((IntegerConstant) value).value();
        } else if (value instanceof StringConstant) {
            throw new UnimplementedError();
        } else {
            throw new AssertionError();
        }
    }

}
