package linkedlist;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SinglyLinkedList_07 {

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


    private static Node createCycleChain() {
        //构造环链表
        Node list = null;
        Node tail = null;
        for (int i = 5; i >= 1; i--) {
            list = new Node(i, list);
            if (i == 5) {
                tail = list;
            }
        }
        tail.next = list.next;
        return list;
    }

    @Test
    public void testCheckCycle() {
        Node list = createCycleChain();
        System.out.println(checkCycle(list));
    }

    /**
     * 检测环是否存在 思路：快慢指针法
     */
    public static boolean checkCycle(Node list) {
        Node list1 = list;
        Node list2 = list;
        while (true) {
            if (list1 == null || list2 == null || list2.next == null) {
                return false;
            }
            list1 = list1.next;
            list2 = list2.next.next;
            if (list1 == list2) {
                return true;
            }
        }
    }

    @Test
    public void testGetCycleLength() {
        Node list = createCycleChain();
        System.out.println(getCycleLength(list));
    }

    /**
     * 获取环的长度
     */
    public static int getCycleLength(Node list) {
        Node list1 = list;
        Node list2 = list;
        while (true) {
            if (list1 == null || list2 == null || list2.next == null) {
                return -1;
            }
            list1 = list1.next;
            list2 = list2.next.next;
            if (list1 == list2) {
                break;
            }
        }

        int count = 0;
        while (true) {
            list1 = list1.next;
            list2 = list2.next.next;
            count++;
            if (list1 == list2) {
                return count;
            }
        }
    }

    @Test
    public void testGetPortalNode() {
        Node list = createCycleChain();
        System.out.println(getPortalNode(list));
    }

    /**
     * 获取环入口节点
     */
    public static Node getPortalNode(Node list) {
        Node list1 = list;
        Node list2 = list;
        while (true) {
            if (list1 == null || list2 == null || list2.next == null) {
                return null;
            }
            list1 = list1.next;
            list2 = list2.next.next;
            if (list1 == list2) {
                break;
            }
        }

        //list2重头开始，一步一个开始跑
        list2 = list;
        while (true) {
            list1 = list1.next;
            list2 = list2.next;
            if (list1 == list2) {
                return list1;
            }
        }
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
        Node list = null;
        for (int i = 5; i >= 1; i--) {
            list = new Node(i, list);
        }
//        for (int i = 1; i <= 5; i++) {
//            System.out.println("删除倒数第" + i + "个，数据节点为：" + deleteNode(list, i).data);
//        }
        Node node = deleteNode(list, 3);
        System.out.println(node);
    }

    /**
     * 删除链表倒数第 n 个结点
     */
    public static Node deleteNode(Node list, int n) {
        Node before = null;
        Node after = null;
        Node deleteNode = null;
        Node slow = list;
        if (n <= 0 || list == null) {
            return null;
        }
        Node fast = list;
        for (int i = 0; i < n; i++) {
            if (fast == null) {
                return null;
            }
            fast = fast.next;
        }

        while (true) {
            if (fast == null) {
                break;
            }
            before = slow;
            slow = slow.next;
            fast = fast.next;
        }
        deleteNode = slow;
        after = deleteNode.next;

        if (before == null) {
            list = list.next;
        } else {
            before.next = after;
        }

        return list;
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
