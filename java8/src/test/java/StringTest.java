import org.junit.Test;

import java.lang.reflect.Field;

/**
 * 1、String s1 = new String("1a1")，检查常量池查看是否存在"1a1"，如果不存在，则创建string对象。
 *  然后再在堆中创建string对象，返回堆中的引用。
 *  值得注意的是：这两个string对象的value属性指向的是同一个char[]对象，目的是节约内存。
 *
 * 2、String s2 = new String("1a1")，此时，因为常量池已存在"1a1"，则不再在常量池中创建string对象。
 *  在堆中创建一个string对象，因为已经堆中已经存在一个与s2相等的（即equals）s1对象，因此s2.value == s1.value
 */
public class StringTest {

    /**
     * 测试string对象的地址
     */
    @Test
    public void test1() {
        String s1 = "112";
        String s2 = "11" + "2"; // 编译器编译是优化为"112"
        System.out.println(s1 == s2);// true

        String temp = "2";
        String s3 = "11" + temp; // 因为temp为变量，编译时无法优化，底层实现详解test2
        System.out.println(s1 == s3);// false

        final String temp2 = "2";
        String s4 = "11" + temp; // 因为temp为常量，编译时优化为"112"，故返回的是常量池引用
        System.out.println(s1 == s4);// false

        String s5 = new String("112");
        s5 = s5.intern();// 是的s5的指针指向常量池中的对象
        System.out.println(s1 == s5);// true

    }

    /**
     * 1、String相加底层原理
     * 2、String效率底的原因及其示例
     */
    @Test
    public void test2() {
        String s1 = "a";
        String s2 = "b";
        
        // 底层：String result = new StringBuilder("12").append(s1).append("3").append("4").append(s2).toString()
        // 由此可见，编译器会优化前面字符串常量，而中间的则不会优化
        String result = "1" + "2" + s1 + "3" + "4" + s2;

        // 为什么说String效率要比StringBuilder，如下示例，总是在不断的创建和销毁StringBuilder对象
        String s = "";
        for (int i = 0; i < 100; i++) {
            s = s + i;
        }
    }


    /**
     * 多数equals的string对象，其value属性是==的
     */
    @Test
    public void test3() {
        String s1 = "112"; // 返回常量池的引用
        String s2 = new String("112"); // 返回堆中的引用
        String s3 = new String("112"); // 返回堆中的引用
        String s4 = "11" + "2"; // 编译器自动优化为"12"

        // 验证s1,s2,s3的value指向的是同一个char[]数组对象
        System.out.println(getValue(s1)==getValue(s2));// true
        System.out.println(getValue(s1)==getValue(s3));// true
        System.out.println(getValue(s1)==getValue(s4));// true

        String temp1 = "2";
        String s5 = "11" + temp1;
//        String s5 = new StringBuilder("11").append(temp1).toString(); // String s5 = "11" + temp1的底层实现
        // 通过源代码可知，s5的value指向的char[]是新new出来的，而且s5也是new新new出来的
        System.out.println(getValue(s1)==getValue(s5));// false

        final String temp2 = "2";
        String s6 = "11" + temp2; // 底层：因为temp2是常量，故编译时优化成s6 = "112"。故s6.value=="112".value
        System.out.println(getValue(s1)==getValue(s6));// true

    }

    /**
     * 在JDK1.6，intern()方法会把首次遇到的字符串实例复制到永久代，返回的也是永久代中这个字符串实例的引用。
     * 而JDK1.7的intern()实现不会再复制实例，只是在常量池中记录首次出现的实例引用。
     */
    @Test
    public void test() {
        // "计算机软件"是首次出现
        // 在JDK1.6测试时，返回false
        // 在JDK1.7测试时，返回true
        String s1 = new String(new char[]{'计','算','机','软', '件'});
        System.out.println(s1.intern() == s1);

        // "java"之前出现过
        // 在JDK1.6测试时，返回false
        // 在JDK1.7测试时，返回false
        String s2 = new String(new char[]{'j','a','v','a'});
        System.out.println(s2.intern() == s2);

        // JDK1.7，因为出现过了，返回false
        String s3 = new String(new char[]{'计','算','机','软', '件'});
        System.out.println(s3.intern() == s3);
    }

    private Object getValue(String s) {
        try {
            Field f = s.getClass().getDeclaredField("value");
            f.setAccessible(true);
            return f.get(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
