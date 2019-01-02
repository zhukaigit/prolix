package com.zk.leetCode;

/**
 * Add Two Numbers:
 *
 * You are given two non-empty linked lists representing two non-negative integers. The digits are
 * stored in reverse order and each of their nodes contain a single digit. Add the two numbers and
 * return it as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4) Output: 7 -> 0 -> 8 Explanation: 342 + 465 = 807.
 */
public class AddTwoNumbers {

  public static void main(String[] args) {
    ListNode l1 = null;
    ListNode tail = null;
    for (int i = 0; i < 10; i++) {
      if (i == 0) {
        l1 = new ListNode(9);
        tail = l1;
      } else {
        ListNode listNode = new ListNode(9);
        tail.next = listNode;
        tail = listNode;
      }
    }

    ListNode l2 = null;
    ListNode tail2 = null;
    for (int i = 0; i < 10; i++) {
      if (i == 0) {
        l2 = new ListNode(9);
        tail2 = l2;
      } else {
        ListNode listNode = new ListNode(9);
        tail2.next = listNode;
        tail2 = listNode;
      }
    }

    ListNode exe = exe(l1, l2);
    System.out.println();
  }

  public static ListNode exe(ListNode l1, ListNode l2) {
    ListNode cur1 = l1;
    ListNode cur2 = l2;
    ListNode node = null;
    ListNode tail = null;
    int sum = 0;
    int prev = 0;
    //遍历求和
    while (true) {
      if (cur1 == null && cur2 == null) {
        break;
      }
      int num1 = cur1 != null ? cur1.val : 0;
      int num2 = cur2 != null ? cur2.val : 0;
      sum = num1 + num2 + prev;
      String sumStr = String.valueOf(sum);
      Integer lastNum = Integer.valueOf(sumStr.substring(sumStr.length() - 1, sumStr.length()));
      Integer beforeNum = sumStr.length() == 1 ? 0 : Integer.valueOf(sumStr.substring(0, sumStr.length() - 1));
      ListNode newNode = new ListNode(lastNum);
      if (node == null) {
        node = newNode;
        tail = node;
      } else {
        tail.next = newNode;
        tail = newNode;
      }
      prev = beforeNum;

      cur1 = cur1 != null ? cur1.next : null;
      cur2 = cur2 != null ? cur2.next : null;
    }

    if (prev != 0) {
      tail.next = new ListNode(prev);
    }

    return node;
  }


  private static class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
    }
  }
}
