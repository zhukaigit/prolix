package com.zk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试回文链表
 */
public class PalindromeTest {

    /**
     * 回文字符串的校验
     *
     * 偶数个
     */
    @Test
    public void testPalindromeTest() {
        Node n1 = new Node(1, null);
        Node n2 = new Node(2, n1);
        Node n3 = new Node(2, n2);
        Node n4 = new Node(1, n3);
        boolean palindromeNode = isPalindromeNode(n4);
        print(n4);
        Assert.assertTrue(palindromeNode);
    }

    /**
     * 回文字符串的校验
     *
     * 偶数个
     */
    @Test
    public void testPalindromeTest2() {
        Node n1 = new Node(1, null);
        Node n2 = new Node(3, n1);
        Node n3 = new Node(2, n2);
        Node n4 = new Node(1, n3);
        boolean palindromeNode = isPalindromeNode(n4);
        print(n4);
        Assert.assertFalse(palindromeNode);
    }

    /**
     * 回文字符串的校验
     *
     * 奇数个
     */
    @Test
    public void testPalindromeTest3() {
        Node n1 = new Node(1, null);
        Node n2 = new Node(2, n1);
        Node n3 = new Node(1, n2);
        boolean palindromeNode = isPalindromeNode(n3);
        print(n3);
        Assert.assertTrue(palindromeNode);
    }

    /**
     * 回文字符串的校验
     *
     * 奇数个
     */
    @Test
    public void testPalindromeTest4() {
        Node n1 = new Node(0, null);
        Node n2 = new Node(2, n1);
        Node n3 = new Node(1, n2);
        boolean palindromeNode = isPalindromeNode(n3);
        print(n3);
        Assert.assertFalse(palindromeNode);
    }

    private void print(Node node) {
        if (node == null) return;
        StringBuilder str = new StringBuilder();
        Node cur = node;
        while (cur != null) {
            str.append(cur.data).append(", ");
            cur = cur.next;
        }
        String result = "[" + str.subSequence(0, str.lastIndexOf(",")) + "]";
        System.out.println(result);

    }

    /**
     * 判断是否是回文链表
     */
    private boolean isPalindromeNode(Node node) {
        boolean result = true;
        if (node == null) return false;
        // 表示只有一个节点
        if (node.next == null) return true;

        // 1、寻找中间节点，并将前半部分链表反转
        Node slow = node;
        Node fast = node;
        // 原始链表中slow节点的前面一个节点，链表反转时使用
        Node slowPre = null;
        // 原始链表中slow节点的后面一个节点，链表反转时使用
        Node slowAfter = slow.next;

        while (fast.next != null && fast.next.next != null) {
            // fast节点移动，每次移动2步
            fast = fast.next.next;
            // 前半部分反转
            slow.next = slowPre;
            // slowPre节点移动
            slowPre = slow;
            // slow节点移动
            slow = slowAfter;
            // slowAfter节点移动
            slowAfter = slow.next;
        }
        // slow反转
        slow.next = slowPre;

        // 中间元素
        Node middleLeft = slow;
        Node middleRight = slowAfter;


        // ================= 轮询比较前、后半段、判断是否是回文 - 开始  =================
        Node curBeforeStart;
        Node curAfterStart = middleRight;
        if (fast.next == null) {// 链表元素个数：奇数
            curBeforeStart = middleLeft.next;
        } else { // 链表元素个数：偶数
            curBeforeStart = middleLeft;
        }
        while (curBeforeStart != null && curAfterStart != null) {
            if (curBeforeStart.data != curAfterStart.data) {
                result = false;
                break;
            }
            curBeforeStart = curBeforeStart.next;
            curAfterStart = curAfterStart.next;
        }
        // ================= 轮询比较前、后半段、判断是否是回文 - 结束 =================

        // 链表前半段恢复
        Node pre = middleLeft.next;
        Node after = middleRight;
        do {
            middleLeft.next = after;
            after = middleLeft;
            middleLeft = pre;
            if (pre != null) pre = pre.next;
        } while (middleLeft != null);

        return result;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    private static class Node {
        private int data;
        private Node next;
    }
}
