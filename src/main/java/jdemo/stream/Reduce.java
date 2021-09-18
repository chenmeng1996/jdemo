package jdemo.stream;

import java.util.Arrays;
import java.util.List;

public class Reduce {
    public static void main(String[] args) {
        var list = Arrays.asList("a", "b", "c");
        var sb =
        list.stream().reduce(
                new StringBuilder(),
                (stringBuilder, s) -> {
                    if (stringBuilder.length() > 0)
                        stringBuilder.append(",");
                    stringBuilder.append(s);
                    return stringBuilder;
                },
                StringBuilder::append);

        System.out.println(sb.toString());
    }
}
