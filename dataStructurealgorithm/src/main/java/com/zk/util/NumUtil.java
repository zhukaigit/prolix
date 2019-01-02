package com.zk.util;

import java.util.Random;

public class NumUtil {

  /**
   * 生成指定范围随机数
   */
  public static Long generateScopeRandom(long min, long max) {
    long num = min + (long) (Math.random() * (max - min + 1));
    return num;
  }

  /**
   * 创建随机数组
   */
  public static Integer[] createRandomArr(int length) {
    Integer[] arr = new Integer[length];
    for (int i = 0; i < length; i++) {
      arr[i] = new Random().nextInt(100);
    }
    return arr;
  }

  /**
   * 创建随机数组
   */
  public static Integer[] createRandomArr(int arrLength, int min, int max) {
    Integer[] arr = new Integer[arrLength];
    for (int i = 0; i < arrLength; i++) {
      arr[i] = generateScopeRandom(min, max).intValue();
    }
    return arr;
  }

  /**
   * 创建递增数组
   */
  public static Integer[] createSortedArr(int length) {
    Integer[] arr = new Integer[length];
    arr[0] = new Random().nextInt(100);
    for (int i = 1; i < length; i++) {
      arr[i] = arr[i - 1] + new Random().nextInt(100);
    }
    return arr;
  }

  /**
   * 创建递减数组
   */
  public static Integer[] createDecreasedArr(int length) {
    Integer[] arr = new Integer[length];
    arr[length - 1] = new Random().nextInt(100);
    for (int i = length - 2; i >= 0; i--) {
      arr[i] = arr[i + 1] + new Random().nextInt(100);
    }
    return arr;
  }
}
