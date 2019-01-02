package DateFormat;

import static java.time.temporal.ChronoUnit.DAYS;

import DateFormat.DateUtil.DatePattern;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {

  @Test
  public void testStringToDate() {
    Date date = DateUtil.parse("2018-12-17", DatePattern.YYYY_MM_DD);
    System.out.println(date);
    System.out.println(DateUtil.format(date, DatePattern.YYYY_MM_DD_HH_MM_SS_E));

    System.out.println("\n========== 分割符 ==========");
    Date date2 = DateUtil.parse("20181217", DatePattern.YYYYMMDD);
    System.out.println(date2);
    System.out.println(DateUtil.format(date2, DatePattern.YYYY_MM_DD_HH_MM_SS_E));

    System.out.println("\n========== 分割符 ==========");
    Date date3 = DateUtil.parse("2018-12-17 12:01:10", DatePattern.YYYY_MM_DD_HH_MM_SS);
    System.out.println(date3);
    System.out.println(DateUtil.format(date3, DatePattern.YYYY_MM_DD_HH_MM_SS_E));
  }

  @Test
  public void testPlus() {
    Date date = new Date();
    date = DateUtil.plus(date, -18, DAYS);
    System.out.println(DateUtil.format(date, DatePattern.YYYY_MM_DD_HH_MM_SS_E));

  }

  @Test
  public void testCompare() {
    Date targetDay = new Date();

    // 同一天，但在后面1000秒
    Date sameDay = Date.from(targetDay.toInstant().plusSeconds(1000));
    Assert.assertFalse(DateUtil.compare(sameDay, targetDay));
    Assert.assertTrue(sameDay.after(targetDay));

    // 前一天
    Date beforeDay = Date.from(targetDay.toInstant().plus(-1, DAYS));
    Assert.assertTrue(DateUtil.compare(beforeDay, targetDay));

    // 后一天
    Date afterDay = Date.from(targetDay.toInstant().plus(1, DAYS));
    Assert.assertFalse(DateUtil.compare(afterDay, targetDay));
  }

  @Test
  public void testEqual() {
    Date d1 = new Date();
    Date d2 = Date.from(d1.toInstant().plusSeconds(1000));
    System.out.println(DateUtil.equal(d1, d2));
  }

}
