package com.zk.sort;

public class ClassifyString {

    public static void main(String[] args) {
        String src = "fj80809ffjdlsDFLK0fsljfj80fsd98fs0d9f80sd9f80sFDJfsLSfK";
        System.out.println(execute(src));

    }

    /**
     *
     * 功能：将字符串小写字母放在最左侧，数字放在中间，大些字母放在右侧
     *
     * 思路：用两个指针（【指针i】和【指针j】）作为分界线，初始时【指针i】指向数组头，
     * 另一个【指针j】指向数组尾部。再使用第三个【指针k】用来遍历元素，若元素为小写字母，
     * 则与【指针i】所指元素交换，然后i++，k++；若元素为数字，不交换数据，k++；若元素
     * 为大写字母，则与【指针j】所指元素交换，j--，【指针k】不移动。直到k>j时，遍历结束。
     *
     * @param src 数据要求：由小写字母，大写字母，数字组成
     * @return
     */
    public static String execute(String src) {
        char[] chars = src.toCharArray();
        int i = 0;//数字与小写字母分界线，初始时指向数组头部
        int j = chars.length - 1;//数字与大写字母分界线，初始时指向数组尾部
        int k = 0;//用来遍历元素
        while (true) {
            if (k > j) break;
            if (Character.isLowerCase(chars[k])) {
                char temp = chars[i];
                chars[i] = chars[k];
                chars[k] = temp;
                i++;
                k++;
            } else if (Character.isUpperCase(chars[k])) {
                char temp = chars[j];
                chars[j] = chars[k];
                chars[k] = temp;
                j--;
            } else {
                k++;
            }
        }
        return new String(chars);
    }

}
