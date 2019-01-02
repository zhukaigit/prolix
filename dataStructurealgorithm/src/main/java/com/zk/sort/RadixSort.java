package com.zk.sort;

/**
 * 基数排序
 */
public class RadixSort {

    public static void main(String[] args) {
        long[] arr = createRandomArr(1000000, 1000000000l, 9999999999l);
        System.out.println("原始数据是否有序：" + isSorted(arr));
//        System.out.println("原始数组：" + Arrays.toString(arr));
        arr = executeSort(arr);
//        System.out.println("排序数组：" + Arrays.toString(arr));
        System.out.println("是否排序成功：" + isSorted(arr));
    }

    /**
     * 基数排序
     */
    public static long[] executeSort(long[] arr) {
        int[] radixArr = new int[arr.length];
//        int count = 1;
        //需要对每一位基础进行桶排序
        for (int i = String.valueOf(arr[0]).length(); i >=1 ; i--) {
            for (int j = 0; j < arr.length; j++) {
                radixArr[j] = numAtIndex(arr[j], i);
            }
            arr = countingSort(radixArr, arr);
//            System.out.println("第" + (count++) + "轮：" + Arrays.toString(arr));
        }
        return arr;
    }

    /**
     * 判断数组是否是已排序数组
     */
    private static boolean isSorted(long[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对arr按照某个指定位下标数据，进行桶排序
     *
     * @param radixArr 选取某指定位下标的数字，组成的数组； radixArr[i]对应arr[i]的某指定位数据
     * @param arr 原始排序数组， radixArr[i]对应arr[i]的某指定位数据
     * @return
     */
    private static long[] countingSort(int[] radixArr, long[] arr) {
        /** 寻找数组中最大值 **/
//        int maxData = radixArr[0];
//        for (int i = 1; i < radixArr.length; i++) {
//            if (radixArr[i] > maxData) {
//                maxData = radixArr[i];
//            }
//        }
        int maxData = 9;//最大数可以推测，就是9，故没有必要再遍历那么多数据去找最大值了

        /** 桶的个数为【maxData + 1】个，计算桶中元素的个数，每个桶放的元素相同，元素的值与桶的下标相同 **/
        int[] bucketArr = new int[maxData + 1];
        for (int i = 0; i < radixArr.length; i++) {
            bucketArr[radixArr[i]]++;
        }

        /** 累加桶中的个数 **/
        for (int i = 0; i < bucketArr.length - 1; i++) {
            bucketArr[i + 1] = bucketArr[i + 1] + bucketArr[i];
        }

        /** 遍历数组中元素，将结果放入resultArr中 **/
        long[] resultArr = new long[radixArr.length];
        for (int i = radixArr.length - 1; i >= 0; i--) {
            //bucketArr[arr[i]]-1 的值就表示在结果数组的下标
            int resultArrSubscript = bucketArr[radixArr[i]] - 1;
            resultArr[resultArrSubscript] = arr[i];//关键此处使用arr，而不是radixArr
            bucketArr[radixArr[i]]--;
        }
        return resultArr;
    }

    /**
     * 获取一个指定位置的数据
     * @param src
     * @param index
     * @return
     */
    private static int numAtIndex(long src, int index) {
        char[] chars = String.valueOf(src).toCharArray();
        if (chars.length < index) {
            throw new RuntimeException("参数错误");
        }
        int value = Integer.valueOf(String.valueOf(chars[index - 1]));
        return value;
    }


    /**
     * 创建指定长度的随机数组
     * @param arrSize 数组长度
     * @param min 元素最小值
     * @param max 元素最大值
     * @return
     */
    private static long[] createRandomArr(int arrSize, long min, long max) {
        long[] arr = new long[arrSize];
        for (int i = 0; i < arrSize; i++) {
            arr[i] = generateScopeRandom(min, max);
        }
        return arr;
    }

    /**
     * 生成指定范围随机数
     */
    private static long generateScopeRandom(long min, long max) {
        long num = min + (long) (Math.random() * (max - min + 1));
        return num;
    }
}
