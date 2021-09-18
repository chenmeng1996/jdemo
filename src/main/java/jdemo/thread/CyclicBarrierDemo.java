package jdemo.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) {

        var cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("开始跑！");
            }
        });

        for (int i = 0; i < 9; i++) {
            int finalI = i;
            var thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(finalI + "准备就绪");
                    try {
                        Thread.sleep(1000);
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(finalI + "起跑");
                }
            });
            thread.start();
        }

    }
}
