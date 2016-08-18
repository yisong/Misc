/*

给出 n 个节点，标号分别从 0 到 n - 1 并且给出一个 无向 边的列表 (给出每条边的两个顶点), 写一个函数去判断这张｀无向｀图是否是一棵树

 注意事项

你可以假设我们不会给出重复的边在边的列表当中. 无向边　[0, 1] 和 [1, 0]　是同一条边， 因此他们不会同时出现在我们给你的边的列表当中。

给出n = 5 并且 edges = [[0, 1], [0, 2], [0, 3], [1, 4]], 返回 true.

给出n = 5 并且 edges = [[0, 1], [1, 2], [2, 3], [1, 3], [1, 4]], 返回 false.

*/

public class Solution {
    /**
     * @param n an integer
     * @param edges a list of undirected edges
     * @return true if it's a valid tree, or false
     */
    public boolean validTree(int n, int[][] edges) {
        // Write your code here
     
        
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        
        for (int i = 0; i < edges.length; i++) {
            int aroot = findroot(array, edges[i][0]);
            int broot = findroot(array, edges[i][1]);
            
            if (aroot == broot) {
                return false;
            } else if (aroot > broot) {
                array[aroot] = broot;
            } else {
                array[broot] = array[aroot];
            }
        }
        
        for (int i = 1; i < n; i++) {
            if (array[i] == i) {
                return false;
            }
        }
        return true;
    }
    
    private int findroot(int[] array, int i) {
        if (array[i] == i) {
            return i;
        }
        return findroot(array, array[i]);
    }
}
