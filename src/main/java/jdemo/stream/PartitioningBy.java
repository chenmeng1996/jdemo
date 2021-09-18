package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitioningBy {

    public static void main(String[] args) {
        List<People> peoples = new ArrayList<>();
        peoples.add(new People("a", 1));
        peoples.add(new People("b", 1));
        peoples.add(new People("c", 2));
        Map<Boolean, List<People>> peopleMap = peoples
                .stream()
                .collect(Collectors.partitioningBy(x -> x.getAge() < 18));
        System.out.println(peopleMap);
    }



    static class People {
        private String name;
        private int age;

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
