package stream;

import java.util.Arrays;
import java.util.List;

/**
 * @author 陈濛
 * @date 2020/11/8 10:13 下午
 */
public class Reduce {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        StringBuilder sb =
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
