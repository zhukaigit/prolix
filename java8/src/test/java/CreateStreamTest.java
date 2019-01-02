import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 创建Stream
 * Created by zhuk on 2018/3/28.
 */
public class CreateStreamTest {

    //Of-直接构造
    @Test
    public void test1() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        stream.forEach(System.out::println);//等同于：stream.forEach(e -> System.out.println(e));
    }

    //Iterator迭代
    @Test
    public void test2() {
        Stream.iterate(1, n -> n * 2).limit(3).forEach(e -> System.out.println(e));
    }

    //Generate-函数构造
    @Test
    public void test3() {
        Stream.generate(() -> new Random().nextInt()).limit(8).forEach(e -> System.out.println(e));
    }

    //CollectionStreams-集合构造
    @Test
    public void test4() {
        Arrays.asList(1, 2, 3).stream().forEach(System.out::println);
    }

    //Primitive-基本数据类型
    @Test
    public void test5() {
        IntStream.range(1, 10).forEach(System.out::println);
    }
}
