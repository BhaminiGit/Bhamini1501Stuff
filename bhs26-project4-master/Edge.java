import java.util.ArrayList;

public class Edge implements Comparable<Edge> {
	
	/*
	 the line 0 5 optical 10000 25 describes 
	 
	 an edge between vertex 0 and vertex 5 
	 represents a 25 meter long 
	 optical cable 
	 bandwidth of 10 gigabits per second.  10 and 3 zeros

	 */
	
	public int v1;
	public int v2;

	public char type;
	public int bandW;
	public double length;
	public double weight;
	
	private int arrayV= -100000;
	private int otherV = -100000;
	
	public static int copperOnly = 0;
	

	
	public Edge(String line) {
		

		//copper cable at a speed of 230000000 meters per second.
		//optic 200000000 meters per second.
		v1 = Integer.parseInt(line.substring(0,line.indexOf(' ')));
		line = line.substring(line.indexOf(' ')+1);
		v2 = Integer.parseInt(line.substring(0,line.indexOf(' ')));
		line = line.substring(line.indexOf(' ')+1);
		
		type = (line.substring(0,line.indexOf(' '))).charAt(0);
		line = line.substring(line.indexOf(' ')+1);
		
		bandW = Integer.parseInt(line.substring(0,line.indexOf(' ')));
		line = line.substring(line.indexOf(' ')+1);
		
		length = Integer.parseInt(line);
		
//		//divide speed by 1 E 6
		
		if(type == 'c')
		{
			weight = length/2.3;

			if(copperOnly == 0)
				copperOnly = 1;
			
		}
		else if(type == 'o')
		{
			weight = length/2;
			
			copperOnly = 2;
				
		}
		
	
		
		
	}
	
	@Override
	public String toString() {
	
		return "(" + v1 + "," + v2 + ")"; 
	}

	public void setarrayV(int x)
	{
		arrayV = x;
	}
	
	public int getarrayV()
	{
		return arrayV;
	}
	
	
	public void setotherV(int x)
	{
		otherV = x;
	}
	
	public int getotherV()
	{
		return otherV;
	}

	
	 public int other(int vertex) {
	        if      (vertex == v1) return v2;
	        else if (vertex == v2) return v1;
	        else throw new IllegalArgumentException("Illegal endpoint");
	    }

	
	
	@Override
	public int compareTo(Edge o) {
		double w = o.weight;
		double thisw = this.weight;
		
		if(thisw > w)
			return 1;
		else if(w > thisw)
			return -1;
		else
			return 0;
		
	}
	
	
	
    
	
	

}
