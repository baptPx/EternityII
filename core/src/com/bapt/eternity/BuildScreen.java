package com.bapt.eternity;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import Tools.Camera;
import Tools.Fonction;
import Tools.res;
import Tools.zoneInterface;

public class BuildScreen implements Screen {

	private SpriteBatch batch, batchI;
	private Camera cam;
	private Piece map[][];
	private zoneInterface generation, choiseW[], choiseH[], makeLevel, changePiece, saveLvl;
	private String pos;
	private float distance, oldDistance, touchX, touchY, oldX, oldY, x, y;
	private float selection[], posCam[];
	private byte largeur, longueur, piece;
	private int nbDessin, largeP;
	private BitmapFont font;
	private Eternity game;
	boolean justTouched1, stillClick;
	private Skin skin;
	private TextField text;
	private Table table;
	private Stage stage;
	int random;
	
	public BuildScreen(Eternity game) {
		this.game = game;
	}

	public void autoBuild(){
		for(int i=0; i< longueur - 1; i++)
		{
			random = (int) (1 + Math.random()*(res.piece.length - 2));
			map[i][largeur - 1 ].setRight(res.getNPiece((byte) random));
			map[i+1][largeur - 1 ].setLeft(res.getNPiece((byte) random));
			
			for(int j=0; j<largeur - 1; j++)
			{
				int random = (int) (1 + Math.random()*(res.piece.length - 2));
				map[i][j].setRight(res.getNPiece((byte) random));
				map[i+1][j].setLeft(res.getNPiece((byte) random));
				random = (int) (1 + Math.random()*(res.piece.length - 2));
				map[i][j].setTop(res.getNPiece((byte) random));
				map[i][j+1].setBottom(res.getNPiece((byte) random));
			}
		}
		for(int i=0; i<largeur - 1; i++)
		{
			random = (int) (1 + Math.random()*(res.piece.length - 2));
			map[longueur - 1][i ].setTop(res.getNPiece((byte) random));
			map[longueur - 1][i+1].setBottom(res.getNPiece((byte) random));
	
		}
		nbDessin =  longueur * largeur * 2 - longueur - largeur;

	}
	@Override
	public void show() {

		largeP = Gdx.graphics.getWidth()/Piece.width;
	    skin = new Skin();
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);
	    skin = new Skin( Gdx.files.internal("ui/uiskin.json"));
		text = new TextField("monLvl",skin);
		table = new Table();
		table.setSize(200, 200);
		table.add(text).width(200).padTop(30);
		table.row();
		table.setPosition(0,  Gdx.graphics.getHeight()/100*60);
		stage.addActor(table);
		
		nbDessin = 0;
		largeur =-1;
		longueur = -1;
		font = new BitmapFont();
		selection = new float[2];	
		selection[0] = -1; selection[1] =-1;
		piece = 1;
		stillClick = false;
	
		batch = new SpriteBatch();
		batchI = new SpriteBatch();
		cam = new Camera();
		cam.position.x -= Gdx.graphics.getWidth();
		cam.position.y -= Gdx.graphics.getHeight();
		posCam = new float[3];


		generation = new zoneInterface(80f, 0f, 20, 20f, res.autobuild);
		makeLevel = new zoneInterface(0f, 0f, 100f, 20f, res.makeLevel);
		changePiece = new zoneInterface(20f, 0f, 30, 20f, res.changeP);
		saveLvl = new zoneInterface(50f, 0f, 30, 20f, res.saveLvl);

//		saveLvl.setTextMidV();
//		saveLvl.setTextMidH();
//		saveLvl.setText("Save Lvl");
//		
//		getBack.setTextMidV();
//		getBack.setTextMidH();
//		getBack.setText("get Back");
//
//		makeLevel.setTextMidV();
//		makeLevel.setTextMidH();
//		makeLevel.setText("Commencer");		
		
		choiseW = new zoneInterface[10];
		choiseH = new zoneInterface[10];
		for(int i=0; i<10; i++)
		{	
			choiseW[i] = new zoneInterface((float)i/10*100, 90, 100/10, 10, Fonction.getDegrade(0.05f, 0.96f, 0.33f, 1, 0, 0.01f, (float)i/10));
			choiseW[i].setText(""+(int)(i+1)*2);
			choiseW[i].setTextMidH();
			choiseW[i].setTextMidV();
			choiseH[i] = new zoneInterface((float)i/10*100, 75, 100/10, 10, Fonction.getDegrade(0.05f, 0.96f, 0.33f, 1, 0, 0.01f, (float)i/10));
			choiseH[i].setText(""+(int)(i+1)*2);
			choiseH[i].setTextMidH();
			choiseH[i].setTextMidV();
		}
		pos = "buildMenu";	

	}

	public void buildScreen(float delta)
	{
		if(Gdx.input.isKeyJustPressed(Keys.T) || generation.isJustTouched())
			autoBuild();

		changePiece.update(delta);
		generation.update(delta);
		if(nbDessin == longueur * largeur * 2 - longueur - largeur)
			saveLvl.update(delta);
		batch.begin();
		for(int i=0 ; i<longueur; i++)
			for(int j=0; j<largeur; j++)	
				map[i][j].draw(batch, i*Piece.width, j*Piece.width);

		batch.end();
		if(changePiece.isJustTouched() && !stillClick)	{
			pos = "changePiece";
			stillClick = true;
			setPosCam();
		}
		if(Gdx.input.justTouched())
		{
			selection[0] = touchX;
			selection[1] = touchY;
		}
		if(nbDessin == longueur * largeur * 2 - longueur - largeur && saveLvl.isTouch())
		{
			saveETY();
			dispose();
			game.setScreen(new Menu(game));
		}
		else if(
				selection[1]<Gdx.graphics.getHeight()*0.8f && !Gdx.input.isTouched() && selection[0]!=-1 && Math.abs(selection[0] - touchX) < Gdx.graphics.getWidth()/10 && Math.abs(selection[1] - touchY) < Gdx.graphics.getHeight()/10) // si on viens seulement de relacher l'appui
		{
			if(x%Piece.width < Piece.width/2 && y%Piece.width > x%Piece.width && y%Piece.width < Piece.width - x%Piece.width && x> Piece.width)	{
				if(map[(int)x/Piece.width][(int)y/Piece.width].getLeft() == 1 )
					nbDessin++;
				map[(int)x/Piece.width][(int)y/Piece.width].setLeft(res.getNPiece(piece));
				map[(int)x/Piece.width - 1][(int)y/Piece.width].setRight(res.getNPiece(piece));
			}	
			else if(x%Piece.width > Piece.width/2 && y%Piece.width < x%Piece.width && y%Piece.width > Piece.width - x%Piece.width && x < Piece.width * (largeur-1) )	{	
				if(map[(int)x/Piece.width][(int)y/Piece.width].getRight() == 1 )
					nbDessin++;
				map[(int)x/Piece.width][(int)y/Piece.width].setRight(res.getNPiece(piece));
				map[(int)x/Piece.width + 1][(int)y/Piece.width].setLeft(res.getNPiece(piece));
			}
			else if(y%Piece.width < Piece.width/2 && x%Piece.width > y%Piece.width && x%Piece.width < Piece.width - y%Piece.width && y > Piece.width )	{
				if(map[(int)x/Piece.width][(int)y/Piece.width].getBottom() == 1 )
					nbDessin++;
				map[(int)x/Piece.width][(int)y/Piece.width].setBottom(res.getNPiece(piece));
				map[(int)x/Piece.width][(int)y/Piece.width-1].setTop(res.getNPiece(piece));
			}
			else if(y%Piece.width > Piece.width/2 && x%Piece.width > y%(Piece.width/2) && x%Piece.width < y%Piece.width && y < Piece.width * (longueur-1))	{
				if(map[(int)x/Piece.width][(int)y/Piece.width].getTop() == 1 )
					nbDessin++;
				map[(int)x/Piece.width][(int)y/Piece.width].setTop(res.getNPiece(piece));
				map[(int)x/Piece.width][(int)y/Piece.width+1].setBottom(res.getNPiece(piece));
			}
		}

		if(!Gdx.input.isTouched())
			selection[0]=-1;
		else
		{
			if((map.length*Piece.width+2) > Gdx.graphics.getWidth())
			{
				cam.position.x -= (touchX - oldX)*cam.zoom;
				if(cam.position.x > (map.length+1) * Piece.width - Gdx.graphics.getWidth()/2)
					cam.position.x = (map.length+1) * Piece.width - Gdx.graphics.getWidth()/2;
				else if (cam.position.x < Gdx.graphics.getWidth()/2 - Piece.width)
					cam.position.x = Gdx.graphics.getWidth()/2 - Piece.width;
			}
			else 
				cam.position.x = map.length/2*Piece.width;
			
			if((longueur+2)*Piece.width > Gdx.graphics.getHeight()*0.8f)
			{
				cam.position.y += (touchY - oldY)*cam.zoom;
				if(cam.position.y > (map[0].length+1) * Piece.width - Gdx.graphics.getHeight()/2)
					cam.position.y = (map[0].length+1) * Piece.width - Gdx.graphics.getHeight()/2;
				else if(cam.position.y < Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width)
					cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width;
			}
			else 
				cam.position.y = longueur/2*Piece.width;
		}	
			batchI.begin();
			generation.draw(batchI);
			changePiece.draw(batchI);
			batchI.draw(res.piece[piece], Piece.width*1.5f, 0, 0, 0, Piece.width, Piece.width, 1, 1, 90);
			batchI.draw(res.piece[piece], -Piece.width*0.5f,Piece.width*1, 0, 0, Piece.width, Piece.width, 1, 1, -90);
			if(nbDessin == longueur * largeur * 2  - longueur - largeur)
				saveLvl.draw(batchI);
			batchI.end();
			if(!Gdx.input.isTouched())
				stillClick = false;
	}
	
	public void saveETY()
	{
		FileHandle save = Gdx.files.local("lvl/"+text.getText()+".ety");
		byte contenu[] = new byte[longueur*largeur*4 +2];
		contenu[0] = largeur; contenu[1] = longueur;
		int random, nbRotation;
		LinkedList<Piece> pieces = new LinkedList<Piece>();
		for(byte i=0; i<longueur; i++)
			for(byte j=0; j<largeur; j++)
				pieces.add(map[i][j]);
		for(int i=2; i< longueur*largeur*4 + 2; i+=4)
		{
			random = (int)Math.random()*pieces.size();
			nbRotation = (int)Math.random()*4;
			if(nbRotation == 1)
				pieces.get(random).rightRotation();
			else if(nbRotation == 2)
				pieces.get(random).leftRotation();
			else if(nbRotation == 3)
				pieces.get(random).doubleRotation();
			contenu[i] = pieces.get(random).getTop();
			contenu[i+1] = pieces.get(random).getRight();
			contenu[i+2] = pieces.get(random).getBottom();
			contenu[i+3] = pieces.get(random).getLeft();
			pieces.remove(random);
		}
		save.writeBytes(contenu, true);
	}
	public void changePiece(float delta)
	{
		batch.begin();
		for(int i =0; i<= res.piece.length/largeP; i++)
			for(int j=0; j<(i==res.piece.length/largeP ? res.piece.length%largeP - 2 : largeP); j++)
			{
				batch.draw(res.piece[i*largeP+j+1], (j-1.5f)*Piece.width, (i+1)*Piece.width, 0, 0, Piece.width, Piece.width, 1, 1, -90);
				batch.draw(res.piece[i*largeP+j+1], (j+0.5f)*Piece.width, i*Piece.width, 0, 0, Piece.width, Piece.width, 1, 1, 90);
			}
		batch.end();
		
		if(Gdx.input.justTouched())
		{
			selection[0] = touchX;
			selection[1] = touchY;
		}
		
		if(!stillClick && !Gdx.input.isTouched() && selection[0]!=-1 && Math.abs(selection[0] - touchX) < Gdx.graphics.getWidth()/largeP && Math.abs(selection[1] - touchY) < Gdx.graphics.getHeight()/largeP) // si on viens seulement de relacher l'appui
		{
			pos = "buildScreen";
			setCam();
			if(x > -Piece.width*2 && x < largeP * Piece.width && y>0 && y < Piece.width * res.piece.length/largeP)
				piece = (byte) ((int)(y/Piece.width)*largeP + (x / Piece.width));
			piece+=2;
			if(piece>22 )
				piece=1;

		}

		if(!Gdx.input.isTouched())	{
			selection[0]=-1;
			stillClick = false;
		}
		else
		{
			if(5*Piece.width > Gdx.graphics.getWidth())
			{
				cam.position.x -= (touchX - oldX)*cam.zoom;
				if(cam.position.x > 5 * Piece.width - Gdx.graphics.getWidth()/2)
					cam.position.x = 5 * Piece.width - Gdx.graphics.getWidth()/2;
				else if (cam.position.x < Gdx.graphics.getWidth()/2 - Piece.width)
					cam.position.x = Gdx.graphics.getWidth()/2 - Piece.width;
			}
			else 
				cam.position.x = 2.5f*Piece.width ;
			
			if(res.piece.length/5*Piece.width > Gdx.graphics.getHeight())
			{
				cam.position.y += (touchY - oldY)*cam.zoom;
				if(cam.position.y > res.piece.length/5 * Piece.width - Gdx.graphics.getHeight()/2)
					cam.position.y = res.piece.length/5 * Piece.width - Gdx.graphics.getHeight()/2;
				else if(cam.position.y < Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width)
					cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width;
			}
			else 
				cam.position.y = res.piece.length/10*Piece.width ;
	
		}
		
	}
	
	
	public void buildMenu(float delta)
	{
		for(int i=0; i<choiseW.length; i++)
		{
			choiseW[i].update(delta);
			choiseH[i].update(delta);
		}
		if(longueur != -1 && largeur !=-1)
			makeLevel.update(delta);
		batchI.begin();
		for(int i=0; i<choiseH.length; i++)
		{
			choiseW[i].draw(batchI);
			choiseH[i].draw(batchI);
		}
		font.draw(batchI, "Largeur", Gdx.graphics.getWidth()/2 - 20, Gdx.graphics.getHeight() - font.getAscent() - Gdx.graphics.getHeight()/100*10);
		font.draw(batchI, "Longueur", Gdx.graphics.getWidth()/2 - 22, Gdx.graphics.getHeight() - font.getAscent() - Gdx.graphics.getHeight()/100*25);
		
		if(	Gdx.files.local("lvl/"+text.getText()+".ety").exists())
			font.draw(batchI, "Ce niveau existe deja, choisissez un autre nom", 250, Gdx.graphics.getHeight()/100 * 70 );
		if(longueur != -1 && largeur != -1 && !Gdx.files.local("lvl/"+text.getText()+".ety").exists())
			makeLevel.draw(batchI);

		batchI.end();
		stage.act();
		stage.draw();
		for(byte i=0;i<10;i++)
		{
			if(choiseW[i].isJustTouched())
			{
				choiseW[i].setColorAlpha(1);
				largeur = (byte) (i*2 + 2);
				for(byte j=0;j<10;j++)
					if(j!=i)
						choiseW[j].setColorAlpha(0.4f);
			}
			if(choiseH[i].isJustTouched())
			{
				choiseH[i].setColorAlpha(1);
				longueur = (byte) (i*2 + 2);
				for(int j=0;j<10;j++)
					if(j!=i)
						choiseH[j].setColorAlpha(0.4f);
			}
		}
		if(longueur != -1 && largeur != -1 && makeLevel.isJustTouched() &&	!Gdx.files.local("lvl/"+text.getText()+".ety").exists())
		{
			map = new Piece[longueur][largeur];
			for(byte i=0; i<longueur; i++)
				for(byte j=0; j<largeur; j++)
					map[i][j] = new Piece((byte)1);
			pos = "buildScreen";
			if((map.length*Piece.width+2) < Gdx.graphics.getWidth())
				posCam[0] = map.length/2*Piece.width;
			else posCam[0] = Gdx.graphics.getWidth()/2;
			if((longueur+2)*Piece.width < Gdx.graphics.getHeight()*0.8f)
				posCam[1] = longueur/2*Piece.width;
			else posCam[1] = Gdx.graphics.getHeight()/2;
			posCam[2]=1;
			setCam();
			table.remove();
			cam.position.x = map.length*Piece.width/2;
			cam.position.y = map[0].length*Piece.width/2;
			if( (map.length*Piece.width)/Gdx.graphics.getWidth() > (map[0].length*Piece.width)/Gdx.graphics.getHeight())
				cam.zoom = ((float)map.length*Piece.width)/Gdx.graphics.getWidth();
			else
				cam.zoom = ((float)map[0].length*Piece.width)/Gdx.graphics.getHeight();		

			
		}
	}
	private void setCam()
	{
		cam.position.x = posCam[0];
		cam.position.y = posCam[1];
		cam.zoom  = posCam[2];
	}
	private void setPosCam()
	{
		posCam[0] = cam.position.x ;
		posCam[1] = cam.position.y;
		posCam[2] = cam.zoom ;
	}
	@Override
	public void render(float delta) {

		if(Gdx.input.justTouched())
		{
			Gdx.input.vibrate(100);
			res.click.play();
		}
		cam.update();
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		
		if(Gdx.input.justTouched())
		{
			touchX = Gdx.input.getX(0);
			touchY = Gdx.input.getY(0);

		}

		oldX = touchX;
		oldY = touchY;
		 if(Gdx.input.isTouched())
		{
			touchX = Gdx.input.getX(0);
			touchY = Gdx.input.getY(0);
		}
		if(Gdx.input.isTouched(1) )
		{
			if(!justTouched1)
				distance = (float)Math.sqrt(	(Gdx.input.getX(1) - touchX) * (Gdx.input.getX(1) - touchX) + 	(Gdx.input.getY(1) - touchY) * (Gdx.input.getY(1) - touchY));
			oldDistance = distance;
			distance = (float)Math.sqrt(	(Gdx.input.getX(1) - touchX) * (Gdx.input.getX(1) - touchX) + 	(Gdx.input.getY(1) - touchY) * (Gdx.input.getY(1) - touchY));
			justTouched1 = true;
			cam.zoom -= (distance - oldDistance) / Gdx.graphics.getWidth();
		}
		else
			justTouched1 = false;

		x = cam.position.x + Gdx.input.getX()*cam.zoom - Gdx.graphics.getWidth()/2*(cam.zoom);
		y = cam.position.y + (Gdx.graphics.getHeight() - Gdx.input.getY())*cam.zoom - Gdx.graphics.getHeight()/2*cam.zoom;

		if(pos == "buildMenu")
			buildMenu(delta);
		else if(pos == "buildScreen")
			buildScreen(delta);
		else if(pos == "changePiece")
			changePiece(delta);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();
	}

	@Override
	public void dispose() {

		generation.dispose();
		for(int i=0; i<choiseW.length; i++)
			choiseW[i].dispose();
		
		for(int i=0; i<choiseH.length; i++)
			choiseH[i].dispose();
		makeLevel.dispose();
		changePiece.dispose();
		saveLvl.dispose();
		font.dispose();
		skin.dispose();
	}

	
}
