package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 陈濛
 * @date 2020/11/10 11:09 下午
 */
public class Parallel {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1 ,2 ,3, 4);
        int sum = list.parallelStream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);

        int n = 2;
        List<Integer> a = IntStream.range(10, 15).boxed().collect(Collectors.toList());
        System.out.println(a);
    }
}
