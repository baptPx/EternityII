package com.bapt.eternity;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tools.res;
import Tools.zoneInterface;

public class Menu implements Screen{

	private BitmapFont font;
	private SpriteBatch batch; 
	private Sprite EternityImg;
	private String screen;
	private Eternity Game;
	private Pixmap particleP;
	private float pos[][];
	private zoneInterface getBack;
	int select;
	private FileHandle path[];
	private LinkedList<String> nomLvl, nomSave;
	private LinkedList<Integer> Llvl, llvl, llvlSave, LlvlSave;
	private float time;
	private boolean choise;
	
	public Menu(Eternity Game) {
		
		this.Game = Game;
	}
	@Override
	public void show() {
		
		choise = false;
		time=0;
		path = Gdx.files.internal("lvl").list();
		nomLvl = new LinkedList<String>();
		nomSave = new LinkedList<String>();
		Llvl = new LinkedList<Integer>();
		llvl = new LinkedList<Integer>();
		LlvlSave = new LinkedList<Integer>();
		llvlSave = new LinkedList<Integer>();
		for(int i=0;i<path.length;i++)
		{
			nomLvl.add( path[i].name());
			nomLvl.set(i, nomLvl.get(i).replaceAll(".ety", ""));
			Llvl.add((int) path[i].readBytes()[0]);
			llvl.add((int) path[i].readBytes()[1]);
		}
		path = Gdx.files.internal("save").list();
		for(int i=0;i<path.length;i++)
		{
			nomSave.add( path[i].name());
			nomSave.set(i, nomSave.get(i).replaceAll(".build", ""));
			LlvlSave.add((int) path[i].readBytes()[0]);
			llvlSave.add((int) path[i].readBytes()[1]);
		}
		getBack = new zoneInterface(0, 0, 100f, 15f, res.getBack);
		select=-1;
		pos = new float[1000][8];
		particleP = new Pixmap(8, 8, Format.RGBA8888);
		particleP.setColor(Color.WHITE);
		particleP.fillRectangle(0, 0, 8, 8);
		for(int i=0; i< pos.length; i++)
		{
			pos[i][0] = (float) (Math.random()*Gdx.graphics.getWidth());
			pos[i][1] = (float) (Math.random()*Gdx.graphics.getHeight());
			pos[i][2] = 0;
			
			pos[i][3] = (Gdx.graphics.getWidth() - pos[i][0])/60;
			pos[i][4] = (Gdx.graphics.getHeight() - pos[i][1])/60;
			pos[i][5] = (float) (Math.random()*4);
		}
		batch = new SpriteBatch();
		EternityImg = new Sprite(new Texture(Gdx.files.internal("Eternity.PNG")));
		font = new BitmapFont(Gdx.files.internal("AlphaWood.fnt"), false);
		screen = "menuP";
	}

	@Override
	public void render(float delta) {
		
		if(!choise)
		{
			if(Gdx.input.justTouched())
			{
				Gdx.input.vibrate(100);
				res.click.play();
			}
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			if(Gdx.input.isTouched() && Gdx.input.getY()<Gdx.graphics.getHeight()/100*40)
				time+=5;
			time+=delta;
			for(int i=0; i<pos.length;i++)
			{
				pos[i][7]++;
				if(pos[i][7] >=pos[i][6])
				{
					pos[i][0] = (float) (Math.random()*Gdx.graphics.getWidth());
					pos[i][1] = (float) (Math.random()*Gdx.graphics.getHeight());
					pos[i][2] = 0;
					pos[i][6] = (float) (Math.random()*60);
					pos[i][7] = 0;
					pos[i][3] = -(Gdx.graphics.getWidth()/2 - pos[i][0])/pos[i][6];
					pos[i][4] = -(Gdx.graphics.getHeight()/1.5f - pos[i][1])/pos[i][6];
					pos[i][5] = (float) (Math.random()*4);
				}
			}
			
			batch.begin();
			if(time < 1)
			{
				batch.draw(EternityImg, Gdx.graphics.getWidth()/2 - EternityImg.getWidth()*0.75f,Gdx.graphics.getHeight()/2 - EternityImg.getHeight(), 0, 0, EternityImg.getWidth(),
						EternityImg.getHeight(), 1.5f, 1.5f, 0);
			}
			else if(time<3)	
	
			{
				batch.draw(EternityImg, Gdx.graphics.getWidth()/2 - EternityImg.getWidth()*(0.5f + 0.25f*(3-time)/2), Gdx.graphics.getHeight()*(0.5f +0.5f*(time-1)/2) - EternityImg.getHeight(), 0, 0, EternityImg.getWidth(),
						EternityImg.getHeight(), 1+(3-time)*0.25f, 1+(3-time)*0.25f, 0);
			}
			else
			{
				if(screen == "menuP")
					menuP();
				else if(screen == "choixLvl")
					choixLvl();
				else if(screen == "creations" && !choise)
				{
					dispose();
					Game.setScreen(new BuildScreen(Game));
					choise = true;
				}
				else
					continuer();
				for(int i=0; i<pos.length;i++)
				{
					pos[i][0] += pos[i][3];
					pos[i][1] += pos[i][4];
					pos[i][2] += pos[i][5];
					batch.draw(res.particleS, pos[i][0], pos[i][1], 0, 0, (pos[i][6] - pos[i][7])/15, (pos[i][6] - pos[i][7])/15, 1, 1, pos[i][2]);
				}
			}
			batch.end();
		}
	}
	public void choixLvl()
	{
		for(int i=0; i<nomLvl.size() && !choise; i++)
		{
			if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/100*(9-i)*10 && 
					Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()/100*(10-i)*10)
			{
				if(!Gdx.input.isTouched())	
					font.setColor(Color.CYAN);
				
				else {
					font.setColor(Color.RED);
					select = i;
				}
			font.draw(batch, i+" - " + nomLvl.get(i)  , Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/100 * (100-i*10));
			font.draw(batch,"    L - " + Llvl.get(i) + " l - " + llvl.get(i) , Gdx.graphics.getWidth()/100*50, Gdx.graphics.getHeight()/100 * (100-i*10));
				font.setColor(Color.WHITE);
			}
			else	{
				font.draw(batch, i+" - " + nomLvl.get(i)  , Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/100 * (100-i*10));
				font.draw(batch,"    L - " + Llvl.get(i) + " l - " + llvl.get(i) , Gdx.graphics.getWidth()/100*50, Gdx.graphics.getHeight()/100 * (100-i*10));
			
			}
			if(getBack.isTouch())
				select = -2;
			if(!Gdx.input.isTouched() && select != -1)
			{
				if(select == -2)	{
					screen = "menuP";
					select = -1;
				}
				else if (!choise)
				{
//					dispose();
					Game.setScreen(new InGameScreen(Game, nomLvl.get(select)));
					choise = true;
				}
			}
		}
		getBack.update(Gdx.graphics.getDeltaTime());
		getBack.draw(batch);
	}
	public void creations()
	{
		getBack.update(Gdx.graphics.getDeltaTime());
		getBack.draw(batch);
	}
	
	public void continuer()
	{
		getBack.update(Gdx.graphics.getDeltaTime());
		getBack.draw(batch);
		
	
		for(int i=0; i<nomSave.size() && !choise; i++)
		{
			if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/100*(9-i)*10 && 
					Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()/100*(10-i)*10)
			{
				if(!Gdx.input.isTouched())	
					font.setColor(Color.CYAN);
				
				else {
					font.setColor(Color.RED);
					select = i;
				}
			font.draw(batch, i+" - " + nomSave.get(i)  , Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/100 * (100-i*10));
			font.draw(batch,"    L - " + LlvlSave.get(i) + " l - " + llvlSave.get(i) , Gdx.graphics.getWidth()/100*50, Gdx.graphics.getHeight()/100 * (100-i*10));
				font.setColor(Color.WHITE);
			}
			else	{
				font.draw(batch, i+" - " + nomSave.get(i)  , Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/100 * (100-i*10));
				font.draw(batch,"    L - " + LlvlSave.get(i) + " l - " + llvlSave.get(i) , Gdx.graphics.getWidth()/100*50, Gdx.graphics.getHeight()/100 * (100-i*10));
			
			}
			if(getBack.isTouch())
				select = -2;
			if(!Gdx.input.isTouched() && select != -1)
			{
				if(select == -2)	{
					screen = "menuP";
					select = -1;
				}
				else if(!choise)
				{
					choise = true;
					dispose();
					Game.setScreen(new InGameScreen(Game, nomSave.get(select)+".build"));
				}
			}
		}
	}
	
	public void menuP()
	{
		batch.draw(EternityImg, Gdx.graphics.getWidth()/2 - EternityImg.getWidth()/2,Gdx.graphics.getHeight()-EternityImg.getHeight());
		
		if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/100*30 &&  Gdx.graphics.getHeight() - Gdx.input.getY() <= Gdx.graphics.getHeight()/100 * 40)
		{
			if(!Gdx.input.isTouched())	
				font.setColor(Color.CYAN);
			
			else {
				font.setColor(Color.RED);
				select = 1;
			}
			if(time<5)
				font.setColor(1, 1, 1, (time-3)/2);
			font.draw(batch, "1 - Choisir un niveau", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 40);
			font.setColor(Color.WHITE);
		}
		else 
			font.draw(batch, "1 - Choisir un niveau", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 40);

		if(Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()/100*30 && Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/100*20)
		{
			if(!Gdx.input.isTouched())	
				font.setColor(Color.CYAN);
			else	{
				font.setColor(Color.RED);	
				select = 2;
			}
			if(time<5)
				font.setColor(1, 1, 1, (time-3)/2);
			font.draw(batch, "2 - Creer un niveau", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 30);
			font.setColor(Color.WHITE);
		}
		else {
			if(time<5)
				font.setColor(1, 1, 1, (time-3)/2);
			font.draw(batch, "2 - Creer un niveau", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 30);

		}
		

		if(Gdx.graphics.getHeight() - Gdx.input.getY()<Gdx.graphics.getHeight()/100*20 && Gdx.graphics.getHeight() - Gdx.input.getY()>Gdx.graphics.getHeight()/100*10)
		{
			if(!Gdx.input.isTouched())	
				font.setColor(Color.CYAN);
			
			else	{
				font.setColor(Color.RED);
				select = 3;
			}
			if(time<5)	
				font.setColor(1, 1, 1, (time-3)/2);
			font.draw(batch, "3 - Continuer une partie", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 20);
			font.setColor(Color.WHITE);
		}
		else {
			if(time<5)
				font.setColor(1, 1, 1, (time-3)/2);
			font.draw(batch, "3 - Continuer une partie", Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/100 * 20);
		}
		if(!Gdx.input.isTouched() && select != -1)
		{
			if(select == 1)
				screen = "choixLvl";
			else if(select == 2) 
				screen = "creations";
			else
				screen = "continue";
			select = -1;

		}
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

		EternityImg.getTexture().dispose();
		font.dispose();
		nomLvl.clear();
		nomSave.clear();
		Llvl.clear();
		llvl.clear();
		llvlSave.clear();
		LlvlSave.clear();
	}

}
