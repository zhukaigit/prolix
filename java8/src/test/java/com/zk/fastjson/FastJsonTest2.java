package com.zk.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.junit.Test;

import java.util.Date;

/**
 * 测试 FastJson
 */
public class FastJsonTest2 {

    @Test
    public void test1() {
        Policy policy = new Policy();
        long mills = 1640966400000L;
        //2022-01-01 00:00:00
        policy.setTime(new Date(mills));
        System.out.println("原始：" + policy.getTime().toLocaleString());

        String string = JSONObject.toJSONString(policy);
        System.out.println("1、JSONObject.toJSONString(policy):  " + string);

        policy = JSONObject.parseObject(string).toJavaObject(Policy.class);
        System.out.println("2: " + (policy.getTime().getTime() == mills));
        System.out.println("2、JSONObject.parseObject(string).toJavaObject(Policy.class): " + JSONObject.toJSONString(policy));

        policy = JSONObject.toJavaObject(JSONObject.parseObject(string), Policy.class);
        System.out.println("3: " + (policy.getTime().getTime() == mills));
        System.out.println("3、JSONObject.toJavaObject(JSONObject.parseObject(string), Policy.class): " + JSONObject.toJSONString(policy));

        policy = JSONObject.parseObject(string, Policy.class);
        System.out.println("4: " + (policy.getTime().getTime() == mills));
        System.out.println("4、JSONObject.parseObject(string, Policy.class): " + JSONObject.toJSONString(policy));


    }

    @Data
    static class Policy {
                @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//        @JSONField(format = "yyyyMMddHHmmss")
        private Date time;
    }
}
