import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class OtherTest {

  @Test
  public void test1() {
    String str = "";
    Optional.ofNullable(str).ifPresent(e -> System.out.println("jie guo = " + e));
  }

  @Test
  public void testSupplier() {
    HashSet<Object> set = new HashSet<>();
    set.add(1);
    set.add(2);
    HashMap<Object, Object> map = getObj(Maps::newHashMap);
    System.out.println(map);

  }

  public <T> T getObj(Supplier<T> supplier) {
    System.out.println("do some thing");
    return supplier.get();
  }

  // ======== 测试方法引用 ==========
  @Test
  public void testMethodInvoke() {
    Integer result = getInt("10", 2, OtherTest::change);
    System.out.println(result);
  }

  // 被引用的方法
  private static Integer change(String target) {
    return Integer.valueOf(target) + 1;
  }

  private Integer getInt(String target, Integer multi, Function<String, Integer> function) {
    Integer integer = function.apply(target);
    return integer * multi;
  }

  @Test
  public void test() {
    HashMap<Object, Object> map = Maps.newHashMap();
    map.computeIfAbsent("name", (key) -> key + "_v");
    System.out.println(map);
  }

  @Test
  public void testRegex() {
    String src = "http://www.baidu.com/download?bucket=b_value&key2=value2&key3=value3";
    System.out.println(getValue("bucket", src));
    System.out.println(getValue("key2", src));
    System.out.println(getValue("key3", src));

  }

  public String getValue(String key, String src) {
    String regex1 = String.format("%s=(.*?)&", key);
    String regex2 = String.format("%s=(.*)", key);
    String regexValue = getRegexValue(src, regex1);
    return regexValue == null ? getRegexValue(src, regex2) : regexValue;
  }

  public String getRegexValue(String src, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(src);
    boolean existed = matcher.find();
    return existed ? matcher.group(1) : null;
  }

}
