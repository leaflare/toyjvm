package vjvm.classloader;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;
import vjvm.classloader.searchpath.ClassSearchPath;
import vjvm.runtime.class_.JClass;
import vjvm.util.Logger;
import vjvm.vm.VMContext;

import java.io.Closeable;
import java.io.DataInputStream;
import java.util.HashMap;

public class JClassLoader implements Closeable {
    private final JClassLoader parent;
    private final ClassSearchPath[] searchPaths;
    private final HashMap<String, JClass> definedClass = new HashMap<>();
    @Getter
    private final VMContext context;

    public JClassLoader(JClassLoader parent, ClassSearchPath[] searchPaths, VMContext context) {
        this.context = context;
        this.parent = parent;
        this.searchPaths = searchPaths;
    }

    /**
     * Load class
     * <p>
     * If a class is found, construct it using the data returned by ClassSearchPath.findClass and return it.
     * <p>
     * Otherwise, return null.
     */
    public JClass loadClass(String descriptor) {

        // Return directly if already acquired
        JClass defined = definedClass.get(descriptor);
        if (defined != null) {
            return defined;
        }

        // Load class with parent if parent is not null
        if (parent != null) {
            JClass parentResult = parent.loadClass(descriptor);
            if (parentResult != null) {
                return parentResult;
            }
        }

        // Try loading the classes using each ClassSearchPath in turn
        for (ClassSearchPath searchPath : searchPaths) {
            var result = searchPath.findClass(descriptor);
            if (result != null) {
                Logger.debug("Class Found: " + result);

                JClass clazz = new JClass(new DataInputStream(result), this);
                definedClass.put(descriptor, clazz);  // Cached results
                return clazz;
            }
        }

        return null;
    }

//    public JClass loadClass()

    @Override
    @SneakyThrows
    public void close() {
        for (var s : searchPaths)
            s.close();
    }
}
