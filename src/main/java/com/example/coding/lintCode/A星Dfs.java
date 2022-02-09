package com.example.coding.lintCode;

import java.util.*;

/**
 * @Author 神様だよ
 * @Date 2022/2/9 17:31
 * @Version 1.0
 * @description:
 */
//g 到起点的花费 f为到终点和到起点的估计价值总和(g+h)
class State {
    int g, f;

    State(int g_in, int f_in) {
        g = g_in;
        f = f_in;
    }
}

public class A星Dfs {
    /**
     * @param init_state:  the initial state of chessboard
     * @param final_state: the final state of chessboard
     * @return: return an integer, denote the number of minimum moving
     */
    //入口函数
    public int minMoveStep(int[][] init_state, int[][] final_state) {
        //初始状态
        String source = matrixToString(init_state);
        //目标状态
        String target = matrixToString(final_state);
        //状态检查函数
        if (!check(source, target)) {
            return -1;
        }
        //当前地图对应的状态价值
        Map<String, State> open = new HashMap<>();
        //当前地图是否已经走过了
        Set<String> close = new HashSet<>();
        //初始化状态
        open.put(source, new State(0, getH(source, target)));
        //哈希表模拟队列
        while (open.size() > 0) {
            //寻找当前拥有状态中最小的估价起点
            String cur = findMin(open);
            //是答案就返回
            if (cur.equals(target)) {
                return open.get(cur).g;
            }
            //标记为遍历过
            close.add(cur);
            //遍历当前状态能到达的所有状态
            for (String next : getNext(cur)) {
                //当前状态没走过
                if (!close.contains(next)) {
                    //价值函数里没有含有这个key或者next位置的g函数值大于当前位置的价值尝试更换对应key位置状态
                    if (!open.containsKey(next) || open.get(next).g > open.get(cur).g + 1) {
                        //算出状态值
                        open.put(next, new State(open.get(cur).g + 1, open.get(cur).g + 1 + getH(next, target)));
                    }
                }
            }
            //出队列
            open.remove(cur);
        }
        return -1;
    }

    private boolean check(String s, String t) {
        char[] sc = s.toCharArray();
        char[] tc = t.toCharArray();
        int r1 = 0, r2 = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < i; j++) {
                if (sc[i] != '0' && sc[j] != '0' && sc[j] > sc[i]) {
                    r1++;
                }
                if (tc[i] != '0' && tc[j] != '0' && tc[j] > tc[i]) {
                    r2++;
                }
            }
        }
        if (r1 % 2 == r2 % 2) {
            return true;
        }
        return false;
    }

    //寻找当前拥有状态中最小的估价起点
    private String findMin(Map<String, State> open) {
        String ret = "";
        //打擂台找下最小值
        int minF = Integer.MAX_VALUE;
        //遍历所有key
        for (String key : open.keySet()) {
            //拿出对应状态的f值找到最小的
            if (open.get(key).f < minF) {
                minF = open.get(key).f;
                ret = key;
            }
        }
        return ret;
    }

    //二维转换成一维容易处理
    private String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(matrix[i][j]);
            }
        }
        return sb.toString();
    }

    //H的估价函数
    private int getH(String cur, String target) {
        //判断两个状态之间的距离价值
        int ret = 0;
        //两个状态(字符串) 长度 为9
        for (int i = 0; i < 9; i++) {
            if (target.charAt(i) != '0') {
                //拿出当前cur字符串i位置下字符 对应目标位置字符所在的下标
                int idx = cur.indexOf(target.charAt(i));
                //当前状态一维小标转2维下标
                int cx = idx / 3;
                int cy = idx % 3;
                //目标状态一维小标转2维下标
                int tx = i / 3;
                int ty = i % 3;
                //横纵坐标赌赢相减绝对值和作为估价函数答案
                ret += Math.abs(cx - tx) + Math.abs(cy - ty);
            }
        }
        //返回估价值
        return ret;
    }

    //找到当前二维数组能到达的所有状态
    private List<String> getNext(String str) {
        List<String> ret = new ArrayList<>();
        //4个方向数组
        int[] dx = {0, 1, -1, 0};
        int[] dy = {1, 0, 0, -1};
        //找到0的下标
        int zeroIdx = str.indexOf('0');
        //映射到二维的状态
        int x = zeroIdx / 3;
        int y = zeroIdx % 3;
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            //越界返回
            if (nx < 0 || nx >= 3 || ny < 0 || ny >= 3) {
                continue;
            }
            //java String不可变需要先变成char数组
            char[] sc = str.toCharArray();
            //交换0和n 的对应位置值 二维映射成一维
            sc[x * 3 + y] = sc[nx * 3 + ny];
            sc[nx * 3 + ny] = '0';
            ret.add(new String(sc));
        }
        return ret;
    }
}