package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈濛
 * @date 2020/11/7 11:40 下午
 */
public class Joining {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "b");
        String joined = strings.stream().collect(Collectors.joining(",", "[", "]"));
        System.out.println(joined);
    }
}
