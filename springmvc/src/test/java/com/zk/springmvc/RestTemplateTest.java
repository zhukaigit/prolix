package com.zk.springmvc;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * restTemplate相关文档：
 *  https://www.jb51.net/article/145618.htm
 *  https://www.open-open.com/lib/view/open1436018677419.html
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
@Slf4j
public class RestTemplateTest {



    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplateTimeout() {
        String url = "http://localhost:8081/api/feign/sleep?sleepMills={sleepMills}";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sleepMills", 500);
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class, paramMap);
        System.out.println("返回结果：" + resp.getBody());

    }

    /**
     * 测试时，maxTotal与defaultMaxPerRoute设置为2
     * connectionRequestTimeout设置大一点
     */
    @Test
    public void testMaxTotal() throws InterruptedException {
        System.out.println("====== start test =====");
        asyncExe(5);
        asyncExe(5);
        asyncExe(5);
        asyncExe(5);
        Thread.sleep(15000);
    }

    // 异步调用
    private void asyncExe(int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://localhost:8081/api/feign/sleep?sleepMills={sleepMills}";
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("sleepMills", i*1000);
                    ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class, paramMap);
                    System.out.println(Thread.currentThread() + " - 返回结果：" + resp.getBody());
                } catch (Exception e) {
                    System.err.println(Thread.currentThread() + " -  错误信息：" + e.getMessage());
                }
            }
        }).start();
    }

}
