import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;

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

  /**
   * 假如一个类有两个String类型s,s2字段，会因为s.equal(s2)的结果得到不同的sizeOf
   * 还没弄明白为什么？
   */
  @Test
  public void test2() {
    print(new A1());
    System.out.println("-----------------");
    print(new A2());
  }

  private static class A1 {
    String s = new String("12345678");
    String s2 = new String("87654321");
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
    print(new B1());
    System.out.println("--------------------");
    print(new B2());
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
   * 引用：4
   */
  private void print(Object target) {
    //计算指定对象及其引用树上的所有对象的综合大小，单位字节
    System.out.println("sizeOf = " + RamUsageEstimator.sizeOf(target));
    //计算指定对象本身在堆空间的大小，单位字节
    System.out.println("shallowSizeOf = " + RamUsageEstimator.shallowSizeOf(target));
    //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
    System.out.println("humanSizeOf = " + RamUsageEstimator.humanSizeOf(target));

  }

}
