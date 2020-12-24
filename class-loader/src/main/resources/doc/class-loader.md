# class-loader 类加载器

**本部分内容基于Java8版本**

相关博文：[https://blog.csdn.net/javazejian/article/details/73413292/]

每个.java文件都存储着需要执行的程序逻辑，这些.java文件经过编译编程扩展名为.class的文件，在.class文件中保存着Java代码经过转换后
的虚拟机指令，当需要使用到某个类时，虚拟机会加载对应的.class文件，并创建对应的class对象，将class文件加载到虚拟机的内存，这个过程
称为类加载

类加载过程：加载 -> 验证 -> 准备 -> 解析 -> 初始化

> 加载：第一个阶段，通过类的完全限定名查找类字节码文件，并利用字节码文件创建一个class对象
> 
> 验证：确保class文件的字节流中包含的信息符合虚拟机的规范要求，不会危害虚拟机自身；主要包括四种验证：文件格式验证、元数据验证、
> 字节码验证、符号引用验证
>
> 准备：为类中的静态变量分配内存并设置该变量的初始值（初始值可理解为int = 0这种默认的值，而不是赋予的值，这个过程也不包含final修饰的静态
> 变量，因为final会在编译的时候就直接分配），注意这个过程只会分配类静态变量，类静态变量会分配在方法区，类实例变量会随着对象一起分配到堆中
>
> 解析：主要将常量池中的符号引用替换为直接引用的过程；符号引用就是一组符号来描述目标，可以是任何字面常量，而直接引用就是直接指向目标的指针、相对
> 偏移量或一个间接定位到目标的句柄
>
>验证-准备-解析：三个阶段也称为链接阶段
>
> 初始化：类加载最后阶段，若该类具有超类，则对其进行初始化，执行静态初始化器和静态初始化成员变量（如准备阶段只设置了默认值的静态变量会在该
> 阶段赋值，成员变量也会初始化）

类加载器的任务：根据一个类的全限定名来读取此类的二进制字节流到JVM中，然后转化为一个与目标类对应的java.lang.Class对象实例，在虚拟机中提供了三
种类加载器：引导类加载器（bootstrap-class-loader）、扩展类（启动类）加载器（ extension-class-loader）、系统加载器（system-class-loader）也称为
应用类加载器

## 引导类（启动类）加载器

引导类（启动类）加载器主要加载的是JVM自身需要的类，这个类加载器使用C++语言实现，是虚拟机自身的一部分，它负责将<JAVA_HOME>/lib路径下的核心类
库或-Xbootclasspath参数指定的路径下的jar包加载到内存中，注意：因为虚拟机是按照文件名识别加载jar包的，如果文件名不被虚拟机识别，即使把jar包
放到lib目录下也是无效的（引导类（启动类）加载器只加载包名为java/javax/sun等开头的类）

## 扩展类加载器

扩展类加载器是指Sun公司实现的sun.misc.Launcher$ExtClassLoader类，由Java语言实现，是Launcher的静态内部类，它负责加载<JAVA_HOME>/lib/ext
目录下或系统变量-Djava.ext.dir指定路径中的类库，开发者可以使用标准扩展类加载器

## 系统类加载器

系统类加载器也称为应用程序加载器，是指Sun公司实现的sun.misc.Launcher$AppClassLoader类，它负责加载系统类路径java -classpath或-D java.class.path
指定路径下的类库，也就是常用的classpath路径，开发者可以直接使用系统类加载器，一般情况下该类加载是程序中默认的类加载器，通过ClassLoader#getSystemClassLoader()
方法获取到该类加载器

在Java的应用程序开发中，类的加载几乎是由以上三种类加载器相互配合执行的，在必要时，我们也可以自定义类加载器；需要注意的是，在Java虚拟机中对class文件
采用的是按需加载的方式，也就是说当需要使用到该类的时候才会将它的class文件加载到内存生成class对象，而加载某个类的class文件时，Java虚拟机采用的是双亲委派
模式即把请求交由父类处理，它是一种任务委派模式

## 双亲委派模式

### 双亲委派模式工作原理

双亲委派模式要求除了顶层的引导类（启动类）加载器之外，其余的类加载器都应当有自己的父类加载器，注意双亲委派模式中的父子关系并非通常说的类继承关系，而是
采用组合关系来复用父类加载器中的相关代码（Thinking In Java中建议，如果使用继承和组合都能实现的情况下，优先使用组合关系），类加载器之间的关系如下：

引导类（启动类）加载器 <--- 向上委托 --- 扩展类加载器 <--- 向上委托 --- 系统类加载器 <--- 向上委托 --- 自定义加载器
--------------------------------父类加载失败，交由子类加载器自己处理-------------------------------------->

工作原理：如果一个类加载器收到了类加载请求，并不会自己先去加载，而是把这个请求委托给父类的加载器去执行，如果父类加载器还存在父类加载器，则进一步向上
委托，依次递归，请求最终将达到顶层的引导类（启动类）加载器，如果父类加载器可以完成任务，就成功返回，如果无法完成加载，子加载器才会尝试自己加载，这就是
双亲委派模式

* 双亲委派模式的优势：Java类随着它的类加载器一起具备了一种带有优先级的层次关系，通过这种层级关系可以避免类的重复加载，当父类已经加载了该类时，就不会子加载器
再加载一次；其次是考虑到安全因素，Java核心API中定义类型不会被随意替换

### Java中定义的类加载器以及双亲委派模式的实现

abstract class ClassLoader是类加载器的顶层类，它是一个抽象类，其后所有的类加载器都继承自ClassLoader类（不包括引导类加载器）

ClassLoader ---> SecureClassLoader ---> URLClassLoader ---> Launcher（ExtClassLoader/AppClassLoader）

1. 重要方法浅析

> loadClass(String name)：根据包全限定名称加载指定的二进制类型，该方法是ClassLoader自己实现的，方法中的逻辑就是双亲委派的实现
>
> loadClass(String name, boolean resolve)：是一个重载方法，resolve参数代表是否生成class对象的同时进行解析等相关操作
```
// 如果不想重新定义类加载器，只想在运行时加载自己的类，可以直接使用this.getClass().getClassLoader.loadClass("className")即可获取到指定的class对象
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
  synchronized (getClassLoadingLock(name)) {
      // First, check if the class has already been loaded
      // 先从缓存中查找该class对象，如果存在则不需要重新加载，直接返回
      Class<?> c = findLoadedClass(name);
      if (c == null) {
          long t0 = System.nanoTime();
          try {
              if (parent != null) {
                  // 如果不存在，且父类加载器非空，则委托给父类加载器去加载
                  c = parent.loadClass(name, false);
              } else {
                  // 如果不存在，且上一级父类加载为空，则委托给引导类加载器去加载 
                  c = findBootstrapClassOrNull(name);
              }
          } catch (ClassNotFoundException e) {
              // ClassNotFoundException thrown if class not found
              // from the non-null parent class loader
          }

          if (c == null) {
              // If still not found, then invoke findClass in order
              // to find the class.
              long t1 = System.nanoTime();
              // 如果都没有找到，则通过自定义实现的findClass方法区查找并加载（自定义的类加载器需实现该方法）
              c = findClass(name);

              // this is the defining class loader; record the stats
              sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
              sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
              sun.misc.PerfCounter.getFindClasses().increment();
          }
      }
      if (resolve) {
          resolveClass(c);
      }
      return c;
  }
}
```

> findClass(String name)：继承ClassLoader类覆盖findClass方法，把自定义的类加载逻辑写在该方法中，在父类加载器加载失败后，会调用自己的findClass方法
> 来完成加载，这样就可以保证自定义的类加载器也符合双亲委派模式；需要注意的是在ClassLoader类中并没有实现findClass方法的具体代码逻辑，而是抛出
> ClassNotFoundException异常，同时findClass方法和defineClass方法通常一起使用
>
> defineClass(byte[] b, int off, int len)：用来将byte字节流解析成JVM能够识别的Class对象（ClassLoader中已实现该方法），通过这个方法不仅能够
> 通过class文件实例化Class对象，也可以通过其他方式实例化对象，如通过网络接收一个类的字节码，然后转换为byte字节流创建对应的Class对象；通常情况下，在
> 自定义类加载器时，会直接覆盖ClassLoader类的findClass方法并编写加载规则，取得要加载类的字节码后转换成流，然后调用defineClass方法生成Class对象，
> 需要注意的是，如果直接调用defineClass方法生成类的Class对象，这个类的Class对象并没有解析，其解析操作需要等待初始化阶段进行

> resolveClass(Class<?> c)：使用该方法可以使类的Class对象创建完成也同时被解析

* SecureClassLoader

SecureClassLoader继承ClassLoader类，新增了几个与使用相关的代码源（对代码源的位置及其证书的验证）和权限定义类验证（指对class源码的访问权限）
的方法

* URLClassLoader

URLClassLoader继承SecureClassLoader类，URLClassLoader类为ClassLoader类中的findClass/findResource等方法提供了具体的实现，并新增了
URLClassPath类协助取得Class字节码流功能，如果自定义类加载器时，不需要实现太过复杂的需求，则可以直接继承URLClassLoader类，直接使用其实现的
方法，使自定义类加载器编写更加简洁

URLClassLoader类中存在一个URLClassPath类，通过这个类可以找到要加载的字节码流，URLClassPath类负责找到要加载的字节码，再读取成字节流，最后
通过defineClass方法创建类的Class对象

在创建URLClassPath对象时，会根据构造URLClassLoader对象传递的URL数组中的路径判断是文件还是jar包，然后根据不同的路径创建FileLoader或者
JarLoader或默认Loader类去加载相应路径下的class文件，而当JVM调用findClass方法时，就由这三个加载器中的一个将class文件的字节码流加载到内存中，
最后利用字节码流创建类的class对象

* ExtClassLoader/AppClassLoader

这两个都是sun.misc.Launcher类的内部类，都继承了URLClassLoader类，而sun.misc.Launcher主要被系统用于启动主应用程序

## 类加载器之间的关系

* 引导类（启动类）加载器，由C++编写，没有父类
* 扩展类加载器（ExtClassLoader），由Java语言实现，父类加载器为Null
* 系统类加载器（SystemClassLoader），由Java语言实现，父类加载器为ExtClassLoader
* 自定义类加载器，父类加载器为AppClassLoader

1. 源码分析

sun.misc.Launcher类初始化时，首先会创建ExtClassLoader类加载器，然后再创建AppClassLoader并把ExtClassLoader类传递给它作为父类加载器，同时
还把AppClassLoader默认设置为线程上下文类加载器；在创建ExtClassLoader时强制设置了其父类加载器为Null（这里指的父类子类不是Java中的继承关系）

