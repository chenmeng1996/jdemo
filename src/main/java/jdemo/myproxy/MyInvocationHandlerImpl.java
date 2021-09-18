package myproxy;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyInvocationHandlerImpl implements MyInvocationHandler {

    public static void main(String[] args) {
        Calculator productService = new CalculatorImpl();
        Calculator proxy = (Calculator) new MyInvocationHandlerImpl().getInstance(productService);
        proxy.hello();
    }

    private Calculator target;

    public Object getInstance(Calculator target){
        this.target = target;
        Class clazz = this.target.getClass();
        // 参数1：被代理类的类加载器 参数2:被代理类的接口 参数3
        // 这里的MyClassLoader先用new的方式保证编译不报错，后面会修改
        return MyProxy.newProxyInstance(new MyClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate  = simpleDateFormat.format(new Date());
        System.out.println("日期【"+currentDate + "】");

        return method.invoke(this.target,args);
    }


}
