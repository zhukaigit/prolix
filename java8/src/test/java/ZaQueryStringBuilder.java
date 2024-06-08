import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author zhukai001
 * @create 2023/11/23 10:13
 * @description
 */
public class ZaQueryStringBuilder {
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String BLANK_SPACE = " ";
    private StringBuilder result = new StringBuilder();

    public static void main(String[] args) {
        ZaQueryStringBuilder builder = ZaQueryStringBuilder.builder();
        builder.and(QueryCondition.or(QueryCondition.eq("a1", "1"), QueryCondition.notExisted("a1")));
        builder.and(QueryCondition.or(QueryCondition.eq("a2", "1"), QueryCondition.notExisted("a2")));
        builder.and(QueryCondition.or(QueryCondition.eq("a3", "1"), QueryCondition.notExisted("a3")));
        builder.and(QueryCondition.startWith("a7", "hello"));
        builder.or(QueryCondition.and(QueryCondition.eq("a4", "4")));
        builder.and(QueryCondition.or(QueryCondition.eq("a5", "5")));
        builder.and(QueryCondition.eltNow("effectiveTime"));
        builder.and(QueryCondition.egtNow("expireTime"));
        builder.and(QueryCondition.gt("expireTime2", LocalDateTime.now()));
        String query = builder.build();

        System.out.println(query);
    }

    public static ZaQueryStringBuilder builder() {
        return new ZaQueryStringBuilder();
    }

    public String build() {
        return result.toString();
    }

    public ZaQueryStringBuilder and(String queryCondition) {
        return concatCondition(queryCondition, AND);
    }

    public ZaQueryStringBuilder or(String queryCondition) {
        return concatCondition(queryCondition, OR);
    }

    private ZaQueryStringBuilder concatCondition(String queryCondition, String opr) {
        if (StringUtils.isNotEmpty(queryCondition)) {
            if (StringUtils.isEmpty(result)) {
                result.append(queryCondition);
            } else {
                result.append(getOpr(opr)).append(queryCondition);
            }
        }
        return this;
    }

    private static String getOpr(String opr) {
        return BLANK_SPACE + opr + BLANK_SPACE;
    }

    public static class QueryCondition {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private static final String GT_OPR = "["; // 区间操作符：大于
        private static final String EGT_OPR = "{"; // 区间操作符：大于等于
        private static final String LT_OPR = "]"; // 区间操作符：小于
        private static final String ELT_OPR = "}"; // 区间操作符：小于等于
        public static String neq(String key, String value) {
            return "NOT " + key + ":" + value;
        }

        // 等值查询
        public static String eq(String key, String value) {
            return key + ":" + value;
        }

        // 前缀查询
        public static String startWith(String key, String value) {
            return key + ":" + value + "*";
        }

        // 大于
        public static String gt(String key, Object value) {
            return range(key, GT_OPR, value, null, null);
        }

        // 大于等于
        public static String egt(String key, Object value) {
            return range(key, EGT_OPR, value, null, null);
        }

        // 小于
        public static String lt(String key, Object value) {
            return range(key, null, null, LT_OPR, value);
        }

        // 小于等于
        public static String elt(String key, Object value) {
            return range(key, null, null, ELT_OPR, value);
        }

        // 大于当前时间
        public static String gtNow(String key) {
            return gt(key, "now");
        }

        // 大于等于当前时间
        public static String egtNow(String key) {
            return egt(key, "now");
        }

        // 小于当前时间
        public static String ltNow(String key) {
            return lt(key, "now");
        }

        // 小于等于当前时间
        public static String eltNow(String key) {
            return elt(key, "now");
        }

        // 范围查询
        public static String range(String key, String leftOpr, Object start, String rightOpr, Object end) {
            if (start == null && end == null) {
                return "";
            }
            start = wrapparam(start);
            end = wrapparam(end);
            leftOpr = leftOpr == null ? GT_OPR : leftOpr;
            rightOpr = rightOpr == null ? LT_OPR : rightOpr;
            return String.format("%s:%s%s TO %s%s]", key, leftOpr, start, end, rightOpr);
        }

        // 值处理
        private static Object wrapparam(Object value) {
            if (value == null) {
                return "*";
            }

            if (value instanceof LocalDateTime) {
                return "\"" + ((LocalDateTime) value).format(FORMATTER) + "\"";
            }else {
                return String.valueOf(value);
            }
        }

        // key存在
        public static String existed(String key) {
            return "_exists_:" + key;
        }

        // key不存在
        public static String notExisted(String key) {
            return "NOT _exists_:" + key;
        }

        // 多个条件用and连接
        public static String and(String... conditions) {
            return concatCondition(AND, conditions);
        }

        // 多个条件用or连接
        public static String or(String... conditions) {
            return concatCondition(OR, conditions);
        }

        // 多个条件用指定的连接符连接
        private static String concatCondition(String opr, String... conditions) {
            if (conditions == null || conditions.length == 0) {
                return "";
            } else if (conditions.length == 1) {
                return conditions[0];
            } else {
                StringBuilder result = new StringBuilder();
                result.append("(").append(conditions[0]);
                for (int i = 1; i < conditions.length; i++) {
                    result.append(getOpr(opr)).append(conditions[i]);
                }
                result.append(")");
                return result.toString();
            }
        }
    }
}