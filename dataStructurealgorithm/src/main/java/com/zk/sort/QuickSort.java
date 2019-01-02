package com.zk.sort;

import java.util.Arrays;

import static com.zk.util.NumUtil.createRandomArr;

@SuppressWarnings("all")
public class QuickSort {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            System.out.println("\n====================== 第" + i + "轮 =======================");
            Integer[] arr = createRandomArr(20);
            System.out.println("原始数组：" + Arrays.toString(arr));
//            QuickSort.execute(arr, 0, arr.length - 1, 1);
            QuickSort.quictSort(arr, 0, arr.length - 1);
            System.out.println("排序数组：" + Arrays.toString(arr));
            System.out.println("是否已排序：" + isSorted(arr));
        }
    }

    /**
     * 判断数组是否是已排序数组
     */
    private static boolean isSorted(Integer[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将数组arr中的arr[start] ～ arr[end]这个区间的数据排序
     *
     * 优化，随机选择一个元素作为基础元素
     * <p>
     * 思路：随机选择数组中的一个元素为基础元素，将所有小于基础元素的其他元素放在其左边，这样就将原始数组分成
     * 了3个部分，左右元素都小于基础元素，中间为基础元素，右边则是大于等于基础元素。然后在分别对左右区间
     * 进行递归式的排序。
     * <p>
     * 分区实现：设置两个指针下标。partionIndex指针：所有下标值小于该指针的元素，都是小于基础元素的数据；
     * eleIndex：用于轮询整个数组的数据，来与基础元素比较。初始时两个指针都指向起始位置，在轮询比
     * 较过程中，若出现小于基础元素的数据，则将其与partionIndex所指位置的元素进行交换，并将partionIndex++。
     * 轮询结束后，再将基础元素与partionIndex所指位置的元素进行交换。注意：在交换时，需要始终记录
     * 基础元素所在的位置
     * <p>
     *
     * @param arr   待排序数组
     * @param start 待排序开始位置
     * @param end   待排序结束位置
     */
    public static void quictSort(Integer[] arr, int start, int end) {
        if (start >= end) return;
        // 1、随机获取下标，作为基础元素的下标。
        int baseIndex = start + (int) (Math.random() * (end - start + 1));
        // 基础元素变量申明
        Integer baseElement = arr[baseIndex];

        // 2、大小区间分割下标，该下标左边的数据全是小于基础元素的数据
        int partionIndex = start;

        // 3、轮询与基础元素比较，将所有小于基础元素的数据移动到partionIndex指针的左边。
        // 实现方式：对比元素与partionIndex位置的元素交换，并将partionIndex++
        for (int i = start; i <= end; i++) {
            if (arr[i] < baseElement) {
                // 数据交换
                Integer temp = arr[partionIndex];
                arr[partionIndex] = arr[i];
                arr[i] = temp;

                // 若partionIndex == baseIndex，表示此时分区下标指针指向的就是基础元素。那么在基础元素发生交换后，需要更新基础元素的下标值
                // 之所以需要记录基础元素所在位置，因为在比较结束之后，需要将基础元素与partionIndex位置的元素交换
                if (partionIndex == baseIndex) baseIndex = i;

                //分区下标递增
                partionIndex++;
            }
        }
        // 4、将基础元素与partionIndex位置的元素交换
        arr[baseIndex] = arr[partionIndex];
        arr[partionIndex] = baseElement;

        // 递归从新左右区间数据排序
        quictSort(arr, start, partionIndex - 1);
        quictSort(arr, partionIndex + 1, end);
    }

}
