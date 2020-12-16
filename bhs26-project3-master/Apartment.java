


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Apartment implements Comparable<Apartment> {

	public String address;
	public String aptNum;
	public String city;
	public int zip;
	public int rent;
	public int sqFoot;
	public String full;
	public String key;
	
	public static HashMap<String, Integer> apHash = new HashMap<String,Integer>();
	public static IndexMaxPQ<Apartment> sqpq = null;
	public static ApartmentRentMinPQ rentpq = null;
	
	//these are hash maps that take the city name and give the the pq of city
		//it give you a min/max pq of apartments with the same city 
	public static HashMap<String, ApartmentRentMinPQ> minRentCity= new HashMap<String, ApartmentRentMinPQ>();
	public static HashMap<String,IndexMaxPQ<Apartment>> maxSqCity = new HashMap<String,IndexMaxPQ<Apartment>>();
	
	//these array lists have hashmaps where you give the apartment key and it gives you the index of that apartment in it's repecttive city pq
	public static ArrayList<HashMap<String,Integer>> minRentCityHash = new ArrayList<HashMap<String,Integer>>();
	public static ArrayList<HashMap<String,Integer>> maxSqCityHash = new ArrayList<HashMap<String,Integer>>();

	public static int c = 0;
	public static int n = 0;
	
	public Apartment(String address,String aptNum, String city,int zip,int rent, int sqFoot, String key) {
		this.address = address;
		this.aptNum = aptNum;
		this.city = city;
		this.zip = zip;
		this.rent = rent;
		this.sqFoot = sqFoot;
		this.key = key;
		
		this.full =  this.aptNum + " " + this.address+ " " + this.city + " " + this.zip + " r:" + this.rent + " s:" + this.sqFoot;
		
	}
	
	
	
	public Apartment(String line) {
		
		
		
		if(line.indexOf(":") >= 0)
		{
			this.address = line.substring(0, line.indexOf(":"));
			line = line.substring(line.indexOf(":")+1);
		}
		
		if(line.indexOf(":") >= 0)
		{
			this.aptNum = line.substring(0,line.indexOf(":"));
			line = line.substring(line.indexOf(":") +1); 
		}
		
		if(line.indexOf(":") >= 0)
		{
			this.city = line.substring(0,line.indexOf(":"));
			line = line.substring(line.indexOf(":") +1); 
		}
		if(line.indexOf(":") >= 0)
		{
			this.zip = Integer.parseInt(line.substring(0,line.indexOf(":")));
			line = line.substring(line.indexOf(":") +1); 
		}
		
		if(line.indexOf(":") >= 0)
		{
			this.rent = Integer.parseInt(line.substring(0,line.indexOf(":")));
			line = line.substring(line.indexOf(":") +1); 
		}
		
		
		
		this.sqFoot = Integer.parseInt(line);
		
		this.full =  this.aptNum + " " + this.address+ " " + this.city + " " + this.zip + " r:" + this.rent + " s:" + this.sqFoot;
		
		
		
		this.key =  this.address.replaceAll("[ .]","").toUpperCase() + this.zip + this.aptNum.toUpperCase() + "";
			
		
	}
	
	 @Override
	public String toString() { 
	        return this.address + "APT " + this.aptNum + "\n" + this.city + " " + this.zip + "\n Rent: " + this.rent + " SqFoot: " + this.sqFoot; 
	    } 
	 
	public static void setSizeAndIndPQ(int size,int ind) {
		sqpq = new IndexMaxPQ<Apartment>(size,ind);
		rentpq = new ApartmentRentMinPQ(size,ind);

	}
	public static void addApt(Apartment ap) {	
		apHash.put(ap.key, n); //add to hash map
		
		sqpq.insert(n,ap); // add to max pq
		rentpq.insert(n, ap); //add to min pq
		
		//city stuff
		String cityKey = ap.city.toUpperCase();
		
		// min and max city stuff
		if(minRentCity.containsKey(cityKey)) 
		{
			//add said apartment to that pq
			
			//get the pq for that city -->> minRentCity.get(cityKey); 
			//get index of the pq of that city
			int minindexPqCity = minRentCity.get(cityKey).idNum;
			int maxindexPqCity = maxSqCity.get(cityKey).idNum;
			
			//size of the pq of that city -->> where we will be adding the apartment
			int minnOfPq = minRentCity.get(cityKey).size();
			int maxnOfPq = maxSqCity.get(cityKey).size();
			
			//get the hash map of the pq of that city -->> minRentCityHash.get(indexPqCity)
			//put apartment key and it's cityPQindex in the hash map 
			minRentCityHash.get(minindexPqCity).put(ap.key, minnOfPq);
			maxSqCityHash.get(maxindexPqCity).put(ap.key, maxnOfPq);
			
			//now insert that apartment into the pq
			minRentCity.get(cityKey).insert(minnOfPq, ap);
			maxSqCity.get(cityKey).insert(maxnOfPq, ap);
			
		}
		else //does not contain
		{
			//put and create a pq for this city with the cityKey
			minRentCity.put(cityKey, new ApartmentRentMinPQ(sqpq.getMaxSize()+10,c));
			maxSqCity.put(cityKey, new IndexMaxPQ<Apartment>(sqpq.getMaxSize()+10,c));
			
			//add new hashMap for the city to the arrayList
			minRentCityHash.add(c,new HashMap<String, Integer>());
			maxSqCityHash.add(c,new HashMap<String, Integer>());

			
			//getting the hashmap from arrayList and putting an apartment key 
			//index of apartment in it's respective city pq is:
			int minapCityIndex = minRentCity.get(cityKey).size();
			int maxapCityIndex = maxSqCity.get(cityKey).size();

			
			//key is the apartment, index is the index of the pq
			minRentCityHash.get(c).put(ap.key, minapCityIndex);
			maxSqCityHash.get(c).put(ap.key, maxapCityIndex);

			//add apartment to the city's pq
			minRentCity.get(cityKey).insert(minapCityIndex,ap);
			maxSqCity.get(cityKey).insert(maxapCityIndex,ap);

			c++;
		}
		n++;
		
	}
	
	public static void remove(String apKey)
	{		
		
		//remove from city stuff
		String cityKey = sqpq.get(apHash.get(apKey)).city.toUpperCase();
		
		//get the pq for apartment's city -->> minRentCity.get(cityKey); 
		//get index of the pq of that apartments city
		int minindexPqCity = minRentCity.get(cityKey).idNum;
		int maxindexPqCity = maxSqCity.get(cityKey).idNum;
		
		//get apartment index from hash map 
		int minapCityIndex = minRentCityHash.get(minindexPqCity).get(apKey);
		int maxapCityIndex = maxSqCityHash.get(maxindexPqCity).get(apKey);
		
		//delete the apartment from its city pq
		minRentCity.get(cityKey).delete(minapCityIndex);
		maxSqCity.get(cityKey).delete(maxapCityIndex);
		
		//remove that apartment from the hashmap of that city
		minRentCityHash.get(minindexPqCity).remove(apKey);
		maxSqCityHash.get(maxindexPqCity).remove(apKey);
		
		
		sqpq.delete(apHash.get(apKey));
		rentpq.delete(apHash.get(apKey));
		apHash.remove(apKey);
		
		
	}

	public static void update() {
		
		//GET APARTMENT FROM USER
		String apKey = "";
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter the address of the apartment: ");
		String street = sc.nextLine();
		street = street.replaceAll("[ .]","").toUpperCase();
		
		System.out.print("\nEnter the apartment number: ");
		String aptnum = sc.nextLine();
		aptnum = aptnum.toUpperCase();
		

		

		System.out.print("\nEnter the zip code of the apartment: ");
		String zipString = sc.nextLine();
		
		//search for apartment the user gave us
		//make key
		apKey = street + zipString + aptnum;
		
		//get the apartment the user gave us
		Apartment xx;
		try {
			 xx = rentpq.get(apHash.get(apKey));
		}
		catch(NullPointerException e) {
			System.out.println("NULL POINTER");
			return;
		}
		//get the apartment's current rent 
		int rent = xx.rent;
		
		System.out.print("\nThe rent is " + rent + ". Would you like to change it?(y/n): ");
		String choice = sc.next();
		if(choice.equals("n"))
			return;
	
		
		System.out.print("\nWhat do you want to change the rent to? ");
		int rentChange= Integer.parseInt(sc.next());

		
		//update the rent.
		 //changing the rent
		Apartment changed = new Apartment(xx.address, xx.aptNum, xx.city, xx.zip, rentChange, xx.sqFoot, xx.key);
		
		
		
		//CITY STUFF
		String cityKey = xx.city.toUpperCase();
		//get the pq for apartment's city -->> minRentCity.get(cityKey); 
		//get index of the pq of that apartments city
		int minindexPqCity = minRentCity.get(cityKey).idNum;
		int maxindexPqCity = maxSqCity.get(cityKey).idNum;
				
		//get apartment index from hash map 
		int minapCityIndex = minRentCityHash.get(minindexPqCity).get(apKey);
		int maxapCityIndex = maxSqCityHash.get(maxindexPqCity).get(apKey);
		
		
		
		maxSqCity.get(cityKey).changeKey(maxapCityIndex, changed);
		sqpq.changeKey(apHash.get(apKey), changed);
		if (rentChange < rent)
		{
			minRentCity.get(cityKey).decreaseKey(minapCityIndex, changed);
			rentpq.decreaseKey(apHash.get(apKey),changed);
		}
		else if(rentChange > rent)
		{
			//increase
			minRentCity.get(cityKey).changeKey(minapCityIndex, changed);
			rentpq.increaseKey(apHash.get(apKey), changed);
		}
		else
			return;
	}
	
	public static Apartment getMinRentApt() {
		return rentpq.minKey();
	}
	
	public static Apartment getMaxSqFootApt(){
		return sqpq.maxKey();
	}
	
	public static Apartment getminRentinCity(String cityKey)
	{
		
		return minRentCity.get(cityKey).minKey();		
	}
	
	public static Apartment getmaxSqinCity(String cityKey) 
	{
		return maxSqCity.get(cityKey).maxKey();
	}
	
	@Override
	public int compareTo(Apartment o) {
		
		int apSq = o.sqFoot;
		int thisSq = this.sqFoot;
		
		if(thisSq > apSq)
			return 1;
		else if(thisSq < apSq)
			return -1;
		else 
			return 0;
	}
	
	//2 st.:2:Houston:77007:500:500
	
}
