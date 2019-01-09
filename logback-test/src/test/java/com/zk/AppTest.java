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
   */
  @Test
  public void test() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 10; i++) {
      executorService.execute(() -> {
        MDC.put("uniqueKey", UUID.randomUUID().toString());
        service1();
        try {
          Thread.sleep(Long.valueOf(new Random().nextInt(5)*1000));
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
