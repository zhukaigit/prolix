package DateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DateUtil {

  /**
   * 若新增常量，在方法{@link DateUtil#parse(java.lang.String, DateFormat.DateUtil.DatePattern)}中，需要增加对应的处理方式
   */
  public enum DatePattern {
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYYMMDD("yyyyMMdd"),
    HH_MM_SS("HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_E("yyyy-MM-dd HH:mm:ss E"),;

    private String pattern;

    DatePattern(String pattern) {
      this.pattern = pattern;
    }

    public String getPattern() {
      return pattern;
    }
  }

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");//本地默认时区

  private static final ConcurrentHashMap<String, DateTimeFormatter> CACHED_FORMATTER = new ConcurrentHashMap<>();

  public static Date parse(String strDate, DatePattern pattern) {
    return parse(strDate, pattern, DEFAULT_ZONE_ID);
  }

  /**
   * 解析字符串
   */
  public static Date parse(String strDate, DatePattern pattern, ZoneId zoneId) {
    //LocalDateTime是不包含时区概念的
    LocalDateTime localDateTime = null;
    DateTimeFormatter dateTimeFormatter = getFormatter(pattern.getPattern());
    switch (pattern) {
      case YYYY_MM_DD:
      case YYYYMMDD:
        LocalDate localDate = LocalDate.parse(strDate, dateTimeFormatter);
        localDateTime = localDate.atTime(0, 0, 0);//默认设置了00:00:00
        break;
      case HH_MM_SS:
        LocalTime localTime = LocalTime.parse(strDate, dateTimeFormatter);
        localDateTime = localTime.atDate(LocalDate.of(1970, 1, 1));//默认设置了1970-01-01
        break;
      case YYYY_MM_DD_HH_MM_SS:
        localDateTime = LocalDateTime.parse(strDate, dateTimeFormatter);
        break;
      default:
        throw new RuntimeException("没有设置对应的解析");
    }
    //设定时区
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = Instant.from(zonedDateTime);
    return Date.from(instant);
  }

  public static String format(Date date, DatePattern pattern) {
    return format(date, pattern, DEFAULT_ZONE_ID);
  }

  /**
   * 日期格式化输出
   */
  public static String format(Date date, DatePattern pattern, ZoneId zoneId) {
    Instant instant = date.toInstant();
    ZonedDateTime zonedDateTime = instant.atZone(zoneId);
    String format = zonedDateTime.format(getFormatter(pattern.getPattern()));
    return format;
  }

  /**
   * 日期操作
   */
  public static Date plus(Date date, long amountToAdd, ChronoUnit unit) {
    Instant instant = date.toInstant().plus(amountToAdd, unit);
    return Date.from(instant);
  }

  /**
   * 日期对比
   *
   * @return 如果before是after之前的日期，则返回true；否则返回false。
   *
   * 注意：只是日期维度的比较，不包含时间点
   */
  public static boolean compare(Date before, Date after) {
    ZonedDateTime z1 = before.toInstant().atZone(ZoneId.systemDefault());
    ZonedDateTime z2 = after.toInstant().atZone(ZoneId.systemDefault());
    return z1.toLocalDate().isBefore(z2.toLocalDate());
  }

  /**
   * 日期对比
   *
   * @return 如果before是after之前的日期，则返回true。
   *
   * 注意：只是日期维度的比较，不包含时间点
   */
  public static boolean equal(Date date1, Date date2) {
    ZonedDateTime z1 = date1.toInstant().atZone(ZoneId.systemDefault());
    ZonedDateTime z2 = date2.toInstant().atZone(ZoneId.systemDefault());
    LocalDate localDate1 = LocalDate.of(z1.getYear(), z1.getMonth(), z1.getDayOfMonth());
    LocalDate localDate2 = LocalDate.of(z2.getYear(), z2.getMonth(), z2.getDayOfMonth());
    return localDate1.equals(localDate2);
  }


  private static DateTimeFormatter getFormatter(String pattern) {
    DateTimeFormatter dateTimeFormatter = CACHED_FORMATTER.get(pattern);
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
      CACHED_FORMATTER.put(pattern, dateTimeFormatter);
    }
    return dateTimeFormatter;
  }

}
