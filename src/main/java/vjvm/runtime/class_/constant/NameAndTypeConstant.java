package vjvm.runtime.class_.constant;

import lombok.SneakyThrows;
import vjvm.runtime.class_.JClass;

import java.io.DataInput;

public class NameAndTypeConstant extends Constant {
    private final int nameIndex;
    private final int descriptorIndex;
    private final JClass self;
    private String name;
    private String descriptor;

    @SneakyThrows
    NameAndTypeConstant(DataInput input, JClass self) {
        nameIndex = input.readUnsignedShort();
        descriptorIndex = input.readUnsignedShort();
        this.self = self;
    }

    public String name() {
        if (name == null) {
            name = ((UTF8Constant) self.constantPool().constant(nameIndex)).value();
        }
        return name;
    }

    public String type() {
        if (descriptor == null) {
            descriptor = ((UTF8Constant) self.constantPool().constant(descriptorIndex)).value();
        }
        return descriptor;
    }

    @Override
    public String toString() {
        return String.format("NameAndType: %s:%s", name(), type());
    }
}
