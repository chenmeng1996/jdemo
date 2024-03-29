package jdemo.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingByAndDownStream {

    public static void main(String[] args) {
        var peoples = new ArrayList<People>();
        peoples.add(new People("a", 1));
        peoples.add(new People("b", 1));
        peoples.add(new People("c", 2));
        var peopleMap = peoples
                .stream()
                .collect(Collectors.groupingBy(
                        People::getAge,
                        Collectors.mapping(People::getName, Collectors.toList())));
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
