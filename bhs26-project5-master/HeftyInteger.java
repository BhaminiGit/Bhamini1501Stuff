
public class HeftyInteger {

	public static int mycount = 0;
	private final byte[] ONE = {(byte) 1};
	
	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	
//	
//
	
	public HeftyInteger(byte[] b) {
		val = b;
	}

	public static void print(HeftyInteger h) {
		
		printBarr(h.getVal());		
	}
	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		
		this.extend((byte)0B00000000);
		other.extend((byte)0B00000000);

		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	
	public HeftyInteger original_add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.original_add(new HeftyInteger(ONE));
	}
	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.original_add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public static HeftyInteger byteMul(byte x, byte y)
	{
		
		if(x == 0 || y == 0) {
			byte[] Zero = {(byte)0B00000000};
			HeftyInteger zERo = new HeftyInteger(Zero);
			return zERo;
		}
		
		//System.out.println(x + " " + y);
		int a = Byte.toUnsignedInt(x);
		int b = Byte.toUnsignedInt(y);
		//System.out.println(a + " " + b);
		Integer pro = a*b;
		String res = Integer.toHexString(pro);
		//System.out.println("byteM actual answer " + res );
		byte[] arr;
		int count = 0;
		
		if(res.length() % 2 >0){
			res = 0 + "" + res;
			arr = new byte[res.length()/2];
		}
		else {
			arr = new byte[res.length()/2];
		}
		
		for(int i= 0; i < arr.length; i++) {
			arr[i] = (byte)Integer.parseInt(res.substring(count,count+2),16);
			count += 2;
		}
		
		return new HeftyInteger(arr);
	}
	
	public HeftyInteger mul2(int x)
	{
//		System.out.print("x is: " + x + " before: ");
//		print(this);
//		System.out.println();
		
		
		
		String binar = "";
		int beg = 8-(x % 8);
		//System.out.println(beg);

		for(int i = 0; i< this.length(); i++) {
			
			byte b = this.getVal()[i];
			binar +=(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
			
			
		}
		
//		System.out.println(binar);
		
		for(int i = 0; i< beg; i++)
		{
			
				binar = "0" + binar;
		}
		for(int i = 0; i< x; i++)
		{
			binar = binar + "0";
		}
		
		int count = 0;
		byte[] arr = new byte[binar.length()/8];
		//System.out.println(binar);
		for(int i = 0; i< binar.length(); ) {
			String temp = binar.substring(i,i+8);
			//temp = "0B" + temp;
			
			i += 8;
			arr[count] = (byte)Integer.parseInt(temp,2);
			count++;
		}
		HeftyInteger th = new HeftyInteger(arr);
//		System.out.print("after: " + binar + " ");
//		print(th);
//		System.out.println();
		return th;
		
	}
		
	
	public static void printBarr(byte[] a) {
		
		System.out.print(" #");
		Integer pro = Byte.toUnsignedInt(a[0]);//(int)a[0];
		String res = Integer.toHexString(pro);
		System.out.print(res);
		
		for(int i = 1; i< a.length; i++) {

			 pro = Byte.toUnsignedInt(a[i]);
			 res = Integer.toHexString(pro);
			
			 System.out.print("~" + res );
		}
		System.out.print("# ");
		
		
	}
	
	//this / other = quotient 
	public HeftyInteger[] divide(HeftyInteger other) {
				
		byte[] quo = new byte[this.length()];
		byte[] Zero = {(byte)0B00000000};
		HeftyInteger _one = new HeftyInteger(ONE);
		HeftyInteger quotient = new HeftyInteger(quo);
		HeftyInteger rem = null;
		
		HeftyInteger diff = new HeftyInteger(this.getVal());
		int count = 0;
		while(true) {
			count++;
			
			diff = diff.subtract(other);
			
			if(diff.isNegative())
				break;
			rem = diff;
			
			quotient = quotient.original_add(_one);	
		//	System.out.println();
			//print(diff);
			
		}
		
		
		return new HeftyInteger[] {quotient,rem};
	}
	
	//sign -> true = positive, false = negative
	public HeftyInteger multiply(HeftyInteger other) {
		
		boolean sign;

		HeftyInteger current = this;
		
		if(this.isNegative() && other.isNegative())
		{
			sign = true;
			current = this.negate();
			other = other.negate();
					
		}
		else if(this.isNegative() && !other.isNegative()) {
			sign = false;
			current = this.negate();
		}
		else if(!this.isNegative() && other.isNegative())
		{
			sign = false;
			other = other.negate();
		}
		else 
			sign = true;
				
		
		other.extend((byte)0B00000000);
		
		this.extend((byte)0B00000000);
		
		HeftyInteger ans = current.actualMultiply(other);
		//ans.extend((byte)0B00000000);
		
		if(sign == false)
			ans = ans.negate();
		
		
		return ans;
		
	}
		
	public HeftyInteger actualMultiply(HeftyInteger other) {
		
		if(isZero(other) || isZero(this)) {
			byte[] Zero = {(byte)0B00000000};
			HeftyInteger zERo = new HeftyInteger(Zero);
			return zERo;
		}
			
		
		//MAKE SURE THEY ARE THE SAME SIZE-------------------------
		if(other.length() > this.length()) {
			this.makesame(other);
		}
		else if(other.length() < this.length()) {
			other.makesame(this);
		}
		
//		
		
		if(other.length() == 1)
		{
			return byteMul(this.getVal()[0], other.getVal()[0]);
		}
		
		
		
		int size = other.length()/2; //IN BYTES YO!
		int actLen = size*2;
		
		//get xh, xl, yh, yl
		//this = x
		//other = y
		//System.out.println();
		byte[] xh = new byte[size];
		for(int i =0; i<xh.length; i++)
			xh[i] = this.getVal()[i];
		//printBarr(xh);
		
		byte[] xl = new byte[this.length() - size];
		for(int i=0; i<xl.length; i++) 
			xl[i] = this.getVal()[i+size];
		//printBarr(xl);
		
		byte[] yh = new byte[size];
		for(int i =0; i<yh.length; i++)
			yh[i] = other.getVal()[i];
		//printBarr(yh);
				
		byte[] yl = new byte[other.length() - size];
		for(int i=0; i<yl.length; i++) 
			yl[i] = other.getVal()[i+size];
		//printBarr(yl);
			
		
		HeftyInteger m1, m2, m3, m4;
		HeftyInteger a1, a2, a3;
		
		byte[] temp;
		if(other.length() - size == 1 )
		{
			
//			int m1 = xh * yh;
			m1 = byteMul(xh[0], yh[0]);
			
//			int m2 = xh * yl;
			m2 = byteMul(xh[0], yl[0]);
			
//			int m3 = xl * yh;
			m3 = byteMul(xl[0], yh[0]);
			
//			int m4 = xl * yl;
			m4 = byteMul(xl[0], yl[0]);
		
//			
//			
//			int a1 = 10^2 *m1;
			a1 = m1.mul2(16);
			
//			int a2 = 10^1 *(m2 + m3);
			a2 = (m2.add(m3).mul2(8));
////			

//			
//			int a3 = a1 + a2;
			a3 = a1.add(a2);
	
//		

					
			 HeftyInteger answer = a3.add(m4);

			 
			 return answer;
//			
		}
		else
		{
			HeftyInteger hxh = new HeftyInteger(xh);
			 HeftyInteger hxl = new HeftyInteger(xl);
			 HeftyInteger hyh = new HeftyInteger(yh);
			 HeftyInteger hyl = new HeftyInteger(yl);
			 
			 m1 = hxh.actualMultiply(hyh);
			 m2 = hxh.actualMultiply(hyl);
			 m3 = hxl.actualMultiply(hyh);
			 m4 = hxl.actualMultiply(hyl);
			 

//		 
		
			 if(this.length()%2  != 0)
			 {
				 actLen = this.length() +1;
				 
			 }
			 actLen *= 8;
			 a1 = m1.mul2(actLen);
			 a2 = (m2.add(m3).mul2(actLen/2));
			 a3 = a1.add(a2);
			 

			 HeftyInteger answer = a3.add(m4);
//			 
			 
			 return answer;

		}
		
		
	}
		
	
	public void makesame(HeftyInteger x) {
		int count = x.length() - this.length();
		byte extension;
		//System.out.println("making same, checking first byte " + x.getVal()[0]);
		
			extension = (byte) 0B00000000;
		
		byte[] newv = new byte[val.length + count];
		for(int i = 0; i< count; i++) {
			newv[i] = extension;

		}
		for (int i = 0; i < val.length; i++) {
			newv[i + count] = val[i];
		}
		val = newv;
		
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
	
	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public HeftyInteger[] XGCD(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		 HeftyInteger first = null;
		 HeftyInteger second = null;
		if(other.length() > this.length())
		{
			first = other;
			second = this;
		}
		else if(other.length() < this.length())
		{
			first = this;
			second = other;
		}
		else {
			first = this;
			second = other;
		}
			
		return BigGCD.findXGCD(first, second);
		// return null;
	 }
}
