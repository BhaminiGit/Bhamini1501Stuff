import java.util.*;

import java.io.*;
import java.nio.file.Files;

public class ac_test {

	public static void main(String[] args) throws IOException {
		
		ArrayList<Object[]> userIn = new ArrayList<Object[]>();
		
		MyWord testing = createDLB("dictionary.txt");
		
		File user = new File("user_history.txt");
		double totalTime = 0.0;
		int totalCounter = 0;
		
		if(!user.exists()) {
			user.createNewFile();
		}
			
		Scanner fromFile = new Scanner (user);
		Object[] userTempArr = new Object[2];
		String userTempStr = "";
		int dollar;
		int countFromFile = 0;
		
		while(fromFile.hasNextLine()) {
			userTempStr = fromFile.nextLine();
			
			dollar = userTempStr.indexOf('$');
			userTempArr = new Object[2];
			userTempArr[0] = userTempStr.substring(0,dollar) ;
		
			userTempArr[1] = Integer.parseInt(userTempStr.substring(dollar+1,userTempStr.length()));
			
			userIn.add(userTempArr);
			countFromFile++;
			//System.out.println(userTempArr[0] + " " + userTempArr[1]);
		}	
	
		Scanner sc = new Scanner(System.in);
		String entered = "";
		String soFar = "";
		char roundChar = ' ';
		char finalChar = ' ';
		
		
		long startTime, estiTime;
		
		do {
			soFar = "";
			System.out.print("Enter your first character "+ soFar + "_:");
			entered = sc.nextLine();
			
			soFar += entered;
			// end program
			if(entered.equals("!"))
			{	entered = "";
				break;				
			}
			do {
				
				if(entered.equals("$"))
					break;
				// present user with list of predictions
				
				startTime = System.nanoTime();
				ArrayList<String> theList = predictions(soFar, testing,analyzeUser(soFar, userIn));
				estiTime = System.nanoTime()- startTime;
				double seconds = (double)estiTime / 1000000000.0;
				totalTime += seconds;
				totalCounter++;
				System.out.println("\n(" + seconds+ " s)");
				
				if(theList == null)
				{
					System.out.println("No words in dictionary that match. Press $ when you've finished typing the word");
					
				}
				else 
				{
					System.out.println("Predictions");
					for(int i = 0; i < theList.size(); i++)
					{
						System.out.print((i+1) + "). " + theList.get(i) + "       ");
					}
				}
				
				
				System.out.print("\n\nEnter your next character "+ soFar + "_:");
				entered = sc.nextLine();
				soFar += entered;
				
				
				if(entered.equals("$"))
				{
					soFar = soFar.substring(0,soFar.length()-1);
					System.out.println("\n\n WORD COMPLETED: " + soFar);	
					
					break;// end program
				}
				else if(entered.equals("!")) {
					entered = "";
					finalChar = '+';
					break;
				}
				

				else if(entered.equals("1") ||entered.equals("2") ||entered.equals("3") ||entered.equals("4") ||entered.equals("5")) {
					int num = Integer.parseInt(entered);
					System.out.println("\n\n WORD COMPLETED: " + theList.get(num-1)+ "\n");	
					soFar = theList.get(num-1);
					break;
				}
				else
					continue;
				
			} while (roundChar == ' ');
			
			
			int y = searchUserInput(userIn,soFar,null);
			if(y == -1) 
			{
				Object[] temp = {soFar, 1};
				userIn.add(temp);
				sortUserInputString(userIn); //inserting into sorted array
			}
			else 
			{
				Object[] temp = userIn.get(y);
				temp[1] = (int)temp[1] + 1;
				userIn.set(y,temp);
			}
			
			//for(int i = 0; i< userIn.size(); i++) 
				//System.out.println(userIn.get(i)[0] + " " + userIn.get(i)[1]);
			
			
		}while(finalChar == ' ');
			
	
		FileWriter fw = new FileWriter(user);
		String allWords = "";
		for(int i = 0; i< userIn.size(); i++)
		{
			allWords += (userIn.get(i)[0] + "$" + userIn.get(i)[1] + "\n");
		}
		fw.write(allWords);
		fw.flush();
		fw.close();
		
		System.out.println("Average time: " + totalTime/totalCounter + "s");
		sc.close();
	
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static MyWord createDLB(String filename) throws FileNotFoundException {
		Scanner dict = new Scanner(new File(filename));

		// create linked list root for the main roots(first letters of each word)

		MyWord topLayer = new MyWord();

		String temp_string = "";

		while (dict.hasNextLine()) {

			temp_string = dict.nextLine();
			putWord(topLayer, temp_string);

		}
		dict.close();
		return topLayer;

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void putWord(MyWord topLayer, String tempWord) {

		char tempChar = ' ';
		Letter tempLetter = null;

//		System.out.println("\n\n" +tempWord);

		tempLetter = topLayer.head; // set tempLetter to head

		for (int i = 0; i < tempWord.length(); i++) {

			tempChar = tempWord.charAt(i);

//			System.out.println(i + ". Going to add " + tempChar );

			if (topLayer.head == null) // FIRST LETTER
			{
//				System.out.println("toplayer head == null");
				topLayer.head = new Letter(tempChar, i);
				tempLetter = topLayer.head;
//				System.out.println("added" + tempLetter.info);

			} else if (i == 0) // FIRST LETTER
			{
//				System.out.println("i = 0");

				while (tempLetter.info != tempChar) {
					if (tempLetter.nextRoot == null) {
						tempLetter.nextRoot = new Letter(tempChar, i);
//						System.out.println("added" + tempLetter.nextRoot.info);
					}

					tempLetter = tempLetter.nextRoot;

				}

			}

			else // OTHER LETTERS
			{
//				System.out.println("else");

				if (tempLetter.nextLetter == null) // NO NEXT LETTER SO GOTTA CREATE ONE
				{
					tempLetter.nextLetter = new Letter(tempChar, i);
					tempLetter = tempLetter.nextLetter;
//					System.out.println("added" + tempLetter.info);
				} else // WE HAVE A NEXT LETTER BUT WE GOTTA CHEKC IF IT'S RIGHT
				{
					tempLetter = tempLetter.nextLetter; // GOTTA MOVE IT TO THE NEXT LETTER TO CHECK IT

					if (tempLetter.info != tempChar) { // K SO THAT LETTER AINT IT WE GOTTA LOOK AT ALL THE ROOTS
						while (tempLetter.info != tempChar) { // LOOP FOR ^^^^^^

							if (tempLetter.nextRoot == null) // K SO THE ONE WE NEED ISN'T THERE SO LETS MAKE ONE
							{
								tempLetter.nextRoot = new Letter(tempChar, i);
//								System.out.println("added" + tempLetter.nextRoot.info);

							}
							tempLetter = tempLetter.nextRoot; // NOW TEMP IS THE tempChar LETTER THING

						}
					}
				}

			}
//			System.out.println("tempLetter = " + tempLetter.info + "," + tempLetter.index);

		} // WE FINISHED THAT WORD NOW WE GOTTA ADD AN ENDING CHAR
		if (tempLetter.nextLetter == null) {
			tempLetter.nextLetter = new Letter('^', tempWord.length());
		} else {

			tempLetter = tempLetter.nextLetter;
			while (tempLetter.info != '^') {
				if (tempLetter.nextRoot == null) {
					tempLetter.nextRoot = new Letter('^', tempWord.length());
				}
				tempLetter = tempLetter.nextRoot;

			}

		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean contains(String s, MyWord tree) {

		s += '^';

		Letter tempLetter = tree.head;
		char tempChar = ' ';

		for (int i = 0; i < s.length(); i++) {
			tempChar = s.charAt(i);
	//		System.out.println("temp char = " + tempChar);

			if (tempLetter.index == i) { // check the indexes
				// now check the letter
				while (tempLetter != null) {
		//			System.out.println("checking temp char = " + tempChar);
					if (tempLetter.info != tempChar) { // if this letter is not the same, check the next root
						tempLetter = tempLetter.nextRoot; // set tempLetter to next root

						// System.out.println("set tempLetter to next root temp char = " + tempChar);
						if (tempLetter == null) // if the next root is null, then the letter is no where to be
												// found bye FALSE
							return false;

					} else 
					{
						if(tempLetter.info == '^')
							return true;
						tempLetter = tempLetter.nextLetter;
						// System.out.println(tempLetter.info);
						break;
					}

				}

			}

		}

		System.out.println("If this is printing, something went wrong. Check it!");
		return false;
	}

	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ArrayList<String> predictions (String pre, MyWord dict,ArrayList<Object[]> fromHis){
		
		ArrayList<String> words = new ArrayList<String>();
		for(int i = 0; i < fromHis.size(); i++)
		{
			words.add((String)(fromHis.get(i)[0]));
		}
		
		if(words.size() == 5)
		{
			return words;
		}
		
		Letter preEnd = dict.head;
		char tempChar = ' ';
		
		for(int i = 0; i < pre.length(); i++)
		{
		
			tempChar = pre.charAt(i);

			if (preEnd.index == i) { // check the indexes
				// now check the letter
				while (preEnd != null) {
					if (preEnd.info != tempChar) { // if this letter is not the same, check the next root
						preEnd = preEnd.nextRoot; // set tempLetter to next root

						if (preEnd == null) // if the next root is null, then the letter is no where to be
							return null; //no predictions

					} else 
					{
						preEnd = preEnd.nextLetter;
						break;
					}

				}

			}
			
		}
		
		//preEnd is letter after prefix typed 
		Letter tempLetter = preEnd;
		Letter start = preEnd;
		Letter end = preEnd;
		//add letters on to tempString 
		Stack<Letter> stringStack = new Stack<Letter>();
		stringStack.push(tempLetter);
		
		String tempString;	
		String currentWord = pre;
		int size = 5 - words.size();
		for(int i = 0; i <size ; i++) //loop amount of predictions we need from dictionary
		{
			tempString = currentWord;//string
			
			while(end.info != '^') {
				if(end.nextRoot == null)
				{					
					stringStack.push(start);
					
					currentWord += start.info;
					tempString += start.info;
					
					start = start.nextLetter;
					end = start;
				}
				else {
					end = end.nextRoot;
				}
			}
			//tempLetter.info == '^'
			
			if(searchUserInput(null,tempString,words) == -1)
			{
				words.add(tempString);
			}
			
			while(end.nextRoot == null)
			{
				if(stringStack.empty())
				{
					return words;
				}
				end = stringStack.pop();
				currentWord = currentWord.substring(0, currentWord.length()-1);

			}
			
			end = end.nextRoot;
			currentWord += end.info;
			stringStack.push(end);
			
			end = end.nextLetter;
			
			start = end;
			
			
		}
		
		return words;
	}
	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ArrayList<Object[]> analyzeUser (String input,ArrayList<Object[]> list) {
		
		ArrayList<Object[]> sortArr = new ArrayList<Object[]>();
	    for(int i = 0; i < list.size(); i++)
	    {
	    	    String temp = (String)(list.get(i)[0]);
	    	    if(input.length() < temp.length())
	    	    {
	    	    	if(input.equals(temp.substring(0, input.length()))) {
	    	    		if(sortArr.size()<5)
	    	    			sortArr.add(list.get(i));
	    	    	}
	    	    }
	    	    
	    }
	    
	    if(sortArr.size() >0 ) {
	    	sortUserInputInt(sortArr);  //small array 
	    	
	    }
	    
	    return sortArr;
	    
	           
	  
	       
	}
	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void sortUserInputString(ArrayList<Object[]> list) {

		if(list.size() >1 )
			mergeSortString(list);		
	}

	public static void mergeSortString(ArrayList<Object[]> list) {
		 ArrayList<Object[]> Lar = new ArrayList<Object[]>();
		 ArrayList<Object[]> Rar = new ArrayList<Object[]>();
		 
		 if(list.size() !=1)
		 {
			 int mid = list.size()/2;
			 
			 for(int i = 0; i < mid; i++)
			 {
				 Lar.add(list.get(i));
			 }
			 
			 for(int i = mid; i < list.size(); i++)
			 {
				 Rar.add(list.get(i));
			 }
			 
			 mergeSortString(Lar);
			 mergeSortString(Rar);
			 
			 //merging
			 
			 mergingString(Lar,Rar, list);
		 }
	}

	public static void mergingString(ArrayList<Object[]> l, ArrayList<Object[]> r, ArrayList<Object[]> list)
	{
		ArrayList<Object[]> t = new ArrayList<Object[]>();
		
		int ni = 0, li = 0, ri = 0, ti = 0;
		
		while(li < l.size() && ri < r.size()) {
			if(((String)(l.get(li)[0])).compareTo((String)(r.get(ri)[0])) <=0 )
			{
				list.set(ni, l.get(li));
				li++;
			}
			else
			{
				list.set(ni, r.get(ri));
	            ri++;
			}
			ni++;
		}
		
	    if (li >= l.size()) {
	        t = r;
	        ti = ri;
	    } 
	    else {
	        t = l;
	        ti = li;
	    }
	 
	    for (int i = ti; i < t.size(); i++) {
	        list.set(ni, t.get(i));
	        ni++;
	    }
		
		
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static int searchUserInput(ArrayList<Object[]> list, String x, ArrayList<String> sList) {
		//binary search
		List tempList  = null;
		if(list == null)
			tempList = sList;
		else
			tempList = list;
		
        int left = 0, right = tempList.size() - 1; 
        while (left <= right) { 
            int m = left + (right - left) / 2; 
  
            int res;
            if(list == null)
            	res = x.compareTo(sList.get(m));
            else
            	res = x.compareTo((String)(list.get(m)[0])); 
  
            
            if (res == 0) 
                return m; 
  
            if (res > 0) 
                left = m + 1; 
  
            else
                right = m - 1; 
        } 
  
        return -1; 
   
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void sortUserInputInt(ArrayList<Object[]> list)
	{
		if(list.size() >1 )
		{
			mergeSortInt(list);
		}
	}




	public static void mergeSortInt(ArrayList<Object[]> list) {
		 ArrayList<Object[]> Lar = new ArrayList<Object[]>();
		 ArrayList<Object[]> Rar = new ArrayList<Object[]>();
		 
		 if(list.size() !=1)
		 {
			 int mid = list.size()/2;
			 
			 for(int i = 0; i < mid; i++)
			 {
				 Lar.add(list.get(i));
			 }
			 
			 for(int i = mid; i < list.size(); i++)
			 {
				 Rar.add(list.get(i));
			 }
			 
			 mergeSortInt(Lar);
			 mergeSortInt(Rar);
			 
			 //merging
			 
			 mergingInt(Lar,Rar, list);
		 }
	}

	public static void mergingInt(ArrayList<Object[]> l, ArrayList<Object[]> r, ArrayList<Object[]> list)
	{
		ArrayList<Object[]> t = new ArrayList<Object[]>();
		
		int ni = 0, li = 0, ri = 0, ti = 0;
		
		while(li < l.size() && ri < r.size()) {
			if(((int)(l.get(li)[1])) <= ((int)(r.get(ri)[1])) )
			{
				list.set(ni, l.get(li));
				li++;
			}
			else
			{
				list.set(ni, r.get(ri));
	            ri++;
			}
			ni++;
		}
		
	    if (li >= l.size()) {
	        t = r;
	        ti = ri;
	    } 
	    else {
	        t = l;
	        ti = li;
	    }
	 
	    for (int i = ti; i < t.size(); i++) {
	        list.set(ni, t.get(i));
	        ni++;
	    }
		
		
		
	}

}
