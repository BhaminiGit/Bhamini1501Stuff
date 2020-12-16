import java.io.*;
import java.util.*;

public class AptTracker {
	
	public static Scanner sc = new Scanner(System.in);

	
	public static int menu() {
		
		
		//display menu 
		
		System.out.println("Menu:");
		System.out.println("1: Add an apartment");
		System.out.println("2: Update apartment");
		System.out.println("3: Remove apartment");
		System.out.println("4: Apartment with lowest rent");
		System.out.println("5: Apartment with highest square footage");
		System.out.println("6: Lowest rent apartment by city");
		System.out.println("7: Highest square footage apartment by city");
		System.out.println("8: Quit");
		System.out.println("\nPlease enter the number of the option you would like to select: ");
		return Integer.parseInt(sc.nextLine());
		
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		File aptFile = new File("apartments.txt");
		Scanner aptSc = new Scanner(aptFile);
		ArrayList<String> apts = new ArrayList<String>();
		ArrayList<Apartment> arrList = new ArrayList<Apartment>();
		
		while (aptSc.hasNextLine()) {
			
			apts.add(aptSc.nextLine());
			
		}	
		
		Apartment.setSizeAndIndPQ(apts.size()+20, 0);
		for(int i = 1; i < apts.size(); i++)
		{
			Apartment.addApt(new Apartment(apts.get(i)));
			arrList.add(new Apartment(apts.get(i)));
			
		}
		
		System.out.println("Welcome!");
		int keepGoing = 1;
		do {
			switch (menu()) {
			case 1:
				addApartment();
				break;	
			case 2: 
				updateApartment();
				break;
			case 3:
				try {
					removeApartment();
				}
				catch(NullPointerException e)
				{
					System.out.println("NULL POINTER");
				}
				break;
			case 4:
				apLowRent();
				break;
			case 5:
				apHighSqFoot();
				break;
			case 6:
				try {
					lowRentByCity();
				}
				catch(NullPointerException e)
				{
					System.out.println("NULL POINTER");
				}
				break;
			case 7:
				
				try {
					highSqFootByCity();
				}
				catch(NullPointerException e)
				{
					System.out.println("NULL POINTER");
				}

				break;
			case 8:
				keepGoing = 0;
			
			}
		}while(keepGoing == 1);
		
		
	}

	public static void highSqFootByCity() {
		// TODO Auto-generated method stub
		System.out.println("HIGHEST SQUARE FOOTAGE BY CITY:");
		System.out.println("Please enter the name of the city: ");
		String cityKey = sc.nextLine().toUpperCase();
		System.out.println(Apartment.getmaxSqinCity(cityKey));		
	}

	public static void lowRentByCity() {
		// TODO Auto-generated method stub
		System.out.println("LOWERST RENT BY CITY:");
		System.out.println("Please enter the name of the city: ");
		String cityKey = sc.nextLine().toUpperCase();
		System.out.println(Apartment.getminRentinCity(cityKey));;	
	}

	public static void apHighSqFoot() {
		
		// TODO Auto-generated method stub
		System.out.println("APARTMENT WITH THE HIGHEST SQUARE FOOTAGE:");
		System.out.println(Apartment.getMaxSqFootApt());
		
	}

	public static void apLowRent() {
		System.out.println("APARTMENT WITH THE LOWEST RENT:");
		System.out.println(Apartment.getMinRentApt());
		
		
	}

	public static void removeApartment() {
		// TODO Auto-generated method stub
		System.out.println("REMOVE APARTMENT:");
		System.out.println("Enter the information of the apartment you want to remove.");
		
		String apKey = "";
		
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
		
		
		
		Apartment.remove(apKey);
		
		

		
	}

	public static void updateApartment() {
		// TODO Auto-generated method stub
		System.out.println("UPDATE APARTMENT");
		Apartment.update();
	}

	public static void addApartment() {
		// TODO Auto-generated method stub
		System.out.println("ADD APARTMENT");
		System.out.println("Enter the address of the apartment: ");
		String address = sc.nextLine();
		
		
		System.out.println("Enter the apartment number: ");
		String aptnum = sc.nextLine();
		
		
		System.out.println("Enter the city of the apartment: ");
		String cityString = sc.nextLine();
		

		System.out.println("Enter the zip code of the apartment: ");
		int zip =  Integer.parseInt(sc.nextLine());
		
		System.out.println("Enter the rent of the apartment: ");
		int rent = Integer.parseInt(sc.nextLine());
		
		System.out.println("Enter the square footage of the apartment: ");
		int sqFoot = Integer.parseInt(sc.nextLine());
		
		
		
		String key = address.replaceAll("[ .]","").toUpperCase() + zip+ aptnum.toUpperCase() + "";
			
		Apartment apt = new Apartment(address,aptnum,cityString,zip,rent,sqFoot,key);
		Apartment.addApt(apt);

	}
	
}
