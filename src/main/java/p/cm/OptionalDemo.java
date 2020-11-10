package p.cm;

import java.util.Optional;

/**
 * @author 陈濛
 * @date 2020/11/7 10:27 下午
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> optional = Optional.ofNullable(null);
        String s = optional.orElse("b");
        System.out.println(s);
    }
}
