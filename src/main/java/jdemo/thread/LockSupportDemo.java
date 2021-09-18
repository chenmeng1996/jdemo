package thread;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
    public static void main(String[] args) {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A：暂停执行");
                LockSupport.park();
                System.out.println("A：得到通知，继续执行");
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("B：通知所有人继续执行");
                LockSupport.unpark(threadA);
            }
        });

        threadA.start();
        threadB.start();
    }
}
