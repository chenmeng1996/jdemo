package jdemo.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

// 自定义Collector，实现joining
public class StringCollector implements Collector<String, StringCollector.StringCombiner, String> {

    public static void main(String[] args) {
        var list = Arrays.asList("a", "b", "c");
        var s = list.stream().collect(new StringCollector(",", "[", "]"));
        System.out.println(s);
    }


    private String delim;
    private String prefix;
    private String suffix;

    public StringCollector(String delim, String prefix, String suffix) {
        this.delim = delim;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    // 创建容器的工厂函数
    @Override
    public Supplier<StringCombiner> supplier() {
        return () -> new StringCombiner(delim, prefix, suffix);
    }

    // 累加元素到容器
    @Override
    public BiConsumer<StringCombiner, String> accumulator() {
        return StringCombiner::add;
    }

    // 合并容器
    @Override
    public BinaryOperator<StringCombiner> combiner() {
        return StringCombiner::merge;
    }

    // 容器转输出元素
    @Override
    public Function<StringCombiner, String> finisher() {
        return StringCombiner::toString;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    static class StringCombiner {
        private final StringBuilder builder;
        private final String delim;
        private final String prefix;
        private final String suffix;

        public StringCombiner(String delim, String prefix, String suffix) {
            builder = new StringBuilder();
            this.delim = delim;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public StringCombiner add(String element) {
            if (builder.length() > 0)
                builder.append(delim);
            builder.append(element);
            return this;
        }

        public StringCombiner merge(StringCombiner other) {
            builder.append(other.builder);
            return this;
        }

        public String toString() {
            builder.insert(0, prefix);
            builder.append(suffix);
            return builder.toString();
        }
    }
}
