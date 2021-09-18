package thread;

import java.util.concurrent.*;

/**
 * @author 陈濛
 * @date 2020/10/5 4:04 下午
 *
 * 线程池 submit 异步任务
 */
public class FutureDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("异步任务开始");
                Thread.sleep(2000);
                System.out.println("异步任务结束");
                return "OK";
            }
        });

        System.out.println("提交完异步任务，主线程继续执行");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程需要使用异步结果，等待异步任务完成");
        try {
            String res = future.get();
            System.out.println("异步结果为:" + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
