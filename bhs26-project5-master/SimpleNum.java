import java.math.BigInteger;

public class SimpleNum  {
	
	public int[] numArr;
	public int num;
	

	public static void main(String[] args) {
			
		 int p = 99;
	      int q = 78;
	      if (p <= 0 || q <= 0) throw new IllegalArgumentException("p and q must be positive integers");
	      int vals[] = gcd(p, q);
	     System.out.println("gcd(" + p + ", " + q + ") = " + vals[0]);
	      System.out.println(vals[1] + "(" + p + ") + " + vals[2] + "(" + q + ") = " + vals[0]);
		
	}
	
	public static byte[] byteMul(byte x, byte y)
	{
		//System.out.println(x + " " + y);
		int a = Byte.toUnsignedInt(x);
		int b = Byte.toUnsignedInt(y);
		System.out.println(a + " " + b);
		Integer pro = a*b;
		String res = Integer.toHexString(pro);
		System.out.println("byteM actual answer " + res );
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
		
		return arr;
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
	
	
	
	public void print() {
		String temp1 = "";
		for(int i = 0; i < this.numArr.length; i++)
		{
			temp1 += "" + this.numArr[i];
			
			
		}
	}
	public SimpleNum(int [] x)
	{
		this.numArr = new int[x.length];
		for(int i = 0; i < x.length; i++)
		{
			this.numArr[i] = x[i];
		}
		
		int temp = 0; 
		for(int i = x.length-1; i >= 0; i++)
		{
			temp += (int)Math.pow(10, i) * x[i];
		}
		this.num = temp;
	}
	
	public SimpleNum add(SimpleNum x) {
		
		String temp1 = "", temp2 = "";
		for(int i = 0; i < this.numArr.length; i++)
		{
			temp1 += "" + this.numArr[i];
		
		}
		for(int i = 0; i < x.numArr.length; i++)
		{
			temp2 += "" + x.numArr[i];
		
		}
		Integer sum = Integer.parseInt(temp1) + Integer.parseInt(temp2);
		String str = sum.toString();
		
		int[] arr = new int[str.length()];
		for(int i = 0; i < str.length(); i++)
		{
			arr[i] = Character.getNumericValue(str.charAt(i));
		}
		return new SimpleNum(arr);
		
	}
	
	public SimpleNum mult(SimpleNum x, int size) {
		return null;
//		int xnum = x.num;
//		int ynum = this.num;
//		int xh = xnum/(int)Math.pow(10, size/2);
//		int xl = xnum % (int)Math.pow(10, size/2);
//		int yh = ynum/(int)Math.pow(10, size/2);
//		int yl = ynum % (int)Math.pow(10, size/2);
//		
//		System.out.println(xh + "-" + xl + " " + yh + "-" + yl);
//		
//		
//		if(size/2 == 1 )
//		{
//			int m1 = xh * yh;
//			int m2 = xh * yl;
//			int m3 = xl * yh;
//			int m4 = xl * yl;
//
//			int a1 = 100*m1;
//			int a2 = 10*(m2 + m3);
//			int a3 = m4;
//		
//			return new SimpleNum(a1 + a2 + a3);
//			
//		}
//		else
//		{
//			 SimpleNum sxh = new SimpleNum(xh);
//			 SimpleNum sxl = new SimpleNum(xl);
//			 SimpleNum syh = new SimpleNum(yh);
//			 SimpleNum syl = new SimpleNum(yl);
//			 
//			 SimpleNum m1 = sxh.mult(syh, size/2);
//			 SimpleNum m2 = sxh.mult(syl, size/2);
//			 SimpleNum m3 = sxl.mult(syh, size/2);
//			 SimpleNum m4 = sxl.mult(syl, size/2);
//			 
//					 
//			 SimpleNum a1 = new SimpleNum((int)Math.pow(10, size) * m1.num);
//			 SimpleNum a2 = new SimpleNum((int)Math.pow(10, size/2) * m2.add(m3).num);
//			 SimpleNum a3 = new SimpleNum(m4.num);
//			 
//			 SimpleNum temp = a1.add(a2);
//			 temp = temp.add(a3);
//			 
//			 return temp;
//		}
		
		
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
	
	 static int[] gcd(int p, int q) {
	      if (q == 0)
	         return new int[] { p, 1, 0 };

	      int[] vals = gcd(q, p % q);
	      int d = vals[0];
	      int a = vals[2];
	      int b = vals[1] - (p / q) * vals[2];
	      return new int[] { d, a, b };
	   }

}
