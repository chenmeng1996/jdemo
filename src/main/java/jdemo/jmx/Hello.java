package jdemo.jmx;

// 资源对象，类名必须是Hello
public class Hello implements HelloMBean {
    private String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        System.out.println("Hello world, "+ name);
    }

    @Override
    public void printHello(String whoName) {
        System.out.println("Hello, "+whoName);
    }
}