package p.cm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理demo
 */
public class DynamicProxyDemo {

    public static void main(String[] args) {
        Calculator calculator = new CalculatorImpl();

        LogHandler logHandler = new LogHandler(calculator);
        Calculator proxy = (Calculator) Proxy.newProxyInstance(
                calculator.getClass().getClassLoader(),
                calculator.getClass().getInterfaces(),
                logHandler);
        proxy.hello();
    }



    interface Calculator {
        void hello();
    }

    static class CalculatorImpl implements Calculator {
        @Override
        public void hello() {
            System.out.println("hello world");
        }
    }

    /**
     * 提供代理的类
     */
    static class LogHandler implements InvocationHandler {
        //传入的原对象
        private Object obj;

        public LogHandler(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            doBefore();
            //执行原对象的方法
            Object o = method.invoke(obj, args);
            doAfter();
            return o;
        }

        public void doBefore() {
            System.out.println("do before");
        }

        public void doAfter() {
            System.out.println("do after");
        }
    }

}
