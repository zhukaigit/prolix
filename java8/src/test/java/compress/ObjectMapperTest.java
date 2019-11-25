package compress;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ObjectMapperTest {

    private static com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();

    static {
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);//美化的json串
    }

    // ================ objectMapper.setSerializationInclusion(Include.ALWAYS) - start ================

    /**
     * 序列化，如：objectMapper.setSerializationInclusion(Include.ALWAYS)
     * Include.ALWAYS  是序列化对像所有属性
     * Include.NON_NULL 只有不为null的字段才被序列化
     * Include.NON_EMPTY 如果为null或者 空字符串和空集合都不会被序列化
     *
     * 注意：一旦mapper实例setSerializationInclusion后，该实例即使重新setSerializationInclusion，也不会改变之前的模式
     */
    @Test
    public void test1() throws JsonProcessingException {;
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);//美化的json串
        First a = new First();
        a.setA("aa");
        a.setB("");
        a.setList(Lists.newArrayList(1, 2, 3));
        a.setList2(Lists.newArrayList());
        a.setD(11);

        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        System.out.println(mapper.writeValueAsString(a));

    }

    /**
     * 序列化，如：objectMapper.setSerializationInclusion(Include.ALWAYS)
     * Include.ALWAYS  是序列化对像所有属性
     * Include.NON_NULL 只有不为null的字段才被序列化
     * Include.NON_EMPTY 如果为null或者 空字符串和空集合都不会被序列化
     */
    @Test
    public void test2() throws JsonProcessingException {;
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);//美化的json串
        First a = new First();
        a.setA("aa");
        a.setB("");
        a.setList(Lists.newArrayList(1, 2, 3));
        a.setList2(Lists.newArrayList());
        a.setD(11);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println(mapper.writeValueAsString(a));

    }

    /**
     * 序列化，如：objectMapper.setSerializationInclusion(Include.ALWAYS)
     * Include.ALWAYS  是序列化对像所有属性
     * Include.NON_NULL 只有不为null的字段才被序列化
     * Include.NON_EMPTY 如果为null或者 空字符串和空集合都不会被序列化
     */
    @Test
    public void test3() throws JsonProcessingException {;
        First a = new First();
        a.setA("aa");
        a.setB("");
        a.setList(Lists.newArrayList(1, 2, 3));
        a.setList2(Lists.newArrayList());
        a.setD(11);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);//美化的json串
        System.out.println(mapper.writeValueAsString(a));

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class First {
        private String a;
        private String b;
        private String c;
        private List list;
        private List list2;
        private List list3;
        private int d;
        private int d2;
        private Integer e;
    }
    // ================ objectMapper.setSerializationInclusion(Include.ALWAYS) - end ================



    // ================ 序列化是没有get方法是否抛异常测试 - start ================

    /**
     * 默认没有get方法会抛异常
     * com.fasterxml.jackson.databind.JsonMappingException: No serializer found for class compress.ObjectMapperTest$Two2 and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) )
     */
    @Test
    public void test2_1() throws JsonProcessingException {
        Two1 t1 = new Two1(11);
        System.out.println(mapper.writeValueAsString(t1));
        Two2 t2 = new Two2();
        boolean hasError = false;
        try {
            System.out.println(mapper.writeValueAsString(t2));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            hasError = true;
        }
        Assert.assertTrue(hasError);
    }
    /**
     * mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)，设置之后不抛出异常
     */
    @Test
    public void test2_2() throws JsonProcessingException {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Two1 t1 = new Two1(11);
        System.out.println(mapper.writeValueAsString(t1));
        Two2 t2 = new Two2();
        System.out.println(mapper.writeValueAsString(t2));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Two1 {
        private int sex;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Two2 {
        private int sex;
    }

    // ================ 序列化是没有get方法是否抛异常测试 - end ================



    // ================ 反序列化的时候如果多了其他属性,不抛出异常 - start ================

    /**
     * 默认情况下，反序列化json字符串中出现其他属性会报异常
     * 如：com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "age" (class compress.ObjectMapperTest$Three), not marked as ignorable (2 known properties: "sex", "name"])
     */
    @Test
    public void test3_1() {
        String json = "{\"sex\":1, \"age\":10}";// age为多余的字段
        try {
            mapper.readValue(json, Three.class);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof UnrecognizedPropertyException);
        }
    }

    @Test
    public void test3_2() throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = "{\"sex\":1, \"age\":10}";
        Three obj = mapper.readValue(json, Three.class);
        Assert.assertEquals(1, obj.getSex());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Three {
        private int sex;
        private int name;
    }
    // ================ 反序列化的时候如果多了其他属性,不抛出异常 - end ================


    // ================ 取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式 - start ================

    /**
     * Date属性默认在序列化时转换成时间戳
     */
    @Test
    public void test4_1() throws JsonProcessingException {
        Date date = new Date();
        Four obj = new Four(date);
        String json = mapper.writeValueAsString(obj);
        System.out.println(json);
        System.out.println("date.getTime = " + date.getTime());
        Assert.assertTrue(json.contains(date.getTime() + ""));
    }
    @Test
    public void test4_2() throws JsonProcessingException {
        Four obj = new Four(new Date());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        System.out.println(mapper.writeValueAsString(obj));
    }
    @Test
    public void test4_3() throws IOException {
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String json = "{\"birthday\" : \"2019-11-19 01:11:31\"}";
        Four obj2 = mapper.readValue(json, Four.class);
        Assert.assertEquals("2019-11-19 1:11:31", obj2.getBirthday().toLocaleString());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Four {
        private Date birthday;
    }
    // ================ 取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式 - end ================


    // ================ 复杂对象的反序列化 - start ================

    /**
     * 在使用ObjectMapper时，最好设置：
     * mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // json字符串中多余属性不报异常
     */
    @Test
    public void test5_1() throws IOException {
        String jsonStr = "{\"sex\":1,\"b\":{\"d\":{\"age\":11},\"blist\":[1,2,3,4]},\"cs\":[{\"name\":\"zk\"}]}";
        A<B<D>, C> obj = toObject(jsonStr, new TypeReference<A<B<D>, C>>() {
        });
        Assert.assertEquals(11, obj.getB().getD().getAge());
        Assert.assertEquals("zk", obj.getCs().get(0).getName());
        Assert.assertEquals(4, obj.getB().getBList().length);
        Assert.assertEquals(1, obj.getSex());

        // 使用fastjson
        A<B<D>, C> obj2 = JSONObject.parseObject(jsonStr, new com.alibaba.fastjson.TypeReference<A<B<D>, C>>() {
        });
        System.out.println(obj2);
    }

    @Test
    public void test5_2() throws IOException {
        String jsonStr = "[1,2,3,4]";
        List<Integer> list = toObject(jsonStr, new TypeReference<List<Integer>>() {
        });
        System.out.println(list);
        Assert.assertEquals(4, list.size());
    }

    @Test
    public void test5_3() throws IOException {
        String jsonStr = "{\"name\":[1,2,3,4]}";

        Map<String, List<Integer>> map = toObject(jsonStr, new TypeReference<Map<String, List<Integer>>>() {
        });

        System.out.println(map);
    }

    private static <T> T toObject(String jsonStr, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(jsonStr, typeReference);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class A<B,C> extends E {
        private B b;
        private List<C> cs;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class B<D> {
        private D d;
        private int[] bList;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class C {
        private String name;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class D {
        private int age;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class E {
        private int sex;
    }
    // ================ 复杂对象的反序列化 - end ================


    // ================ 方法泛型返回 - start ================

    @Test
    public void test6() throws Exception {
        String jsonStr = "{\"sex\":1,\"b\":{\"d\":{\"age\":11},\"blist\":[1,2,3,4]},\"cs\":[{\"name\":\"zk\"}]}";
        A<B, C> a = t1(jsonStr);
        System.out.println(a);
    }

    public A<B, C>  t1(String json) throws Exception {
        Type type = ObjectMapperTest.class.getMethod("t1", String.class).getGenericReturnType();
        return mapper.readValue(json, mapper.constructType(type));
    }

    // ================ 方法泛型返回 - end ================

    @Test
    public void test7_1() throws IOException {
        String jsonStr = "[1,2,3]";
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Integer.class);
        List<Integer> list = mapper.readValue(jsonStr, javaType);
        System.out.println(list);
    }
    @Test
    public void test7_2() throws IOException {
        String jsonStr = "{\"d\":[1,2,3]}";

        // 方式一
        JavaType javaType1 = mapper.getTypeFactory().constructParametricType(List.class, Integer.class);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(B.class, javaType1);
        B b = mapper.readValue(jsonStr, javaType);
        System.out.println(b);

        // 方式二
        B b2 = mapper.readValue(jsonStr, new TypeReference<B<List<Integer>>>(){});
        System.out.println(b2);
    }
    @Test
    public void test7_3() throws IOException {
        String jsonStr = "{\"d\":[1,2,3]}";

        // 方式一
        JavaType javaType1 = mapper.getTypeFactory().constructParametricType(List.class, Integer.class);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(
                Map.class, SimpleType.construct(String.class), javaType1);
        Map<String, List<Integer>> map = mapper.readValue(jsonStr, javaType);
        System.out.println(map);

        // 方式二
        Map<String, List<Integer>> map2 = mapper.readValue(jsonStr, new TypeReference<Map<String, List<Integer>>>(){});
        System.out.println(map2);

        // 方式三
        Map<String, List<Integer>> map3 = JSONObject.parseObject(jsonStr, new com.alibaba.fastjson.TypeReference<Map<String, List<Integer>>>() {
        });
        System.out.println(map3);
    }

    @Test
    public void test8() {
        A a = new A();
        System.out.println(a.getClass());

        TypeReference<A> typeReference = new TypeReference<A>() {
        };
        System.out.println(typeReference.getClass());
    }
}
