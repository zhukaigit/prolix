import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsTest {

    // 求平均值
    @Test
    public void testAveraging () {
        Double collect = Stream.of(1, 2, 3).collect(Collectors.averagingInt(e -> e));
        System.out.println(collect);
    }

    // 流式处理之后再进行整合返回
    @Test
    public void testCollectingAndThen () {
        // 返回：各个元素减5之后的平均值*100
        Double collect = Stream.of(10, 5, 15).collect(
                Collectors.collectingAndThen(
                        Collectors.averagingDouble(e -> (double) (e - 5)), // 对各元素减5后求平均值
                        e -> e * 100)   // 对上面的平均值*100
        );
        System.out.println(collect);
    }

    // 返回次数
    @Test
    public void testCounting () {
        // 统计元素的个数
        Long collect = Stream.of(10, 5, 15).collect(
                Collectors.counting()
        );
        System.out.println(collect);
    }

    // 分组
    @Test
    public void testGroupingByConcurrent () {
        ConcurrentMap<Integer, List<Integer>> collect = Stream.of(10, 5, 15, 10).collect(
                Collectors.groupingByConcurrent(e -> e)
        );
        System.out.println(collect);

        ConcurrentMap<Integer, Long> collect2 = Stream.of(10, 5, 15, 10).collect(
                Collectors.groupingByConcurrent(
                        e -> e,
                        Collectors.counting()
                )
        );
        System.out.println(collect2);
    }

    @Test
    public void testGroupingAndReducing() {
        Map<Integer, Integer> collect = Stream.of(1, 2, 1, 2, 1, 2)
                .collect(Collectors.groupingBy(e -> Integer.valueOf(e) % 2, Collectors.reducing(0, (a, b) -> a + b)));
        System.out.println(collect);
    }

    // 对各个元素单独处理之后，再进行流式处理
    @Test
    public void testMapping () {
        // 返回：字符串转成数值后的平均值
        Double collect = Stream.of("1", "2", "3").collect(
                Collectors.mapping(
                        Double::valueOf,//先对各个元素进行处理，返回元素T做为downStream的入参元素
                        Collectors.averagingDouble(e -> (double) e)
                )
        );
        // 简写如下
//        Double collect = Stream.of("1", "2", "3").map(Double::valueOf).collect(Collectors.averagingDouble(e -> (double) e));
        System.out.println(collect);
    }

    // 最大值，minBy不再演示
    @Test
    public void testMaxBy () {
        Optional<Integer> collect = Stream.of(1, 2, 3).collect(
                Collectors.maxBy(Integer::compareTo)
        );
        // 简写如下
//        Optional<Integer> collect = Stream.of(1, 2, 3).max(Integer::compareTo);
        System.out.println(collect);
    }

    // 分区
    @Test
    public void testPartitioningBy () {
        // 按照奇偶数分区
        Map<Boolean, List<Integer>> collect = Stream.of(1, 2, 3, 4, 5, 6, 7, 8).collect(
                Collectors.partitioningBy(
                        (Integer e) -> e % 2 == 0,
                        Collectors.toList()
                )
        );
        System.out.println(collect);
    }

    // reducing按照自己想要的规则，累计流中各个元素
    @Test
    public void testReducing () {
        Optional<Integer> collect = Stream.of(1, 2, 3).collect(
                Collectors.reducing((Integer e1, Integer e2) -> e1 + e2)
        );
        // 简写如下
//        Optional<Integer> collect = Stream.of(1, 2, 3).reduce((Integer e1, Integer e2) -> e1 + e2);
        System.out.println(collect);
    }

    // 收集
    @Test
    public void testReducing2 () {
        Integer collect = Stream.of(1, 3, 4).collect(
                Collectors.reducing(
                        1000,// reducing方法的第三个参数BinaryOperator.apply(t,u)第一次执行时传入的初始化值
                        x -> x + 1,// 对流中的各个元素进行映射处理
                        (sum, b) -> {// 轮询归纳收集操作，sum即上次轮询返回结果，若第一次轮询，sum值即是第一个参数identity的值
                            int nextSum = sum + b;
                            System.out.printf("sum = %s, 当前轮询元素 = %s, nextSum = %s\n", sum, b, nextSum);
                            //返回值作为下一次循环的sum值
                            return nextSum;
                        })
        );

        // 优雅简化
//        Integer collect = Stream.of(1, 3, 4).map(x -> x + 1)
//                .reduce(1000, (sum, b) -> {// 轮询归纳收集操作，sum即上次轮询返回结果，若第一次轮询，sum值即是第一个参数identity的值
//                    int nextSum = sum + b;
//                    System.out.printf("sum = %s, 当前轮询元素 = %s, nextSum = %s\n", sum, b, nextSum);
//                    //返回值作为下一次循环的sum值
//                    return nextSum;
//                });
        System.out.println(collect);
    }

    // 收集成结合
    @Test
    public void testToCollection () {
        HashSet<Integer> collect = Stream.of(1, 3, 4, 1).collect(
                Collectors.toCollection(HashSet::new)
        );
        System.out.println(collect);


        Stream.of(1, 3, 4, 1).collect(
                Collectors.toCollection(ArrayList::new)
        );
    }


    @Test
    public void testToConcurrentMap() {
        ConcurrentMap<String, Integer> collect = Stream.of(1, 2, 3).collect(Collectors.toConcurrentMap(
                e -> "k" + e,   // 提取key；注意：若存在重复的key会抛异常
                e -> e * 100    // 提取value
        ));
        System.out.println("结果：" + collect);
    }

    @Test
    public void testToConcurrentMap2() {
        ConcurrentMap<String, Integer> collect = Stream.of(1, 2, 3, 10).collect(Collectors.toConcurrentMap(
                e -> String.valueOf(e).substring(0, 1),// 提取key
                e -> e,
                (oldValue, newValue) -> {   // 合并重复的key对应的值，我这采用的是覆盖旧的key值
                    System.out.printf("oldValue = %s, newValue = %s\n", oldValue, newValue);
                    return newValue;
                }
        ));
        System.out.println("结果：" + collect);
    }

    @Test
    public void testToConcurrentMap3() {
        ConcurrentHashMap<String, Integer> collect = Stream.of(1, 2, 3, 1).collect(Collectors.toConcurrentMap(
                e -> "k" + e,
                e -> e * 10,
                (e1, e2) -> {   // 与testToConcurrentMap相比，用于合并key相同的元素
                    System.out.printf("e1 = %s, e2 = %s\n", e1, e2);
                    return e1 + e2;
                },
                ConcurrentHashMap::new
        ));
        System.out.println("结果：" + collect);
    }

    // join连接字符串
    @Test
    public void testJoining() {
        String collect = Stream.of(1, 2, 3, 1).map(String::valueOf).collect(Collectors.joining());
        System.out.println("结果：" + collect);
    }

    /**
     * 本单元测试主要考察Function的特殊用法
     * @throws Exception
     */
    @Test
    public void testFunction() throws Exception {
        // 这是常用的写法，类型转换
        List<String> collect1 = Stream.of(1, 2, 3, 1)
                .map(integer -> {
                    System.out.println("map中的转换");
                    return changeIntToString(integer);
                })// 这个时候，changeIntToString()已经调用
                .collect(Collectors.toList());

        // 特殊的转换：复杂点写法
        List<Callable<String>> collect2 = Stream.of(1, 2, 3, 1)
                .map(integer -> {
                    System.out.println("map中的转换");
                    return new Callable<String>(){
                        @Override
                        public String call() throws Exception {
                            return changeIntToString(integer); // 这个时候，changeIntToString()方法中的逻辑尚未调用
                        }
                    };
                })
                .collect(Collectors.toList());

        // 特殊的转换：简化版1
        List<Callable<String>> collect2_1 = Stream.of(1, 2, 3, 1)
                .map(integer -> {
                    System.out.println("map中的转换");
                    return (Callable<String>) () -> changeIntToString(integer);// 这个时候，changeIntToString()方法中的逻辑尚未调用
                })
                .collect(Collectors.toList());

        // 特殊的转换：简化版2
        List<Callable<String>> collect2_2 = Stream.of(1, 2, 3, 1)
                .map(integer -> (Callable<String>) () -> changeIntToString(integer))// 这个时候，changeIntToString()方法中的逻辑尚未调用
                .collect(Collectors.toList());

        // 举一反三：简单写法
        List<A<String>> collect4 = Stream.of(1, 2, 3, 1)
                .map(integer -> (A<String>) (msg) -> msg + "-" + integer)// 这个时候，changeIntToString()方法中的逻辑尚未调用
                .collect(Collectors.toList());

        for (A<String> c : collect4) {
            c.say("");
        }

    }

    public String changeIntToString(int i) {
        System.out.println("执行转换：" + i);
        return i + "xxxx";
    }

    private interface A<T> {
        T say(String msg);
    }
}
