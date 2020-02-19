package com.bapt.eternity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tools.Fonction;
import Tools.res;

public class Piece {

	public static int width = Gdx.graphics.getWidth()/7;
	private byte q[];
//	private Pixmap pixmapQ;
//	private Sprite textureQ;
	private float timeRotation=0, rotation = 0.3f, degre=0;
	private boolean isEmpty, isCompatible;
	

public Piece(byte a) {
		
		isCompatible = true;
		isEmpty = true;
		build(a, a, a, a);	
	}
	public Piece(Piece a) {
		
		isCompatible = true;
		isEmpty = true;
		build(a.getTop(), a.getRight(), a.getBottom(), a.getLeft());

	}
	public void set(byte a)
	{
		isCompatible = true;
		isEmpty = true;
		build(a, a, a, a);
	}
	public Piece(byte q0, byte q1, byte q2, byte q3) {
		
		isCompatible = true;
		isEmpty = false;
		build(q0, q1, q2, q3);
		
	}

	private void build(byte q0, byte q1, byte q2, byte q3) {
		
		
		q = new byte[4];
		q[0] = q0; q[1] = q1; q[2] = q2; q[3] = q3; 	
	}


	public void setCompatible(boolean compatible)
	{
		isCompatible = compatible;
	}
	public boolean isCompatible()
	{
		return isCompatible;
	}
	//	private void buildPixmap(int q0, int q1, int q2, int q3) {
//		
//		q = new int[4];
//		q[0] = q0; q[1] = q1; q[2] = q2; q[3] = q3; 
//		pixmapQ = new Pixmap(width, width, Format.RGBA8888);
//		for(int i=0; i<4; i++)
//		{
//			pixmapQ.setColor(getColor(q[i]));
//			if( i == 0 )
//				pixmapQ.fillTriangle(0, width, width/2, width/2, width, width);
//			else if( i == 1 )
//				pixmapQ.fillTriangle(width, width, width/2, width/2, width, 0);
//			else if( i == 2 )
//				pixmapQ.fillTriangle(0, 0, width/2, width/2, width, 0);
//			else if( i == 3 )
//				pixmapQ.fillTriangle(0, 0, width/2, width/2, 0, width);
//		}1
//		textureQ = new Sprite(new Texture(pixmapQ));
//		textureQ.setX(0);
//		textureQ.setY(0);
//		
//	}
	public void draw(SpriteBatch batch, float x, float y)
	{
//		batch.draw(res.get(q[0]), x, y, res.get(q[0]).getOriginX(), res.get(q[0]).getOriginY(), width, width, 1, 1, 0 + degre);
//		batch.draw(res.get(q[2]), x + width, y + width, res.get(q[2]).getOriginX(), res.get(q[2]).getOriginY(), width, width, 1, 1, 180 + degre);
//		batch.draw(res.get(q[1]), x + width, y + width, res.get(q[1]).getOriginX(), res.get(q[1]).getOriginY(), width, width, 1, 1, -90 + degre);
//		batch.draw(res.get(q[3]), x + width/2, y, res.get(q[3]).getOriginX(), res.get(q[3]).getOriginY(), width, width, 1, 1, 90 + degre);
		if(isCompatible)
		{
		batch.draw(res.get(q[0]), x, y, 0, 0, width, width, 1, 1, 0 + degre);
		batch.draw(res.get(q[2]), x + width, y + width, 0, 0, width, width, 1, 1, 180 + degre);
		batch.draw(res.get(q[1]), x, y + width, 0, 0, width, width, 1, 1, -90 + degre);
		batch.draw(res.get(q[3]), x + width, y, 0, 0, width, width, 1, 1, 90 + degre);
		}
		else
		{
			batch.draw(res.incompatible, x, y, 0, 0, width, width, 1, 1, 0 + degre);
			batch.draw(res.incompatible, x + width, y + width, 0, 0, width, width, 1, 1, 180 + degre);
			batch.draw(res.incompatible, x, y + width, 0, 0, width, width, 1, 1, -90 + degre);
			batch.draw(res.incompatible, x + width, y, 0, 0, width, width, 1, 1, 90 + degre);
		}
	}
	public void draw(SpriteBatch batch, float x, float y, float width, float height)
	{
		if(isCompatible)
		{
			batch.draw(res.get(q[0]), x, y , 0, 0, width, height, 1, 1, 0);
			batch.draw(res.get(q[3]), x + width , y, 0, 0, height, width, 1, 1, 90);
			batch.draw(res.get(q[2]), x + width, y + height , 0, 0, width, height, 1, 1, 180);
			batch.draw(res.get(q[1]), x ,  y + height , 0, 0, height, width, 1, 1, 270);
		}
		else
		{
			batch.draw(res.incompatible, x, y , 0, 0, width, height, 1, 1, 0);
			batch.draw(res.incompatible, x + width , y, 0, 0, height, width, 1, 1, 90);
			batch.draw(res.incompatible, x + width, y + height , 0, 0, width, height, 1, 1, 180);
			batch.draw(res.incompatible, x ,  y + height , 0, 0, height, width, 1, 1, 270);
		}
	}

	public void setQ(int q, byte newVaue) {
		this.q[q] = newVaue;
	}
	public boolean isCompatible (byte q0, byte q1, byte q2, byte q3) 
	{

		if(((q0 == -1 && q[0] != 1) || q0 == q[0]) && ((q1 == -1 && q[1] != 1) || q1 == q[1]) && ((q2 == -1 && q[2] != 1) || q2 == q[2]) && ((q3 == -1 && q[3] != 1) || q3 == q[3])  )
			return true;
		else if(((q0 == -1 && q[1] != 1) || q0 == q[1]) && ((q1 == -1 && q[2] != 1) || q1 == q[2]) && ((q2 == -1 && q[3] != 1) || q2 == q[3]) && ((q3 == -1 && q[0] != 1) || q3 == q[0])) {
			leftRotation();
			return true;
		}
		else if	(((q0 == -1 && q[2] != 1) || q0 == q[2]) && ((q1 == -1 && q[3] != 1) || q1 == q[3]) && ((q2 == -1 && q[0] != 1) || q2 == q[0]) && ((q3 == -1 && q[1] != 1) || q3 == q[1])  ) {
			doubleRotation();
			return true;
		}
		else if (((q0 == -1 && q[3] != 1) || q0 == q[3]) && ((q1 == -1 && q[0] != 1) || q1 == q[0]) && ((q2 == -1 && q[1] != 1) || q2 == q[1]) && ((q3 == -1 && q[2] != 1) || q3 == q[2])  ) {
			rightRotation();
			return true; 
		}
		else	
			return false;
	}
	public void rightRotation()
	{

		timeRotation+=rotation;
		byte t[] = Fonction.copyTableau(q);
		for(int i=1; i<5; i++)
			q[i%4] = t[i-1];
	}
	public void update(float delta)
	{
		if(timeRotation>0)
		{
			degre = timeRotation/rotation * 90;
			timeRotation-=delta;
			if(timeRotation < 0)	{
				timeRotation = 0;
				degre=0;
			}
		}
		else if(timeRotation<0)
		{
			degre = timeRotation/rotation * 90;
			timeRotation+=delta;
			if(timeRotation > 0)	{
				timeRotation = 0;
				degre = 0;
			}
		}
	}
	public void leftRotation()
	{
		timeRotation-=rotation;
		byte t[] = Fonction.copyTableau(q);
		for(byte i=1; i<5; i++)
			q[i-1] = t[i%4];
	}
	public void doubleRotation()
	{
		timeRotation+=2*rotation;
		byte t[] = Fonction.copyTableau(q);
		for(byte i=0; i<4; i++)
			q[i] = t[(i+2)%4];
	}
	public void setTop(byte index)	{
		q[0] = index;
	}
	public void setRight(byte index)	{
		q[1] = index;
	}
	public void setBottom(byte index)	{
		q[2] = index;
	}
	public void setLeft(byte index)	{
		q[3] = index;
	}
	public byte getTop()	{
		return q[0];
	}
	public byte getRight()	{
		return q[1];
	}
	public byte getBottom()	{
		return q[2];
	}
	public byte getLeft()	{
		return q[3];
	}
	public int getColor(int q)
	{
		if(q == -1 )
			return Color.rgba8888(0.4f, 0.4f, 0.4f, 1);
		else if(q == 1 )
			return Color.rgba8888(0, 0, 0, 1);
		else if(q == 2 )
			return Color.rgba8888(1, 0, 0, 1);
		else if(q == 3 )
			return Color.rgba8888(0, 1, 0, 1);
		else if(q == 5 )
			return Color.rgba8888(0, 0, 1, 1);
		else if(q == 7 )
			return Color.rgba8888(1, 0, 1, 1);
		else if(q == 11 )
			return Color.rgba8888(1, 1, 0, 1);
		else if(q == 13 )
			return Color.rgba8888(0, 1, 1, 1);
		else if(q == 17 )
			return Color.rgba8888(0, 0.5f, 0.5f, 1);
		else if(q == 19 )
			return Color.rgba8888(0.5f, 0, 1, 1);
		else if(q == 23 )
			return Color.rgba8888(1, 0.5f, 1, 1);
		else if(q == 29 )
			return Color.rgba8888(0.5f, 0.5f, 1, 1);
		else if(q == 31 )
			return Color.rgba8888(1, 0.5f, 0.7f, 1);
		else if(q == 37 )
			return Color.rgba8888(1, 0.12f, 0.56f, 1);
		else if(q == 41 )
			return Color.rgba8888(1, 0, 0, 1);
		else if(q == 43 )
			return Color.rgba8888(0, 1, 0, 1);
		else if(q == 47 )
			return Color.rgba8888(0, 0, 1, 1);
		else if(q == 53 )
			return Color.rgba8888(1, 0, 1, 1);
		else if(q == 59 )
			return Color.rgba8888(1, 1, 0, 1);
		else if(q == 61 )
			return Color.rgba8888(0, 1, 1, 1);
		else if(q == 67 )
			return Color.rgba8888(0, 0.5f, 0.5f, 1);
		else if(q == 71 )
			return Color.rgba8888(0.5f, 0, 1, 1);
		else if(q == 73 )
			return Color.rgba8888(1, 0.5f, 1, 1);
		else if(q == 79 )
			return Color.rgba8888(0.5f, 0.5f, 1, 1);
	
		return 0;
	}
	public void presente()
	{
		System.out.println("Q1 : " + q[0] + "   Q2 : " + q[1] + "    Q3 : " + q[2] + "     Q4 : " + q[3]);
	}

	public boolean isEmpty() {
		return isEmpty;
	}
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public void draw(SpriteBatch batch, float x, float y, float rotation)
	{
//		batch.draw(res.get(q[0]), x, y, res.get(q[0]).getOriginX(), res.get(q[0]).getOriginY(), width, width, 1, 1, 0 + degre);
//		batch.draw(res.get(q[2]), x + width, y + width, res.get(q[2]).getOriginX(), res.get(q[2]).getOriginY(), width, width, 1, 1, 180 + degre);
//		batch.draw(res.get(q[1]), x + width, y + width, res.get(q[1]).getOriginX(), res.get(q[1]).getOriginY(), width, width, 1, 1, -90 + degre);
//		batch.draw(res.get(q[3]), x + width/2, y, res.get(q[3]).getOriginX(), res.get(q[3]).getOriginY(), width, width, 1, 1, 90 + degre);
		batch.draw(res.get(q[0]), x, y, 0, 0, width, width, 1, 1, 0 + rotation);
		batch.draw(res.get(q[2]), x + width, y + width, 0, 0, width, width, 1, 1, 180 + rotation);
		batch.draw(res.get(q[1]), x, y + width, 0, 0, width, width, 1, 1, -90 + rotation);
		batch.draw(res.get(q[3]), x + width, y, 0, 0, width, width, 1, 1, 90 + rotation);
	}

	
}
