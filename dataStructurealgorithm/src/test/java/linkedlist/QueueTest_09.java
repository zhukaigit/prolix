package linkedlist;

import com.zk.dataStructure.ArrayCycleQueue_09;
import com.zk.dataStructure.ArrayQueue_09;
import com.zk.dataStructure.LinkedQueue_09;
import org.junit.Test;

public class QueueTest_09 {

    @Test
    public void testArrayQueue() {
        ArrayQueue_09<Integer> queue = new ArrayQueue_09<>(5);
        for (int i = 1; i <= 5; i++) {
            queue.enqueue(i);
            System.out.println("入队：" + i);
        }
        System.out.println("放入6：" + queue.enqueue(6));
        System.out.println("取出一个：" + queue.dequeue());
        System.out.println("取出一个：" + queue.dequeue());
        System.out.println("取出一个：" + queue.dequeue());
        System.out.println("放入7：" + queue.enqueue(7));
        System.out.println(queue);
    }

    @Test
    public void testArrayCycleQueue() {
        ArrayCycleQueue_09<Integer> queue = new ArrayCycleQueue_09<>(5);
        for (int i = 1; i <= 6; i++) {
            queue.enqueue(i);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        for (int i = 6; i <= 10; i++) {
            queue.enqueue(i);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        for (int i = 10; i <= 15; i++) {
            queue.enqueue(i);
        }
        System.out.println(queue);
    }

    @Test
    public void testLinkedQueue() {
        LinkedQueue_09<Object> queue = new LinkedQueue_09<>();
        for (int i = 1; i <= 5; i++) {
            queue.enqueue(i);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        for (int i = 6; i <= 10; i++) {
            queue.enqueue(i);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        for (int i = 11; i <= 15; i++) {
            queue.enqueue(i);
        }
        System.out.println(queue);
    }
}
