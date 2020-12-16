
public class LZW {
	
	  private static  int R = 256;        // number of input chars
	   private static  int W = 9;         // codeword width
	    private static  int  L = (int) Math.pow(2, (double) W);       // number of codewords = 2^W
	    
	 

	    public static void compress() { 
	    	double uncompressed = 0, compressed = 0;
	    	
			double newRatio = 0.0, oldRatio = 0.0;
			
	        String input = BinaryStdIn.readString();
	        TST<Integer> st = new TST<Integer>();
	        for (int i = 0; i < R; i++)
	            st.put("" + (char) i, i);
	        int code = R+1;  // R is codeword for EOF

	        while (input.length() > 0) {
	            String s = st.longestPrefixOf(input);  // Find max prefix match s.
	            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
	            int t = s.length();
	            
	            
				uncompressed += s.length() * 16;
				compressed += W;
				newRatio = uncompressed / compressed;
				
	            if (t < input.length() && code < L)    // Add s to symbol table.
	                st.put(input.substring(0, t + 1), code++);
	            
	            
	            
	            
	            
	            else if(t < input.length() && code >= L)
	            {
	            	if(W < 16)
	            	{ 
	            		//System.out.println("HELLLLOOOO");
	            		W++;
	            		L = (int) Math.pow(2, (double) W);
	            		st.put(input.substring(0, t + 1), code++);
	            	}
	            	else if(W==16 && code >= L)
	            	{
	            	/*	
            		
            			W = 9;
						L = (int) Math.pow(2, (double) W);
						st = new TST<Integer>();
						for (int i = 0; i < R; i++)
							st.put("" + (char) i, i);
						code = R + 1; // R is codeword for EOF aka end of file
						
						
						st.put(input.substring(0, t + 1), code++);*/
	            		
	            		
            		
	            	}
	            }
	            input = input.substring(t);            // Scan past s in input.
	        }
	        BinaryStdOut.write(R, W);
	        BinaryStdOut.close();
	    } 
	    

	    public static void expand() {
	    	double uncompressed = 0, compressed = 0;
	    	
			double newRatio = 0.0, oldRatio = 0.0;
	        String[] st = new String[66000];
	        int i; // next available codeword value

	        // initialize symbol table with all 1-character strings
	        for (i = 0; i < R; i++)
	            st[i] = "" + (char) i;
	        st[i++] = "";                        // (unused) lookahead for EOF

	        int codeword = BinaryStdIn.readInt(W);
	        
	        compressed += W;
	        
	        if (codeword == R) return;           // expanded message is empty string
	        String val = st[codeword];

	        while (true) {
	            BinaryStdOut.write(val);
	            uncompressed += val.length()*16;
	            newRatio = uncompressed/compressed;
	            
	            if(i >= L)
	            {
	            	if(W <16)
	            	{
	            		W++;
	            		L = (int) Math.pow(2, (double) W);
	            	}
	            	else if(W == 16 && i == L) 
	            	{
	            	/*	
            			

            			W = 9;
						L = (int) Math.pow(2, (double) W);
						st = new String[66000];
						
						for (i = 0; i < R; i++)
        		            st[i] = "" + (char) i;
        		        st[i++] = "";                        // (unused) lookahead for EOF
        		         i = R+1;
	            			
	            	*/
	            		if(oldRatio/newRatio > 1.1)
						{
							//reset
							
							W = 9;
							L = (int) Math.pow(2, (double) W);
							st = new String[66000];
							
							for (i = 0; i < R; i++)
            		            st[i] = "" + (char) i;
            		        st[i++] = "";                        // (unused) lookahead for EOF
            		         
            		        
							
							compressed =0;
							uncompressed = 0;
							oldRatio = 0;
						
							
						}
						else
						{
							oldRatio = newRatio;
							
						}
	            	}
	            }
	            
	            
	            codeword = BinaryStdIn.readInt(W);
	            if (codeword == R) break;
	            String s = st[codeword];
	            if (i == codeword) s = val + val.charAt(0);   // special case hack
	            if (i < L) st[i++] = val + s.charAt(0);
	            val = s;
	        }
	        BinaryStdOut.close();
	    }



	    public static void main(String[] args) {
	        if      (args[0].equals("-")) compress();
	        else if (args[0].equals("+")) expand();
	        else throw new IllegalArgumentException("Illegal command line argument");
	    }
}
