package com.zk.wangZheng;

import java.util.Random;

/**
 * 跳表的一种实现方法。 跳表中存储的是正整数，并且存储的是不重复的。
 *
 * Author：ZHENG
 */
public class SkipList {

  //（原始数据层 + 索引层数）<= 16
  private static final int MAX_LEVEL = 16;

  // 当前索引的最高层数。初始时只有原始数据层，即1层。
  private int levelCount = 1;

  // 带头链表。
  private Node head = new Node();

  private Random r = new Random();

  public Node find(int value) {
    Node p = head;
    for (int i = levelCount - 1; i >= 0; --i) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
    }

    if (p.forwards[0] != null && p.forwards[0].data == value) {
      return p.forwards[0];
    } else {
      return null;
    }
  }

  public void insert(int value) {
    //随机获取该节点索引层的高度
    int randomLevel = randomLevel();
    //申明一个新的插入节点
    Node newNode = new Node();
    newNode.data = value;
    newNode.maxLevel = randomLevel;

    //更新当前最高的索引层数
    if (levelCount < randomLevel) {
      levelCount = randomLevel;
    }

    //申明一个数组，用于储存索引层中NewNode的前面一个节点
    Node[] prevNewNodeContainer = new Node[randomLevel];
    //从上至下，寻找每一层的pervNode，放入prevNewNodeContainer中
    Node currentNode = head;
    for (int i = levelCount - 1; i >= 0; --i) {
      //循环寻找每一层中newNode的prevNode。
      while (currentNode.forwards[i] != null && currentNode.forwards[i].data < value) {
        currentNode = currentNode.forwards[i];
      }
      //将找到的prevNode放到prevNewNodeContainer中
      if (i < randomLevel) {
        prevNewNodeContainer[i] = currentNode;
      }
    }

    //插入NewNode
    for (int i = 0; i < randomLevel; ++i) {
      newNode.forwards[i] = prevNewNodeContainer[i].forwards[i];
      prevNewNodeContainer[i].forwards[i] = newNode;
    }
  }

  public void delete(int value) {
    Node[] update = new Node[levelCount];
    Node p = head;
    for (int i = levelCount - 1; i >= 0; --i) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      update[i] = p;
    }

    if (p.forwards[0] != null && p.forwards[0].data == value) {
      for (int i = levelCount - 1; i >= 0; --i) {
        if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
          update[i].forwards[i] = update[i].forwards[i].forwards[i];
        }
      }
    }
  }

  /**
   * 思路：采用抛硬币方式。假如正面，则向后面一层。依次下去，只要抛到反面就停止 数据插入概率如下：
   *
   * 第0层概率：1。 第一层概率：1/2 第二层概率：(1/2)^2 第k层概率：(1/2)^k
   */
  private int randomLevel() {
    int level = 1;
    for (int i = 1; i < MAX_LEVEL; ++i) {
      if (r.nextInt() % 2 == 1) {//假设偶数表示正面
        level++;
      } else {
        return level;//奇数表示反面
      }
    }
    return level;
  }

  public void printAll() {
    Node p = head;
    while (p.forwards[0] != null) {
      System.out.println(p.forwards[0] + " ");
      p = p.forwards[0];
    }
    System.out.println();
  }

  //注意：原始数据层是0。第一级索引即maxLevel=1
  public class Node {

    //数据
    private int data = -1;
    //存放了每个索引层中该节点的后面一个节点。forwards[0]这一层是原始数据层
    private Node[] forwards = new Node[MAX_LEVEL];
    //表示该node出现的索引层范围为[0, maxLevel-1]
    private int maxLevel = 0;

    @Override
    public String toString() {
      return "Node{" +
          "data=" + data +
          ", maxLevel=" + maxLevel +
          '}';
    }
  }

}