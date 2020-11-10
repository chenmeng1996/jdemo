package p.cm;

import java.util.*;

/**
 * @author 陈濛
 * @date 2020/11/7 10:41 下午
 */
public class ComparatorDemo {
    public static void main(String[] args) {
        List<People> peoples = new ArrayList<>();
        /* 年龄比较 */
        Comparator<People> comparator1 = Comparator.comparing(People::getAge);
        peoples.sort(comparator1);
        /* 分数比较 */
        Comparator<List<Integer>> scoreComparator = Comparator.comparingInt(x -> x.stream().mapToInt(Integer::intValue).sum());
        Comparator<People> comparator2 = Comparator.comparing(People::getScores, scoreComparator);
        peoples.sort(comparator2);
    }

    static class People {
        private int age;
        private List<Integer> scores;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<Integer> getScores() {
            return scores;
        }

        public void setScores(List<Integer> scores) {
            this.scores = scores;
        }
    }
}
