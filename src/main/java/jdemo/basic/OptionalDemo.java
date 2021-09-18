package jdemo.basic;

import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> optional = Optional.ofNullable(null);
        String s = optional.orElse("b");
        System.out.println(s);
    }
}
