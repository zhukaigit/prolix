package com.zk.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 线性排序
 */
public class CountingSort {

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            System.out.println("\n====================== 第"+i+"轮 =======================");
            Integer[] arr = createRandomArr(100, 15);
            System.out.println("原始数组："+Arrays.toString(arr));
            arr = executeSort(arr);
            System.out.println("排序数组："+Arrays.toString(arr));
        }

    }

    /**
     * 计数排序
     * <p>
     * 思路：
     */
    public static Integer[] executeSort(Integer[] arr) {
        /** 寻找数组中最大值 **/
        int maxData = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxData) {
                maxData = arr[i];
            }
        }

        /** 桶的个数为【maxData + 1】个，计算桶中元素的个数，每个桶放的元素相同，元素的值与桶的下标相同 **/
        int[] bucketArr = new int[maxData + 1];
        for (int i = 0; i < arr.length; i++) {
            bucketArr[arr[i]]++;
        }

        /** 累加桶中的个数 **/
        for (int i = 0; i < bucketArr.length - 1; i++) {
            bucketArr[i + 1] = bucketArr[i + 1] + bucketArr[i];
        }

        /** 遍历原始数组中元素，将结果放入resultArr中 **/
        Integer[] resultArr = new Integer[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            //bucketArr[arr[i]]-1 的值就表示在结果数组的下标
            int resultArrSubscript = bucketArr[arr[i]] - 1;
            resultArr[resultArrSubscript] = arr[i];
            bucketArr[arr[i]]--;
        }

        return resultArr;
    }

    /**
     * 创建随机数组
     */
    public static Integer[] createRandomArr(int length, int scope) {
        Integer[] arr = new Integer[length];
        for (int i = 0; i < length; i++) {
            arr[i] = new Random().nextInt(scope);
        }
        return arr;
    }
}
