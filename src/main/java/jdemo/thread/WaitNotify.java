package thread;

public class WaitNotify {

    public static void main(String[] args) throws InterruptedException {

        final Object lock = new Object();

        Thread threadA = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        System.out.println("A：暂停执行");
                        lock.wait();
                        System.out.println("A：得到通知，继续执行");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadA.start();


        Thread threadB = new Thread(new Runnable() {
            public void run() {
                synchronized (lock) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.notifyAll();
                    System.out.println("B：通知所有人继续执行");
                }
            }
        });
        threadB.start();

        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
