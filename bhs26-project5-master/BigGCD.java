

public class BigGCD {
	

	static byte[] OnE = {(byte)0B00000000,(byte)0B00000001};
	public static HeftyInteger _one = new HeftyInteger(OnE);
	
	static byte[] zEro = {(byte)0B00000000,(byte)0B0000000};
	public static HeftyInteger _zero = new HeftyInteger(zEro);
	

	
	
	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	public static HeftyInteger[] findXGCD(HeftyInteger current, HeftyInteger other) {
	
		//	System.out.println("----------------------------------------------------------------------");
	      if (isZero(other))
	          return new HeftyInteger[] { current, _one, _zero };

	       HeftyInteger[] vals = findXGCD(other, current.divide(other)[1]);
	       
//	       print(vals[0]);

	       
	       HeftyInteger d = new HeftyInteger(vals[0].getVal());
	       HeftyInteger a = new HeftyInteger(vals[2].getVal());
	       HeftyInteger b= vals[1].subtract((current.divide(other))[0].multiply( vals[2]));
	       
	       	       
	       
	       return new HeftyInteger[] { d, a, b };

	}

		
	
	public static boolean isZero(HeftyInteger x) {
		for(int i = 0; i <x.length(); i++)
		{
			if(x.getVal()[i] != (byte)0b00000000) {
				return false;
			}
		}
		
		return true;
	}
}
