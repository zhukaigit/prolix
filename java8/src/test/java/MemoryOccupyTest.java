import lombok.Getter;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;

import java.util.Date;
import java.util.PriorityQueue;

/**
 * https://www.cnblogs.com/magialmoon/p/3757767.html
 */
public class MemoryOccupyTest {

  @Test
  public void test() {
    int size = 100;
    Asset[] list = new Asset[size];
    for (int i = 0; i < size; i++) {
      list[i] = new Asset();
    }
    //计算指定对象及其引用树上的所有对象的综合大小，单位字节
    System.out.println("sizeOf = "+RamUsageEstimator.sizeOf(list));
    //计算指定对象本身在堆空间的大小，单位字节
    System.out.println("shallowSizeOf = "+RamUsageEstimator.shallowSizeOf(list));
    //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
    System.out.println("humanSizeOf = "+RamUsageEstimator.humanSizeOf(list));

    System.out.println(RamUsageEstimator.sizeOf(new Asset()));
    System.out.println(RamUsageEstimator.shallowSizeOf(new Asset()));

  }

  private static class Asset {
    private String s1 = new String("1你好呀你好呀你好呀");
    private String s2 = new String("2你好呀你好呀你好呀");
    private String s3 = new String("3你好呀你好呀你好呀");
    private String s4 = new String("4你好呀你好呀你好呀");
    private String s5 = new String("5你好呀你好呀你好呀");
    private String s6 = new String("6你好呀你好呀你好呀");
    private String s7 = new String("7你好呀你好呀你好呀");
    private String s8 = new String("8你好呀你好呀你好呀");
    private String s9 = new String("9你好呀你好呀你好呀");
    private String s10 = new String("0你好呀你好呀你好呀");
  }

  @Test
  public void test2() {
    // A1: (12+8+4)=24
    // s: 12+4+4=24, value: 16+16=32, 共56
    // s2: 12+4+4=24, value: 16+16=32, 共56
    // A1累计：24+56+56=136
    System.out.println("----------------- new A1() -----------------");
    printSize(new A1());
    System.out.println("----------------- new A2() -----------------");
    // A2: (12+8+4)=24
    // s: 12+4+4=24, value: 16+16=32, 共56
    // s2: 12+4+4=24, value: 因为与s的value是同一个，所以不再计算，共24
    // A2累计：24+56+24=104
    printSize(new A2());
  }

  private static class A1 {
    String s = new String("12345678");
    String s2 = new String("87654321");
    // 以下注释的两个字段，占用内存与上面的s,s2相同
//    String s = new String("啊啊啊啊啊啊啊啊");
//    String s2 = new String("噢噢噢噢噢噢噢噢");
  }

  private static class A2 {
    String s = new String("01234567");
    String s2 = new String("01234567");
  }

  /**
   * 测试static的成员属性不占用实例对象的内存
   */
  @Test
  public void test3() {
    System.out.println("----------------- new B1() -----------------");
    printSize(new B1());
    System.out.println("----------------- new B2() -----------------");
    printSize(new B2());
  }

  private static class B1 {
    private int i;
  }

  private static class B2 {
    private int i;
    private static long hash;
  }
  /**
   * 对象头：12
   * 数组对象头：16
   */
  private void printSize(Object target) {
    //计算指定对象及其引用树上的所有对象的综合大小，单位字节
    System.out.println("综合大小 = " + RamUsageEstimator.sizeOf(target) + " byte");
    //计算指定对象本身在堆空间的大小，单位字节
    System.out.println("对象本身 = " + RamUsageEstimator.shallowSizeOf(target) + " byte");
    //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
    System.out.println("综合大小 = " + RamUsageEstimator.humanSizeOf(target));
  }
  
  @Test
  public void test_RuleCacheKeyInfo() {

    // 任务队列：存放待执行的任务，底层是一个小顶堆
    PriorityQueue<RuleCacheKeyInfo> taskQueue       = new PriorityQueue<>();

    for (int i = 0; i < 10000; i++) {
      taskQueue.add(RuleCacheKeyInfo.of("compel_toc_rule_fee_current_231_" + i, "compel_toc_rule_fee_current", new Date()));
    }

    printSize(taskQueue);

  }

  @Getter
  public static class RuleCacheKeyInfo implements Comparable<RuleCacheKeyInfo> {
    private String spaceCode;
    private String groupCode;
    private Date expireTime;

    public static RuleCacheKeyInfo of(String spaceCode, Date expireTime) {
      return of(spaceCode, null, expireTime);
    }

    public static RuleCacheKeyInfo of(String spaceCode, String groupCode, Date expireTime) {
      RuleCacheKeyInfo ruleCacheKeyInfo = new RuleCacheKeyInfo();
      ruleCacheKeyInfo.spaceCode = spaceCode;
      ruleCacheKeyInfo.groupCode = groupCode;
      ruleCacheKeyInfo.expireTime = expireTime;
      return ruleCacheKeyInfo;
    }

    @Override
    public int compareTo(RuleCacheKeyInfo o) {
      return this.expireTime.compareTo(o.expireTime);
    }
  }

}
