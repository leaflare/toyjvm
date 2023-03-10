# Notes

## Lab1
#### ClassLoaders:

1. The Bootstrap Loader, is responsible for loading the class files of the Java standard library;
2. The User Loader, is responsible for searching for the class from the path specified by the user.
Java uses a policy called parent-first by default: each loader (except the Bootstrap Loader) has a parent, and when searching for a class, it first delegates the search to the parent, and then searches its own load path if it cannot find it.

#### Dump: print the class file information

Simplified version of the `javap`

When the dump command is executed, the framework invokes parsing the command line parameters and calling the Dump.call method.

1. Create a VMContext, a context that contains all the data needed to run the JVM. Call the constructor of the ClassLoader to initialise it.
2. Try to load the required class with the userLoader. If the load fails, an error is reported.
3. If the load is successful, output information about the class in the dump method.

```
struct ClassFile {
  u4              magic;
  u2              minor_version;
  u2              major_version;
  u2              constant_pool_count;
  cp_info         constant_pool[constant_pool_count - 1]; // Constant pool
  u2              access_flags;
  u2              this_class;
  u2              super_class;
  u2              interfaces_count;
  u2              interfaces[interfaces_count]; // Interface implemented by class
  u2              fields_count;
  field_info      fields[fields_count]; // Member variables of a class
  u2              methods_count;
  method_info     methods[methods_count]; // Methods of class
  u2              attributes_count;
  attribute_info  attributes[attributes_count];
};
```



# Code Records
## Initialisation
v1.0.1-v1.0.3 Implemented the ClassSearchPath class. Complemented the constant type.
## Lab1
v1.1.1 Implemented the loadClass Class in JClassLoader.java. Passed Test FindClasses.
v1.1.2-v1.1.3 Implemented the JClass class and the dump class. Passed Test DumpClass.
