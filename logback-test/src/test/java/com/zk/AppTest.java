package com.zk;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest {

  /**
   * 测试：为每一个请求添加唯一标示
   *
   * 背景：想要完整地追踪某个请求，如果通过线程名称会搜索到很多的记录，原因是服务器响应请求的线程池中，
   * 线程个数是有限的被循环使用。那么通过MDC在日志可以添加一个唯一标示，用来追踪请求
   */
  @Test
  public void test() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 10; i++) {
      executorService.execute(() -> {
        MDC.put("traceId", UUID.randomUUID().toString());
        service1();
        try {
          Thread.sleep((long) (new Random().nextInt(5) * 1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        service1();
      });
    }

    executorService.shutdown();
    boolean terminated = executorService.isTerminated();
    while (!terminated) {
      terminated = executorService.awaitTermination(500, TimeUnit.MICROSECONDS);
    }
    System.out.println("============ finish ============");
  }

  private static void service1() {
    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info("执行方法为：" + methodName);
    service2();
  }

  private static void service2() {
    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info("执行方法为：" + methodName);
  }
}
