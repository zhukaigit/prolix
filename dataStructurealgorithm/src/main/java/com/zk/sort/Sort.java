package com.zk.sort;

@SuppressWarnings("all")
public class Sort {

    /**
     * 冒泡排序
     *
     * 实现：每一轮都是从下标为0的位置开始，依次比较相邻的两个元素，将其中较大的元素放在靠后的位置，
     * 所以每一轮比较结束后，最大的元素都放在了最后边。
     *
     * 第一轮比较结束后，最大的元素被放在的arr[length-1]的位置
     * 第二轮比较结束后，次大的元素被放在的arr[length-2]的位置
     * 第i轮比较结束后，第i大的元素被放在的arr[length-i]的位置
     */
    public static <T extends Comparable> void bubbleSort(T[] arr) {
        if (arr == null || arr.length == 0) return;
        int size = arr.length;//数组大小
        boolean exchange = false;//在一轮排序中，是否有过数据交换。若没有，表示数组已经是完全有序的了
        int count = 0;
        //理论上最多需要【size-1】轮比较
        for (int j = 1; j < size; j++) {
            //每一轮比较都是0开始。第一轮是【0】-【size-1】，第二轮是【0】-【size-2】... 第j轮是【0】-【size-j】
            for (int i = 0; i < arr.length - j; i++) {
                if (arr[i].compareTo(arr[i + 1]) == 1) {//只有大于，才会交换元素，属于稳定排序
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    exchange = true;
                }
            }
            count++;
            if (!exchange) {
                break;
            }
        }
        System.out.println("经历了【" + count + "】轮排序");
    }

    /**
     * 向下冒泡排序
     *
     * 思路：第一轮，始终让下标为0的元素依次与其后面位置的元素比较，将较小的元素放在下标为0的位置，
     *              一轮比较结束后，比较元素中的最小元素已在下标为0的位置
     *      ...
     *      第i轮，始终让下标为i-1的元素依次与其后面位置的元素比较，将较小的元素放在下标为i-1的位置，
     *              一轮比较结束后，比较元素中的最小元素已在下标为i-1的位置
     *
     * 与{@link Sort#bubbleSort(java.lang.Comparable[])}区别：即使初始数据完全有序，
     * 也不能做任何优化。依然需要比较那么多次数。
     */
    public static <T extends Comparable> void buddleDownSort(T[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) return;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i].compareTo(arr[j]) == 1) {
                    //交换
                    T temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 插入排序
     * 思路：将数组分为两个区间，左侧已排序区间，右侧未排序区间。初始状态下，左侧区间是有数组第一个元素。然后依次从右测区域取出元素，进行排序
     * <p>
     * 初始版本，待优化
     */
    public static <T extends Comparable> void insertionSort(T[] arr) {
        if (arr == null || arr.length == 0) return;
        int size = arr.length;//数组大小
        for (int i = 1; i < size; i++) {//i为右侧即将取出的元素
            for (int j = 0; j < i; j++) {
                if (arr[i].compareTo(arr[j]) == -1) {
                    //j至i的数据搬移
                    T temp = arr[i];
                    for (int k = i; k > j; k--) {
                        arr[k] = arr[k - 1];
                    }
                    arr[j] = temp;
                }
            }
        }

    }

    /**
     * 插入排序 - 优化
     *
     * 思路：将数组分为两个区间，左侧已排序区间，右侧未排序区间。初始状态下，
     * 左侧区间只有下标为0的元素。每一轮排序汇总，依次从取出右测区域左边第一个元素，将
     * 其插入到左侧区域中的合适位置
     */
    public static <T extends Comparable> void insertionSort2(T[] arr) {
        if (arr == null || arr.length == 0) return;
        // 依次从右侧的未排序区域取出一个元素，将其插入到已排序区域的合适位置
        for (int i = 1; i < arr.length; i++) {
            // 申明一个变量用于记录从右侧取出的元素，该值即是待插入的元素
            T value = arr[i];
            // 申明待插入数据的数组下标，初始时假设为i
            int emptyIndex = i;
            // 只要value小于前面元素，比较元素往后移动，emtpyIndex往前移动
            while (emptyIndex - 1 >= 0 && value.compareTo(arr[emptyIndex - 1]) == -1) {
                // 前一个元素往后移动一位
                arr[emptyIndex] = arr[emptyIndex - 1];
                emptyIndex--;
            }
            arr[emptyIndex] = value;
        }
    }

    /**
     * 选择排序
     * 思路：数组分为两个区域，左侧区域已排好序，依次从右侧区域取出最小元素，放至左侧区域尾部。初始状态左侧区域没有元素。
     */
    public static <T extends Comparable> void selectionSort(T[] arr) {
        if (arr == null || arr.length == 0) return;
        int size = arr.length;//数组大小
        for (int i = 0; i < size - 1; i++) {
            int smallestSubscript = i;//最小元素下标，原始时选择右侧第一个元素
            for (int j = i + 1; j < size; j++) {
                if (arr[j].compareTo(arr[smallestSubscript]) == -1) {
                    smallestSubscript = j;
                }
            }
            //数据交换，将选取的最小元素移动到左侧区域尾部
            T smallestData = arr[smallestSubscript];
            arr[smallestSubscript] = arr[i];
            arr[i] = smallestData;
        }
    }

    /**
     * 两个有序数组合并
     * 思路：i和j分别指向arr1与arr2第一个元素，比较arr1[i]和arr2[j]，若arr1[i] < arr2[j]，我们就将arr1[i]放入resultArr中，并且i向后
     * 移动一位，否则将arr2[j]放到resultArr数组中，j向后移动一位
     *
     * @param resultArr 排好序的结果数组
     * @param arr1      有序数组1
     * @param arr2      有序数组2
     */
    public static <T extends Comparable> void sortedArrMerge(T[] resultArr, T[] arr1, T[] arr2) {
        int size1 = arr1.length;
        int size2 = arr2.length;
        int current1 = 0;
        int current2 = 0;
        int index = 0;
        while (true) {
            if (arr1[current1].compareTo(arr2[current2]) == -1) {
                resultArr[index++] = arr1[current1++];
            } else {
                resultArr[index++] = arr2[current2++];
            }
            if (current1 == size1) {
                for (int i = current2; i < size2; i++) {
                    resultArr[index++] = arr2[i];
                }
                break;
            } else if (current2 == size2) {
                for (int i = current1; i < size1; i++) {
                    resultArr[index++] = arr1[i];
                }
                break;
            }
        }
    }


    /**
     * 并归排序
     * 思路：将数组从中间分为左、右两个区域。分别对左、右区域排序，然后将左右测区域合并成一个有序数组
     */
    public static <T extends Comparable> void mergeSort(Integer[] arr) {
        if (arr == null || arr.length == 0) return;
        mergeSort(arr, 0, arr.length - 1);
    }

    //保证数组 arr[start] ~ arr[end] 是有序的。
    private static void mergeSort(Integer[] arr, int start, int end) {
        if (start >= end) return;
        int middle = (start + end) / 2;
        mergeSort(arr, start, middle);//保证 start ~ middle 有序
        mergeSort(arr, middle + 1, end);//保证 middle+1 ~ end 有序
        //合并有序的两个区间
        mergePartSorted(arr, start, middle, end);
    }

    /**
     * 初始状态：arr[start] ～ arr[middle] 以及 arr[middle+1] ～ arr[end] 这两个区间，分别都是是有序的
     * 结果：arr[start] ~ arr[end] 这个大区间是有序的
     * <p>
     * 思路同{@link Sort#sortedArrMerge(java.lang.Comparable[], java.lang.Comparable[], java.lang.Comparable[])}
     */
    private static void mergePartSorted(Integer[] arr, int start, int middle, int end) {
        int tempSize = end - start + 1;
        Integer[] temp = new Integer[tempSize];
        int arr1_end = middle;
        int arr2_Start = middle + 1;
        int curArr1 = start;
        int curArr2 = arr2_Start;
        int index = 0;
        while (true) {
            temp[index++] = arr[curArr2] < arr[curArr1] ? arr[curArr2++] : arr[curArr1++];
            if (curArr1 == arr1_end + 1) {
                for (int i = curArr2; i < end + 1; i++) {
                    temp[index++] = arr[i];
                }
                break;
            } else if (curArr2 == end + 1) {
                for (int i = curArr1; i < arr1_end + 1; i++) {
                    temp[index++] = arr[i];
                }
                break;
            }
        }
        //数据迁移
        int count = 0;
        for (int i = start; i <= end; i++) {
            arr[i] = temp[count++];
        }

    }


    /**
     * 快速排序。简称：快排
     * <p>
     * 思路：选取数组中任意一个元素作为基础元素，将数组分为三个部分。左侧的所有元素小于基础元素，
     * 中间就是基础元素，右侧的所有元素大于基础元素。然后再对左、右两侧排序，递归下去...
     * <p>
     * 数据交换原则：
     * 1、若【基础元素】在【当前选择元素】左侧，当【基础元素】>【当前选择元素】时，数据交换。
     * 2、若【基础元素】在【当前选择元素】右侧，当【基础元素】<【当前选择元素】时，数据交换。
     * <p>
     * 为实现原地排序的巧妙的思想：
     * 初始状态时，【基础元素】与【当前选择元素】是相聚最远的，即一个在数组头，另一个在数组尾。随着比较与交换的发生，
     * 这两个元素的距离越来越近，直到完全重合，表示对比结束。
     * <p>
     * 操作细则：一般选择数组最后一个元素为基础元素，数组第一个元素为当前元素，这样就保证了
     *
     * @param arr
     */
    public static <T extends Comparable> void quickSort(T[] arr) {
        if (arr == null || arr.length == 0) return;
//        quickSort(arr, 0, arr.length - 1);
        quickSort2(arr, 0, arr.length - 1);
    }


    private static <T extends Comparable> void quickSort(T[] arr, int start, int end) {
        if (start >= end) return;

        /**
         * 初始时，基础元素与当前对比元素选择
         *【注意】：一般来说，第一次选取基础元素与当前对比元素时，要么"基础元素"=arr[0]，"当前对比元素" = arr[length-1]。
         * 要么"基础元素"=arr[length-1]，"当前对比元素" = arr[0]。
         **/
        int baseSubscript = start;//基础元素下标
        int currentSubscript = end;//当前比对元素的下标
        boolean baseAtRight;//表示【基础元素】是否在【当前对比元素】右侧

        //循环比较
        while (true) {
            if (currentSubscript == baseSubscript) break;
            baseAtRight = (baseSubscript - currentSubscript > 0);//判断两元素位置相对关系
            if ((baseAtRight && arr[baseSubscript].compareTo(arr[currentSubscript]) == -1)
                    || (!baseAtRight && arr[baseSubscript].compareTo(arr[currentSubscript]) == 1)) {
                /** 交换数据 **/
                T tempData = arr[baseSubscript];
                arr[baseSubscript] = arr[currentSubscript];
                arr[currentSubscript] = tempData;

                /** 下标调整 **/
                int temp = baseSubscript;
                //需要保证基础元素不变。所以数据发生交换之后，下标也需要交换
                baseSubscript = currentSubscript;
                //要保证当前对比元素与基础元素相对位置最远，所有在数据发生交换之后，当前元素下标也需要交换，且与基础元素靠近一步
                currentSubscript = baseAtRight ? temp - 1 : temp + 1;
            } else {
                currentSubscript = baseAtRight ? currentSubscript + 1 : currentSubscript - 1;
            }
        }

        quickSort(arr, start, baseSubscript - 1);
        quickSort(arr, baseSubscript + 1, end);
    }

    /**
     * 对quickSort()的优化
     *
     * 如何将数组中小于基础元素的数据放在左边，大于基础元素的数据放在右边，基础元素处在中间？
     * 答：一般选择最后一个元素为基础元素，申明一个int类型变量partLineIndex，其意义时表示大小元素的分割线下标，
     * 数组中所有小于基础元素的数据放在其左侧，大于基础元素的数据放在其位置及其右侧，比较完之后，再将基础元素与
     * partLineIndex该位置元素交换。初始时假设partLineIndex = start。
     * 故在轮训比较过程中，若【当前对比元素】小于基础元素，该位置元素与【当前对比元素】交换，且partLineIndex++。
     * 这样就保证了所有小于基础元素的数据都在partLineIndex左侧了。
     */
    private static <T extends Comparable> void quickSort2(T[] arr, int start, int end) {
        if (start >= end) return;

        /** 寻找基础元素位置，并分好区 **/
        int baseSubsript = end;//初始时，选择最后一个元素为基础元素
        int partLineIndex = start;//大小元素分割线的下标值（即基础元素最后应该所在的位置），初始时设为start
        for (int j = start; j < end; j++) {
            if (arr[j].compareTo(arr[baseSubsript]) == -1) {
                T temp = arr[partLineIndex];
                arr[partLineIndex] = arr[j];
                arr[j] = temp;
                partLineIndex++;
            }
        }
        //将基础元素放到合适位置
        T temp = arr[baseSubsript];
        arr[baseSubsript] = arr[partLineIndex];
        arr[partLineIndex] = temp;
        baseSubsript = partLineIndex;

        /** 给子区域排序 **/
        quickSort2(arr, start, baseSubsript - 1);
        quickSort2(arr, baseSubsript + 1, end);
    }

    /**
     * 如何用快排思想在O(n)内查找第K大元素？
     */
    public static <T extends Comparable> T findKMax(T[] arr, int k) {
        if (arr == null || arr.length == 0) throw new RuntimeException("the arr is null");
        if (k <= 0 || k > arr.length) throw new RuntimeException("the param k is error");
        return quickFindInPartArea(arr, 0, arr.length - 1, k);
    }

    private static <T extends Comparable> T quickFindInPartArea(T[] arr, int start, int end, int k) {
        if (start == end) return arr[end];
        /**
         * 初始时，基础元素与当前对比元素选择
         *【注意】：一般来说，第一次选取基础元素与当前对比元素时，要么"基础元素"=arr[0]，"当前对比元素" = arr[length-1]。
         * 要么"基础元素"=arr[length-1]，"当前对比元素" = arr[0]。
         **/
        int baseSubscript = start;//基础元素下标
        int currentSubscript = end;//当前比对元素的下标
        boolean baseAtRight;//表示【基础元素】是否在【当前对比元素】右侧
        //循环比较
        while (true) {
            if (currentSubscript == baseSubscript) break;
            baseAtRight = (baseSubscript - currentSubscript > 0);//判断两元素位置相对关系
            if ((baseAtRight && arr[baseSubscript].compareTo(arr[currentSubscript]) == -1)
                    || (!baseAtRight && arr[baseSubscript].compareTo(arr[currentSubscript]) == 1)) {
                /** 交换数据 **/
                T tempData = arr[baseSubscript];
                arr[baseSubscript] = arr[currentSubscript];
                arr[currentSubscript] = tempData;

                /** 下标调整 **/
                int temp = baseSubscript;
                //需要保证基础元素不变。所以数据发生交换之后，下标也需要交换
                baseSubscript = currentSubscript;
                //要保证当前对比元素与基础元素相对位置最远，所有在数据发生交换之后，当前元素下标也需要交换，且与基础元素靠近一步
                currentSubscript = baseAtRight ? temp - 1 : temp + 1;
            } else {
                currentSubscript = baseAtRight ? currentSubscript + 1 : currentSubscript - 1;
            }
        }

        int baseKIndex = arr.length - k;
        if (baseKIndex == baseSubscript) {
            return arr[baseSubscript];
        } else if (baseKIndex > baseSubscript) {
            return quickFindInPartArea(arr, baseSubscript + 1, end, k);
        } else {
            return quickFindInPartArea(arr, start, baseSubscript - 1, k);
        }
    }
}
