package com.zk.feign;

import com.zk.feign.protogenesis.FeignBuilder;
import com.zk.feign.protogenesis.LocalApi;
import com.zk.feign.protogenesis.TestApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/7/14.
 */
@Slf4j
public class ProtogenesisFeignTest {

    @Test
    public void test() {
        TestApi api = new FeignBuilder<TestApi>()
                .getApi(TestApi.class, TestApi.BASE_URL, false);
        byte[] index = api.index();
//        System.out.println("结果 :" + new String(index));
    }

    @Test
    public void testSleep() {
        LocalApi localApi = new FeignBuilder<LocalApi>()
                .getApi(LocalApi.class, LocalApi.BASE_URL, true, 1);
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("name", "zhukai002");
        localApi.testSleep10s(queryMap);
    }

    @Test
    public void testSuccess() {
        LocalApi localApi = new FeignBuilder<LocalApi>()
                .getApi(LocalApi.class, LocalApi.BASE_URL, true, 1);
        Object result = null;
        try {
            result = localApi.success();
        } catch (Exception e) {
            log.error("调用失败", e);
        } finally {
            System.out.println("结果：" + result);
        }
    }

    @Test
    public void testError() {
        LocalApi localApi = new FeignBuilder<LocalApi>()
                .getApi(LocalApi.class, LocalApi.BASE_URL, true, 1);
        Object result = null;
        try {
            result = localApi.error();
        } catch (Exception e) {
            log.error("调用失败, 错误信息：", e);
        } finally {
            System.out.println("结果：" + result);
        }
    }


}
