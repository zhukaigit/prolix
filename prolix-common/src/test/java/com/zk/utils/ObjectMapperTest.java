package com.zk.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * //序列化的时候序列对象的所有属性
 * objectMapper.setSerializationInclusion(Include.ALWAYS);
 * <p>
 * //反序列化的时候如果多了其他属性,不抛出异常
 * objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 * <p>
 * //如果是空对象的时候,不抛异常
 * objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 * <p>
 * //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
 * objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 * objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
 */
public class ObjectMapperTest {

//    @Test
//    public void test_WRITE_DATES_AS_TIMESTAMPS() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        //序列化的时候序列对象的所有属性
//        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
//        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//
//        Person person = new Person(1, "zxc", new Date());
//        //这是最简单的一个例子,把一个对象转换为json字符串
//        String personJson = objectMapper.writeValueAsString(person);
//        System.out.println(personJson);
//
//        //默认为true,会显示时间戳
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
//        personJson = objectMapper.writeValueAsString(person);
//        System.out.println(personJson);

//    }


    /**
     * JsonParser.Feature.QUOTE_FIELD_NAMES（默认值true）
     * 1）true：序列化后属性名称有双引号；
     * 2）false：序列化后属性名称没有有双引号
     */
    @Test
    public void test_QUOTE_FIELD_NAMES() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = Person.builder().age(10).name("zhukai").birthday(new Date()).build();

        // true
        // 输出结果：{"age":10,"name":"zhukai","birthday":1648109907685}
        objectMapper.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        System.out.println("QUOTE_FIELD_NAMES=true ====》 " + objectMapper.writeValueAsString(person));

        // false
        // 输出结果：{age:10,name:"zhukai",birthday:1648109907685}
        objectMapper.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        System.out.println("QUOTE_FIELD_NAMES=false ====》 " + objectMapper.writeValueAsString(person));
    }

    /**
     * JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES（默认值 false）
     * 1）true：反序列化时允许属性名称不带引号；
     * 2）false：反序列化时若属性名称不带引号，会报异常
     */
    @Test
    public void test_ALLOW_UNQUOTED_FIELD_NAMES() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//        String personJsonStr = "{\"age\":1,\"name\":\"zxc\",\"birthday\":1648108110737}";
        String personJsonStr = "{age:1, name:\"zxc\", birthday:1648108110737}";// 属性名称没有双引号

        //  1、ALLOW_UNQUOTED_FIELD_NAMES设置为true
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        Person dto = objectMapper.readValue(personJsonStr, Person.class);
        System.out.println("正常转换了： " + dto);

        //  2、ALLOW_UNQUOTED_FIELD_NAMES设置为false
        String errMsg = "";
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
//        objectMapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);// 效果同上
        try {
            objectMapper.readValue(personJsonStr, Person.class);
        } catch (IOException e) {
            errMsg = e.getMessage();
        }
        Assert.assertTrue(errMsg.contains("was expecting double-quote to start field name"));
    }

    /**
     * JsonParser.Feature.ALLOW_SINGLE_QUOTES（默认值 false）
     * 1）true：反序列化时允许单引号包住属性名称和字符串值；
     * 2）false：反序列化时若是单引号包住属性名称和字符串值，会报异常
     */
    @Test
    public void test_ALLOW_SINGLE_QUOTES() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String personJsonStr = "{'age':1,'name':'zxc','birthday':1648108110737}";

        // true
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        Person dto = objectMapper.readValue(personJsonStr, Person.class);
        System.out.println("正常转换了： " + dto);

        // false
        objectMapper.disable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        String errMsg = "";
        try {
            objectMapper.readValue(personJsonStr, Person.class);
        } catch (IOException e) {
            errMsg = e.getMessage();
        }
        Assert.assertTrue(errMsg.contains("was expecting double-quote to start field name"));
    }


    /**
     * JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS（默认值 false）
     * 1）true：反序列化时允许出现特殊字符和转义字符；
     * 2）false：反序列化时不允许出现特殊字符和转义字符，会报异常
     */
    @Test
    public void test_ALLOW_UNQUOTED_CONTROL_CHARS() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String val = "你\t好";
        String personJsonStr = "{\"age\":1,\"name\":\"" + val + "\",\"birthday\":1648108110737}";

        // true
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        Person dto = objectMapper.readValue(personJsonStr, Person.class);
        System.out.println("正常转换了： " + dto);

        // false
        objectMapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        String errMsg = "";
        try {
            objectMapper.readValue(personJsonStr, Person.class);
        } catch (IOException e) {
            System.err.println("错误信息：" + e.getMessage());
            errMsg = e.getMessage();
        }
        Assert.assertTrue(errMsg.contains("Illegal unquoted character"));
    }


    /**
     * JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS（默认值 false）
     * 1）true：允许JSON整数以多个0开始；
     * 2）false：不允许JSON整数以多个0开始，会报异常
     */
    @Test
    public void test_ALLOW_NUMERIC_LEADING_ZEROS() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String personJsonStr = "{\"age\":001,\"name\":\"zxc\",\"birthday\":1648108110737}";

        // true
        objectMapper.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);
        Person dto = objectMapper.readValue(personJsonStr, Person.class);
        System.out.println("正常转换了： " + dto);

        // false
        objectMapper.disable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);
        String errMsg = "";
        try {
            objectMapper.readValue(personJsonStr, Person.class);
        } catch (IOException e) {
            System.err.println("错误信息：" + e.getMessage());
            errMsg = e.getMessage();
        }
        Assert.assertTrue(errMsg.contains("Invalid numeric value: Leading zeroes not allowed"));
    }

    /**
     * JsonInclude.Include（默认值 ALWAYS）
     * [[[注意]]]：若objectMapper对象此前有调用过setSerializationInclusion方法，
     * 那么再次调用setSerializationInclusion方法设置成别的值是不起效果的，原因还不知，测试得出的结果
     */
    @Test
    public void test_JsonInclude() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // NON_NULL：只有不为null的属性才会被序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Person person = Person.builder().name("").birthday(new Date()).toys(new ArrayList<>(0)).build();
        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(StringUtils.center(" NON_NULL ", 50, "#"));
        System.out.println(person);
        System.out.println(jsonStr);

        // ALWAYS：所有属性才会被序列化
        System.out.println();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);// 默认策略
        person = Person.builder().name("").birthday(new Date()).build();
        jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(StringUtils.center(" ALWAYS ", 50, "#"));
        System.out.println(person);
        System.out.println(jsonStr);

        // ALWAYS：所有属性才会被序列化
        System.out.println();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        person = Person.builder().name("").birthday(new Date()).build();
        jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(StringUtils.center(" NON_ABSENT ", 50, "#"));
        System.out.println(person);
        System.out.println(jsonStr);

        // ALWAYS：所有属性才会被序列化
        System.out.println();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        person = Person.builder().name("").birthday(new Date()).toys(new ArrayList<>(0)).build();
        jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(StringUtils.center(" NON_EMPTY ", 50, "#"));
        System.out.println(person);
        System.out.println(jsonStr);

    }


    /**
     * SerializationFeature.WRAP_ROOT_VALUE（默认值 false）
     * 1）true：以类名作为根元素；
     * 2）false：不以类名作为根元素
     */
    @Test
    public void test_WRAP_ROOT_VALUE() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // true
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        Person person = Person.builder().name("zk").birthday(new Date()).toys(new ArrayList<>(0)).build();
        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(jsonStr);
    }

    /**
     * SerializationFeature.WRITE_DATES_AS_TIMESTAMPS（默认值 true）
     * 1）true：序列化时【对象的Date属性值】对应【序列化后的Long数值】
     * 2）false：则使用默认的转换器，默认转成格式如：2022-03-24T11:32:10.578+0000；
     * 或者可以添加转换器，
     */
    @Test
    public void test_WRITE_DATES_AS_TIMESTAMPS() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = Person.builder().name("zk")
                .birthday(new Date())
//                .localDateTime(LocalDateTime.now())
//                .localDate(LocalDate.now())
//                .localTime(LocalTime.now())
                .build();

        // true
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(jsonStr);

        // false
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 如果调用了setDateFormat，那么实际上不需要特别设置SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(jsonStr);
    }

    /**
     * java8中引入的LocalDateTime, LocalDate, LocalTime的格式化问题
     * 1）true：序列化时【对象的Date属性值】对应【序列化后的Long数值】
     * 2）false：则使用默认的转换器，默认转成格式如：2022-03-24T11:32:10.578+0000；
     * 或者可以添加转换器，
     */
    @Test
    public void test_registerModule_localDateTime() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = Person.builder().name("zk")
                .localDateTime(LocalDateTime.now())
                .localDate(LocalDate.now())
                .localTime(LocalTime.now())
                .build();

        //  true
//        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
//        System.out.println(jsonStr);

        // false
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println("===============> localDateTime格式化之后");
        System.out.println(jsonStr);


        // 反序列化
        String jsonStr2 = "{\"localDateTime\" : \"2022-03-24 19:54:01\"}";
        TypeReference<Person> typeReference = new TypeReference<Person>() {
        };
        Person dto = objectMapper.readValue(jsonStr2, typeReference);
        System.out.println(dto);

    }

    /**
     * SerializationFeature.WRITE_ENUMS_USING_TO_STRING（默认值 false）
     * 1）true：序列化时【对象的枚举类型的属性值】对应【该属性值对象的toString()方法返回值】
     * 2）false：序列化时【对象的枚举类型的属性值】对应【该属性值对象的name()方法返回值】
     */
    @Test
    public void test_WRITE_ENUMS_USING_TO_STRING() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        NationEnum china = NationEnum.CHINA;
        Person person = Person.builder().name("zk").nation(china).build();
        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(jsonStr);
        Assert.assertTrue(jsonStr.contains(china.getDesc()));

        // 反序列化
        /* 注意即使WRITE_ENUMS_USING_TO_STRING为true，在反序列化时如果使用toStirng的值还是会报异常，所有仅限于序列化 */
        String jsonStr2 = "{\"nation\":\"中国\"}";
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        Person p2 = objectMapper.readValue(jsonStr2, Person.class);
        ;
        System.out.println(p2);

        // 反序列化
        /* 注意即使WRITE_ENUMS_USING_TO_STRING为true，在反序列化时如果使用toStirng的值还是会报异常，所有仅限于序列化 */
        String jsonStr3 = "{\"nation\":0}";
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        Person p3 = objectMapper.readValue(jsonStr3, Person.class);
        ;
        System.out.println(p3);

    }

    @Test
    public void test_FAIL_ON_UNKNOWN_PROPERTIES() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonStr = "{\"nation\":\"CHINA\", \"unExistedField\":111}";
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Person person = objectMapper.readValue(jsonStr, Person.class);
        System.out.println(person);
    }

    @Test
    public void test_FAIL_ON_EMPTY_BEANS() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonStr = "{\"nation\":\"CHINA\", \"birthday\":\"111a\"}";
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Person person = objectMapper.readValue(jsonStr, Person.class);
        System.out.println(person);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class Person {
        private Integer age;
        private String name;
        private Date birthday;
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private LocalTime localTime;
        private List<String> toys;
        private NationEnum nation;
    }

    @Getter
    @AllArgsConstructor
    public enum NationEnum {
        CHINA("中国"),
        ;
        private String desc;

        @Override
        public String toString() {
            return this.getDesc();
        }
    }

}
