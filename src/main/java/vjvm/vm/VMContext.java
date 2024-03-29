package vjvm.vm;

import lombok.Getter;
import lombok.var;
import vjvm.classfiledefs.Descriptors;
import vjvm.classloader.JClassLoader;
import vjvm.classloader.searchpath.ClassSearchPath;
import vjvm.classloader.searchpath.ModuleSearchPath;
import vjvm.interpreter.JInterpreter;
import vjvm.runtime.JThread;
import vjvm.runtime.frame.Slots;
import vjvm.runtime.heap.JHeap;

import java.util.ArrayList;

public class VMContext {
    @Getter
    private final JClassLoader bootstrapLoader;
    @Getter
    private final JClassLoader userLoader;
    @Getter
    private final JInterpreter interpreter;
    @Getter
    private final ArrayList<JThread> threads = new ArrayList<>();
    @Getter
    private final JHeap heap;

    public VMContext(String userClassPath) {
        interpreter = new JInterpreter();

        heap = new JHeap();

        bootstrapLoader = new JClassLoader(
            null,
            getSystemSearchPaths(),
            this
        );

        userLoader = new JClassLoader(
            bootstrapLoader,
            ClassSearchPath.constructSearchPath(userClassPath),
            this
        );
    }

    void run(String entryClass) {
        var initThread = new JThread(this);
        threads.add(initThread);

        var entry = userLoader.loadClass(Descriptors.of(entryClass));

        var mainMethod = entry.findMethod("main", "([Ljava/lang/String;)V");
        assert mainMethod.jClass() == entry;
        interpreter.invoke(mainMethod, initThread, new Slots(1));
    }

    private static ClassSearchPath[] getSystemSearchPaths() {
        var bootClassPath = System.getProperty("sun.boot.class.path");

        if (bootClassPath != null) {
            return ClassSearchPath.constructSearchPath(bootClassPath);
        }

        // For compatibility with JDK9+
        return new ClassSearchPath[] {
            new ModuleSearchPath()
        };
    }
}
