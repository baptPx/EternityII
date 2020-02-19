package Tools;

import com.badlogic.gdx.graphics.Color;

public class Fonction {
	
	public static int[] copyTableau(int tab[])
	{
		int copy[];
		copy = new int[tab.length];
		for(int i=0; i< tab.length; i++)
			copy[i] = tab[i];
		return copy;
	}
	public static Color getDegrade(float r1, float g1, float b1, float r2, float g2, float b2, float percentage)
	{
		return new Color(r1 + (r2-r1)*percentage, g1 + (g2-g1)*percentage, b1 + (b2 - b1)*percentage, 1);
	}
	public static byte[] copyTableau(byte[] tab) {
		
		byte copy[];
		copy = new byte[tab.length];
		for(byte i=0; i< tab.length; i++)
			copy[i] = tab[i];
		return copy;
	}
	public static byte[] intToByte(int a)
	{
		byte[] b = new byte[4];
		for(int i=3; i>=0; i--)	{
			b[i] = (byte) (a/Math.pow(255, i));
			a -=  b[i]*Math.pow(255, i);
		}
		return b;
	}
	public static int byteToInt(byte[] a)
	{
		int b=0;
		for(int i=0; i<4; i++)	
			b+=  a[i]*Math.pow(255, i);
		return b;
	}
}
