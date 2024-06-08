//import org.apache.commons.lang3.StringUtils;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//
///**
// * @author zhukai001
// * @create 2023/11/23 10:13
// * @description
// */
//public class QueryStringBuilder {
//    public static final String AND = "AND";
//    public static final String OR = "OR";
//    public static final String BLANK_SPACE = " ";
//    private StringBuilder result = new StringBuilder();
//
//    public static void main(String[] args) {
//        String query = QueryStringBuilder.builder()
//                .and(QueryConditionBuilder.builder().or(QueryConditionBuilder.eq("a1", "1"), QueryConditionBuilder.notExisted("a1")))
//                .and(QueryConditionBuilder.or(QueryConditionBuilder.eq("a2", "1"), QueryConditionBuilder.notExisted("a2")))
//                .and(QueryConditionBuilder.or(QueryConditionBuilder.eq("a3", "1"), QueryConditionBuilder.notExisted("a3")))
//                .or(QueryConditionBuilder.and(QueryConditionBuilder.eq("a4", "4")))
//                .and(QueryConditionBuilder.or(QueryConditionBuilder.eq("a5", "5")))
//                .and(QueryConditionBuilder.eltNow("effectiveTime"))
//                .and(QueryConditionBuilder.egtNow("expireTime"))
//                .and(QueryConditionBuilder.gt("expireTime2", LocalDateTime.now()))
//                .build();
//
//        System.out.println(query);
//    }
//
//    public static QueryStringBuilder builder() {
//        return new QueryStringBuilder();
//    }
//
//    public String build() {
//        return result.toString();
//    }
//
//    public QueryStringBuilder and(QueryConditionBuilder condition) {
//        return concatCondition(condition, AND);
//    }
//
//    public QueryStringBuilder or(QueryConditionBuilder condition) {
//        return concatCondition(condition, OR);
//    }
//
//    private QueryStringBuilder concatCondition(QueryConditionBuilder condition, String opr) {
//        if (!condition.isEmpty()) {
//            if (StringUtils.isEmpty(result)) {
//                result.append(condition.getResult());
//            } else {
//                result.append(getOpr(opr)).append(condition);
//            }
//        }
//        return this;
//    }
//
//    private static String getOpr(String opr) {
//        return BLANK_SPACE + opr + BLANK_SPACE;
//    }
//
//    public static class QueryConditionBuilder {
//        private StringBuilder result = new StringBuilder();
//        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        private static final String GT_OPR = "["; // 区间操作符：大于
//        private static final String EGT_OPR = "{"; // 区间操作符：大于等于
//        private static final String LT_OPR = "]"; // 区间操作符：小于
//        private static final String ELT_OPR = "}"; // 区间操作符：小于等于
//
//        public static QueryConditionBuilder builder() {
//            return new QueryConditionBuilder();
//        }
//
//        public StringBuilder getResult() {
//            return result;
//        }
//
//        public boolean isEmpty() {
//            return StringUtils.isEmpty(result);
//        }
//
//        public QueryConditionBuilder neq(String key, String value) {
//            result.append("NOT").append(BLANK_SPACE).append(key).append(":").append(value);
//            return this;
//        }
//
//        // 等值查询
//        public QueryConditionBuilder eq(String key, String value) {
//            result.append(key).append(":").append(value);
//            return this;
//        }
//
//        // 前缀查询
//        public QueryConditionBuilder startWith(String key, String value) {
//            result.append(key).append(":").append(value).append("*");
//            return this;
//        }
//
//        // 大于
//        public QueryConditionBuilder gt(String key, Object value) {
////            return range(key, GT_OPR, value, null, null);
//            return range(key, GT_OPR, value, null, null);
//        }
//
//        // 大于等于
//        public QueryConditionBuilder egt(String key, Object value) {
////            return range(key, EGT_OPR, value, null, null);
//            return range(key, EGT_OPR, value, null, null);
//        }
//
//        // 小于
//        public QueryConditionBuilder lt(String key, Object value) {
////            return range(key, null, null, LT_OPR, value);
//            return range(key, null, null, LT_OPR, value);
//        }
//
//        // 小于等于
//        public QueryConditionBuilder elt(String key, Object value) {
////            return range(key, null, null, ELT_OPR, value);
//            return range(key, null, null, ELT_OPR, value);
//        }
//
//        // 大于当前时间
//        public QueryConditionBuilder gtNow(String key) {
////            return gt(key, "now");
//            return gt(key, "now");
//        }
//
//        // 大于等于当前时间
//        public QueryConditionBuilder egtNow(String key) {
////            return egt(key, "now");
//            return egt(key, "now");
//        }
//
//        // 小于当前时间
//        public QueryConditionBuilder ltNow(String key) {
////            return lt(key, "now");
//            return lt(key, "now");
//        }
//
//        // 小于等于当前时间
//        public QueryConditionBuilder eltNow(String key) {
////            return elt(key, "now");
//            return elt(key, "now");
//        }
//
//        // 范围查询
//        public QueryConditionBuilder range(String key, String leftOpr, Object start, String rightOpr, Object end) {
//            if (start == null && end == null) {
//                return this;
//            }
//            start = wrapparam(start);
//            end = wrapparam(end);
//            leftOpr = leftOpr == null ? GT_OPR : leftOpr;
//            rightOpr = rightOpr == null ? LT_OPR : rightOpr;
////            return String.format("%s:%s%s TO %s%s]", key, leftOpr, start, end, rightOpr);
//            result.append(key).append(":").append(leftOpr).append(start).append(" TO ").append(end).append(rightOpr);
//            return this;
//        }
//
//        // 值处理
//        private static Object wrapparam(Object value) {
//            if (value == null) {
//                return "*";
//            }
//
//            if (value instanceof LocalDateTime) {
//                return "\"" + ((LocalDateTime) value).format(FORMATTER) + "\"";
//            }else {
//                return String.valueOf(value);
//            }
//        }
//
//        // key存在
//        public QueryConditionBuilder existed(String key) {
////            return "_exists_:" + key;
//            result.append("_exists_").append(":").append(key);
//            return this;
//        }
//
//        // key不存在
//        public QueryConditionBuilder notExisted(String key) {
////            return "NOT _exists_:" + key;
//            result.append("NOT _exists_").append(":").append(key);
//            return this;
//        }
//
//        // 多个条件用and连接
//        public QueryConditionBuilder and(QueryConditionBuilder... conditions) {
////            return concatCondition(AND, conditions);
//            return concatCondition(AND, conditions);
//        }
//
//        // 多个条件用or连接
//        public QueryConditionBuilder or(QueryConditionBuilder... conditions) {
////            return concatCondition(OR, conditions);
//            return concatCondition(OR, conditions);
//        }
//
//        // 多个条件用指定的连接符连接
//        private QueryConditionBuilder concatCondition(String opr, QueryConditionBuilder... conditions) {
//            if (conditions == null || conditions.length == 0) {
//                return this;
//            } else if (conditions.length == 1) {
//                return conditions[0];
//            } else {
//                StringBuilder sb = new StringBuilder();
//                sb.append("(").append(conditions[0]);
//                for (int i = 1; i < conditions.length; i++) {
//                    sb.append(getOpr(opr)).append(conditions[i]);
//                }
//                sb.append(")");
//                result.append(sb);
//            }
//            return this;
//        }
//    }
//}