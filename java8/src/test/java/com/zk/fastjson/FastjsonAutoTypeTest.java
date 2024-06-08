package com.zk.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.test.classloader.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public class FastjsonAutoTypeTest {

    @BeforeClass
    public static void before() {
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
        ParserConfig.getGlobalInstance().setSafeMode(true);
//        ParserConfig.getGlobalInstance().addAccept("com.");
//        ParserConfig.getGlobalInstance().addAccept("com.test.classloader.User");
//        ParserConfig.getG
//        lobalInstance().addDeny("com.test.classloader.");
//        boolean safeMode = ParserConfig.getGlobalInstance().isSafeMode();
//        System.out.println("ParserConfig.getGlobalInstance().isSafeMode() = " + safeMode);
    }

    @Test
    public void test() {
        Store store = new Store();
        store.setName("Hollis");
        Apple apple = new Apple();
        apple.setPrice(new BigDecimal(0.5));
        store.setFruit(apple);
//        String jsonString = JSON.toJSONString(store);
        String jsonString = JSON.toJSONString(store, SerializerFeature.WriteClassName);

        System.out.println("toJSONString : " + jsonString);

        Store newStore = JSON.parseObject(jsonString, Store.class);
        System.out.println("parseObject : " + newStore);
        Apple newApple = (Apple) newStore.getFruit();
        System.out.println("getFruit : " + newApple);

    }

    public interface Fruit {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Apple implements Fruit {
        private BigDecimal price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Store {
        private String name;
        private Fruit fruit;
    }

    @Test
    public void test2() {
        Store store = new Store();
        store.setName("Hollis");
        Apple apple = new Apple();
        apple.setPrice(new BigDecimal(0.5));
        store.setFruit(apple);

        Result<Store> t = new Result<>(store);
        String jsonString = JSON.toJSONString(t, SerializerFeature.WriteClassName);
        System.out.println("toJSONString : " + jsonString);
        Result result = JSON.parseObject(jsonString, Result.class);
        System.out.println("parseObject : " + result);

    }

    @Test
    public void test3() {
        Result<User> t = new Result<>(new User("zk"));
        String jsonString = JSON.toJSONString(t, SerializerFeature.WriteClassName);
        System.out.println("toJSONString : " + jsonString);
        Result result = JSON.parseObject(jsonString, Result.class);
        System.out.println("parseObject : " + result);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Result<T> {
        private T data;
    }

}

