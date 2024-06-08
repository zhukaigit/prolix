package com.zk.sort;

public class QuickSort2 {

    public static void main(String[] args) {

    }


    public static void sort(Integer[] arr) {
        sort(arr, 0, arr.length-1);
    }

    private static void sort(Integer[] arr, int startIndex, int endIndex) {
        int boundIndex = part(arr, startIndex, endIndex);
        sort(arr, startIndex, boundIndex);
        sort(arr, boundIndex + 1, endIndex);
    }

    private static int part(Integer[] arr, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return startIndex;
        }

        int indexLeft = startIndex;
        int indexRight = endIndex;
        for (int i = startIndex; i <= endIndex; i++) {
            if (arr[indexRight] < arr[indexLeft]) {
                swap(arr, indexLeft, indexRight);
                indexRight--;
            } else {
                indexLeft++;
            }
            if (indexLeft == indexRight) {
                break;
            }
        }
        return indexLeft;
    }


    private static void swap(Integer arr[], int index1, int index2) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }
}
