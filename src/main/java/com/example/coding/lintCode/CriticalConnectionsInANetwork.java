package com.example.coding.lintCode;

import java.util.*;

/**
 * @Author 神様だよ
 * @Date 2022/2/9 17:11
 * @Version 1.0
 * @description:
 */
public class CriticalConnectionsInANetwork {
    /**
     * 连通图 的 关键路径（拆了就不连通的那个）
     */
    /**
     * @param connections: connections
     * @return: Critical Connections in a Network
     */
    private Map<Integer, HashSet<Integer>> initGraph(List<List<Integer>> connections) {
        //用哈希表 建立图
        Map<Integer, HashSet<Integer>> graph = new HashMap<>();

        for (List<Integer> connection : connections) {
            graph.putIfAbsent(connection.get(0), new HashSet<>());
            graph.get(connection.get(0)).add(connection.get(1));
            graph.putIfAbsent(connection.get(1), new HashSet<>());
            graph.get(connection.get(1)).add(connection.get(0));
        }
        //返回
        return graph;
    }

    private int dfs(int child, int father, int step, int[] steps, Map<Integer, HashSet<Integer>> graph,
                    List<List<Integer>> res) {
        //层数+1
        int curStep = step + 1;
        //记录当前步数 到数组里
        steps[child] = curStep;
        for (int connection : graph.get(child)) {
            //开始遍历能达到的位置
            if (connection == father) {
                //保证不走回头路
                continue;
                //如果子节点没走过 递归往后走
            } else if (steps[connection] == -1) {
                //答案更新为当前这个环里的最小值
                steps[child] = Math.min(steps[child], dfs(connection, child, curStep, steps, graph, res));
            } else {
                //走过了退出递归 更新当前位置最小值
                steps[child] = Math.min(steps[child], steps[connection]);
            }
        }
        //如果当前节点不是根节点 并且当前位置是新的环入口 更新答案
        if (child != 0 && steps[child] == curStep) {
            //答案数组
            List<Integer> critial = new ArrayList<>();
            if (father > child) {
                int tmp = child;
                child = father;
                father = tmp;
            }

            critial.add(father);
            critial.add(child);
            res.add(critial);
        }
        //返回当前环 的最小值
        return steps[child];
    }

    public List<List<Integer>> criticalConnectionsinaNetwork(int n, List<List<Integer>> connections) {
        //建图
        Map<Integer, HashSet<Integer>> graph = initGraph(connections);
        //记录当前位置的步数数组(环里的最小值)
        int[] steps = new int[n];
        //默认为-1
        Arrays.fill(steps, -1);
        //结果数组
        List<List<Integer>> res = new ArrayList<>();
        //当前位置child 父亲位置father 当前步数step 步数数组steps 图graph 结果res
        dfs(0, -1, 0, steps, graph, res);
        return res;
    }
}
