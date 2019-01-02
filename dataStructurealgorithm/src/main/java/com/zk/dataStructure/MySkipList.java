package com.zk.dataStructure;

import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 * 跳表的一种实现方法。 跳表中存储的是正整数，并且存储的是不重复的。
 *
 * Author：ZHENG
 */
public class MySkipList {

  /**
   * 测试代码
   */
  public static void main(String[] args) {
    long length = 20;//数据个数

    MySkipList skipList = new MySkipList();
    for (int i = 1; i <= length; i++) {
      skipList.insert(i);
    }
    System.out.println("============== 原始数据 ==============");
    skipList.printAll();

    System.out.println("\n============== 测试find ==============\n");
    System.out.println("find(6) = " + skipList.find(6));

    System.out.println("\n============== 测试删除6 ==============\n");
    skipList.delete(6);
    skipList.printAll();
  }

  private static final int MAX_LEVEL = 16;

  private int levelCount = 0;

  private Random r = new Random();

  private SkipListNode head = new SkipListNode(-1, MAX_LEVEL);

  public SkipListNode delete(int val) {
    SkipListNode[] prevNode = new SkipListNode[MAX_LEVEL];
    SkipListNode currentNode = this.head;
    for (int i = levelCount - 1; i >= 0; i--) {
      while (currentNode.forwards[i] != null && currentNode.forwards[i].data < val) {
        currentNode = currentNode.forwards[i];
      }
      prevNode[i] = currentNode;
    }
    if (currentNode.forwards[0] != null && currentNode.forwards[0].data == val) {
      SkipListNode deleteNode = currentNode.forwards[0];
      for (int i = 0; i < deleteNode.maxLevel; i++) {
        SkipListNode deleteNodeLevel = prevNode[i].forwards[i];
        SkipListNode nextNodeLevel = prevNode[i].forwards[i].forwards[i];
        prevNode[i].forwards[i] = nextNodeLevel;
        deleteNodeLevel.forwards[i] = null;
      }
      return deleteNode;
    } else {
      return null;
    }
  }

  public SkipListNode find(int val) {
    SkipListNode currentNode = this.head;
    for (int i = levelCount - 1; i >= 0; i--) {
      while (currentNode.forwards[i] != null && currentNode.forwards[i].data < val) {
        currentNode = currentNode.forwards[i];
      }
    }
    if (currentNode.forwards[0] != null && currentNode.forwards[0].data == val) {
      return currentNode.forwards[0];
    } else {
      return null;
    }
  }

  public void insert(int val) {
    int randomLevel = randomLevel();
    SkipListNode newNode = new SkipListNode(val, randomLevel);

    SkipListNode[] prevNodeContainer = new SkipListNode[randomLevel];
    levelCount = levelCount > randomLevel ? levelCount : randomLevel;
    SkipListNode currentNode = head;
    for (int i = levelCount - 1; i >= 0; i--) {
      while (currentNode.forwards[i] != null && currentNode.forwards[i].data < val) {
        currentNode = currentNode.forwards[i];
      }
      if (i < randomLevel) {
        prevNodeContainer[i] = currentNode;
      }
    }

    SkipListNode[] forwards = newNode.forwards;
    for (int i = 0; i < randomLevel; i++) {
      forwards[i] = prevNodeContainer[i].forwards[i];
      prevNodeContainer[i].forwards[i] = newNode;
    }
  }

  public void printAll() {
    for (int i = levelCount - 1; i >= 0; i--) {
      System.out.print("第" + i + "层：");
      long count = 0;
      SkipListNode forward = head;
      while (forward.forwards[i] != null) {
        count++;
        System.out.print(forward.forwards[i].data + "\t");
        forward = forward.forwards[i];
      }
//      System.out.print("第" + i + "层个数：【" + count + "】");
      System.out.println();
    }

  }

  private int randomLevel() {
    int level = 1;
    for (int i = 1; i < MAX_LEVEL; i++) {
      if (r.nextInt() % 2 == 0) {
        level++;
      } else {
        return level;
      }
    }
    return level;
  }

  @Getter
  @Setter
  private static class SkipListNode {

    private int data;
    private int maxLevel;
    private SkipListNode[] forwards = new SkipListNode[MAX_LEVEL];

    public SkipListNode(int data, int maxLevel) {
      this.data = data;
      this.maxLevel = maxLevel;
    }

    @Override
    public String toString() {
      return "SkipListNode{" +
          "data=" + data +
          ", maxLevel=" + maxLevel +
          '}';
    }
  }
}
