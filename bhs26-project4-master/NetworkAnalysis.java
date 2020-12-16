import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/*
 
 5
0 2 optical 10000 100
0 3 optical 10000 200
1 2 optical 10000 300
1 3 optical 10000 400
0 4 copper 100 800
1 4 copper 100 900
2 4 copper 100 600
3 4 copper 100 700

 
 */


public class NetworkAnalysis {
	
	public static int size;
	public static ArrayList<LinkedList<Edge>> AdjList;
	

	public static void main(String[] args) throws FileNotFoundException {
		
		readFile(args[0]);
//		
//		KruskalMST mst = new KruskalMST(size);
//        for (Edge e : mst.edges()) {
//            System.out.println(e);
//        }
//        System.out.printf("%.5f\n", mst.weight());
//		
        Scanner sc = new Scanner(System.in);
      
        
        int num = 0;
        int v1, v2;
        while(num != 5) {
			System.out.println("\n\nMenu:");
			System.out.println("1. Lowest latency path \n2. Copper only or not"
				+ "\n3. Lowest average latency spanning tree "
				+ "\n4. If any two vertices were to fail,\n   would the graph remain connected?"
				+ "\n5. Quit");
			System.out.println("Choose a number.:");
        	num = Integer.parseInt(sc.next());
        	if(num == 1) {
        		
        		System.out.println("LOWEST LATENCY PATH");
        		
        		System.out.println("Enter the two vertices./nEnter the first vertex:");
        		v1 = Integer.parseInt(sc.next());
        		System.out.println("Ente the second vertex");
        		v2 = Integer.parseInt(sc.next());
        		
        		if (v1 != v2) {
					System.out.println("The lowest latency path is: ");
					lowestLatency(v1, v2);
				}
        		else
        			System.out.println("They have to be different.");
        		
        	}
        	else if(num == 2) {
        		if(copperOnly()== true)
        			System.out.println("Copper-only connected");
        		else 
        			System.out.println("Not copper-only connected");
        	}
        	else if(num == 3) {
        	
        		System.out.println("LOWEST AVERAGE LATENCY SPANNING TREE");    	
	        	KruskalMST mst = new KruskalMST(size);
	        	for (Edge e : mst.edges()) {
	        		System.out.println(e);
	        	}
	        	
        	}
        	else if(num == 4)
        	{
        		if(anyTwo())
        			System.out.println("Will break");
        		else
        			System.out.println("Will not break");
        	}
        	else if(num == 5)
        		System.exit(0);
        	else
        		System.out.println("That number is not valid");
        	//System.out.printf("%.3f\n", mst.weight());
//  		
        }
        
	}
	
	//function copied from EdgeWeightedGraph.java
	public static Iterable<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < size; v++) {
            int selfLoops = 0;
            for (Edge e : AdjList.get(v)) {
                if (e.getotherV() > v) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.getotherV() == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }
	
	public static void readFile(String filename) throws FileNotFoundException {
		
		Scanner fscan = new Scanner(new File(filename));
		size = Integer.parseInt(fscan.nextLine());
		AdjList = new ArrayList<LinkedList<Edge>>();
		
		for(int i = 0; i< size; i++)
			AdjList.add(new LinkedList<Edge>());
		
		
		while(fscan.hasNextLine())
		{
			//0 2 optical 10000 10
			String line = fscan.nextLine();
			Edge temp1 = new Edge(line);
			Edge temp2 = new Edge(line);
			
			//v1 is arrayV
			temp1.setarrayV(temp1.v1);
			temp1.setotherV(temp1.v2);
			AdjList.get(temp1.getarrayV()).add(temp1);
			
			//v2 is arrayV
			temp2.setarrayV(temp2.v2);
			temp2.setotherV(temp2.v1);
			AdjList.get(temp2.getarrayV()).add(temp2);
			
		}		
		
	}
	
	public static void printGraph() {
		
		for(int i = 0; i <size; i++)
		{
			System.out.print(i + ": ");
			Iterator<Edge> iterator= AdjList.get(i).iterator();
		    while(iterator.hasNext()){
		      System.out.print(iterator.next().getotherV() + " ");
		    }
		    System.out.println();
		}
		
	}

	
	
	
	public static void lowestLatency(int x, int y) {
	//	System.out.println("\n----------------------------\nx: " + x + " y: " + y);
		IndexMinPQ<Double> distPQ = new IndexMinPQ<Double>(size);
		Double[] dist = new Double[size];
		Double[] band = new Double[size];
		Integer[] via = new Integer[size];
		boolean[] visited = new boolean[size];
		int cur = x;
		
		distPQ.insert(x, 0.0); //distance from x to x is 0
		dist[x] = 0.0; 
		band[x] = 0.0;
		
		
		
		
		while(visited[y] == false)
		{
			//System.out.println("cur: " + cur);

			//compute tentative distance from x to unvisited neighbor through cur
			Iterator<Edge> iterator= AdjList.get(cur).iterator();
		    while(iterator.hasNext()){
		    	Edge temp = iterator.next();
		    	if(dist[temp.getotherV()] == null) 
		    	{
		    		dist[temp.getotherV()] = dist[cur] + temp.weight ;
		    		band[temp.getotherV()] = band[cur] + temp.length;
		    		distPQ.insert(temp.getotherV(), dist[temp.getotherV()]);
		    		via[temp.getotherV()] = cur;	
		    	}
		    	else
		    	{
		    		if(dist[temp.getotherV()] > dist[cur] + temp.weight)
		    		{
		    			dist[temp.getotherV()] = dist[cur] + temp.weight ;
		    			band[temp.getotherV()] = band[cur] + temp.length;
		    			distPQ.changeKey(temp.getotherV(), dist[temp.getotherV()]);
			    		via[temp.getotherV()] = cur;	

		    		}
		    	}
		    	
//		    	System.out.println(temp.getotherV() + " " + (dist[cur] + temp.weight));
//		    	for(int i = 0; i < size; i++)
//		    	{
//		    		System.out.print(dist[i] + " ");
//		    		
//		    	}
//		    	System.out.println();
		    	
		    }
		    
			
			visited[cur] = true;
			distPQ.delete(cur);
			
			if(cur == y)
				break;
			
			if(distPQ.getN() !=0)
				cur = distPQ.minIndex();
			else
			{
				System.out.println("nope");
				System.exit(0);
			}
			
			
		}	
		
		
		//get path
		
		
		int v = via[y];
		String path = "";
		path += y + "," + v ;
		while(v != x)
		{
			
			v = via[v];
			path += "," + v ;
		}
		String[] arr = path.split(",");
		
		String edges = "";
		int minBand = findEdge(arr[arr.length-1],arr[arr.length-2]).bandW;
		for(int i = arr.length-1; i > 0; i--)
		{
			
			edges += ("(" + arr[i] + "," + arr[i-1] + ")\n");
			if(minBand > findEdge(arr[i], arr[i-1]).bandW)
				minBand = findEdge(arr[i], arr[i-1]).bandW; 
		}
		
		System.out.println(edges);
		System.out.println("Bandwidth available: " + minBand);


	}
	
	public static boolean copperOnly() {
		
		ArrayList<LinkedList<Integer>> adj = new ArrayList<LinkedList<Integer>>();
		for(int i = 0; i<size; i++)
		{
			adj.add(i,new LinkedList<Integer>());
			
			
			Iterator<Edge> iterator= AdjList.get(i).iterator();
			
		    while(iterator.hasNext()){
		    	
		    	Edge temp = iterator.next();
		    	int num = temp.getotherV();
		    	if(temp.type == 'c')
		    		adj.get(i).add(num);
		    	
			}
		}
		
		
		//DepthFirstSearch
		DepthFirstSearch ds = new DepthFirstSearch(adj, size);
			
		return ds.count() == size;
		
		
		
		
		
	}


	public static Edge findEdge(String x, String y)
	{
		int v1 = Integer.parseInt(x);
		int v2 = Integer.parseInt(y);
		Iterator<Edge> iterator= AdjList.get(v1).iterator();
		
	    while(iterator.hasNext()){
	    	Edge temp = iterator.next();
	    	if(v2 == temp.getotherV())
	    		return temp;
	    
		}
	    return null;
		
	}
	
	
	public static boolean articulation(ArrayList<LinkedList<Integer>> al,int size,int remove) {
		Biconnected bi = new Biconnected(al, size,remove);
		if(bi.artiPoints().length()>0)
			return true;
		else
			return false;
	}
	
	
	public static boolean anyTwo()
	{
		
		ArrayList<LinkedList<Integer>> adj = new ArrayList<LinkedList<Integer>>();
		for(int i = 0; i<size; i++)
		{
			adj.add(i,new LinkedList<Integer>());
			
			
			Iterator<Edge> iterator= AdjList.get(i).iterator();
			
		    while(iterator.hasNext()){
		    	
		    	int num = iterator.next().getotherV();
		    	adj.get(i).add(num);
		    	
			}
		}
			
		//check for articulation points
		if(articulation(adj,size,-1) == false) {
			
			for(int i = 0; i< size; i++)
			{
				if(articulation(adj, size, i)== true)
				{
					return true;
				}
			}
			
		}
		else
		{
			return true;
		}
		
		return false;
		
		
		
	}
}
