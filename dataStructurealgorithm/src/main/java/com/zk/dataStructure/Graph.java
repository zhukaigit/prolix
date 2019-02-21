package com.zk.dataStructure;


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

    g.breadthSearch(0, 6);

  }

  private int v; // 顶点的个数
  private LinkedList<Integer>[] adjList; // 邻接表

  public Graph(int v) {
    this.v = v;
    adjList = new LinkedList[v];
    for (int i=0; i<v; ++i) {
      adjList[i] = new LinkedList<>();
    }
  }

  public void addEdge(int s, int t) { // 无向图一条边存两次
    adjList[s].add(t);
    adjList[t].add(s);
  }

  /**
   * 广度优先搜索算法
   * @param source
   * @param target
   */
  public void breadthSearch(int source, int target) {
    if (source == target) return;

    // 保存即将访问顶点的上一个顶点
    LinkedList<Integer> queue = new LinkedList<>();
    // 记录已访问过的顶点
    boolean[] visited = new boolean[v];
    // 记录已访问顶点的父顶点
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

  public void depthSearch(int source, int target) {
    boolean[] visited = new boolean[v];
    visited[source] = true;
    if (source == target) {
      return;
    }

    int current = source;
    while (true) {
      LinkedList<Integer> nextList = adjList[current];
      for (Integer integer : nextList) {
        // 是否能走
        if (visited[integer]) continue;
        current = integer;
        break;
      }
    }
  }
}

