package linkedlist;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * 链表题目练习
 */
public class CycleNodeTest {

    /**
     * 测试1：判断链表是不是环形
     */
    @Test
    public void testNodeIsCycle() {
        // 环形测试结果
        Node node = generateCycleNode();
        Node meetingNode1 = checkNodeIsCycle(node);
        Assert.assertNotNull(meetingNode1);

        // 非环形测试结果
        Node unCycleNode = generateUnCycleNode();
        Node meetingNode2 = checkNodeIsCycle(unCycleNode);
        Assert.assertNull(meetingNode2);
    }

    /**
     * 判断链表是不是环形
     *
     * 思路：快慢指针从head节点同时出发，若相遇则是环形链表
     *
     * @return 若是环形，返回相遇的节点；否则返回null
     */
    private Node checkNodeIsCycle(Node note) {
        Objects.requireNonNull(note, "入参不能为空");
        Node slow = note;
        Node fast = note;
        while (slow != null && fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {//必须用==，不能用equal。表示是同一个节点
                return slow;
            }
        }
        return null;
    }

    /**
     * 测试2：环中节点的个数
     */
    @Test
    public void testNodeLength() {
        // 环形测试结果
        Node node = generateCycleNode();
        int length = calculateCycleNodeLength(node);
        System.out.println("环的长度：" + length);
        Assert.assertTrue(length > 0);

        // 非环形测试结果
        Node node2 = generateUnCycleNode();
        int length2 = calculateCycleNodeLength(node2);
        Assert.assertTrue(length2 == 0);
    }

    /**
     * 计算环形链表中"环中的节点个数"
     *
     * 思路：快慢指针第一次相遇必然在环中。此时可以说slow指针在fast指针的前面length步
     * （注意：这里的length就是环中节点的个数），由于每一次指针的移动，fast指针与slow
     * 指针的距离都缩小1，那么当两个指针距离缩小到0时，即fast与slow相遇。此时slow走了length步
     */
    private int calculateCycleNodeLength(Node node) {
        // 第一次相遇的节点
        Node meeting = checkNodeIsCycle(node);
        if (meeting == null) {
            // 不是环形链表
            return 0;
        }

        // 推理可知：slow、fast再次相遇后依然在meeting节点，此时slow节点刚好绕着环跑一圈
        Node slow = meeting;
        Node fast = meeting;
        int count = 0;
        do {
            slow = slow.next;
            fast = fast.next.next;
            count++;
        } while (slow != fast);
        return count;
    }

    /**
     * 测试：环形入口节点
     */
    @Test
    public void testGetPortalNode() {
        // 环形测试结果
        Node node = generateCycleNode();
        Node portalNode = getCyclePortalNode(node);
        System.out.println("环形入口节点：" + portalNode);
        Assert.assertNotNull(portalNode);

        // 非环形测试结果
        Node node2 = generateUnCycleNode();
        Node portalNode2 = getCyclePortalNode(node2);
        Assert.assertNull(portalNode2);
    }

    /**
     * 获取环形入口节点
     *
     * 必要知识：
     * 假设：fast指针每次移动2步，slow指针每次移动1步。环中节点个数为length个
     * 1、若slow在环中【节点a】，fast也在环中【节点a】，可以理解为fast需要追赶length步才能与slow再次相遇。
     *      而每一次移动他们距离都会缩小1步，那么意味着需要移动length才会相遇，此时相遇的节点依然是【节点a】；
     * 2、若slow在环中【节点a】，fast在【节点a】的前面的"x个"节点。此时fast距离slow相差"length-x"个节点，
     *      故相遇时，slow节点移动了"length-x"步。即相遇的节点为【节点a】后面的第"x个"节点
     *
     * 思路：
     * 1、fast、slow同时从head节点开始。假设当slow到达环形【入口节点】时，slow移动了【k步】，fast在【入口节点】前面"x步"。
     *      slow移动过的节点数量 * 2 = fast移动过的节点数量  ====>   2*k = k + x + n*length ====> k = x + n*length
     * 2、所以fast、slow相遇的节点在【入口节点】后面的x步。称相遇节点为"meeting节点"
     * 3、根据k = x + n*length知：若让slowA指针从head开始、slowB指针从"meeting节点"开始，两个指针每次都只跑一步。那么当
     *      slowA到达【入口节点】时，slowB在环中走了n圈再加上x步，此时也处在【入口节点】，也就意味着他们相遇了，而且相遇的
     *      节点就是【入口节点】
     */
    private Node getCyclePortalNode(Node node) {
        // 第一次相遇的节点
        Node meeting = checkNodeIsCycle(node);
        if (meeting == null) {
            // 表示不是一个环形链表
            return null;
        }

        Node slowA = node;
        Node slowB = meeting;
        while (slowA != slowB) {
            slowA = slowA.next;
            slowB = slowB.next;
        }
        return slowA;

    }

    @Getter
    @Setter
    private static class Node {
        private String data;
        private Node next;

        public Node(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data='" + data + '\'' +
                    '}';
        }
    }

    /**
     * 生产一个环形链表
     */
    private static Node generateCycleNode() {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        Node g = new Node("g");
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        e.next = f;
        f.next = g;
        // 形成环
//        g.next = a;
//        g.next = b;
//        g.next = c;
//        g.next = d;
        g.next = e;
//        g.next = f;
//        g.next = g;
        return a;
    }

    /**
     * 生产一个非环形链表
     */
    private static Node generateUnCycleNode() {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        a.next = b;
        b.next = c;
        c.next = null;
        return a;
    }
}
