package jdemo.basic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {
    public static void main(String[] args) {
        var peoples = new ArrayList<People>();
        peoples.add(new People());

        /* 年龄比较 */
        var comparator1 = Comparator.comparing(People::getAge);
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
