package linkedlist;

import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode;
import com.zk.dataStructure.LinkedHeap;
import com.zk.dataStructure.LinkedHeap.Classify;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import org.junit.Test;

/**
 * 堆的应用测试
 */
public class LinkedHeapTest {

  // 寻找中位数
  @Test
  public void test1() {
    HeapForMiddle myList = new HeapForMiddle();
    for (int i = 0; i < 10; i++) {
      myList.insert(new Random().nextInt(100));
    }
    System.out.println(myList.list);
    Collections.sort(myList.list);
    System.out.println(myList.list);
    System.out.println(myList.getMiddle());
  }


  @Getter
  private static class HeapForMiddle {
    // 后半部分存在小顶堆
    private LinkedHeap smallHeap = new LinkedHeap(Classify.SMALL);
    // 前半部分存在大顶堆
    private LinkedHeap bigHeap = new LinkedHeap(Classify.BIG);
    // 主要用来记录存储的数据，用于测试，无实际用途
    private List list = new ArrayList();

    public void insert(int val) {
      list.add(val);
      if (bigHeap.getSize() == 0) {
        bigHeap.insert(val);
        return;
      }
      if (val < bigHeap.getTopValue()) {
        bigHeap.insert(val);
      } else {
        smallHeap.insert(val);
      }

      while (!(bigHeap.getSize() - smallHeap.getSize() == 0
          || bigHeap.getSize() - smallHeap.getSize() == 1)) {
        if (bigHeap.getSize() > smallHeap.getSize()) {
          smallHeap.insert(bigHeap.deleteHeapTop());
        } else {
          bigHeap.insert(smallHeap.deleteHeapTop());
        }
      }
    }

    public int getMiddle() {
      return bigHeap.getTopValue();
    }

  }

  // 求topK
  @Test
  public void testTopK() {
    HeapForTopK list = new HeapForTopK(4);
    for (int i = 0; i < 20; i++) {
      System.out.printf("============= i = %s ===========\n", i);
      list.insert(new Random().nextInt(100));
      Collections.sort(list.list);
      System.out.println(list.list);
      list.printTopK();
    }

  }

  @Getter
  private static class HeapForTopK {
    // 最大的k个元素存在小顶堆
    private LinkedHeap smallHeap = new LinkedHeap(Classify.SMALL);
    // 其余部分存在大顶堆
    private LinkedHeap bigHeap = new LinkedHeap(Classify.BIG);
    private int k;

    public HeapForTopK(int k) {
      this.k = k;
    }

    // 主要用来记录存储的数据，用于测试，无实际用途
    private List list = new ArrayList();

    public void insert(int val) {
      list.add(val);
      if (smallHeap.getSize() < k) {
        smallHeap.insert(val);
        return;
      }

      if (val > smallHeap.getTopValue()) {
        smallHeap.insert(val);
        bigHeap.insert(smallHeap.deleteHeapTop());
      } else {
        bigHeap.insert(val);
      }

    }

    public void printTopK() {
      smallHeap.print();
    }

  }

}
