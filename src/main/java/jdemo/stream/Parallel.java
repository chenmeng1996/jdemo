package jdemo.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Parallel {
    public static void main(String[] args) {
        var list = Arrays.asList(1 ,2 ,3, 4);
        var sum = list.parallelStream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);

        var n = 2;
        var a = IntStream.range(10, 15).boxed().collect(Collectors.toList());
        System.out.println(a);
    }
}
