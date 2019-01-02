package linkedlist;

import com.zk.dataStructure.ArrayStack_08;
import com.zk.dataStructure.LinkedStack_08;
import org.junit.Test;

public class StackTest_08 {

    @Test
    public void testArrayStack() {
        ArrayStack_08 stack = new ArrayStack_08(10);
        for (int i = 1; i <= 6; i++) {
            stack.push(i);
        }
        System.out.println(stack);
        System.out.println("移除的元素：" + stack.pop());
        System.out.println(stack);
    }

    @Test
    public void testLinkedStack_08() {
        LinkedStack_08 stack = new LinkedStack_08();
        for (int i = 1; i <= 6; i++) {
            stack.push(i);
        }
        System.out.println(stack);
        System.out.println("移除的元素：" + stack.pop());
        System.out.println(stack);
    }



}
