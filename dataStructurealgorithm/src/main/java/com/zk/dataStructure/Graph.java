package com.zk.dataStructure;


import java.util.Collections;
import java.util.LinkedList;

// 无向图
public class Graph {

    public static void main(String[] args) {
        Graph g = new Graph(8);
        g.addEdge(0, 1);
        g.addEdge(0, 3);
        g.addEdge(1, 0);
        g.addEdge(1, 2);
        g.addEdge(1, 4);
        g.addEdge(2, 1);
        g.addEdge(2, 5);
        g.addEdge(3, 0);
        g.addEdge(3, 4);
        g.addEdge(4, 1);
        g.addEdge(4, 3);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(5, 2);
        g.addEdge(5, 4);
        g.addEdge(5, 7);
        g.addEdge(6, 4);
        g.addEdge(6, 7);
        g.addEdge(7, 5);
        g.addEdge(7, 6);

//    g.breadthSearch(0, 6);
//        g.depthSearch(0, 6);
        g.search(0, 2);

    }

    private int v; // 顶点的个数
    private LinkedList<Integer>[] adjList; // 邻接表

    public Graph(int v) {
        this.v = v;
        adjList = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int s, int t) { // 无向图一条边存两次
        adjList[s].add(t);
        adjList[t].add(s);
    }

    /**
     * n度好友的寻找
     * @param source
     * @param deep
     */
    public void search(int source, int deep) {
        // 保存即将访问顶点的上一个顶点
        LinkedList<Integer> queue = new LinkedList<>();
        // 记录已访问过的顶点
        boolean[] visited = new boolean[v];
        // 记录已访问路径顶点的父顶点
        Integer[] prev = new Integer[v];

        queue.add(source);
        visited[source] = true;

        // 寻找
        for (int i = 0; i < deep; i++) {
            LinkedList<Integer> subQueue = new LinkedList<>();
            while (queue.size() != 0) {
                Integer ele = queue.poll();
                LinkedList<Integer> nextList = adjList[ele];
                for (Integer next : nextList) {
                    // 访问过则跳过
                    if (visited[next]) continue;

                    visited[next] = true;
                    subQueue.add(next);
                    prev[next] = ele;
                }
            }
            queue.addAll(subQueue);
        }

        // 打印
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                System.out.printf(i + "\t");
            }
        }
    }

    /**
     * 广度优先搜索算法
     *
     * @param source
     * @param target
     */
    public void breadthSearch(int source, int target) {
        if (source == target) return;

        // 保存即将访问顶点的上一个顶点
        LinkedList<Integer> queue = new LinkedList<>();
        // 记录已访问过的顶点
        boolean[] visited = new boolean[v];
        // 记录已访问路径顶点的父顶点
        Integer[] prev = new Integer[v];

        queue.add(source);
        visited[source] = true;

        // 寻找
        outer:
        while (queue.size() != 0) {
            Integer ele = queue.poll();
            LinkedList<Integer> nextList = adjList[ele];
            for (Integer next : nextList) {
                // 访问过则跳过
                if (visited[next]) continue;

                visited[next] = true;
                queue.add(next);
                prev[next] = ele;

                // 找到了目标
                if (next == target) {
                    break outer;
                }
            }
        }

        // 打印
        Integer current = target;
        while (current != null) {
            System.out.print(current + " -> ");
            current = prev[current];
        }
    }

    /**
     * 深度优先搜索算法
     * @param source
     * @param target
     */
    public void depthSearch(int source, int target) {
        boolean[] visited = new boolean[v];
        visited[source] = true;
        Integer[] prev = new Integer[v];
        if (source == target) {
            return;
        }

        int parent = -1;
        prev[source] = parent;
        int current = source;
        while (true) {
            int move = move(parent, current, visited);
            // 回退
            if (parent == move) {
                parent = prev[parent];
            }
            // 前进
            else {
                prev[move] = current;
                visited[current] = true;
                parent = current;
            }
            current = move;

            // 打印
            if (current == target) {
                Integer c = target;
                while (c != null) {
                    System.out.print(c + " -> ");
                    if (c == source) return;
                    c = prev[c];
                }
                return;
            }
        }
    }

    /**
     * 返回当前顶点移动后的顶点。
     *
     * @param prev    当前顶点的前一个顶点
     * @param current 当前所处的顶点
     * @param visited
     * @return
     */
    private int move(int prev, int current, boolean[] visited) {
        LinkedList<Integer> nextList = adjList[current];
        // 制造随机路径
        Collections.shuffle(nextList);
        // 前进
        for (Integer integer : nextList) {
            if (visited[integer]) continue;
            visited[integer] = true;
            return integer;
        }
        // 回退
        return prev;
    }
}

