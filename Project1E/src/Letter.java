public class Letter {
	
	char info;
	int index;
	Letter nextRoot;
	Letter nextLetter;
	
	Letter(char l, int ind ){
				
		index = ind;	
		info = l;
		nextRoot = null;
		nextLetter = null;
	}

}