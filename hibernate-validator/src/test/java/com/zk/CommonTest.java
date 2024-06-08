package com.zk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonTest {

  public static void main(String[] args) throws InterruptedException {
    ExecutorService pool = Executors.newCachedThreadPool();

    for (int i = 0; i < 10000; i++) {
      pool.execute(() -> {
        System.out.println(Thread.currentThread().getName());
        try {
          Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      Thread.sleep(3);
    }

    Thread.sleep(30 * 1000);
    pool.shutdown();
  }

  @Test
  public void test() {
    int i = 0;
    int j = 0;
    if (i == 0) {
      System.out.println("i = 0");

    } else if (j == 0) {
      System.out.println("j = 0");
    }

  }

  @AllArgsConstructor
  @Getter
  public enum BorrowerType {
    COMPANY("企业"),
    PERSONAL("个人"),
    ;
    private String desc;
  }

}
