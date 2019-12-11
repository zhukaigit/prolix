package com.zk;

import com.zk.logback.CheckMdcKeyIsExistFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * 有时我们希望job中的日志单独放在一个日志文件中
 *
 * 实现方式：
 * 1、单独配置job日志数据源，对该日志输出源添加“自定义job滤器”，如{@link CheckMdcKeyIsExistFilter}，如果满足条件，则ACCEPT，否则DENY
 * 2、其他日志输出源如果不想输出job线程打印出来的日志，则也许添加“自定义job滤器”,如果满足条件，则DENY，否则NEUTRAL
 *
 * 参考配置文件：logback.xml
 */
@Slf4j
public class JobLogTest {

    @Test
    public void testJobLog() throws InterruptedException {
        mockJobMethod();
        Thread.sleep(300L);
    }

    @Test
    public void testControllerLog() throws InterruptedException {
        mockController();
        Thread.sleep(300L);
    }

    /**
     * 这里模拟一个job方法的入口，希望日记记录在job.log日志文件中
     */
    public void mockJobMethod () {
        MDC.put("isJob", "");
        log.info("这里模拟一个job方法的入口");
        mockServiceMethod();
    }

    /**
     * 这里模拟一个controller方法的入口,希望在日志记录在其他日志文件中
     */
    public void mockController () {
        log.info("这里模拟一个controller方法的入口");
        mockServiceMethod();
    }

    /**
     * 模拟其他的service方法
     */
    public void mockServiceMethod () {
        log.info("普通方法被调用了");

        new Thread(new Runnable() {
            @Override
            public void run () {
                // 注意：此时的子线程没法区分
                log.info("子线程在做事情");
            }
        }).start();
    }

}
