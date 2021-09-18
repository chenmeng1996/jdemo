package jdemo.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Joining {
    public static void main(String[] args) {
        var strings = Arrays.asList("a", "b");
        var joined = strings.stream().collect(Collectors.joining(",", "[", "]"));
        System.out.println(joined);
    }
}
