package linkedlist;

import org.junit.Test;

import java.util.HashMap;

/**
 * 递归测试
 */
public class RecursionTest {

    //f1避免了重复计算，节约了大量的时间
    @Test
    public void testCostTime1() {
        int n = 40;

        long l1 = System.currentTimeMillis();

        // f
        int f = f(n);
        long l2 = System.currentTimeMillis();
        System.out.println("f()用时：" + (l2 - l1) + "，结果：" + f);

        // f1
        int f1 = f1(n);
        long l3 = System.currentTimeMillis();
        System.out.println("f1()用时：" + (l3 - l2) + ", 结果：" + f1);

    }


    //这两种方式耗时相差并不是特别大，主要在于f1比f2使用了更多的栈内存。
    @Test
    public void testCostTime2() {
        int n = 1000;

        long l2 = System.currentTimeMillis();

        // f1
        int f1 = f1(n);
        long l3 = System.currentTimeMillis();
        System.out.println("f1()用时：" + (l3 - l2) + ", 结果：" + f1);

        // f2
        int f2 = f2(n);
        long l4 = System.currentTimeMillis();
        System.out.println("f2()用时：" + (l4 - l3) + ", 结果：" + f2);

    }

    @Test
    public void test() {
        int n = 100000;
        System.out.println("f2执行结果：" + f2(n));
        System.out.println("f1执行结果：" + f1(n));
    }

    //普通的递归方式
    int f(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        return f(n-1) + f(n-2);
    }

    //使用hashMap避免重复运算
    private HashMap<Integer, Integer> hashMap = new HashMap();
    public int f1(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;

        // 先从hashMap从获取，只有不存在的时候再去计算，这样可避免重复计算问题
        if (hashMap.containsKey(n)) {
            return hashMap.get(n);
        }

        int ret = f1(n-1) + f1(n-2);
        hashMap.put(n, ret);
        return ret;
    }

    //使用迭代方式替换递归写法
    int f2(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;

        int ret = 0;
        int pre = 2;
        int prepre = 1;
        for (int i = 3; i <= n; ++i) {
            ret = pre + prepre;
            prepre = pre;
            pre = ret;
        }
        return ret;
    }
}
