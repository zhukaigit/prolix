package com.zk.dataStructure;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LinkedHeap {

  public static void main(String[] args) {
//    LinkedHeap heap = new LinkedHeap(Classify.BIG);
    LinkedHeap heap = new LinkedHeap(Classify.SMALL);
    for (int i = 10; i > 0; i -= 2) {
      heap.insert(i);
    }
    for (int i = 1; i <= 9; i+=2) {
      heap.insert(i);
    }

    while (heap.head != null) {
      System.out.println(heap.deleteHeapTop());
    }
  }

  // 头部节点
  private HeapNode head;
  // 最后一个节点的父节点
  private HeapNode tail;
  // 大小顶堆标志
  private Classify flag;
  @Getter
  private int size;

  public LinkedHeap(Classify flag) {
    this.flag = flag;
  }

  public int getTopValue() {
    return head.data;
  }

  // 插入元素
  public void insert(int data) {
    size++;
    // 头节点判断
    if (head == null) {
      head = new HeapNode(data, null, null, null);
      tail = head;
      return;
    }

    // 插入
    HeapNode nextFather = findNextFather(tail);
    HeapNode newNode = new HeapNode(data, null, null, nextFather);
    if (nextFather.left == null) {
      nextFather.left = newNode;
    } else {
      nextFather.right = newNode;
    }
    tail = newNode;

    // 堆化
    HeapNode current = newNode;
    if (flag == Classify.SMALL) {
      while (current.father != null && current.data < current.father.data) {
        int temp = current.data;
        current.data = current.father.data;
        current.father.data = temp;
        current = current.father;
      }
    } else {
      while (current.father != null && current.data > current.father.data) {
        int temp = current.data;
        current.data = current.father.data;
        current.father.data = temp;
        current = current.father;
      }
    }
  }

  // 删除堆顶元素
  public int deleteHeapTop() {
    size--;
    int result = head.data;

    // 校验是否只有一个元素
    if (head == tail) {
      head = null;
      tail = null;
      return result;
    }

    HeapNode newTail = findPrevNode(tail);

    // *** 删除堆顶 ***
    // 交换tail与堆顶元素
    head.data = tail.data;
    // 删除tail元素
    HeapNode father = tail.father;
    if (father.left == tail) {
      father.left = null;
    } else {
      father.right = null;
    }

    // 更新tail
    tail = newTail;

    // 堆head进行堆化
    HeapNode cur = head;
    if (flag == Classify.SMALL) {
      boolean left = cur.left != null && cur.left.data < cur.data;
      boolean right = cur.right != null && cur.right.data < cur.data;
      while ((left) || (right)) {
        if (right && cur.right.data < cur.left.data) {
          int temp = cur.right.data;
          cur.right.data = cur.data;
          cur.data = temp;
          cur = cur.right;
        } else {
          int temp = cur.left.data;
          cur.left.data = cur.data;
          cur.data = temp;
          cur = cur.left;
        }
        left = cur.left != null && cur.left.data < cur.data;
        right = cur.right != null && cur.right.data < cur.data;
      }
    } else {
      boolean left = cur.left != null && cur.left.data > cur.data;
      boolean right = cur.right != null && cur.right.data > cur.data;
      while ((left) || (right)) {
        if (right && cur.right.data > cur.left.data) {
          int temp = cur.right.data;
          cur.right.data = cur.data;
          cur.data = temp;
          cur = cur.right;
        } else {
          int temp = cur.left.data;
          cur.left.data = cur.data;
          cur.data = temp;
          cur = cur.left;
        }
        left = cur.left != null && cur.left.data > cur.data;
        right = cur.right != null && cur.right.data > cur.data;
      }
    }

    return result;
  }

  /**
   * 寻找指定节点相邻节点的父节点
   * @param node
   * @return
   */
  private HeapNode findNextFather(HeapNode node) {
    HeapNode father = findNextGrandFather(node);

    // 该节点刚好为这一层的最后一个元素
    if (father == null) {
      HeapNode cur = head;
      while (cur.left != null) {
        cur = cur.left;
      }
      return cur;
    }

    // 表示该节点为其父节点的左节点
    if (father.left != null && father.right == null) {
      return father;
    }

    // 表示该节点为其父节点的右节点，且不是最后一个元素
    father = father.right;
    while (father.left != null) {
      father = father.left;
    }
    return father;
  }

  private HeapNode findNextGrandFather(HeapNode node) {
    Position position = nodePosition(node);
    HeapNode father = null;
    switch (position) {
      case LEFT:
        father = node.father;
        break;
      case RIGHT:
        father = findNextGrandFather(node.father);
        break;
      case ROOT:
        break;
    }
    return father;
  }

  /**
   * 寻找指定节点的前一个node
   */
  private HeapNode findPrevNode(HeapNode node) {
    // 表示该节点为其父节点的右节点
    if (nodePosition(node) == Position.RIGHT) {
      return node.father.left;
    }

    HeapNode father = findPrevGrandFather(node);
    // 该节点刚好为这一层的第一个元素
    if (father == null) {
      HeapNode cur = head;
      while (cur.right != null) {
        cur = cur.right;
      }
      return cur;
    }

    // 表示该节点为其父节点的右节点，且不是最后一个元素
    father = father.left;
    while (father.right != null) {
      father = father.right;
    }
    return father;
  }

  private HeapNode findPrevGrandFather(HeapNode node) {
    Position position = nodePosition(node);
    HeapNode father = null;
    switch (position) {
      case LEFT:
        father = findPrevGrandFather(node.father);
        break;
      case RIGHT:
        father = node.father;
        break;
      case ROOT:
        break;
    }
    return father;
  }


  /**
   * @return 返回ROOT：表示该node是根节点；返回LEFT：表示该node是其父节点的左子节点； 返回RIGHT：表示该node是其父节点的右子节点
   */
  private Position nodePosition(HeapNode node) {
    if (node.father == null) {
      return Position.ROOT;
    }
    if (node.father.left == node) {
      return Position.LEFT;
    }
    return Position.RIGHT;
  }


  private enum Position {
    ROOT, // 根节点
    LEFT, // 左节点
    RIGHT // 右节点
  }

  public enum Classify {
    SMALL, // 小顶堆
    BIG // 大顶堆
  }

  public void print() {
    print(head);
  }

  private void print(HeapNode node) {
    if (node == null) {
      return;
    }
    System.out.println(node.data);
    print(node.left);
    print(node.right);
  }

  @AllArgsConstructor
  private static class HeapNode {

    private int data;
    private HeapNode left;
    private HeapNode right;
    private HeapNode father;
  }

}
