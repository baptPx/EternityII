package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class res {
	
	public static Sprite piece[], side, buildP, changeP, cote, getBack, rotationLeft, rotationRight, 
	makeLevel, newPiece, plateau, saveLvl, showall, felicitation, particleS, autobuild, supprimer, incompatible;
	public static Pixmap particleP;
	public static Music musique, click, musiqueIndiana;
	
	public static void build() { 
		side = new Sprite(new Texture(Gdx.files.internal("Piece/couche.png")));
		side.setColor(0.3f, 0.3f, 0.3f, 0.3f);
		piece = new Sprite[24]; 
		piece[0] = new Sprite(new Texture(Gdx.files.internal("Piece/1.png")));
		piece[1] = new Sprite(new Texture(Gdx.files.internal("Piece/2.png")));
		piece[2] = new Sprite(new Texture(Gdx.files.internal("Piece/3.png")));
		piece[3] = new Sprite(new Texture(Gdx.files.internal("Piece/5.png")));
		piece[4] = new Sprite(new Texture(Gdx.files.internal("Piece/7.png")));
		piece[5] = new Sprite(new Texture(Gdx.files.internal("Piece/11.png")));
		piece[6] = new Sprite(new Texture(Gdx.files.internal("Piece/13.png")));
		piece[7] = new Sprite(new Texture(Gdx.files.internal("Piece/17.png")));
		piece[8] = new Sprite(new Texture(Gdx.files.internal("Piece/19.png")));
		piece[9] = new Sprite(new Texture(Gdx.files.internal("Piece/23.png")));
		piece[10] = new Sprite(new Texture(Gdx.files.internal("Piece/29.png")));
		piece[11] = new Sprite(new Texture(Gdx.files.internal("Piece/31.png")));
		piece[12] = new Sprite(new Texture(Gdx.files.internal("Piece/37.png")));
		piece[13] = new Sprite(new Texture(Gdx.files.internal("Piece/41.png")));
		piece[14] = new Sprite(new Texture(Gdx.files.internal("Piece/43.png")));
		piece[15] = new Sprite(new Texture(Gdx.files.internal("Piece/47.png")));
		piece[16] = new Sprite(new Texture(Gdx.files.internal("Piece/53.png")));
		piece[17] = new Sprite(new Texture(Gdx.files.internal("Piece/59.png")));
		piece[18] = new Sprite(new Texture(Gdx.files.internal("Piece/61.png")));
		piece[19] = new Sprite(new Texture(Gdx.files.internal("Piece/67.png")));
		piece[20] = new Sprite(new Texture(Gdx.files.internal("Piece/71.png")));
		piece[21] = new Sprite(new Texture(Gdx.files.internal("Piece/73.png")));
		piece[22] = new Sprite(new Texture(Gdx.files.internal("Piece/79.png")));
		piece[23] = new Sprite(new Texture(Gdx.files.internal("Piece/-1.png")));
		incompatible = new Sprite(new Texture(Gdx.files.internal("Piece/incompatible.png")));
		
		buildP = new Sprite(new Texture(Gdx.files.internal("boutons/buildpiece.jpg")));
		changeP = new Sprite(new Texture(Gdx.files.internal("boutons/changepiece.jpg")));
		cote = new Sprite(new Texture(Gdx.files.internal("boutons/cote.jpg")));
		getBack = new Sprite(new Texture(Gdx.files.internal("boutons/getback.jpg")));
		rotationLeft = new Sprite(new Texture(Gdx.files.internal("boutons/leftrotation.jpg")));
		rotationRight = new Sprite(new Texture(Gdx.files.internal("boutons/rightrotation.jpg")));
		makeLevel = new Sprite(new Texture(Gdx.files.internal("boutons/makelevel.jpg")));
		newPiece = new Sprite(new Texture(Gdx.files.internal("boutons/newpiece.jpg")));
		plateau = new Sprite(new Texture(Gdx.files.internal("boutons/plateau.jpg")));
		saveLvl = new Sprite(new Texture(Gdx.files.internal("boutons/savelvl.jpg")));
		showall = new Sprite(new Texture(Gdx.files.internal("boutons/showall.jpg")));
		felicitation = new Sprite(new Texture(Gdx.files.internal("felicitations.png")));
		autobuild = new Sprite(new Texture(Gdx.files.internal("boutons/autobuild.jpg")));
		supprimer = new Sprite(new Texture(Gdx.files.internal("boutons/supprimer.jpg")));
		felicitation.setX(Gdx.graphics.getWidth()/2 - felicitation.getWidth()/2);
		felicitation.setY(Gdx.graphics.getHeight()/2 - felicitation.getHeight()/2);
		
		particleP = new Pixmap(8, 8, Format.RGBA8888);
		particleP.setColor(Color.WHITE);
		particleP.fillRectangle(0, 0, 8, 8);
		particleS = new Sprite(new Texture(particleP));
		
		musique=Gdx.audio.newMusic(Gdx.files.internal("musique.mp3"));
		musique.setLooping(true);
		musique.play();
		musiqueIndiana=Gdx.audio.newMusic(Gdx.files.internal("indiana.mp3"));

		click = Gdx.audio.newMusic(Gdx.files.internal("clic.mp3"));

	}
	public static Sprite get(byte index)
	{
		if(index == -1)
			return piece[23];
		if(index == 1 )
			return piece[0];
		else if(index == 2)
			return piece[1];
		else if(index == 3)
			return piece[2];
		else if(index == 5)
			return piece[3];
		else if(index == 7)
			return piece[4];
		else if(index == 11)
			return piece[5];
		else if(index == 13)
			return piece[6];
		else if(index == 17)
			return piece[7];
		else if(index == 19)
			return piece[8];
		else if(index == 23)
			return piece[9];
		else if(index == 29)
			return piece[10];
		else if(index == 31)
			return piece[11];
		else if(index == 37)
			return piece[12];
		else if(index == 41)
			return piece[13];
		else if(index == 43)
			return piece[14];
		else if(index == 47)
			return piece[15];
		else if(index == 53)
			return piece[16];
		else if(index == 59)
			return piece[17];
		else if(index == 61)
			return piece[18];
		else if(index == 67)
			return piece[19];
		else if(index == 71)
			return piece[20];
		else if(index == 73)
			return piece[21];
		else if(index == 79)
			return piece[22];
		return null;
	}
	public static byte getNPiece(byte index)
	{
		if(index == 0)
			return 1;
		else if(index == 1)
			return 2;
		else if(index == 2)
			return 3;
		else if(index == 3)
			return 5;
		else if(index == 4)
			return 7;
		else if(index == 5)
			return 11;
		else if(index == 6)
			return 13;
		else if(index == 7)
			return 17;
		else if(index == 8)
			return 19;
		else if(index == 9)
			return 23;
		else if(index == 10)
			return 29;
		else if(index == 11)
			return 31;
		else if(index == 12)
			return 37;
		else if(index == 13)
			return 41;
		else if(index == 14)
			return 43;
		else if(index == 15)
			return 47;
		else if(index == 16)
			return 53;
		else if(index == 17)
			return 59;
		else if(index == 18)
			return 61;
		else if(index == 19)
			return 67;
		else if(index == 20)
			return 71;
		else if(index == 21)
			return 73;
		else if(index == 22)
			return 79;
		return 0;
	}
	public static void dispose()
	{
		for(int i=0; i<piece.length; i++)
			piece[i].getTexture().dispose();
		side.getTexture().dispose();
		buildP.getTexture().dispose();
		changeP.getTexture().dispose();
		cote.getTexture().dispose(); 
		getBack.getTexture().dispose(); 
		rotationLeft.getTexture().dispose(); 
		rotationRight.getTexture().dispose(); 
		makeLevel.getTexture().dispose(); 
		newPiece.getTexture().dispose(); 
		plateau.getTexture().dispose(); 
		saveLvl.getTexture().dispose(); 
		showall.getTexture().dispose(); 
		felicitation.getTexture().dispose(); 
		particleS.getTexture().dispose(); 
		autobuild.getTexture().dispose(); 
		supprimer.getTexture().dispose(); 
		incompatible.getTexture().dispose();;
		particleP.dispose();;
		musique.dispose();
		click.dispose();
		musiqueIndiana.dispose();
	}
}
