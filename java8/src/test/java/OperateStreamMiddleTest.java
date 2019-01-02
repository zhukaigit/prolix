import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 操作Stream（中间方法）
 * Created by zhuk on 2018/3/28.
 */
public class OperateStreamMiddleTest {

    //filter - 过滤
    @Test
    public void test1() {
        LongStream.range(1, 10).filter(e -> e % 2 == 0).forEach(System.out::println);
    }

    //Map - 转换
    @Test
    public void test2() {
        LongStream.range(1, 10).mapToObj(e -> "aa" + e).forEach(System.out::println);
    }

    @Test
    public void test3() {
        Arrays.asList(1, 2, 3).stream().map(e -> "aa" + e).forEach(System.out::println);
    }

    //Sort - 排序
    @Test
    public void test4() {
        Stream.of(3, 4, 6, 1, 2).sorted().forEach(System.out::println);
    }

    //SkipLimit - 分页
    //skip相当于start， limit相当于size
    @Test
    public void test5() {
        Stream.of(3, 4, 6, 1, 2).skip(2).limit(2).forEach(System.out::println);
    }

    //FlatMap - 扁平化
    @Test
    public void test6() {
        Stream.of(1, 2, 3, 4, 5, 6).flatMap(e -> {
            List<String> list = new ArrayList<String>();
            list.add("aa" + e);
            return list.stream();
        }).forEach(System.out::println);
    }

    //FlatMap与map的区别【flatMap的lambda返回Stream。 而map的lambda返回单个元素】
    @Test
    public void test7() {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        List<Integer> b = new ArrayList<>();
        b.add(3);
        b.add(4);
        List<Integer> figures = Stream.of(a, b).flatMap(u -> u.stream()).collect(Collectors.toList());
        figures.forEach(f -> System.out.println(f));

        //第二个示例
        List<String> words = new ArrayList<String>();
        words.add("your");
        words.add("name");
        words.stream().flatMap(w -> characterStream(w)).forEach(System.out::println);
    }
    public static Stream<Character> characterStream(String s){
        System.out.println("输入的元素：" + s);
        List<Character> result = new ArrayList<>();
        for (char c : s.toCharArray())
            result.add(c);
        return result.stream();
    }


}
