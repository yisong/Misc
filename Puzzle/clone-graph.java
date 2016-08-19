/*

克隆一张无向图，图中的每个节点包含一个 label 和一个列表 neighbors。

数据中如何表示一个无向图？http://www.lintcode.com/help/graph/

你的程序需要返回一个经过深度拷贝的新图。这个新图和原图具有同样的结构，并且对新图的任何改动不会对原图造成任何影响。

*/

/**
 * Definition for undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     ArrayList<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
 * };
 */
public class Solution {
    /**
     * @param node: A undirected graph node
     * @return: A undirected graph node
     */
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        // write your code here
        
        if (node == null) {
            return null;
        }
        
        Map<Integer, UndirectedGraphNode> m = new HashMap<Integer, UndirectedGraphNode>();
        Queue<UndirectedGraphNode> q = new LinkedList<UndirectedGraphNode>();
        Set<Integer> s = new HashSet<Integer>();
        
        q.add(node);
        s.add(node.label);
        
        while(!q.isEmpty()){
            UndirectedGraphNode n = q.remove();
            UndirectedGraphNode v = m.get(n.label);
            
            if (v == null) {
                v = new UndirectedGraphNode(n.label);
                m.put(n.label, v);
            }

            for (UndirectedGraphNode child : n.neighbors) {
                UndirectedGraphNode bc = m.get(child.label);
                if (bc == null) {
                    bc = new UndirectedGraphNode(child.label);
                    m.put(child.label, bc);
                }
                
                v.neighbors.add(bc);
                if (!s.contains(child.label)) {
                    q.add(child);
                    s.add(child.label);
                }
            }
        }
        
        return m.get(node.label);
        
    }
}
