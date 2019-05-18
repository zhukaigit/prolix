package com.zk;

import com.zk.logback.IsJobLogFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * 有时我们希望job中的日志单独放在一个日志文件中
 *
 * 方式一，如下：
 * 步骤：1、在job的最开始，使用MDC存入一个变量，如：MDC.put("IS_JOB", "THIS_IS_JOB");
 * 2、在日志配置文件中，添加EvaluatorFilter，参考logback.xml文件中的JOB_LOG输出源配置，
 *  注意filter下的expression配置，使用EvaluatorFilter过滤器，需要额外的两个JAR包，commons-compiler.jar和janino.jar
 * 3、对于其他的输出源，排除该过滤条件即可
 *
 * 方式二：
 * 1、实现自定义过滤器，如{@link IsJobLogFilter}
 * 2、在对应的appender中添加该过滤器
 *
 */
@Slf4j
public class JobLogTest {

    @Test
    public void testJobLog() {
        mockJobMethod();
    }

    @Test
    public void testControllerLog() {
        mockController();
    }

    /**
     * 这里模拟一个job方法的入口，希望日记记录在job.log日志文件中
     */
    public void mockJobMethod () {
        MDC.put("IS_JOB", "THIS_IS_JOB_LOG");
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
    }
}
