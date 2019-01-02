//package com.zk.dataStructure;
//
//import lombok.*;
//
//import java.util.Objects;
//
///**
// * 二叉查找树
// */
//public class BinarySearchTree {
//    private BSNote head = null;
//
//    /**
//     * 插入节点一般为叶子节点
//     * 思路：若新节点数据大于当前遍历的节点，且其右子节点不存在，则新节点作为右子节点插入，否则遍历其右子树；
//     * 若新节点数据小于当前遍历的节点，且其左子节点不存在，则新节点作为左子节点插入，否则遍历其左子树；
//     * 若新节点等于当前遍历节点，报错
//     */
//    public void insert(int t) {
//        BSNote newNote = new BSNote(t, null, null);
//        if (head == null) {
//            head = newNote;
//            return;
//        }
//
//        BSNote cur = head;
//        while (true) {
//            if (t == cur.getData()) {
//                throw new RuntimeException("数据已存在");
//            } else if (t > cur.getData()) {
//                if (cur.getRight() == null) {//若新节点数据大于当前节点数据，且当前节点的右子节点为null，则插入节点插入到当前节点的
//                    cur.right = newNote;
//                    return;
//                } else {
//                    cur = cur.getRight();
//                }
//            } else {
//                if (cur.getLeft() == null) {
//                    cur.left = newNote;
//                    return;
//                } else {
//                    cur = cur.getLeft();
//                }
//            }
//        }
//    }
//
//    /**
//     * 3种
//     * @param t
//     */
//    public boolean delete(int t) {
//        if (head == null) return false;
//        BSNote cur = null;
//        BSNote parent = null;
//        if (head.getData() == t) {
//            // TODO: 2018/11/26 删除
//            delete(null, head);
//        } else if (t > head.getData()) {
//            parent = head;
//            cur = parent.right;
//        } else {
//            parent = head;
//            cur = parent.left;
//        }
//        while (true) {
//            if (cur == null) return false;
//            if (t == cur.getData()) {
//                delete(parent, t);
//                return true;
//            } else if (t > cur.getData()) {
//                parent = cur;
//                cur = cur.right;
//            } else {
//                parent = cur;
//                cur = cur.left;
//            }
//        }
//    }
//
//    public boolean delete(BSNote parent, BSNote deleteNode) {
//        Objects.requireNonNull(deleteNode, "入参deleteNode不能为null");
//        BSNote nextNote = null;
//        if (deleteNode.left == null && deleteNode.right == null) {
//
//        } else if (deleteNode.left == null && deleteNode.right != null) {
//            nextNote = deleteNode.right;
//        } else if (deleteNode.left != null && deleteNode.right == null) {
//            nextNote = deleteNode.left;
//        } else {
//            BSNote minParent = deleteNode;
//            BSNote min = deleteNode.right;
//            if (min.left == null) {
//                min.left = parent.left;
//                minParent.left = null;
//                minParent.right = null;
//                nextNote = min;
//            } else {
//                while (min.left != null) {
//                    parent = min;
//                    min = min.left;
//                }
//                //删除min
//                minParent.left = min.right;
//            }
//
//        }
//        if (parent == null) {
//
//        }
//    }
//
//    public BSNote findMin(BSNote note) {
//        if (note == null) return null;
//        BSNote min = note;
//        while (min.getLeft() != null) {
//            min = min.getLeft();
//        }
//        return min;
//    }
//
//    public BSNote findMax(BSNote note) {
//        if (note == null) return null;
//        BSNote max = note;
//        while (max.getRight() != null) {
//            max = max.getRight();
//        }
//        return max;
//
//    }
//
//
//    public BSNote find(int t) {
//        BSNote cur = head;
//        while (true) {
//            if (cur == null) return null;
//            if (t == cur.getData()) {
//                return cur;
//            } else if (t > cur.getData()) {
//                cur = cur.getRight();
//            } else {
//                cur = cur.getLeft();
//            }
//        }
//    }
//
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @ToString
//    @Getter
//    public static class BSNote {
//        private int data;
//        private BSNote left;
//        private BSNote right;
//    }
//}
