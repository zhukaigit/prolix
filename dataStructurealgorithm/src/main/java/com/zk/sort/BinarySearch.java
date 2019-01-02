package com.zk.sort;

public class BinarySearch {


  /**
   * 查找第一个值等于给定值的元素下标
   */
  public static int searchFirstEQ(int[] sortedArr, int target) {
    int start = 0;
    int end = sortedArr.length - 1;
    int middle;
    while (true) {
      if (start > end) {
        return -1;
      }
      middle = (start + end) / 2;
      //相等
      boolean b11 = sortedArr[middle] == target && middle == 0;
      //middle过大，end = middle - 1
      boolean b21 = sortedArr[middle] > target;
      if (b11 || sortedArr[middle] == target && sortedArr[middle - 1] < target) {
        return middle;
      } else if (b21 || sortedArr[middle] == target && sortedArr[middle - 1] == target) {
        end = middle - 1;
      } else {
        start = middle + 1;
      }
    }
  }

  /**
   * 查找最后一个值等于给定值的元素下标
   */
  public static int searchLastEQ(int[] sortedArr, int target) {
    int start = 0;
    int end = sortedArr.length - 1;
    int middle;
    while (true) {
      if (start > end) {
        return -1;
      }
      middle = (start + end) / 2;
      //相等
      boolean b11 = sortedArr[middle] == target && middle == sortedArr.length - 1;
      //middle过大，end = middle - 1
      if (b11 || sortedArr[middle] == target && sortedArr[middle + 1] > target) {
        return middle;
      } else if (sortedArr[middle] > target) {
        end = middle - 1;
      } else {
        start = middle + 1;
      }
    }
  }

  /**
   * 查找第一个大于等于给定值的元素下标
   */
  public static int searchFirstEqualOrGreater(int[] sortedArr, int target) {
    int start = 0;
    int end = sortedArr.length - 1;
    int middle;
    while (true) {
      if (start > end) {
        return -1;
      }
      middle = (start + end) / 2;
      //相等
      boolean b11 = sortedArr[middle] >= target && middle == 0;
      //middle过大，end = middle - 1
      if (b11 || sortedArr[middle] == target && sortedArr[middle - 1] < target) {
        return middle;
      } else if (sortedArr[middle] < target) {
        start = middle + 1;
      } else {
        end = middle - 1;
      }
    }
  }

  /**
   * 查找第一个大于等于给定值的元素下标
   */
  public static int searchLastEqualOrLess(int[] sortedArr, int target) {
    int start = 0;
    int end = sortedArr.length - 1;
    int middle;
    while (true) {
      if (start > end) {
        return -1;
      }
      middle = (start + end) / 2;
      //相等
      boolean b11 = sortedArr[middle] <= target && middle == sortedArr.length - 1;
      //middle过大，end = middle - 1
      if (b11 || sortedArr[middle] <= target && sortedArr[middle + 1] > target) {
        return middle;
      } else if (sortedArr[middle] > target) {
        end = middle - 1;
      } else {
        start = middle + 1;
      }
    }
  }

}
