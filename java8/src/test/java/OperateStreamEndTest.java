import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 操作Stream（终结方法）
 * Created by zhuk on 2018/3/28.
 */
public class OperateStreamEndTest {

    //GroupBy - 分组
    @Test
    public void test1 () {
        Map<String, Long> collect = Stream.of("aa", "bb", "cc", "aa", "bb")
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        System.out.println(collect);
    }

    // 分组情况：1 =< s < 10、10 <= s < 20、其他情况
    @Test
    public void test1_1 () {
        Map<String, List<Integer>> collect = Stream.of(1, 2, 3, 4, 10, 11, 12, 13, 20, 21, 27)
                .collect(Collectors.groupingBy((Integer s) -> {
                    if (s >= 1 && s < 10) {
                        return "group1";
                    } else if (s >= 10 && s < 20) {
                        return "group2";
                    } else {
                        return "group3";
                    }
                }, Collectors.toList()));
        System.out.println(collect);
    }

    @Test
    public void test1_2 () {
        Map<Integer, List<String>> collect = Stream.of("1", "12", "13", "123", "234", "3455")
                .collect(Collectors.groupingBy(e -> e.length(), Collectors.toList()));
        System.out.println(collect);
    }

    //与test1_2效果相同，默认模式即toList()
    @Test
    public void test1_3 () {
        Map<Integer, List<String>> collect = Stream.of("1", "12", "13", "123", "234", "3455")
                .collect(Collectors.groupingBy(e -> e.length()));
        System.out.println(collect);
    }

    //MaxMin - 最大最小
    @Test
    public void test2 () {
//        Stream.of("a", "bb", "ccc", "dddd").max((e1, e2)-> e1.length()-e2.length()).ifPresent(System.out::println);
        Stream.of("a", "bb", "ccc", "dddd").max(Comparator.comparingInt(e -> e.length())).ifPresent(System.out::println);
    }

    @Test
    public void test2_1 () {
        Optional<Integer> max = Stream.of(1, 2, 3, 4).max((e1, e2) -> {
            System.out.println(String.format("e1=%s, e2=%s", e1, e2));
            return e1 - e2;
        });
        System.out.println(max.get());
    }

    // 测试根据对象的某个属性获取最大值，以及排序
    @Test
    public void test2_2_1 () {
        Person p1 = new Person(5, "zk5");
        Person p2 = new Person(7, "zk7");
        Person p3 = new Person(1, "zk1");
        Person p4 = new Person(10, "zk10");
        List<Person> sourceList = Lists.newArrayList(p1, p2, p3, p4);
        // 获取最大值
        sourceList.stream().max(Comparator.comparingInt(Person::getId)).ifPresent(System.out::println);

        // 排序
        List<Person> sortedList = sourceList.stream()
                .sorted(Comparator.comparingInt(Person::getId)).collect(Collectors.toList());
        System.out.println(sortedList);
    }

    // 测试根据对象的某个属性获取最大值（备注：只是属性类型为引用对象）
    @Test
    public void test2_2_2 () {
        Person p1 = new Person(5, "zk5");
        Person p2 = new Person(7, "zk7");
        Person p3 = new Person(1, "zk1");
        Person p4 = new Person(10, "zk10");
        List<School> sourceList = Lists.newArrayList((new School(p1)), new School(p2), new School(p3), new School(p4));

        // 最大值
        sourceList.stream().max(Comparator.comparing(School::getPerson)).ifPresent(System.out::println);

        // 排序1
        System.out.println(sourceList.stream().sorted(Comparator.comparing(School::getPerson)).collect(Collectors.toList()));
        // 排序2
        System.out.println(
                sourceList.stream().sorted(Comparator.comparingInt(s -> s.getPerson().getId()))
                        .collect(Collectors.toList())
        );

    }

    //Reduce - 规约
    @Test
    public void test3 () {
        Stream.of("1.0", "2.0", "3.0").mapToDouble(e -> Double.valueOf(e))
                .reduce(Double::sum).ifPresent(System.out::println);
//                .reduce((e1, e2) -> e1 + e2).ifPresent(System.out::println);
    }

    @Test
    public void test3_1 () {
        Optional<Integer> reduce = Stream.of(1, 2, 3, 4).reduce((e1, e2) -> {
            int sum = e1 + e2;
            System.out.println(String.format("e1=%s, e2=%s, sum=%s", e1, e2, sum));
            return sum;
        });
        System.out.println(reduce.get());
    }

    //Collect - 收集
    @Test
    public void test4 () {
        String collect = IntStream.range(1, 10).boxed()
                .map(o -> String.valueOf(o))
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println(collect);
    }

    //Partition - 分区
    //partitioningBy：分组后的key只有boolean；groupingBy：分组后的key可自定义
    @Test
    public void test5 () {
        Map<Boolean, List<Integer>> collect = IntStream.of(1, 3, 5, 8).boxed().collect(Collectors.partitioningBy(o -> o % 2 == 0));
        System.out.println(collect);
    }

    @Test
    public void test5_1 () {
        Map<Boolean, List<Integer>> collect = IntStream.of(1, 3, 5, 8).boxed()
                .collect(Collectors.groupingBy((Integer o) -> {
                    int i = o % 2;
                    if (i == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }, Collectors.toList()));
        System.out.println(collect);
    }

    //Match - 匹配
    @Test
    public void test6 () {
        boolean result = IntStream.range(1, 10).anyMatch(e -> e > 5);
        System.out.println(result);
    }

    //Collectors.toMap()测试
    @Test
    public void test7 () {
        List<Person> list = new ArrayList();
        list.add(new Person(1, "haha"));
        list.add(new Person(2, "rere"));
        list.add(new Person(3, "fefe"));

        Map<Integer, Person> mapp = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println(mapp);
        System.out.println(mapp.get(1).getName());
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println(map);
    }

    @Data
    @ToString
    @AllArgsConstructor
    private class Person implements Comparable {
        private Integer id;
        private String name;

        @Override
        public int compareTo (Object o) {
            return this.getId() - ((Person) o).getId();
        }
    }

    @Data
    @ToString
    @AllArgsConstructor
    private class School {
        private Person person;
    }

}
