package linkedlist;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NodeTest {

    /**
     * 测试单链表反转
     */
    @Test
    public void testReverseLinkedNode() {
        Node list = null;
        for (int i = 1; i <= 4; i++) {
            list = new Node(i, list);
        }
        list.print();
        list = reverseLinkedNode(list);
        list.print();

    }

    /**
     * 单链表反转
     */
    public static Node reverseLinkedNode(Node list) {
        if (list == null) return null;
        Node pre = null;
        Node curNode = list;
        Node next = curNode.next;
        while (next != null) {
            curNode.next = pre;
            pre = curNode;
            curNode = next;
            next = curNode.next;
        }
        curNode.next = pre;
        return curNode;
    }


    @Test
    public void test() {
        // list1构造
        Node list1 = new Node(1, null);
        Node a = new Node(5, null);
        Node b = new Node(30, null);
        Node c = new Node(90, null);
        list1.next = a;
        a.next = b;
        b.next = c;


        // list2构造
        Node list2 = new Node(4, null);
        Node a2 = new Node(12, null);
        Node b2 = new Node(15, null);
        Node c2 = new Node(100, null);
        list2.next = a2;
        a2.next = b2;
        b2.next = c2;

        // 合并
        Node node = mergeSortedChain(list1, list2);
        node.print();
    }


    /**
     * 有序链表合并
     */
    public static Node mergeSortedChain(Node list1, Node list2) {
        Objects.requireNonNull(list1, "入参list1不能为null");
        Objects.requireNonNull(list2, "入参list2不能为null");
        Node resultHead = list1.data < list2.data ? list1 : list2;// 合并后的链表头部
        Node currentTail = resultHead;// 合并过程中链表的尾部节点

        // list1链表的当前循环节点
        Node currList1 = list1.data < list2.data ? list1.next : list1;
        // list2链表的当前循环节点
        Node currList2 = list1.data < list2.data ? list2 : list2.next;
        while (currList1 != null && currList2 != null) {
            if (currList1.data < currList2.data) {
                currentTail.next = currList1;
                currentTail = currList1;
                currList1 = currList1.next;
            } else {
                currentTail.next = currList2;
                currentTail = currList2;
                currList2 = currList2.next;
            }
        }
        if (currList1 == null) {
            currentTail.next = currList2;
        } else {
            currentTail.next = currList1;
        }
        return resultHead;
    }

    /**
     * 测试：删除链表倒数第 n 个结点
     */
    @Test
    public void testDeleteNode() {
        System.out.println("================ 删除头部节点 ================");
        Node list = null;
        for (int i = 5; i >= 1; i--) {
            list = new Node(i, list);
        }
        list.print();
        list = deleteReversedNode(list, 5);
        list.print();


        System.out.println("================ 删除中间一个节点 ================");
        Node list2 = null;
        for (int i = 5; i >= 1; i--) {
            list2 = new Node(i, list2);
        }
        list2.print();
        list2 = deleteReversedNode(list2, 2);
        list2.print();


        System.out.println("================ 删除最后一个节点 ================");
        Node list3 = null;
        for (int i = 5; i >= 1; i--) {
            list3 = new Node(i, list3);
        }
        list3.print();
        list3 = deleteReversedNode(list3, 1);
        list3.print();
    }

    /**
     * 删除链表倒数第 n 个结点
     *
     * 返回删除后的链表
     */
    public static Node deleteReversedNode(Node node, int n) {
        Objects.requireNonNull(node, "node不能为控");
        if (n < 1) throw new RuntimeException("参数n必须大于0");
        Node before = node;
        Node after = node;
        int count = 0;
        while (after != null) {
            if (count++ > n) {
                before = before.next;
            }
            after = after.next;
        }

        // 表示删除的是头部节点
        if (count == n) {
            return node.next;
        }

        // 申明待删除节点的前一个节点
        Node deletePreNode = before;
        deletePreNode.next = deletePreNode.next.next;
        return node;
    }

    /**
     * 测试：求链表的中间结点
     */
    @Test
    public void testFindMiddleNode() {
        // 偶数个元素
        Node list = null;
        for (int i = 6; i >= 1; i--) {
            list = new Node(i, list);
        }
        Map<String, Object> middleNode = findMiddleNode(list);
        System.out.println(middleNode);

        // 奇数个元素
        Node list2 = null;
        for (int i = 5; i >= 1; i--) {
            list2 = new Node(i, list2);
        }
        Map<String, Object> middleNode2 = findMiddleNode(list2);
        System.out.println(middleNode2);
    }

    /**
     * 求链表的中间结点
     * 要求：不是环型链表
     * 返回：链表元素个数时奇数还是偶数。若奇数，返回中间节点；偶数则返回中间两个元素的前一个元素节点
     */
    public static Map<String, Object> findMiddleNode(Node list) {
        Objects.requireNonNull(list, "入参不能为null");
        HashMap<String, Object> result = new HashMap<>();

        Node slow = list;
        Node fast = list.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 链表奇数个元素
        result.put("odd", fast == null);
        result.put("middle", slow);
        return result;
    }

    /**
     * 测试：判断是否是回文链表
     */
    @Test
    public void testIsPalindromeNode() {
        Node list = null;
        list = new Node(1, list);
        list = new Node(2, list);
        list = new Node(3, list);
        list = new Node(2, list);
        list = new Node(1, list);
        list.print();
        System.out.println(isPalindromeNode(list));
    }

    /**
     * 判断是否是回文链表
     *
     * 思路：快慢指针分别从头部第一个、第二个元素移动。慢指针移动的过程中，同时反转前半部分链表。
     * 当快指针到达尾部时，慢指针刚好指向中间节点。然后分别比较前后半段节点值。最后恢复链表
     */
    public static boolean isPalindromeNode(Node list) {
        Objects.requireNonNull(list, "入参list不能为null");
        boolean result = true;

        // 前半部分反转
        Node right; // 申明链表下半部分的开始。
        Node slowPre = null;
        Node slow = list;
        Node fast = list.next;
        while (fast != null && fast.next != null) {
            Node temp = slow.next;
            slow.next = slowPre;// 反转指针
            slowPre = slow;
            slow = temp;
            fast = fast.next.next;
        }
        right = slow.next;
        slow.next = slowPre;

        // 比较是否是回文
        Node leftStart = fast == null ? slowPre : slow;
        Node rightStart = right;
        while (leftStart != null && rightStart != null) {
            if (leftStart.data != rightStart.data) {
                result = false;
                break;
            }
            leftStart = leftStart.next;
            rightStart = rightStart.next;
        }

        // 链表恢复
        while (slow != null) {
            Node temp = slow.next;
            slow.next = right;
            right = slow;
            slow = temp;
        }

        return result;

    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Node {

        private int data;
        private Node next;

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }

        public void print() {
            StringBuilder str = new StringBuilder();
            Node cur = this;
            do {
                str.append(cur.data).append(",");
                cur = cur.next;
            } while (cur != null);
            String linkedStr = "[" + str.subSequence(0, str.lastIndexOf(",")) + "]";
            System.out.println(linkedStr);

        }

    }

}
