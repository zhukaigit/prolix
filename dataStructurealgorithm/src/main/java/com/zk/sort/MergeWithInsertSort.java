package com.zk.sort;

/**
 * 利用归并排序结合插入排序，省去排序空间的耗时
 */
public class MergeWithInsertSort {

    public static void main(String[] args) {
        System.out.println(11/2);
    }

    public static void sort(Integer[] arr) {
        sort(arr, 0, arr.length - 1);
    }
    private static void sort(Integer[] arr, int startIndex, int endIndex) {
        // 终止条件
        if (startIndex >= endIndex) {
            return;
        }
        if (startIndex + 1 == endIndex) {
            if (arr[endIndex] < arr[startIndex]) {
                swap(arr, startIndex, endIndex);
            }
            return;
        }

        // 递归
        int p = (startIndex + endIndex) / 2;

        sort(arr, startIndex, p);
        sort(arr, p + 1, endIndex);

        // 合并
        merge(arr, startIndex, p, endIndex);
    }

    private static void merge(Integer[] arr, int startIndex, int p, int endIndex) {
        int boundIndex = startIndex;
        for (int i = p + 1; i <= endIndex; i++) {
            int temp = arr[i];
            for (int j = i - 1; j >= boundIndex; j--) {
                if (temp < arr[j]) {
                    arr[j + 1] = arr[j];
                    if (j == startIndex) {
                        arr[startIndex] = temp;
                    }
                } else {
                    arr[j + 1] = temp;
                    boundIndex = j + 1;
                    break;
                }
            }
        }
    }


    private static void swap(Integer arr[], int index1, int index2) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

}
