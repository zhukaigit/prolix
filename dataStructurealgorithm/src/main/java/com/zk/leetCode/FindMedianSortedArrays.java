package com.zk.leetCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class FindMedianSortedArrays {

    public static void main(String[] args) {
        int[] arr1 = createSortedArr(3);
        int[] arr2 = createSortedArr(3);
        System.out.println("arr1 = " + Arrays.toString(arr1));
        System.out.println("arr2 = " + Arrays.toString(arr2));
        System.out.println(new FindMedianSortedArrays().findMedianSortedArrays(arr1, arr2));

    }

    public Double findMedianSortedArrays(int[] sortedArr1, int[] sortedArr2) {
        int sumSize = sortedArr1.length + sortedArr2.length;
        double result = 0;
        if (sumSize % 2 == 0) {
            //这种情况，只有二个中间元素。元素下标为sumSize/2-1, sumSize/2
            int midIndex1 = sumSize / 2;
            Double inFirst = findInFirst(sortedArr1, sortedArr2, midIndex1);
            Double result1 = inFirst != null ? inFirst : findInFirst(sortedArr2, sortedArr1, midIndex1);

            int midIndex2 = midIndex1 - 1;
            Double inFirst2 = findInFirst(sortedArr1, sortedArr2, midIndex2);
            Double result2 = inFirst2 != null ? inFirst2 : findInFirst(sortedArr2, sortedArr1, midIndex2);
            System.out.println("中间值分别为：" + result1 + ", " + result2);
            result = (result1 + result2) / 2;
        } else {
            //这种情况，只有一个中间元素。元素下标为(sumSize-1)/2
            int midIndex = (sumSize - 1) / 2;
            Double inFirst = findInFirst(sortedArr1, sortedArr2, midIndex);
            result = inFirst != null ? inFirst : findInFirst(sortedArr2, sortedArr1, midIndex);
        }
        return result;
    }


    /**
     * 两个有序数组，返回合并后的有序数组中，指定下标元素。且该元素在sortedArr1中
     * 返回：存在返回该元素的Double值。不存在返回null
     */
    private static Double findInFirst(int[] sortedArr1, int[] sortedArr2, int index) {
        int sumSize = sortedArr1.length + sortedArr2.length;
        if (index < 0 || index >= sumSize) throw new RuntimeException("smallCount is out size");
        int arr1_start = 0;
        int arr1_end = sortedArr1.length - 1;
        while (true) {
            if (arr1_start > arr1_end) return null;
            int middle = (arr1_start + arr1_end) / 2;
            HashMap<String, Integer> partition = searchEqualAndLess(sortedArr2, sortedArr1[middle]);
            int curIndexMin = middle + partition.get("LS");
            int curIndexMax = curIndexMin + partition.get("EQ");
            //满足条件：index 在 [LsCounts + middle, LsCounts + EqCounts +middle] 范围中
            if (index >= curIndexMin && index <= curIndexMax) {
                return Double.valueOf(sortedArr1[middle]);
            } else if (index > curIndexMax) {
                arr1_start = middle + 1;
            } else {
                arr1_end = middle - 1;
            }
        }
    }


    /**
     * 一个有序数组中有多少个元素小于、等于target
     *
     * @param sortedArr
     * @param target
     * @return
     */
    private static HashMap<String, Integer> searchEqualAndLess(int[] sortedArr, int target) {
        HashMap<String, Integer> result = new HashMap();
        int start = 0;
        int end = sortedArr.length - 1;
        int middle;
        while (true) {
            if (start > end) {
                result.put("EQ", 0);//等于target的个数
                result.put("LS", 0);//小于target的个数
                return result;
            }
            middle = (start + end) / 2;
            //相等
            boolean b11 = sortedArr[middle] <= target && middle == sortedArr.length - 1;
            //middle过大，end = middle - 1
            if (b11 || sortedArr[middle] <= target && sortedArr[middle + 1] > target) {
                int countEQ = 0;
                while (middle >= 0 && sortedArr[middle] == target) {
                    countEQ++;
                    middle--;
                }
                result.put("EQ", countEQ);//等于target的个数
                result.put("LS", middle + 1);//小于target的个数
                return result;
            } else if (sortedArr[middle] > target) {
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }
    }


    /**
     * 创建递增数组
     */
    public static int[] createSortedArr(int length) {
        int[] arr = new int[length];
        arr[0] = new Random().nextInt(10);
        for (int i = 1; i < length; i++) {
            arr[i] = arr[i - 1] + new Random().nextInt(10);
        }
        return arr;
    }

}
