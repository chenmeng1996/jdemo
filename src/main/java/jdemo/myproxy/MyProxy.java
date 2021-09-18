package jdemo.myproxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

public class MyProxy {
    /**
     * 1.生成代理类的源代码
     * 2.将生成的源代码输出到磁盘，保存为.java文件
     * 3.编译源代码，并生成.java文件
     * 4.将class文件中的内容，动态加载到JVM中
     * 5.返回被代理后的代理对象
     */
    public static Object newProxyInstance(MyClassLoader loader, Class<?>[] interfaces, MyInvocationHandler h) {
        try {
            // 1.生成代理类的源代码
            String src = genSesource(interfaces[0]);
            // 2.将生成的源代码输出到磁盘，保存为.java文件
            String path = Objects.requireNonNull(MyProxy.class.getResource("")).getPath();
            File file = new File(path+"$Proxy0.java");

            FileWriter fw = new FileWriter(file);
            fw.write(src);
            fw.close();

            // 3.编译源代码，并生成.java文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null,null,null);
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjects(file);

            JavaCompiler.CompilationTask task = javaCompiler.getTask(null,manager,null,null,null,iterable);
            task.call();
            manager.close();

            // 4.将class文件中的内容，动态加载到JVM中
            Class<?> proxyClass = loader.findClass("$Proxy0");

            // 5.返回被代理后的代理对象
            Constructor<?> c = proxyClass.getConstructor(MyInvocationHandler.class);
            return c.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String genSesource(Class<?> interfaces){
        StringBuilder src = new StringBuilder();
        String ln = "\n";
        src.append("package p.cm.jdemo.myproxy;").append(ln)
                .append("import java.lang.reflect.Method;").append(ln)
                .append("public class $Proxy0 implements ").append(interfaces.getName()).append("{").append(ln)
                .append("private MyInvocationHandler h;").append(ln)
                .append("public $Proxy0(MyInvocationHandler h){").append(ln)
                .append("this.h=h;").append(ln)
                .append("}").append(ln);

        for(Method method:interfaces.getMethods()){
            src.append("public ").append(method.getReturnType()).append(" ").append(method.getName()).append("() {").append(ln)
                    .append("try {").append(ln)
                    .append("Method m = ").append(interfaces.getName()).append(".class.getMethod(\"").append(method.getName()).append("\");").append(ln)
                    .append("this.h.invoke(this, m, new Object[]{});").append(ln)
                    .append("}catch (Throwable e){").append(ln)
                    .append("e.printStackTrace();").append(ln)
                    .append("}").append(ln)
                    .append("}").append(ln);
        }
        src.append("}");

        return src.toString();

    }
}
