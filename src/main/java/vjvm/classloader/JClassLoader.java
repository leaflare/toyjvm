package vjvm.classloader;

import lombok.var;
import lombok.Getter;
import lombok.SneakyThrows;
import vjvm.classloader.searchpath.ClassSearchPath;
import vjvm.runtime.JClass;
import vjvm.vm.VMContext;
import vjvm.utils.UnimplementedError;

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
   *
   * If a class is found, construct it using the data returned by ClassSearchPath.findClass and return it.
   *
   * Otherwise, return null.
   */
  public JClass loadClass(String descriptor) {
    // To construct a JClass, use the following constructor
    // return new JClass(new DataInputStream(istream_from_file), this);

    // If already acquired, return directly
    // definedClass is used as a cache
    JClass defined = definedClass.get(descriptor);
    if (defined != null) {
      return defined;
    }

    // If parent is not null, use parent to load the class
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
        JClass clazz = new JClass(new DataInputStream(result), this);
        definedClass.put(descriptor, clazz);  // save results
        return clazz;
      }
    }

    return null;
  }

  @Override
  @SneakyThrows
  public void close() {
    for (var s : searchPaths)
      s.close();
  }
}
