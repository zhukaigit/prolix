package com.zk.sort;

import java.util.Arrays;

/**
 * 希尔排序
 *
 * 基本思想：希尔排序是把记录按下标的一定增量分组，对每组使用直接插入排序算法排序。
 * 随着增量的递减，每组包含的数据将增加。当增量减至1时，整个文件恰恰被分为1组，
 * 因此最后一趟排序是一次普通的插入排序。
 *
 * 增量：一般情况下，初始增量 gap = arr.length / 2, 此后gap = gap ／ 2； 这一系列的增量称作为增量序列
 *
 *
 * 希尔排序为什么比直接插入排序快？
 * 希尔排序比插入排序快很多，它是基于什么原因呢？当h值大的时候，数据项每一趟排序需要移动元素的个数很少，
 * 但数据项移动的距离很长。这是非常有效率的。当h减小时，每一趟排序需要移动的元素的个数增多，但是此时数
 * 据项已经接近于它们排序后最终的位置，这对于插入排序可以更有效率。正是这两种情况的结合才使希尔排序效率那么高。
 *
 * 注意后期的排序过程不撤销前期排序所做的工作。例如，已经完成了以40-增量的排序的数组，在经过以13-增量
 * 的排序后仍然保持了以40-增量的排序的结果。如果不是这样的话，希尔排序就无法实现排序的目的。
 */
public class ShellSort {

    public static void main(String[] args) {
        Integer[] arr1 = {1, 4, 2, 7, 9, 8, 3, 6};
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(sort(arr1)));
    }

    public static Integer[] sort(Integer[] arr) {
        for (int gap = arr.length / 2; gap > 0; gap = gap / 2) {
            for (int i = gap; i < arr.length; i++) {
                /** 将arr[i]插入到对应组中合适的位置 - 开始 **/
                // 申明一个变量用于记录当前待插入对象的值
                int value = arr[i];
                // 待插入数据的数组下标
                int emptyIndex = i;
                while (emptyIndex - gap > 0 && value < arr[emptyIndex - gap]) {
                    // 移动
                    arr[emptyIndex] = arr[emptyIndex - gap];
                    emptyIndex = emptyIndex - gap;
                }
                arr[emptyIndex] = value;
                /** 将arr[i]插入到对应组中合适的位置 - 结束 **/
            }
        }
        return arr;
    }
}
