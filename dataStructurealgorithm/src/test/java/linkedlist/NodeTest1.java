package linkedlist;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NodeTest1 {

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

        public void printNode() {
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

    @Test
    public void testReverse() {
        Node list = null;
        for (int i = 1; i <= 4; i++) {
            list = new Node(i, list);
        }
        list = reverse(list);
        System.out.println();

    }

    /**
     * 单链表反转
     */
    public static Node reverse(Node list) {
        Node headNode = null;
        Node prevNode = null;
        Node currentNode = list;
        Node nextNode = null;
        while (currentNode != null) {
            nextNode = currentNode.next;
            if (nextNode == null) {
                headNode = currentNode;
            }
            currentNode.next = prevNode;
            prevNode = currentNode;
            currentNode = nextNode;
        }
        return headNode;
    }


    @Test
    public void test() {
        Node list1 = new Node(10, null);
        Node list2 = null;
        for (int i = 5; i >= 1; i--) {
            list2 = new Node(i, list2);
        }
        Node node = mergeSortedChain(list1, list2);
        System.out.println();
    }


    /**
     * 有序链表合并
     */
    public static Node mergeSortedChain(Node list1, Node list2) {
        Node resultHead = null;//合并后的链表头部
        Node currentTail = null;//合并过程中链表的尾部节点
        Node smaller = null;//对比节点的较小节点
        Node bigger = null;//对比节点的较大节点

        Node currList1 = list1;
        Node currList2 = list2;
        if (currList1 == null) {
            return currList2;
        }
        if (currList2 == null) {
            return currList1;
        }

        while (true) {
            if (currList1.data > currList2.data) {
                smaller = currList2;
                bigger = currList1;
                currList2 = currList2.next;
            } else {
                smaller = currList1;
                bigger = currList2;
                currList1 = currList1.next;
            }
            if (resultHead == null) {
                resultHead = smaller;
                currentTail = resultHead;
            } else {
                currentTail.next = smaller;
                currentTail = smaller;
            }
            if (smaller.next == null) {
                currentTail.next = bigger;
                return resultHead;
            }
        }
    }

    @Test
    public void testDeleteNode() {
        System.out.println("================ 删除头部节点 ================");
        Node list = null;
        for (int i = 5; i >= 1; i--) {
            list = new Node(i, list);
        }
        list.printNode();
        list = deleteNode(list, 5);
        list.printNode();


        System.out.println("================ 删除中间一个节点 ================");
        Node list2 = null;
        for (int i = 5; i >= 1; i--) {
            list2 = new Node(i, list2);
        }
        list2.printNode();
        list2 = deleteNode(list2, 2);
        list2.printNode();


        System.out.println("================ 删除最后一个节点 ================");
        Node list3 = null;
        for (int i = 5; i >= 1; i--) {
            list3 = new Node(i, list3);
        }
        list3.printNode();
        list3 = deleteNode(list3, 1);
        list3.printNode();
    }

    /**
     * 删除链表倒数第 n 个结点
     *
     * 返回删除后的链表
     */
    public static Node deleteNode(Node node, int n) {
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

    @Test
    public void testFindMiddleNode() {
        Node list = null;
        for (int i = 6; i >= 1; i--) {
            list = new Node(i, list);
        }
        Map<String, Object> middleNode = findMiddleNode(list);
        System.out.println(middleNode);

    }

    /**
     * 求链表的中间结点
     * 要求：不是环型链表
     * 返回：链表元素个数时奇数还是偶数。若奇数，返回中间节点；偶数则返回中间两个元素的前一个元素节点
     */
    public static Map<String, Object> findMiddleNode(Node list) {
        HashMap<String, Object> result = new HashMap<>();
        Node slow = list;
        Node fast = list;
        if (list == null) {
            return null;
        }
        if (list.next == null) {
            result.put("odd", true);
            result.put("middle", list);
            return result;
        }
        if (list.next.next == null) {
            result.put("odd", false);
            result.put("middle", list);
            return result;
        }
        //以上判断至少保证了链表中有2个元素
        while (true) {
            if (fast.next == null) {//链表个数奇数情况
                result.put("odd", true);
                result.put("middle", slow);
                return result;
            } else if (fast.next.next == null) {//链表个数偶数情况
                result.put("odd", false);
                result.put("middle", slow);
                return result;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
    }

    @Test
    public void testCheckMiddleEqual() {
        Node list = null;
        list = new Node(1, list);
        list = new Node(2, list);
        list = new Node(3, list);
        list = new Node(2, list);
        list = new Node(1, list);
        System.out.println(checkMiddleEqual(list));
    }

    /**
     * 判断是否是回文字符串
     */
    public static boolean checkMiddleEqual(Node list) {
        boolean result = false;
        Map<String, Object> middleNode = findMiddleNode(list);
        Node middle = (Node) middleNode.get("middle");
        Node before = list;
        Node after = middle.next;//后半部分
        //后半部分反转
        Node reverseAfter = reverse(after);

        Node currentBefore = before;
        Node currentAfter = reverseAfter;
        while (true) {
            if (currentAfter.data != currentBefore.data) {
                result =  false;
                break;
            }
            if (currentBefore == middle || currentBefore.next == middle) {
                result = true;
                break;
            }
            currentBefore = currentBefore.next;
            currentAfter = currentAfter.next;
        }

        //恢复
        after = reverse(reverseAfter);
        middle.next = after;
        return result;

    }

}
