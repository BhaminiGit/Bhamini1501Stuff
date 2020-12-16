import java.util.ArrayList;
import java.util.LinkedList;

/*******************************************************************************
 *  Compilation:  javac Biconnected.java
 *  Execution:    java Biconnected V E
 *  Dependencies: Graph.java GraphGenerator.java
 *
 *  Identify articulation points and print them out.
 *  This can be used to decompose a graph into biconnected components.
 *  Runs in O(E + V) time.
 *
 ******************************************************************************/

public class Biconnected {
    private int[] low;
    private int[] pre;
    private int cnt;
    private boolean[] articulation;
    private String points = "";
    
    

    public Biconnected(ArrayList<LinkedList<Integer>> AL,int size,int remove) {
        low = new int[size];
        pre = new int[size];
        articulation = new boolean[size];
        for (int v = 0; v < size; v++)
            low[v] = -1;
        for (int v = 0; v < size; v++)
            pre[v] = -1;
        
        for (int v = 0; v < size; v++)
            if(remove != v)
	        	if (pre[v] == -1)
	                dfs(AL, v, v,remove);
    }

    private void dfs(ArrayList<LinkedList<Integer>> AL, int u, int v,int remove) {
        int children = 0;
        pre[v] = cnt++;
        low[v] = pre[v];
        for (int w : AL.get(v)) {
        	if(w != remove) {
        		
        	
	            if (pre[w] == -1) {
	                children++;
	                dfs(AL, v, w,remove);
	
	                // update low number
	                low[v] = Math.min(low[v], low[w]);
	
	                // non-root of DFS is an articulation point if low[w] >= pre[v]
	                if (low[w] >= pre[v] && u != v) {
	                    articulation[v] = true;
	                    points += v;
	
	                }
	            }
	
	            // update low number - ignore reverse of edge leading to v
	            else if (w != u)
	                low[v] = Math.min(low[v], pre[w]);
        	
        	}
        }

        // root of DFS is an articulation point if it has more than 1 child
        if (u == v && children > 1) {
            articulation[v] = true;
            points+= v;
        	
        }
        
    }

    // is vertex v an articulation point?
    public String artiPoints() { return points;}

    
   
    


}
