package jdemo.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) {
        var semaphore = new Semaphore(2);

        var threads = new ArrayList<Thread>();

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(finalI + "开始运行");
                        Thread.sleep(2000);
                        semaphore.release();
                        System.out.println(finalI + "结束运行");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

//        threads.get(0).interrupt();
//        threads.get(1).interrupt();
    }
}
