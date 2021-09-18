package jdemo.thread;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    public static void main(String[] args) {
        var exchanger = new Exchanger<String>();

        var threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    String data = exchanger.exchange("Hi, it is threadA");
                    System.out.println("ThreadA收到：" + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.start();

        var threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    var data = exchanger.exchange("Hi, it is threadB");
                    System.out.println("ThreadB收到：" + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadB.start();
    }
}
