package com.zk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zk.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonUtilsTest {

    @Test
    public void test() {
        HashMap<String, Object> m1 = new HashMap<String, Object>();
        m1.put("man", new Man("zk"));
        m1.put("woman", new Woman("小花"));
        String jsonStr = JsonUtils.toJsonHasNullKey(m1);

        Map m2 = JsonUtils.toMap(jsonStr, String.class, Object.class);
        Object man = m1.get("man");
        Object woman = m1.get("woman");
        System.out.println();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Man implements Serializable {
        private static final long serialVersionUID = 2435880687056316497L;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Woman implements Serializable {
        private static final long serialVersionUID = -766884333390938368L;
        private String name;
    }


    @Test
    public void test_parse() {
        HashMap<String, ModelOne> map = new HashMap<String, ModelOne>();
        map.put("key", new ModelOne(new Man("zk")));
        String jsonStr = JsonUtils.toJsonHasNullKey(map);

        Map<String, ModelOne> resultMap = JsonUtils.parse(jsonStr, new TypeReference<Map<String, ModelOne>>() {
        });

        Assert.assertEquals(map.get("key").getMan().getName(), resultMap.get("key").getMan().getName());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModelOne{
        private Man man;
    }

}
