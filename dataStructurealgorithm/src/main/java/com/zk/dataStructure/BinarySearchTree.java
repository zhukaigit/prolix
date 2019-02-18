package com.zk.dataStructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 二叉查找树
 */
public class BinarySearchTree {
    private BSNode head = null;

  public static void main(String[] args) {
    BinarySearchTree tree = new BinarySearchTree();
    for (int i = 10; i <= 20; i++) {
      tree.insert(i);
    }
    for (int i = 9; i >= 1; i--) {
      tree.insert(i);
    }
    System.out.println("原始数据如下：");
    tree.printAll();

    System.out.println("max : " + tree.findMax(tree.head));
    System.out.println("min : " + tree.findMin(tree.head));
    System.out.println("test find value = 9， node : " + tree.find(9));
    System.out.println("head : " + tree.head);

    System.out.println("============= delete ============");
    for (int i = 2; i < 25; i += 4) {
      boolean delete = tree.delete(i);
      System.out.println("删除" + i + ": " + delete);
      tree.printAll();
    }
  }

    /**
     * 插入节点一般为叶子节点
     * 思路：若新节点数据大于当前遍历的节点，且其右子节点不存在，则新节点作为右子节点插入，否则遍历其右子树；
     * 若新节点数据小于当前遍历的节点，且其左子节点不存在，则新节点作为左子节点插入，否则遍历其左子树；
     * 若新节点等于当前遍历节点，报错
     */
    public void insert(int t) {
      BSNode newNote = new BSNode(t, null, null);
      if (head == null) {
        head = newNote;
        return;
      }

      BSNode cur = head;
      // 终止条件，满足下面任意一个
      boolean b1 = cur != null && t > cur.data && cur.right == null;
      boolean b2 = cur != null && t < cur.data && cur.left == null;
      while (!(b1 || b2)) {
        if (t > cur.data) {
          cur = cur.right;
        } else if (t == cur.data) {
          throw new RuntimeException("数据已存在：" + t);
        } else {
          cur = cur.left;
        }
        b1 = cur != null && t > cur.data && cur.right == null;
        b2 = cur != null && t < cur.data && cur.left == null;
      }

      if (b1) {
        cur.right = newNote;
      } else {
        cur.left = newNote;
      }
    }


  public boolean delete(int obj) {
    BSNode father = this.head;
    if (father == null) return false;

    // 删除头部节点
    if (father.data == obj) {
      BSNode left = head.left;
      BSNode right = head.right;
      if (left == null) {
        head = right;
        return true;
      }
      if (right == null) {
        head = left;
        return true;
      }
      head = left;
      BSNode max = findMax(left);
      max.right = right;
      return true;
    }

    // 循环终止条件
    boolean b1 = false, b2 = false;
    while (father != null) {
      b1 = father.left != null && father.left.data == obj;
      b2 = father.right != null && father.right.data == obj;
      if (b1 || b2) break;
      father = obj > father.data ? father.right : father.left;
    }

    // 删除father的左节点
    if (b1) {
      deleteLeft(father);
      return true;
    }
    // 删除father的右节点
    if (b2) {
      deleteRight(father);
      return true;
    }
    return false;
  }

  /**
   * 删除指定节点的左节点
   */
  private void deleteLeft(BSNode node) {
    BSNode left = node.left.left;
    BSNode right = node.left.right;
    if (left == null) {
      node.left = right;
      return;
    }
    if (right == null) {
      node.left = left;
      return;
    }
    node.left = left;
    BSNode max = findMax(left);
    max.right = right;
  }

  /**
   * 删除指定节点的右节点
   */
  private void deleteRight(BSNode node) {
    BSNode left = node.right.left;
    BSNode right = node.right.right;
    if (left == null) {
      node.right = right;
      return;
    }
    if (right == null) {
      node.right = left;
      return;
    }
    node.right = left;
    BSNode max = findMax(left);
    max.right = right;
  }

  /**
   * 查找指定节点中的最小节点
   * @param node
   * @return
   */
  public BSNode findMin(BSNode node) {
    if (node == null) {
      return null;
    }
    BSNode min = node;
    while (min.getLeft() != null) {
      min = min.getLeft();
    }
    return min;
  }

  /**
   * 查找指定节点中的最大节点
   */
  public BSNode findMax(BSNode node) {
    if (node == null) {
      return null;
    }
    BSNode max = node;
    while (max.getRight() != null) {
      max = max.getRight();
    }
    return max;
  }


  public BSNode find(int t) {
    BSNode cur = head;
    while (cur != null) {
      if (cur.data == t) {
        return cur;
      } else if (cur.data > t) {
        cur = cur.left;
      } else {
        cur = cur.right;
      }
    }
    return null;
  }

  public void printAll() {
    StringBuilder result = new StringBuilder();
    print(head, result);
    System.out.println(result);
  }

  private void print(BSNode node, StringBuilder result) {
    if (node == null) {
      return;
    }
    print(node.left, result);
    result.append(node.data).append("\t");
    print(node.right, result);
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class BSNode {

    private int data;
    private BSNode left;
    private BSNode right;

    @Override
    public String toString() {
      return "BSNode{" +
              "data=" + data +
              '}';
    }
  }
}
