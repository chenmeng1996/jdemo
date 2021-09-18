package thread;

import java.util.concurrent.*;

/**
 * @author 陈濛
 * @date 2020/10/6 10:55 上午
 *
 * FutureTask是异步任务，可以submit到线程池，也可以execute到线程池
 */
public class FutureTaskDemo {

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "OK";
            }
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(futureTask);

        try {
            String res = futureTask.get();
            System.out.println(res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
