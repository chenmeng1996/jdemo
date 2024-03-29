package jdemo.thread;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    public static void main(String[] args) {

        var lock = new ReentrantLock();
        var condition1 = lock.newCondition();

        var threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("A：暂停执行");
                lock.lock();
                try {
                    condition1.await();
                    System.out.println("A：得到通知，继续执行");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        var threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(2000);
                    System.out.println("B：通知所有人继续执行");
                    condition1.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
