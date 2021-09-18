package thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    public static void main(String[] args) {

        System.out.println("有个很耗时的任务，让别人帮我做一下吧");
        List<Integer> a = Arrays.asList(1, 2, 3);

        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int sum = a.stream().mapToInt(Integer::intValue).sum();
            future.complete(sum);
        });

        System.out.println("我继续运行");
        System.out.println("需要用结果了，取数据");
    }
}
