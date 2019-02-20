package com.zk.sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 堆排序
 */
public class HeapSort {

  public static void main(String[] args) {
    Integer[] arr = {7, 5, 20, 16, 4, 1, 19, 13, 8, 100};
    headSort(arr);
    System.out.println(Arrays.asList(arr));
  }

  /**
   * 堆排序
   */
  public static void headSort(Integer[] arr) {
    buildHeap(arr, arr.length - 1);
    for (int endIndex = arr.length - 1; endIndex > 0; endIndex--) {
      swap(arr, 0, endIndex);
      if (endIndex - 1 > 0) {
        heapify(arr, 0, endIndex - 1);
      }
    }
  }

  /**
   * 对数组中指定的下标范围内的元素进行"建堆"
   *
   * 思路：数据从后往前，堆化从上往下
   *
   * 下标范围：0 ～ endIndex
   */
  private static void buildHeap(Integer[] arr, int endIndex) {
    for (int index = getFatherIndex(endIndex); index >= 0; index--) {
      heapify(arr, index, endIndex);
    }
  }

  /**
   * 对数组中指定下标的元素进行"堆化"
   * @param targetIndex 对该下标元素进行堆化
   * @param endIndex 数组边界下标，即endIndex之后的元素不参与堆化
   */
  private static void heapify(Integer[] arr, int targetIndex, int endIndex) {
    while (true) {
      // 在【本节点、左节点、右节点】中，申明最大元素的下标，初始时为你index
      int maxIndex = targetIndex;
      maxIndex = getLeftSubIndex(targetIndex) <= endIndex && arr[getLeftSubIndex(targetIndex)] > arr[maxIndex] ?
              getLeftSubIndex(targetIndex) : maxIndex;
      maxIndex = getRightSubIndex(targetIndex) <= endIndex && arr[getRightSubIndex(targetIndex)] > arr[maxIndex] ?
              getRightSubIndex(targetIndex) : maxIndex;
      if (targetIndex == maxIndex) break;
      swap(arr, targetIndex, maxIndex);
      targetIndex = maxIndex;
    }
  }

  /**
   * 交换指定下标的元素
   */
  private static void swap(Integer[] arr, int index1, int index2) {
    int temp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = temp;
  }

  /**
   * 获取左节点下标
   */
  private static int getLeftSubIndex(int fatherIndex) {
    return (fatherIndex + 1) * 2 - 1;
  }

  /**
   * 获取右节点下标
   */
  private static int getRightSubIndex(int fatherIndex) {
    return (fatherIndex + 1) * 2;
  }

  /**
   * 通过子节点下标获取父节点下标
   */
  private static int getFatherIndex(int subIndex) {
    return subIndex % 2 == 0 ? subIndex / 2 - 1 : (subIndex + 1) / 2 - 1;
  }

}
