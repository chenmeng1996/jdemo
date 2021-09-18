package jdemo.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        var countDownLatch1 = new CountDownLatch(1);
        var countDownLatch2 = new CountDownLatch(4);


        for (var i = 0; i < 10; i++) {
            var finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("运动员" + finalI+ "：等待裁判鸣枪");
                    try {
                        countDownLatch1.await();
                        System.out.println("运动员" + finalI + "：开始起跑");
                        Thread.sleep(new Random().nextInt(2000));
                        System.out.println("运动员" + finalI + "：到达终点线");
                        countDownLatch2.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        System.out.println("裁判：在准备信号枪");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("裁判：鸣枪");
        countDownLatch1.countDown();

        System.out.println("裁判：等待所有运动员跑完");
        try {
            countDownLatch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("裁判：比赛结束");
    }
}
