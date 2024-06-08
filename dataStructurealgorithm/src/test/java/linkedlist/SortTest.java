package linkedlist;

import com.zk.sort.*;
import com.zk.util.NumUtil;
import org.junit.Test;

import java.util.Arrays;

import static com.zk.sort.Sort.*;
import static com.zk.util.NumUtil.*;

public class SortTest {

    @Test
    public void testBulletSort() {
        Integer[] arr = createRandomArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testBulletDownSort() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("\n====================== 第"+i+"轮 =======================");
            Integer[] arr = createRandomArr(10);
            System.out.println("原始数组："+Arrays.toString(arr));
            buddleDownSort(arr);
            System.out.println("排序数组："+Arrays.toString(arr));
        }
    }

    @Test
    public void testBulletSort2() {
        Integer[] arr = createSortedArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testInsertionSort() {
        Integer[] arr = createDecreasedArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        insertionSort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testInsertionSort2() {
        Integer[] arr = createRandomArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        insertionSort2(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testSelectionSort() {
        Integer[] arr = createRandomArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        selectionSort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testMergeWithInsertSort() {
//        Integer[] arr = {95, 14, 11, 49, 1};
        Integer[] arr = createRandomArr(50);
        System.out.println("原始数组："+Arrays.toString(arr));
        MergeWithInsertSort.sort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testSortedArrMerge() {
        Integer[] arr1 = createSortedArr(5);
        Integer[] arr2 = createSortedArr(3);
        System.out.println("原始：【arr1】=" + Arrays.toString(arr1));
        System.out.println("原始：【arr2】=" + Arrays.toString(arr2));
        Integer[] arr = new Integer[arr1.length + arr2.length];
        sortedArrMerge(arr, arr1, arr2);
        System.out.println("结果：" + Arrays.toString(arr));

    }

    @Test
    public void testMergeSort() {
        Integer[] arr = createRandomArr(10);
        System.out.println("原始："+Arrays.toString(arr));
        mergeSort(arr);
        System.out.println("排序后："+Arrays.toString(arr));
    }

    @Test
    public void testQuickSort() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("\n====================== 第"+i+"轮 =======================");
            Integer[] arr = createRandomArr(10);
            System.out.println("原始数组："+Arrays.toString(arr));
            quickSort(arr);
            System.out.println("排序数组："+Arrays.toString(arr));
        }
    }

    @Test
    public void testQuickSort2() {
        for (int i = 1; i <= 10; i++) {
        }
//        System.out.println("\n====================== 第"+i+"轮 =======================");
        Integer[] arr = {70, 6, 85, 50};
//        Integer[] arr = createRandomArr(10);
        System.out.println("原始数组："+Arrays.toString(arr));
        QuickSort2.sort(arr);
        System.out.println("排序数组："+Arrays.toString(arr));
    }

    @Test
    public void testFindKMax() {
        for (int i = 0; i < 20; i++) {
            System.out.println("\n====================== 第"+i+"轮 =======================");
            Integer[] arr = createRandomArr(10);
            int k = NumUtil.generateScopeRandom(1, 10).intValue();
            System.out.println("原始数组："+Arrays.toString(arr));
            System.out.println("第" + k + "个大的元素为：" + findKMax(arr, k));
            quickSort(arr);
            System.out.println("排序数组："+Arrays.toString(arr));
        }
    }

    /**
     * 直接插入排序与希尔排序的执行效率对比
     */
    @Test
    public void compareInsertAndShell() {
        for (int arrLength = 1000; arrLength < 50000; arrLength += 2000) {
            System.out.println("================ arrLength = " + arrLength + "================");
            Integer[] arr = createRandomArr(arrLength, 0, 10000);
            Integer[] arr2 = arr.clone();
            long l1 = System.currentTimeMillis();

            insertionSort2(arr);
            long l2 = System.currentTimeMillis();
            System.out.println("直接插入排序耗时：" + (l2 - l1));

            ShellSort.sort(arr2);
            long l3 = System.currentTimeMillis();
            System.out.println("希尔排序耗时：" + (l3 - l2));
        }

    }

    /**
     * 直接插入排序与希尔排序的执行效率对比
     */
    @Test
    public void compareCountSortAndQuickSort() {
        int arrLength = 300000;
        System.out.println("================ arrLength = " + arrLength + "================");
        Integer[] arr = createRandomArr(arrLength, 0, 600);
        long l1 = System.currentTimeMillis();

        //快速排序
        Integer[] arr1 = arr.clone();
        QuickSort.quictSort(arr1, 0, arr1.length - 1);
        long l2 = System.currentTimeMillis();
        System.out.println("快速排序耗时：" + (l2 - l1));

        Integer[] arr2 = arr.clone();
        CountingSort.executeSort(arr2);
        long l3 = System.currentTimeMillis();
        System.out.println("计数排序耗时：" + (l3 - l2));

    }

}
