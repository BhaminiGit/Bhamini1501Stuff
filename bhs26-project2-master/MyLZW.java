import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;

/*************************************************************************
 * Compilation: javac LZW.java Execution: java LZW - < input.txt (compress)
 * Execution: java LZW + < input.txt (expand) Dependencies: BinaryIn.java
 * BinaryOut.java
 *
 * Compress or expand binary input from standard input using LZW.
 *
 * WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING METHOD TAKES
 * TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED SUBSTRING (INSTEAD OF
 * CONSTANT SPACE AND TIME AS IN EARLIER IMPLEMENTATIONS).
 *
 * See <a href =
 * "http://java-performance.info/changes-to-string-java-1-7-0_06/">this
 * article</a> for more details.
 *
 *************************************************************************/

public class MyLZW {
	private static int R = 256; // number of input chars
	private static int W = 9; // codeword width //// *****PART ONE CHANGES***** ////
	private static int L = (int) Math.pow(2, (double) W); // number of codewords = 2^W //// *****PART ONE CHANGES*****
															// ////
	

	public static void compress(char choice) throws IOException  {
		
		double uncompressed = 0, compressed = 0;
	
		double newRatio = 0.0, oldRatio = 0.0;
	
			if(choice == 'r')
			{
				BinaryStdOut.write('r', W); //adding the mode to the beginning of the file if its r or m
			}
			if(choice == 'm')
				BinaryStdOut.write('m', W);
			
			
			String input = BinaryStdIn.readString();
	        TST<Integer> st = new TST<Integer>();
	        for (int i = 0; i < R; i++)
	            st.put("" + (char) i, i);
	        int code = R+1;  // R is codeword for EOF

	        while (input.length() > 0) {
	        	
	            String s = st.longestPrefixOf(input);  // Find max prefix match s.
	            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
	            int t = s.length();
	            
				compressed += (t * W); // value for ratio
				
		         

	            if (t < input.length() && code < L)    // Add s to symbol table.
	            {
	            	//newRatio = uncompressed / compressed;
	            	
	            	 uncompressed += (input.substring(0, t ).length() * 8);  //value for ratio
	                st.put(input.substring(0, t + 1), code++);
	               
	            }
	            
	            else if(t < input.length() && code >= L)
	            {
	            	if(W < 16)
	            	{ 
	            		//System.out.println("HELLLLOOOO");
	            		W++;
	            		L = (int) Math.pow(2, (double) W);
	            		
	            		uncompressed += input.substring(0, t ).length()* 8;  //value for ratio
	            		st.put(input.substring(0, t + 1), code++);
	            	}
	            	else if(W==16 && code >= L) //CODE BOOK IS FULL
	            	{
	            		if(choice == 'r') //RESET MODE IF R IS TYPED
	            		{
	            			W = 9;
							L = (int) Math.pow(2, (double) W);
							st = new TST<Integer>(); 
							
							for (int i = 0; i < R; i++)
								st.put("" + (char) i, i);
							code = R + 1; // R is codeword for EOF aka end of file
							
							
							st.put(input.substring(0, t + 1), code++);
	            		}
	            		else if(choice == 'm') //MONITOR MODE
	            		{
	            			newRatio = uncompressed / compressed;
	            			
	            				                        
	            			
	            			if(oldRatio/newRatio > 1.1)
							{
								
								W = 9;
								L = (int) Math.pow(2, (double) W);
								st = new TST<Integer>();
								for (int i = 0; i < R; i++)
									st.put("" + (char) i, i);
								code = R + 1; // R is codeword for EOF aka end of file
								
								
								st.put(input.substring(0, t + 1), code++);
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
	            }
	            
	            uncompressed += (input.substring(0, t ).length() * 8);
	            
	            input = input.substring(t);            // Scan past s in input.
	        }
	        BinaryStdOut.write(R, W);
	        BinaryStdOut.close();

	}

	public static void expand() throws IOException {
		
		
		double uncompressed = 0, compressed = 0;
		
		double newRatio = 0.0, oldRatio = 0.0;
	

        String[] st = new String[66000];  //code book
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
        int codeword;
            
        
        int letterChoose = BinaryStdIn.readInt(W); //chooseLetter
        
        if(letterChoose == 114 || letterChoose == 109)
        	codeword = BinaryStdIn.readInt(W);
        	
        else
        	codeword = letterChoose;
        
  
       
        
        
        
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword]; 
        
       
        
        while (true) {
        	
            BinaryStdOut.write(val);
            
            compressed += val.length() *W;  //value for ratio
            
                    
            
            if(i >= L)
            {
            	if(W <16)
            	{
            		W++;
            		L = (int) Math.pow(2, (double) W);
            	}
            	else if(W == 16 && i == L) {
            		if(letterChoose == 114)  //RESET MODE
            		{
            		

            			W = 9;
						L = (int) Math.pow(2, (double) W);
						st = new String[66000];
						
						for (i = 0; i < R; i++)
        		            st[i] = "" + (char) i;
        		        st[i++] = "";                        // (unused) lookahead for EOF
        		         i = R+1;
            			
            		}
            		else if(letterChoose == 109) //MONITOR MODE
            		{
                        newRatio = uncompressed/compressed;
                        
                    
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
            }
            
                      
            codeword = BinaryStdIn.readInt(W);
            
            if (codeword == R) break;
            
            String s = st[codeword];
            
            if (i == codeword) 
            	s = val + val.charAt(0);   // special case hack
            
            
           // System.out.println(i);
            if (i < L) 
            {
            	st[i++] = val + s.charAt(0);
            	String hello = val + s.charAt(0);
            	
            	uncompressed += hello.length() * 8;
            	
            }

            val = s;
        }
        BinaryStdOut.close();

	}

	
	public static void main(String[] args) throws IOException {
		if (args[0].equals("-")) {
			
			compress(args[1].charAt(0));

		} else if (args[0].equals("+"))
			expand();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}

	/*
	
	*/

}
