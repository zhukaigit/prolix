package com.zk.sort;

/**
 * 桶排序
 */
public class BucketSort {

    public static void bucket(int[] arr) {

        // 设置每个桶的数据范围
        int scope = 5;

        //1.查找数组最大的数
        int max = arr[0];
        for(int i=1; i<arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
            }
        }

        // 确定桶的个数
        int bucketSize = max / scope + 1;

        // 确定各个桶的大小
        // 解释：数组中每个index下标对应的值，即第index+1个桶的元素个数
        int[] eachBucketSize = new int[bucketSize];
        int[] eachBucketSizeCopy = new int[bucketSize];
        for (int i = 0; i < arr.length; i++) {
            eachBucketSize[arr[i]/scope]++;
            eachBucketSizeCopy[arr[i]/scope]++;
        }

        // 存放桶的容器
        int[][] bucketContainer = new int[bucketSize][];

        // 创建各个桶
        for (int i = 0; i < eachBucketSize.length; i++) {
            int[] bucketArr = new int[eachBucketSize[i]];
            bucketContainer[i] = bucketArr;
        }

        // 将每个元素放入相应的桶子
        for (int i = 0; i < arr.length; i++) {
            // 应该存放在哪一个桶
            int index = arr[i] / scope ;
            bucketContainer[index][--eachBucketSize[index]] = arr[i];
        }

        // 对各个桶进行快速排序
        for (int i = 0; i < bucketContainer.length; i++) {
            int[] bucket = bucketContainer[i];
            quick(bucket);
        }

        // 返回结果
        int index = 0;
        for (int i = 0; i < bucketContainer.length; i++) {
            int[] bucket = bucketContainer[i];
            for (int j = 0; j < bucket.length; j++) {
                arr[index++] = bucket[j];
            }
        }
    }

    private static void quick(int[] arr) {
        quick(arr, 0, arr.length-1);
    }

    private static void quick(int[] arr, int left, int right) {
        if(left>=right) return;
        int mid = partition(arr, left, right);
        quick(arr, left, mid-1);
        quick(arr, mid+1, right);
    }

    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i=left,j=left;
        for(; j<right; j++) {
            if(arr[j] < pivot) {
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
                i++;
            }
        }
        int temp2 = arr[j];
        arr[j] = arr[i];
        arr[i] = temp2;
        return i;

    }

    public static void main(String[] args) {
        int[] arr = {22,5,11,41,45,26,29,10,7,8,30,27,42,43,40};
        bucket(arr);
        System.out.println(arr);
    }
}
