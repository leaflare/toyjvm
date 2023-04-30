package vjvm.interpreter.instruction.references;

import lombok.var;
import vjvm.interpreter.instruction.Instruction;
import vjvm.runtime.JThread;
import vjvm.runtime.ProgramCounter;
import vjvm.runtime.class_.MethodInfo;
import vjvm.runtime.class_.constant.ClassInfoConstant;

public class NEW extends Instruction {
  /**
   * new an Object
   */

    private final ClassInfoConstant classInfo;

    public NEW(ProgramCounter pc, MethodInfo method) {
        var index = pc.ushort();
        this.classInfo = (ClassInfoConstant) method.jClass().constantPool().constant(index);
    }

    @Override
    public void run(JThread thread) {
        var stack = thread.top().stack();
        classInfo.getJClass().init(thread);
        stack.pushReference(thread.context().heap().objAlloc(classInfo.getJClass()));
    }

    @Override
    public String toString() {
        return "new " + classInfo.name();
    }
}
