import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    // 过滤器，过滤掉不满足条件的元素
    @Test
    public void filter() {
        // 只打印出偶数
        Stream.of(1, 2, 3, 4, 5, 6, 7).filter(e -> e % 2 == 0).forEach(System.out::println);
    }

    @Test
    public void allMatch() {
        boolean b1 = Stream.of(1, 2, 3).allMatch(e -> e % 2 == 0);// 是否所有元素都满足指定条件
        boolean b2 = Stream.of(2, 4, 6).allMatch(e -> e % 2 == 0);
        boolean b3 = Stream.of(1, 2, 3).anyMatch(e -> e % 2 == 0);// 是否存在至少一个元素满足指定条件
        Assert.assertFalse(b1);
        Assert.assertTrue(b2);
        Assert.assertTrue(b3);
    }

    // 计数
    @Test
    public void count() {
        System.out.println(Stream.of(1, 2, 3).count());
    }

    // 去重
    @Test
    public void distinct() {
        Stream.of(1, 2, 3, 3).distinct().forEach(e -> System.out.print(e + "\t"));
    }

    // flatMap与map的差异
    @Test
    public void flatMapAndMap() {
        List<String[]> eggs = new ArrayList<>();

        // 第一箱鸡蛋
        eggs.add(new String[]{"鸡蛋_1", "鸡蛋_1", "鸡蛋_1", "鸡蛋_1", "鸡蛋_1"});
        // 第二箱鸡蛋
        eggs.add(new String[]{"鸡蛋_2", "鸡蛋_2", "鸡蛋_2", "鸡蛋_2", "鸡蛋_2"});

        Stream<Stream<String>> stream1 =
                eggs.stream().map(e -> Arrays.stream(e));// 只能该流中的各个元素做类型切换
        stream1.forEach(x -> System.out.println(x.collect(Collectors.toList())));

        Stream<String> stream2 =
                eggs.stream().flatMap(e -> Arrays.stream(e));// 可以将该流中的各个子集合中的元素汇聚到一个大集合中
        stream2.forEach(x -> System.out.print(x + "\t"));

    }

    /**
     * forEach与forEachOrdered的区别
     */
    @Test
    public void forEach() {
        int length = 10;
        ArrayList<Integer> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(i);
        }
        list.stream().forEachOrdered(e -> System.out.print(e + "\t"));  // 按照顺序消费
        System.out.println();
        list.stream().forEach(e -> System.out.print(e + "\t")); // 在parallelStream的流中并发的消费
        System.out.println();

        /**
         * 使用的parallelStream的流，这个流表示一个并行流，也就是在程序内部迭代的时候，会帮你免费的并行处理，所以这种流使用forEach
         * 效率高于forEachOrdered
         */
        list.parallelStream().forEachOrdered(e -> System.out.print(e + "\t"));
        System.out.println();
        list.parallelStream().forEach(e -> System.out.print(e + "\t"));
        System.out.println();
    }

    // 限制长度
    @Test
    public void limit() {
        Stream.of(1, 2, 3).limit(2).forEach(System.out::println);
    }

    // 最大值；min、sorted不再测试，都是基于Comparator
    @Test
    public void max() {
        // 按照User的年龄取最大，注意：age不能为null，否则报空指针异常
        User user = Stream.of(new User(10), new User(30), new User(20))
                .max(Comparator.comparing(User::getAge)).get();
        System.out.println(user);
    }

    @Data
    @AllArgsConstructor
    private static class User {
        private Integer age;
    }

    @Test
    public void reduce() {
        System.out.println(
                Stream.of(1, 2, 3, 4)
                        .reduce((e1, e2) -> {
                            System.out.printf("e1=%s, e2=%s, reduce之后=%s\n", e1, e2, e1 + e2);
                            return e1 + e2;
                        })
                        .get()
        );
    }

    @Test
    public void toArray() {
        // 两种方式List转数组
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        Integer[] arr1 = list.stream().toArray(Integer[]::new);
        Integer[] arr2 = list.toArray(new Integer[0]);
    }


}
