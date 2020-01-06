import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsTest {


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

}
