package com.bapt.eternity;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Tools.Camera;
import Tools.res;
import Tools.zoneInterface;

public class InGameScreen implements Screen {


	private SpriteBatch batch, batchI;
	private Camera cam;
	private Piece map[][], pieceS, pieceSwap, pieceN, buildP;
	private List<Piece> piece, compatibles, resolution[][];
	private zoneInterface newPiece, showAll, buildPiece, getBack, makeLevel, rightRotation, leftRotation,
	 supprimerP, setAuto, Save;
	private String pos;
	private float x, y, touchX, touchY, oldX, oldY, oldDistance, distance, curseur, xBuild, yBuild, widthB, curseurB;
	private float selection[], posCamGame[], timeAnimation, explosion[][][];
	private int nbPiece, select, selectCase[], curseurR, indexR[][], position[][], positionB2[][];
	private Eternity game;
	private boolean justTouched1, doublon, continuer, isAuto;
	private byte side;
   	String message;
	boolean afficher;
	private float particle[][];
	private int test;

	
	
	private List<Piece> readPieceCSV(String file)
	{
		List<Piece> pieces = new LinkedList<Piece>();	// creation de la liste
		FileHandle fichier = Gdx.files.internal(file);	// lecture du fichier
		byte[] contenu = fichier.readBytes();		

		map = new Piece[16][16];
		int index=0, line = 0, info = 0;
		int q[] = new int[5];
		for(byte i=0;i<5;i++)	// initialisation des valeurs des cotÃ© a 0
			q[i]=0;
		for(index=0; line<2; index++)
			if(contenu[index] == 10)
				line++;					// Passage des 2 premieres lignes (en tï¿½te du fichier)
		for(;index<fichier.length();index++)
		{
			if(contenu[index]>= 48 && contenu[index]<=57)	// stockage de l'information dans le tableau d'entier
			{
				q[info%5]+=contenu[index] - 48;
				q[info%5]*=10;
			}
			else if(contenu[index] == 10 || contenu[index] == 44)	// saut de ligne ou changement d'information
			{	
				q[info%5]/=10;
				info++;
				if(contenu[index] == 10)	// saut de ligne, ajout de la piece, reinitialisation des valeurs du tableau d'entier.
				{
					pieces.add(new Piece((byte)q[1], (byte)q[2], (byte)q[3], (byte)q[4]));
					for(int i=0; i<5; i++)
						q[i]=0;
				}
			}
		}
		for(int i=0; i<16; i++)
			for(int j=0; j<16; j++)
				map[i][j] = new Piece((byte)1);		// on rempli le tterrain de piece "vide"
		return pieces;
	}
	
	private List<Piece> readPieceETY(String file)
	{
		List<Piece> pieces = new LinkedList<Piece>();	// creation de la liste
		FileHandle fichier = Gdx.files.internal("lvl/"+file+".ety");	// lecture du fichier
		byte[] contenu = fichier.readBytes();		
		map = new Piece[contenu[1]][contenu[0]];
		for(byte i=0; i<contenu[1];i++)
			for(byte j=0; j<contenu[0];j++)
				map[i][j] = new Piece((byte)1);
		for(int i=2;i<contenu.length;i+=4)	{
			pieces.add(new Piece(contenu[i], contenu[i+1], contenu[i+2], contenu[i+3]));
		}
		return pieces;
	}
		
	void rempliPos()	// remplir en spirale
	{
		int provisoire[][] = new int[map.length][map[0].length], index = 0, x=0, y=0, side = 2;
		for(int i=0; i<map.length; i++)
			for(int j=0; j<map[0].length;j++)
				provisoire[i][j]=-1;
		
		
		for(int i = 0; i<map.length ; i++)
		{
			provisoire[x][0] = index;
			position[index][2] = 1;
			index++;
			x++;
		}
		x--;
		
		
		for(int j=1; j<map[0].length; j++)
		{
			provisoire[x][j] = index;
			position[index][2] = 2;

			index++;
			y++;
		} 
		while(index < map.length * map[0].length)
		{
			if(side==0)
			{
				x++;
				provisoire[x][y]=index;
				position[index][2] = 1;

				index++;
				if(x == map.length - 1 || provisoire[x + 1][y]!=-1)	
					side = 1;
			}
			else if(side==1)
			{
				y++;
				provisoire[x][y]=index;
				position[index][2] = 2;

				index++;
				if(y == map[0].length - 1 || provisoire[x][y+1]!=-1)
					side = 2;

			}
			else if(side==2)
			{
				x--;
				provisoire[x][y]=index;
				position[index][2] = 3;

				index++;
				if(x == 0 || provisoire[x-1][y]!=-1)	
					side = 3;
			}
			else if(side==3)
			{
				y--;
				provisoire[x][y]=index;
				position[index][2] = 0;
				index++;
				if(y == 0 || provisoire[x][y-1]!=-1)	
					side = 0;
			}
		}
		for(int i =0; i<map.length; i++)
			for(int j=0; j<map[0].length; j++)
			{
				position[provisoire[i][j]][0] = i;
				position[provisoire[i][j]][1] = j;
			}
	}
	void rempliPosCadre()	// rempli un cadre puis de bas en haut
	{
		int  index = 0;
		for(int i=0; i< map.length; i++)	{
			position[index][0] = i;
			position[index][1] = 0;
			index++;
		}
		for(int i=1; i< map[0].length; i++)	{
			position[index][0] = map[0].length-1;
			position[index][1] = i;
			index++;
		}
		for(int i=map.length-2; i>=0; i--)	{
			position[index][0] = i;
			position[index][1] = map[0].length-1;
			index++;
		}
		for(int i=map[0].length-2; i>0; i--)	{
			position[index][0] = 0;
			position[index][1] = i;
			index++;
		}
		for(int i=1; i< map.length-1; i++)
		{
			for(int j=1; j<map[0].length-1; j++)
			{
				position[index][0] = j;
				position[index][1] = i;
				index++;
			}
		}
	}
	public void show() {

	}
	
	public void verifP()	// verifier si il y a des case du plateau ou il n'y a aucune possibilité
	{
		boolean haveP = false;
		
		for(int i=0; i< map.length; i++)
		{
			for(int j=0; j< map[0].length; j++)
			{
				haveP = true; 
				
				if(map[i][j].isEmpty())
				{
					haveP = false;

					if(i == 0)
						buildP.setQ(3, (byte) 1);
					else if(map[i- 1][j].isEmpty())
						buildP.setQ(3, (byte) -1); 
					else
						buildP.setQ(3, map[i- 1][j].getRight());
					
					
					if(i == map.length - 1)
						buildP.setQ(1, (byte) 1);
					else if(map[i+ 1][j].isEmpty())
						buildP.setQ(1, (byte) -1); 
					else
						buildP.setQ(1, map[i+ 1][j].getLeft());
					
					
					if(j == 0)
						buildP.setQ(2, (byte) 1);
					else if(map[i][j - 1].isEmpty())
						buildP.setQ(2, (byte) -1); 
					else
						buildP.setQ(2, map[i][j - 1].getTop());
					
					
					if(j == map[0].length - 1)
						buildP.setQ(0, (byte) 1);
					else if(map[i][j + 1].isEmpty())
						buildP.setQ(0, (byte) -1); 
					else
						buildP.setQ(0, map[i][j + 1].getBottom());
					
					for(int z=0; z<piece.size(); z++)
					{	
						if(piece.get(z).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))
						{	
							haveP = true;
							z = piece.size();	 
						}
					}
				}
				map[i][j].setCompatible(haveP);
			}
		}
		
		
		
		
		
	}
	public InGameScreen(Eternity game) {
		this.game = game;
	}
	
	public InGameScreen(Eternity game2, String name) {

		particle = new float[1000][8];
		if(name.contains("Indi"))
		{
			res.musique.stop();
			res.musiqueIndiana.play();
		}
		test=0;
		this.game = game2;
		isAuto = false;
		timeAnimation = 0;
		continuer = true;
		nbPiece = (int)(Gdx.graphics.getWidth()*0.5f/Piece.width) ;
		curseurB=0;
		side =0;
		widthB = Gdx.graphics.getWidth()/4;
		curseur = 0;
		buildP = new Piece((byte) -1);
		posCamGame = new float[3];
		
		justTouched1=false;
		selection = new float[2];	
		selection[0] = -1; selection[1] =-1;
		piece = null;
		pieceSwap = null;
		pieceN = new Piece((byte)1);
		compatibles = new LinkedList<Piece>();

		piece = new LinkedList<Piece>();
		if(name.contains(".csv"))
			piece = readPieceCSV(name);
		else if(name.contains(".build"))
			readSaveGame(name);
		else
			piece = readPieceETY(name);
		doublon =false;
		for(int i=0; i<piece.size(); i++)
			for(int j=i+1; j<piece.size(); j++)
			{
				if(piece.get(i).isCompatible(piece.get(j).getTop(), piece.get(j).getRight(), piece.get(j).getBottom(), piece.get(j).getLeft()))
				{
					i= piece.size();
					j= piece.size();
					doublon = true;
				}
			}

		position = new int[map.length*map[0].length][3];
		positionB2 = new int[map.length*map[0].length][2];
		explosion = new float[map.length][map[0].length][6];
		for(int i=0; i<map.length; i++)
		{
			for(int j=0; j<map[0].length; j++)
			{
				explosion[i][j][0] = i*Piece.width;
				explosion[i][j][1] = j*Piece.width;
				explosion[i][j][2] = 0;
				explosion[i][j][3] = (float) (10 - Math.random()*20);
				explosion[i][j][4] = (float) (10 - Math.random()*20);
				explosion[i][j][5] = (float) (20 - Math.random()*40);

			}
		}
		resolution = new LinkedList[map.length][map[0].length];
		indexR = new int[resolution.length][resolution[0].length];
		for(byte i=0; i<resolution.length; i++)
			for(byte j =0; j<resolution[0].length; j++)	{
				resolution[i][j] = new LinkedList<Piece>();
				indexR [i][j] = 0;
			}
		curseurR = 0;
		batch = new SpriteBatch();
		batchI = new SpriteBatch();
		cam = new Camera();
		cam.position.x = map.length*Piece.width/2;
		cam.position.y = map[0].length*Piece.width/2;
		if( (map.length*Piece.width)/Gdx.graphics.getWidth() > (map[0].length*Piece.width)/Gdx.graphics.getHeight())
			cam.zoom = ((float)map.length*Piece.width)/Gdx.graphics.getWidth();
		else
			cam.zoom = ((float)map[0].length*Piece.width)/Gdx.graphics.getHeight();	

		posCamGame[0] = cam.position.x;
		posCamGame[1] = cam.position.y;
		posCamGame[2] = 1;
		newPiece = new zoneInterface(20f, 0f, 60, 20f, res.newPiece);
		getBack = new zoneInterface(0f, 0f, 100f, 20f, res.getBack);
		leftRotation = new zoneInterface(20, 0f, 30, 20f, res.rotationLeft);
		rightRotation = new zoneInterface(50, 0f, 30, 20f, res.rotationRight);
		makeLevel = new zoneInterface(0f, 0f, 100f, 20f, res.makeLevel);
		showAll = new zoneInterface(0f, 20f, 50, 80, res.showall);
		buildPiece = new zoneInterface(50, 20f, 50, 80, res.buildP);
		supprimerP = new zoneInterface(80, 0, 20, 20, res.supprimer);
		setAuto = new zoneInterface(80, 0, 20, 20, res.autobuild);
		Save = new zoneInterface(0, 0, 20, 20, res.saveLvl);

		newPiece.setColorAlpha(0.85f);
		setAuto.setColorAlpha(0.85f);
		Save.setColorAlpha(0.85f);
		getBack.setColorAlpha(0.85f);
		leftRotation.setColorAlpha(0.85f);
		rightRotation.setColorAlpha(0.85f);
		supprimerP.setColorAlpha(0.85f);
		
//		newPiece.setTextMidV();
//		newPiece.setTextMidH();
//		newPiece.setText("Ajoutï¿½ une nouvelle piece");
//		getBack.setTextMidV();
//		getBack.setTextMidH();
//		getBack.setText("get Back");
//		showAll.setTextMidV();
//		showAll.setTextMidH();
//		showAll.setText("Show All !");
//		buildPiece.setTextMidV();
//		buildPiece.setTextMidH();
//		buildPiece.setText("Build Piece !");
//		makeLevel.setTextMidV();
//		makeLevel.setTextMidH();
//		makeLevel.setText("Commencer");		
		
		pos = "jeu";	
		rempliPos();
		this.game = game;
		for(int i=0; i< particle.length; i++)
		{
			double nb = Math.random();
			if(nb<0.25f)
			{
				particle[i][0] = 0;
				particle[i][1] = (float) (Math.random()*Gdx.graphics.getHeight());
			}
			else if(nb<0.5f)
			{
				particle[i][0] = map.length*Piece.width;
				particle[i][1] = (float) (Math.random()*Gdx.graphics.getHeight());
			}
			else if(nb<0.75f)
			{
				particle[i][0] = (float) (Math.random()*Gdx.graphics.getWidth());
				particle[i][1] = 0;
			}
			else 
			{
				particle[i][0] = (float) (Math.random()*Gdx.graphics.getWidth());
				particle[i][1] = map[0].length;
			}
			particle[i][2] = 0;
			
			particle[i][3] = (Gdx.graphics.getWidth() - particle[i][0])/60;
			particle[i][4] = (Gdx.graphics.getHeight() - particle[i][1])/60;
			particle[i][5] = (float) (Math.random()*4);
		}
	}

	boolean verify()	// verifie si le plateau est finie et est correct
	{
		boolean isComplete = true;
		for(byte i=0; i<map.length - 1; i++)
			for(byte j=0; j<map[0].length -1 ; j++)
 				if(map[i][j].getRight() <= 1 || map[i][j].getRight() != map[i+1][j].getLeft() || map[i][j].getTop() <= 1 || map[i][j].getTop() != map[i][j+1].getBottom())
 					isComplete = false;
		if(map[map.length-1][map[0].length-1].getLeft() <= 1 || map[map.length-1][map[0].length-1].getLeft() != map[map.length-2][map[0].length-1].getRight() || map[map.length-1][map[0].length-1].getBottom() <= 1 || map[map.length-1][map[0].length-1].getBottom() != map[map.length-1][map[0].length-2].getTop())
				isComplete = false;
		if(isComplete){
			System.out.println("Complet");
		}
 					
		return isComplete;	
	}
	public void saveBuild()	// sauvegarde du plateau en resolution automatique ne pas confondre avec la sauvegarde utilisateur
	{
		FileHandle save = Gdx.files.local("lvl/save.selfBuild");
		byte contenu[] = new byte[100000];
		int index=0;
		contenu[0] = (byte) map.length; contenu[1] = (byte) map[0].length;
		index+=2;
		
		for(int i=0; i<piece.size(); i++)
		{
			contenu[index] = piece.get(i).getTop();
			contenu[index+1] = piece.get(i).getRight();
			contenu[index+2] = piece.get(i).getBottom();
			contenu[index+3] = piece.get(i).getLeft();
			index+=4;
		}
		contenu[index]=-10; index++;
		for(int i=0;!map[i%map.length][i/map.length].isEmpty();i++)
		{
			contenu[index ] = map[i%map.length][i/map.length].getTop();
			contenu[index +1] = map[i%map.length][i/map.length].getRight();
			contenu[index +2] = map[i%map.length][i/map.length].getBottom();
			contenu[index +3] =map[i%map.length][i/map.length].getLeft();
			index+=4;
		}
		contenu[index] = -10; index++;

		for(int i=0; i<curseurR; i++)	{
			for(int j=0; j<resolution[i%map.length][i/map.length].size();j++)
			{
				contenu[index ] = resolution[i%map.length][i/map.length].get(j).getTop();
				contenu[index +1] = resolution[i%map.length][i/map.length].get(j).getRight();
				contenu[index +2] = resolution[i%map.length][i/map.length].get(j).getBottom();
				contenu[index +3] = resolution[i%map.length][i/map.length].get(j).getLeft();
				index+=4;
			}
			contenu[index] = -5;index++;
		}
		contenu[index] = -10;
		save.writeBytes( contenu, true);

	}
	public void readSave(String file)	// lecture de la sauvegarde en resolution automatique
	{
		FileHandle save = Gdx.files.local("lvl/save.selfBuild");
		byte contenu[] = save.readBytes();
		int index=0, index2=0, large;
		large = contenu[0]; index+=2;
		while(contenu[index] != -10)
		{
			piece.add(new Piece(contenu[index], contenu[index+1], contenu[index+2], contenu[index+3]));
			index+=4;
		}
		index++;
		while(contenu[index] != -10)
		{
			map[index2%large][index2/large] = new Piece(contenu[index ], contenu[index +1], contenu[index +2], contenu[index +3]);
			index+=4;	index2++;
		}
		index++; index2=0;

		while(contenu[index] != -10)	{
			while(contenu[index] != -5 )
			{
				if(index2/large == 16)
				resolution[index2%large][index2/large].add(new Piece(contenu[index ] , contenu[index +1] , contenu[index +2] , contenu[index+3] ));
				index+=4;
			}
			index++; index2++;	curseurR++;
		}

	}
	public void autoBuild()	// Resolution automatique
	{
		for(int z=0; z<1000000 && continuer; z++)	// il y a 1000000de déplacement entre chaque images
		{
			if( !verify())
			{

				int X = position[curseurR][0], Y = position[curseurR][1];
				if(X == 0)
					buildP.setQ(3, (byte) 1);
				else if(map[X - 1][Y ].isEmpty())
					buildP.setQ(3, (byte) -1); 
				else
					buildP.setQ(3, map[X - 1][Y].getRight());
				
				
				if(X == map.length - 1)
					buildP.setQ(1, (byte) 1);
				else if(map[X + 1][Y].isEmpty())
					buildP.setQ(1, (byte) -1); 
				else
					buildP.setQ(1, map[X + 1][Y].getLeft());
				
				
				if(Y == 0)
					buildP.setQ(2, (byte) 1);
				else if(map[X ][Y - 1].isEmpty())
					buildP.setQ(2, (byte) -1); 
				else
					buildP.setQ(2, map[X ][Y - 1].getTop());
	
				
				if(Y == map[0].length - 1)
					buildP.setQ(0, (byte) 1);
				else if(map[X][Y + 1].isEmpty())
					buildP.setQ(0, (byte) -1); 
				else
					buildP.setQ(0, map[X ][Y + 1].getBottom());		
				
				
				
				if(resolution[X][Y].size() == 0)	{
					for(int i =0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	
						{
							resolution[X][Y].add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(),
									piece.get(i).getBottom(), piece.get(i).getLeft()));	
						
						}
				}
				if(	resolution[X][Y] .size() != 0)
					for(int i=0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(resolution[X][Y].get(0).getTop(), 			
								resolution[X][Y].get(0).getRight(), 			
								resolution[X][Y].get(0).getBottom(), 		
								resolution[X][Y].get(0).getLeft()))
						{
							piece.remove(i);
							i=piece.size();
						}
				if(	resolution[X][Y].size() == 0   )
				{
					while(	resolution[X][Y].size() == 0  )
					{					
					
					
					curseurR--;
					X = position[curseurR][0]; Y = position[curseurR][1];
					piece.add(
							new Piece(map[X][Y].getTop(), map[X][Y].getRight(), map[X][Y].getBottom(),		map[X][Y].getLeft()	))	;
					
					resolution[X][Y].remove(0);
					map[X][Y].set((byte)-1);
					
					}
				}
				else	{
				map[X][Y] = resolution[X][Y].get(0);
				curseurR++;
				}
	
			}
			else
				z = 1000000000;
		}
	}
	
	public boolean verifAuto()	// verification des cases ayant déja des contraintes autour
	{
		boolean haveP = false, canSee = true;
		int x, y;
		for(int i=0; i <= (curseurR < map.length*map[0].length - 1 ? curseurR : curseurR-1); i++)
		{
				haveP = true;
				canSee = true;
				
				x = positionB2[i][0];
				y = positionB2[i][1];
				for(int j=0; j<=curseurR; j++)
				{
					if(x == position[j][0] && y == position[j][1])
						{canSee = false; j = curseurR+1;	}
				}
				if(map[x][y].isEmpty() && canSee)
				{
					haveP = false;

					if(x == 0)
						buildP.setQ(3, (byte) 1);
					else if(map[x - 1][y].isEmpty())
						buildP.setQ(3, (byte) -1); 
					else
						buildP.setQ(3, map[x - 1][y].getRight());
					
					
					if(x == map.length - 1)
						buildP.setQ(1, (byte) 1);
					else if(map[x + 1][y].isEmpty())
						buildP.setQ(1, (byte) -1); 
					else
						buildP.setQ(1, map[x + 1][y].getLeft());
					
					
					if(y == 0)
						buildP.setQ(2, (byte) 1);
					else if(map[x][y - 1].isEmpty())
						buildP.setQ(2, (byte) -1); 
					else
						buildP.setQ(2, map[x][y - 1].getTop());
					
					
					if(y == map[0].length - 1)
						buildP.setQ(0, (byte) 1);
					else if(map[x][y + 1].isEmpty())
						buildP.setQ(0, (byte) -1); 
					else
						buildP.setQ(0, map[x][y + 1].getBottom());
					
					for(int z=0; z<piece.size(); z++)
					{	
						if(piece.get(z).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))
						{	

							haveP = true;
							z = piece.size();	 
						}
					}
					if(!haveP)
						return false;
				}
				map[x][y].setCompatible(haveP);
		}
		return true;		
	}
	public void autoBuild2()	// Resolution automatique 3e algorithme avec verification de toutes les cases ayant des contraintes
	{
		for(int z=0; z<100000 && continuer; z++)
		{
			if( !verify())
			{

				int X = position[curseurR][0], Y = position[curseurR][1];
				
				if(position[curseurR][2] == 0)
				{
					positionB2[curseurR][0] = X + 1;
					positionB2[curseurR][1] = Y;
				}
				else if(position[curseurR][2] == 1)
				{
					positionB2[curseurR][0] = X;
					positionB2[curseurR][1] = Y+1;
				}
				else if(position[curseurR][2] == 2)
				{
					positionB2[curseurR][0] = X-1;
					positionB2[curseurR][1] = Y;
				}
				else if(position[curseurR][2] == 1)
				{
					positionB2[curseurR][0] = X;
					positionB2[curseurR][1] = Y-1;
				}

				if(X == 0)
					buildP.setQ(3, (byte) 1);
				else if(map[X - 1][Y ].isEmpty())
					buildP.setQ(3, (byte) -1); 
				else
					buildP.setQ(3, map[X - 1][Y].getRight());
				
				
				if(X == map.length - 1)
					buildP.setQ(1, (byte) 1);
				else if(map[X + 1][Y].isEmpty())
					buildP.setQ(1, (byte) -1); 
				else
					buildP.setQ(1, map[X + 1][Y].getLeft());
				
				
				if(Y == 0)
					buildP.setQ(2, (byte) 1);
				else if(map[X ][Y - 1].isEmpty())
					buildP.setQ(2, (byte) -1); 
				else
					buildP.setQ(2, map[X ][Y - 1].getTop());
	
				
				if(Y == map[0].length - 1)
					buildP.setQ(0, (byte) 1);
				else if(map[X][Y + 1].isEmpty())
					buildP.setQ(0, (byte) -1); 
				else
					buildP.setQ(0, map[X ][Y + 1].getBottom());	
				
				
				
				if(resolution[X][Y].size() == 0)	{
					for(int i =0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	
						{
							resolution[X][Y].add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(),
									piece.get(i).getBottom(), piece.get(i).getLeft()));	
						
						}
				}
				if(	resolution[X][Y] .size() != 0)
				{
					for(int i=0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(resolution[X][Y].get(0).getTop(), 			
								resolution[X][Y].get(0).getRight(), 			
								resolution[X][Y].get(0).getBottom(), 		
								resolution[X][Y].get(0).getLeft()))
						{
							piece.remove(i);
							i=piece.size();
						}
				}
				if(	resolution[X][Y].size() == 0   )
				{
					while(	resolution[X][Y].size() == 0 )
					{					
					
					
					curseurR--;
					X = position[curseurR][0]; Y = position[curseurR][1];
					piece.add(
							new Piece(map[X][Y].getTop(), map[X][Y].getRight(), map[X][Y].getBottom(),		map[X][Y].getLeft()							))					;
					
					resolution[X][Y].remove(0);
					map[X][Y].set((byte)-1);
					
					}
				}

				else	{
				map[X][Y] = resolution[X][Y].get(0);
				curseurR++;
				}
				if (test > 1000 )
				{
					while(	!verifAuto())
					{	
						
						curseurR--;

						X = position[curseurR][0]; Y = position[curseurR][1];
						piece.add(new Piece(map[X][Y].getTop(), map[X][Y].getRight(), map[X][Y].getBottom(), map[X][Y].getLeft()))	;

						resolution[X][Y].remove(0);
						map[X][Y].set((byte)-1);
						resolution[X][Y].clear();

					}
					test = 0;
				}
	
			}
			else
				z = 1000000000;
			test++;
		}
	}
	public void autoBuildDoublon()	// Resolution automatique avec doublon pour les map contenant des doublons 
	{
		for(int z=0; z<100000 && continuer; z++)
		{
			if( !verify())
			{

				int X = position[curseurR][0], Y = position[curseurR][1];
				if(X == 0)
					buildP.setQ(3, (byte) 1);
				else if(map[X - 1][Y ].isEmpty())
					buildP.setQ(3, (byte) -1); 
				else
					buildP.setQ(3, map[X - 1][Y].getRight());
				
				
				if(X == map.length - 1)
					buildP.setQ(1, (byte) 1);
				else if(map[X + 1][Y].isEmpty())
					buildP.setQ(1, (byte) -1); 
				else
					buildP.setQ(1, map[X + 1][Y].getLeft());
				
				
				if(Y == 0)
					buildP.setQ(2, (byte) 1);
				else if(map[X ][Y - 1].isEmpty())
					buildP.setQ(2, (byte) -1); 
				else
					buildP.setQ(2, map[X ][Y - 1].getTop());
	
				
				if(Y == map[0].length - 1)
					buildP.setQ(0, (byte) 1);
				else if(map[X][Y + 1].isEmpty())
					buildP.setQ(0, (byte) -1); 
				else
					buildP.setQ(0, map[X ][Y + 1].getBottom());		
				
				
				
				if(resolution[X][Y].size() == 0)	{
					for(int i =0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	
						{
							resolution[X][Y].add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(),
									piece.get(i).getBottom(), piece.get(i).getLeft()));	
						
						}
				}
				for(int i=0; i <resolution[X][Y].size(); i++)
				{
					for(int j=i+1;j<resolution[X][Y].size();j++)
					{
						if(resolution[X][Y].get(i).isCompatible(resolution[X][Y].get(j).getTop(), resolution[X][Y].get(j).getRight(), resolution[X][Y].get(j).getBottom(), resolution[X][Y].get(j).getLeft()))
						{
							resolution[X][Y].remove(j);
						}
					}
				}
				if(	resolution[X][Y] .size() != 0)
//					piece.remove(posPiece[X][Y].get(0));

				for(int i=0; i<piece.size(); i++)	
					if(piece.get(i).isCompatible(resolution[X][Y].get(0).getTop(), 			
							resolution[X][Y].get(0).getRight(), 			
							resolution[X][Y].get(0).getBottom(), 		
							resolution[X][Y].get(0).getLeft()))
					{
						piece.remove(i);
						i=piece.size();
					}
				if(	resolution[X][Y].size() == 0   )
				{
					while(	resolution[X][Y].size() == 0  )
					{					
					
					
					curseurR--;
					X = position[curseurR][0]; Y = position[curseurR][1];
					piece.add(
							new Piece(map[X][Y].getTop(), map[X][Y].getRight(), map[X][Y].getBottom(),		map[X][Y].getLeft()							))					;
					
					resolution[X][Y].remove(0);
					map[X][Y].set((byte)-1);
					
					}
				}
				else	{
				map[X][Y] = resolution[X][Y].get(0);
				curseurR++;
				}
	
			}
			else
				z = 1000000000;
		}
	}
	public void autreSaisi()	// resaisir un nom de sauvegarde tant que celui rentré existe déjà 
	{
		 Gdx.input.getTextInput(new TextInputListener() {
	         
	         @Override
	         public void input(String texteSaisi) {
	         message = texteSaisi;
	         if(Gdx.files.internal("save/"+message+".build").exists())
	         	autreSaisi();
	         else 
             	saveGame(texteSaisi);
	         }
	         @Override
	         public void canceled() {
	         }
	},  "dï¿½ja pris, choisi autre chose", "", "maSauvegarde");	
	}
	public void jeu(float delta){	// plateau, fonction principal

		if(Gdx.input.isKeyJustPressed(Keys.L))	
			verifP();
		
		if(Gdx.input.isKeyJustPressed(Keys.P))	{
			if(isAuto)
				isAuto = false;
			else
				isAuto = true;
		}

		if(verify())	{
			pos = "endGame";
			cam.position.x = map.length/2 * Piece.width ;
			cam.position.y = map[0].length/2 * Piece.width ;
			cam.zoom = 0.001f;
		}
		
		
		selectCase = new int[2];
		selectCase[0]=-1;
		if(!isAuto)
			{
			newPiece.update(delta);
			
			rightRotation.update(delta);
			leftRotation.update(delta);
			supprimerP.update(delta);
			setAuto.update(delta);
			Save.update(delta);
			for(int i=0; i<piece.size(); i++)
				piece.get(i).update(delta);
			
			if(setAuto.isJustTouched() && pieceS == null)
			{
				cam.position.x = map.length*Piece.width/2;
				cam.position.y = map[0].length*Piece.width/2;
				if( (map.length*Piece.width)/Gdx.graphics.getWidth() > (map[0].length*Piece.width)/Gdx.graphics.getHeight())
					cam.zoom = ((float)map.length*Piece.width)/Gdx.graphics.getWidth();
				else
					cam.zoom = ((float)map[0].length*Piece.width)/Gdx.graphics.getHeight();

				isAuto = true;
				for(int i=0;i<map.length; i++)
					for(int j=0; j<map[0].length; j++)
					{
						if(!map[i][j].isEmpty())
						{
							piece.add(new Piece(map[i][j].getTop(), map[i][j].getRight(), map[i][j].getBottom(), map[i][j].getLeft()));
							map[i][j].set((byte)1);		// ORIGINE DU CRASH LORS DE LA SOUTENANCE
							// Lors de la soutenance, la pièce ajouté était identique, ce qui ajoutais une piece (1, 1, 1, 1).
							// Ce problème à été corrigé, le programme ne peut plus planté, du moins à notre connaissance.
							piece.get(piece.size()-1).presente();	
						}
					}
			}
			else if(newPiece.isJustTouched() && pieceS == null)	{
				pos = "buildPiece";
				posCamGame[0] = cam.position.x;
				posCamGame[1] = cam.position.y;
				posCamGame[2] = cam.zoom;
				cam.position.x = Gdx.graphics.getWidth()/2;
				cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight() * 0.2f;
				cam.zoom = 1;
				compatibles.clear();
				for(int i =0; i<piece.size(); i++)	
					if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	{
						compatibles.add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(), piece.get(i).getBottom(), piece.get(i).getLeft()));
						
					}	
			}
			else if(Save.isJustTouched())
			{
				if(pieceS != null )
				{
					piece.add(pieceS);
					pieceS = null;
				}
	            Gdx.input.getTextInput(new TextInputListener() {
	                
	                @Override
	                public void input(String texteSaisi) {
	                message = texteSaisi;
	                if(Gdx.files.internal("save/"+message+".build").exists())
	                	autreSaisi();
	                else 
	                	saveGame(texteSaisi);
	                }
	                @Override
	                public void canceled() {
	                }
	     },  "choisis un nom pour ta sauvegarde", "", "maSauvegarde");				
			}
			else if(Gdx.input.justTouched())
			{
				selection[0] = touchX;
				selection[1] = touchY;
			}
	
			else if(x > 0 && x < map.length * Piece.width && y > 0 && y < map[0].length * Piece.width && 
					selection[1]<Gdx.graphics.getHeight()*0.8f && !Gdx.input.isTouched() && selection[0]!=-1 && Math.abs(selection[0] - touchX) < Gdx.graphics.getWidth()/10 && Math.abs(selection[1] - touchY) < Gdx.graphics.getHeight()/10) // si on viens seulement de relacher l'appui
			{					// et que nous avons pas trop bouger la souris donc que l'on souhaite effectuer un changement sur la map et non la dÃ©placer
				if(pieceS != null)	// si une piÃ¨ce est selectionner
				{
					if(map[(int)(x/Piece.width)][(int)(y/Piece.width)].isEmpty())	// si l'emplacement est vide on pose la piÃ¨ce
					{
						map[(int)(x/Piece.width)][(int)(y/Piece.width)] = pieceS;
						pieceS = null;
						verifP();
					}
					
					else	// sinon, elle est posÃ©, puis la piÃ¨ce qui Ã©tait dÃ©jÃ  prÃ©sente devient la piÃ¨ce sÃ©lÃ©ctionner
					{
						pieceSwap = pieceS;
						pieceS = map[(int)(x/Piece.width)][(int)(y/Piece.width)];
						map[(int)(x/Piece.width)][(int)(y/Piece.width)] = pieceSwap;
						verifP();

					}
				}
				else // s'il n'y a pas de piÃ¨ce sÃ©lectionner
				{
					
					if(!map[(int)(x/Piece.width)][(int)(y/Piece.width)].isEmpty())	// si il y a une piÃ¨ce a l'emplacement choisi, alors elle devient la piÃ¨ce sÃ©lectionner
					{
						pieceS = map[(int)(x/Piece.width)][(int)(y/Piece.width)];
						map[(int)(x/Piece.width)][(int)(y/Piece.width)] = pieceN;
						verifP();

	
					}
					else if(!map[(int)(x/Piece.width)][(int)(y/Piece.width)].isCompatible())	{}
					else if((int)(x/Piece.width)> 0 && (int)(x/Piece.width)< map.length - 1 && (int)(y/Piece.width) > 0 && (int)(y/Piece.width) < map[0].length - 1 &&  map[(int)(x/Piece.width) - 1][(int)(y/Piece.width)].isEmpty()
							&&  map[(int)(x/Piece.width) + 1][(int)(y/Piece.width)].isEmpty() &&  map[(int)(x/Piece.width)][(int)(y/Piece.width)-1].isEmpty() &&  map[(int)(x/Piece.width)][(int)(y/Piece.width) + 1].isEmpty())
						{
							posCamGame[0] = cam.position.x;
							posCamGame[1] = cam.position.y;
							posCamGame[2] = cam.zoom;
							cam.position.x = Gdx.graphics.getWidth()/2;
							cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight() * 0.2f;
							cam.zoom = 5;
							pos = "newPiece2" ;
							selectCase[0] = (int) (x/Piece.width);
							selectCase[1] = (int) (y/Piece.width);
	
						}
					else
					{
						if((int)(x/Piece.width) == 0)
							buildP.setQ(3, (byte) 1);
						else if(map[(int)(x/Piece.width) - 1][(int)(y/Piece.width) ].isEmpty())
							buildP.setQ(3, (byte) -1); 
						else
							buildP.setQ(3, map[(int)(x/Piece.width) - 1][(int)(y/Piece.width)].getRight());
						
						
						if((int)(x/Piece.width) == map.length - 1)
							buildP.setQ(1, (byte) 1);
						else if(map[(int)(x/Piece.width) + 1][(int)(y/Piece.width)].isEmpty())
							buildP.setQ(1, (byte) -1); 
						else
							buildP.setQ(1, map[(int)(x/Piece.width) + 1][(int)(y/Piece.width)].getLeft());
						
						
						if((int)(y/Piece.width) == 0)
							buildP.setQ(2, (byte) 1);
						else if(map[(int)(x/Piece.width) ][(int)(y/Piece.width) - 1].isEmpty())
							buildP.setQ(2, (byte) -1); 
						else
							buildP.setQ(2, map[(int)(x/Piece.width) ][(int)(y/Piece.width) - 1].getTop());
						
						
						if((int)(y/Piece.width) == map[0].length - 1)
							buildP.setQ(0, (byte) 1);
						else if(map[(int)(x/Piece.width)][(int)(y/Piece.width) + 1].isEmpty())
							buildP.setQ(0, (byte) -1); 
						else
							buildP.setQ(0, map[(int)(x/Piece.width) ][(int)(y/Piece.width) + 1].getBottom());
	
						posCamGame[0] = cam.position.x;
						posCamGame[1] = cam.position.y;
						posCamGame[2] = cam.zoom;
						cam.position.x = Gdx.graphics.getWidth()/2;
						cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight() * 0.2f;
						cam.zoom = 1;
	
						selectCase[0] = (int) (x/Piece.width);
						selectCase[1] = (int) (y/Piece.width);
						pos = "buildPiece" ;
						compatibles.clear();
						for(int i =0; i<piece.size(); i++)	
							if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	{
								compatibles.add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(), piece.get(i).getBottom(), piece.get(i).getLeft()));
								
							}	
					}
					
				}
			}
	
			if(!Gdx.input.isTouched())	// pas d'appui
				selection[0] = -1;
			else
			{
				if(map.length*Piece.width > Gdx.graphics.getWidth())
				{
					cam.position.x -= (touchX - oldX)*cam.zoom;
					if(cam.position.x > (map.length+1) * Piece.width - Gdx.graphics.getWidth()/2)
						cam.position.x = (map.length+1) * Piece.width - Gdx.graphics.getWidth()/2;
					else if (cam.position.x < Gdx.graphics.getWidth()/2 - Piece.width)
						cam.position.x = Gdx.graphics.getWidth()/2 - Piece.width;
				}
				else 
					cam.position.x = map.length/2*Piece.width;
				
				if(map[0].length*Piece.width > Gdx.graphics.getHeight() )
				{
					cam.position.y += (touchY - oldY)*cam.zoom;
					if(cam.position.y > (map[0].length+1) * Piece.width - Gdx.graphics.getHeight()/2)
						cam.position.y = (map[0].length+1) * Piece.width - Gdx.graphics.getHeight()/2;
					else if(cam.position.y < Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width)
						cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width;
				}
				else 
					cam.position.y = map[0].length/2*Piece.width - Gdx.graphics.getHeight()*0.1f;
			}
			
			if(pieceS != null)
			{
				
				if(Gdx.input.isKeyJustPressed(Keys.R) || rightRotation.isJustTouched())	
					pieceS.rightRotation();
				if(Gdx.input.isKeyJustPressed(Keys.E) || leftRotation.isJustTouched())	
					pieceS.leftRotation();	
				if(Gdx.input.isKeyJustPressed(Keys.D) )	
					pieceS.doubleRotation();	
				
				pieceS.update(delta);
				if(Gdx.input.isKeyJustPressed(Keys.S) || supprimerP.isJustTouched())	
				{
					piece.add(pieceS);
					pieceS = null;
					verifP();

				}
			}
		}
		else
		{
			if(!doublon  )	{
				autoBuild2();
		

			}
			else
				autoBuildDoublon();
		}
		batch.begin();
		for(int i=0; i<particle.length;i++)
		{
			particle[i][0] += particle[i][3];
			particle[i][1] += particle[i][4];
			particle[i][2] += particle[i][5];
			batch.draw(res.particleS, particle[i][0], particle[i][1], 0, 0, (particle[i][6] - particle[i][7])/30, (particle[i][6] - particle[i][7])/30, 1, 1, particle[i][2]);
		}
		for(int i=0 ; i<map.length; i++)
			for(int j=0; j<map[i].length; j++)
				map[i][j].draw(batch, i*Piece.width, j*Piece.width);

		if(pieceS != null && Gdx.app.getType() == Application.ApplicationType.Desktop)
			pieceS.draw(batch, cam.position.x - (Gdx.graphics.getWidth()/2)*cam.zoom + Gdx.input.getX() *cam.zoom - Piece.width/2, 
				 cam.position.y - (Gdx.graphics.getHeight()/2)*cam.zoom + (Gdx.graphics.getHeight() - Gdx.input.getY())  * cam.zoom - Piece.width/2);
		
		batch.end();
		if(!isAuto)
		{
		batchI.begin();
		Save.draw(batchI);
			if(pieceS == null)	{
				newPiece.draw(batchI);
				setAuto.draw(batchI);
			}
			else	{
				
				pieceS.draw(batchI, 0, 0, Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);
				rightRotation.draw(batchI);
				leftRotation.draw(batchI);
				supprimerP.draw(batchI);
			}
			batchI.end();
		}
	}
	public void newPiece1(float delta) // menu selection entre voir tout et construire la piece
	{
		cam.position.x = Gdx.graphics.getWidth()/2;
		cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight() * 0.2f;
		cam.zoom = 1;
		getBack.update(delta);
		showAll.update(delta);
		buildPiece.update(delta);
		batchI.begin();
		
		getBack.draw(batchI);
		showAll.draw(batchI);
		buildPiece.draw(batchI);
		
		batchI.end();
		
		if(getBack.isJustTouched())	{
			pos = "jeu";
			cam.position.x = posCamGame[0];
			cam.position.y = posCamGame[1];
			cam.zoom = posCamGame[2];
		}
		if(showAll.isJustTouched())
			pos = "newPiece2";
		else if (buildPiece.isJustTouched())
			pos = "buildPiece";
	}
	
	public void newPiece2(float delta)	// selection d'une piece parmis toutes celles de disponible dans notre liste de piece
	{

		getBack.update(delta);
		batch.begin();
		for(byte i=0 ; i<= piece.size() / 10; i++)
			for(byte j=0; j< (i < piece.size()/10 ? 10 : piece.size()%10) ; j++)
				piece.get(i*10 + j).draw(batch, j*Piece.width, i*Piece.width);			
		
		batch.end();
		
		batchI.begin();
		
		getBack.draw(batchI);
		
		batchI.end();
		
		if(getBack.isJustTouched())
		{	pos = "jeu";
			cam.position.x = posCamGame[0];
			cam.position.y = posCamGame[1];
			cam.zoom = posCamGame[2];
		}
		else if(Gdx.input.justTouched())
		{
			selection[0] = touchX;
			selection[1] = touchY;

		}
		if(	!Gdx.input.isTouched() && selection[0]!=-1 && Math.abs(selection[0] - touchX) < Gdx.graphics.getWidth()/10 && Math.abs(selection[1] - touchY) < Gdx.graphics.getHeight()/10) // si on viens seulement de relacher l'appui
		{
			if((int)(y/Piece.width)*10 + (int)(x/Piece.width) < piece.size())
			{
				pos = "jeu";
				cam.position.x = posCamGame[0];
				cam.position.y = posCamGame[1];
				cam.zoom = posCamGame[2];
				if(selectCase[0] == -1)
					pieceS = piece.get((int)(y/Piece.width)*10 + (int)(x/Piece.width));
				else	{
					map[selectCase[0]][selectCase[1]] = piece.get((int)(y/Piece.width)*10 + (int)(x/Piece.width));
					selectCase[0] = -1;
				}
				piece.remove((int)(y/Piece.width)*10 + (int)(x/Piece.width));
				verifP();

			}
		}

		if(!Gdx.input.isTouched())
			selection[0]=-1;
		else
		{
			if(piece.size() * Piece.width > Gdx.graphics.getWidth())
			{
				cam.position.x -= (touchX - oldX)*cam.zoom;
				if(cam.position.x > (piece.size() < 10 ? piece.size()%10 + 1 : 12) * Piece.width - Gdx.graphics.getWidth()/2)
					cam.position.x = (piece.size() < 10 ? piece.size()%10 + 1 : 12) * Piece.width  - Gdx.graphics.getWidth()/2;
				else if (cam.position.x < Gdx.graphics.getWidth()/2 - Piece.width)
					cam.position.x = Gdx.graphics.getWidth()/2 - Piece.width;
			}
			else
				cam.position.x = piece.size()/2*Piece.width;
			
			if(piece.size()/10 * Piece.width > Gdx.graphics.getHeight())
			{
				cam.position.y += (touchY - oldY)*cam.zoom;
				if(cam.position.y >  (2+piece.size()/10) * Piece.width )
					cam.position.y = (2+piece.size()/10) * Piece.width ;
				else if(cam.position.y < Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width)
					cam.position.y = Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()*0.2f - Piece.width;
			}
			else
				cam.position.y = (piece.size()/10)/2*Piece.width ;
		}		
	}
	
	public void buildPiece(float delta)	// selection d'une piece avec contraintes
	{

		getBack.update(delta);
		if(Gdx.app.getType() == ApplicationType.Desktop)
			select = (int) ((Gdx.input.getX() - Gdx.graphics.getWidth() * 0.45f)/Piece.width + 
				nbPiece*(int)((Gdx.input.getY() - Gdx.graphics.getHeight()*0.20 + curseurB)/Piece.width));
		else 
			select = (int) ((Gdx.input.getX() - Gdx.graphics.getWidth() * 0.45f)/Piece.width + 
					nbPiece*(int)((Gdx.input.getY() - Gdx.graphics.getHeight()*0.35f + curseurB)/Piece.width));

		batch.begin();

		
		for(byte i=0; i<=compatibles.size()/nbPiece; i++)
			for(byte j=0; j<( i == compatibles.size()/nbPiece  ? compatibles.size()%nbPiece : nbPiece) ; j++ ) {
//				if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
//					if (Gdx.graphics.getHeight() * 0.38f - i * Piece.width + curseurB < Gdx.graphics.getHeight() / 100 * 47)
//						compatibles.get(i * nbPiece + j).draw(batch, Gdx.graphics.getWidth() * 0.45f + j * Piece.width, Gdx.graphics.getHeight() * 0.38f - i * Piece.width + curseurB);
//					System.out.println("desktop");
//				} else
				if (Gdx.graphics.getHeight() * 0.38f - i * Piece.width + curseurB <   Gdx.graphics.getHeight() - Piece.width*2.5f)
				{
					compatibles.get(i * nbPiece + j).draw(batch, Gdx.graphics.getWidth() * 0.45f + j * Piece.width, Gdx.graphics.getHeight() * 0.38f - i * Piece.width + curseurB);
				}
			}


		batch.end();
		batchI.begin();
		for(byte i=0;i<res.piece.length;i++)
			batchI.draw(res.piece[i], i*Piece.width + curseur, Gdx.graphics.getHeight() - Piece.width  , 0, 0, Piece.width, Piece.width, 1, 1, 0);

		
		getBack.draw(batchI);
		
		buildP.draw(batchI,  Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4);
		if(side == 0)
			batchI.draw(res.side, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/3 , 0, 0, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4, 1, 1, 0);
		else if(side == 3)
			batchI.draw(res.side, Gdx.graphics.getWidth()/10 + Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3 , 0, 0, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4, 1, 1, 90);
		else if(side == 2)
			batchI.draw(res.side, Gdx.graphics.getWidth()/10 + Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3 + Gdx.graphics.getWidth()/4, 0, 0, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4, 1, 1, 180);
		else
		batchI.draw(res.side, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/3 + Gdx.graphics.getWidth()/4, 0, 0, Gdx.graphics.getWidth()/4, Gdx.graphics.getWidth()/4, 1, 1, 270);

		batchI.end();
		
		
		if(getBack.isJustTouched())	{
			pos = "jeu";
			buildP.set((byte) -1);
			cam.position.x = posCamGame[0];
			cam.position.y = posCamGame[1];
			cam.zoom = posCamGame[2];
		}
		else if(Gdx.input.justTouched())
		{
			selection[0] = touchX;
			selection[1] = touchY;

		}
		if(	!Gdx.input.isTouched() && selection[0]!=-1 && Math.abs(selection[0] - touchX) < Gdx.graphics.getWidth()/10 && 
				Math.abs(selection[1] - touchY) < Gdx.graphics.getHeight()/10) // si on viens seulement de relacher l'appui
		{
			xBuild = Gdx.input.getX() - Gdx.graphics.getWidth()/10;
			yBuild = Gdx.graphics.getHeight() - Gdx.input.getY() - Gdx.graphics.getHeight()/3;
			if(Gdx.input.getX() > Gdx.graphics.getWidth()*0.45f && Gdx.input.getX() < Gdx.graphics.getWidth()*0.45f + nbPiece*Piece.width &&
					Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight()*0.2f &&	Gdx.input.getY() > Piece.width) 
			{
				if(select >= 0 && select < compatibles.size())
				{
					if(selectCase[0] == -1)
						pieceS = compatibles.get(select);
					else	{
						map[selectCase[0]][selectCase[1]] = compatibles.get(select);
						selectCase[0] = -1;
					}
					for(int i=0; i<piece.size(); i++)	
						if(piece.get(i).isCompatible(compatibles.get(select).getTop(), compatibles.get(select).getRight(), compatibles.get(select).getBottom(), compatibles.get(select).getLeft()))
						{
							piece.remove(i);
							i=piece.size();
						}
					compatibles.clear();
					pos = "jeu";
					buildP.set((byte) -1); 
					cam.position.x = posCamGame[0];
					cam.position.y = posCamGame[1];
					cam.zoom = posCamGame[2];
					verifP();

				}
			}
			if(Gdx.graphics.getHeight() - Gdx.input.getY() > Gdx.graphics.getHeight() - Piece.width/2)	// selection d'un dessin 
			{
				buildP.setQ(side, res.getNPiece((byte) ((-curseur + Gdx.input.getX() )/Piece.width)));
				compatibles.clear();
				for(int i =0; i<piece.size(); i++)	
					if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	{
						compatibles.add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(), piece.get(i).getBottom(), piece.get(i).getLeft()));
					}

			}
			if(xBuild > 0 && xBuild < widthB && yBuild > 0 && yBuild < widthB)
			{
				if(xBuild < widthB/2 && yBuild>xBuild && yBuild < widthB - xBuild)	{
					if(side == 3)
						buildP.setQ(3, (byte) -1);
					side = 3;
				}
				else if(xBuild > widthB/2 && yBuild > widthB - xBuild && yBuild < xBuild) {
					if(side == 1)
						buildP.setQ(1, (byte) -1);
					side = 1;
				}
				else if(yBuild < widthB/2 && xBuild>yBuild && xBuild < widthB - yBuild) {
					if(side == 2)
						buildP.setQ(2, (byte) -1);
					side = 2;
				}
				else  {
					if(side == 0)
						buildP.setQ(0, (byte) -1);
					side = 0;
				}
				compatibles.clear();
				for(int i =0; i<piece.size(); i++)	
					if(piece.get(i).isCompatible(buildP.getTop(), buildP.getRight(), buildP.getBottom(), buildP.getLeft()))	{
						compatibles.add(new Piece(piece.get(i).getTop(), piece.get(i).getRight(), piece.get(i).getBottom(), piece.get(i).getLeft	()));
						
					}	
		

			}
		}

		if(!Gdx.input.isTouched())
			selection[0]=-1;
		else
		{
			if(Math.abs((touchX - oldX)*cam.zoom) < 100)
				curseur += (touchX - oldX)*cam.zoom;
			if(curseur <  -res.piece.length*Piece.width + Gdx.graphics.getWidth())
				curseur = -res.piece.length*Piece.width + Gdx.graphics.getWidth();
			else if (curseur > 0)
				curseur = 0;

			if(Math.abs((touchY - oldY)*cam.zoom) < 100)
				curseurB -= (touchY - oldY)*cam.zoom;
			if(curseurB > (compatibles.size()/nbPiece + 1)*Piece.width )
				curseurB = (compatibles.size()/nbPiece + 1)*Piece.width;
			else if(curseurB < 0)
				curseurB = 0;
		}		
	}
		
	void saveGame(String nom)	// sauvegarde utilisateur
	{
		FileHandle save = Gdx.files.local("save/"+nom+".build");
		byte contenu[] = new byte[100000];
		int index=0;
		contenu[0] = (byte) map.length; contenu[1] = (byte) map[0].length;
		index+=2;
		
		for(int i=0; i<piece.size(); i++)
		{
			contenu[index] = piece.get(i).getTop();
			contenu[index+1] = piece.get(i).getRight();
			contenu[index+2] = piece.get(i).getBottom();
			contenu[index+3] = piece.get(i).getLeft();
			index+=4;
		}
		
		contenu[index]=-10; index++;
		for(int i=0;i<map.length*map[0].length;i++)
		{
			contenu[index ] = map[i%map.length][i/map.length].getTop();
			contenu[index +1] = map[i%map.length][i/map.length].getRight();
			contenu[index +2] = map[i%map.length][i/map.length].getBottom();
			contenu[index +3] =map[i%map.length][i/map.length].getLeft();
			index+=4;
		}
		contenu[index] = -10; index++;

		contenu[index] = -10;
		save.writeBytes( contenu, true);
	}
	
	public void readSaveGame(String file)	// lecture d'une sauvegarde
	{
		FileHandle save = Gdx.files.local("save/"+file);
		byte contenu[] = save.readBytes();
		int index=0, index2=0, large, longueur;
		large = contenu[0]; longueur = contenu[1];
		map = new Piece[contenu[0]][contenu[1]]; index+=2;
		while(contenu[index] != -10)
		{
			piece.add(new Piece(contenu[index], contenu[index+1], contenu[index+2], contenu[index+3]));
			index+=4;
		}
		index++;
		while(contenu[index] != -10)
		{
			map[index2%large][index2/large] = new Piece(contenu[index ], contenu[index +1], contenu[index +2], contenu[index +3]);
			if(map[index2%large][index2/large].isCompatible((byte)1, (byte)1, (byte)1, (byte)1))
				map[index2%large][index2/large].setEmpty(true);
			index+=4;	index2++;
		}
		index++; index2=0;
	}
	
	void endGame(float delta)	// animation en fin de jeu
	{
		timeAnimation+=delta;
	
		batch.begin();
	
		for(int i=0 ; i<map.length; i++)
			for(int j=0; j<map[i].length; j++)	
			{
				if(timeAnimation>8)
				{
					explosion[i][j][0]+=explosion[i][j][3];
					explosion[i][j][1]+=explosion[i][j][4];
					explosion[i][j][2]+=explosion[i][j][5];
				}
				for(int x=0;x<1;x++)
					for(int z=0;z<1;z++)
						map[i][j].draw(batch, explosion[i][j][0] + Piece.width*x*map.length,  explosion[i][j][1] + Piece.width*z*map[0].length, explosion[i][j][2]);
			}
				
		batch.end();
		
		batchI.begin();
		if(timeAnimation < 1){
			cam.zoom = 0.5f+timeAnimation;
		}
		else if(timeAnimation < 2)	{
			cam.zoom = 1+(timeAnimation-1)*6;
		}
		else if(timeAnimation < 3){
			cam.zoom = 1+(timeAnimation-2)*6;
		}
		else if(timeAnimation < 4){
			cam.zoom = 1+(timeAnimation-3)*10;
		}
		else if(timeAnimation < 4.5f){
			cam.zoom = 0.7f+(timeAnimation-4)*20;
		}
		else if(timeAnimation < 5){
			cam.zoom = 0.5f+(timeAnimation-4.5f)*30;
		}
		else if(timeAnimation < 5.4f){
			cam.zoom = 0.4f+(timeAnimation-5)*30;
		}
		else if(timeAnimation < 5.8f){
			cam.zoom = 0.4f+(timeAnimation-5.4f)*30;
		}
		else if(timeAnimation < 5.8f){
			cam.zoom = 0.4f+(timeAnimation-5.8f)*30;
		}
		else if(timeAnimation < 6.1f){
			cam.zoom = 0.4f+(timeAnimation-6.1f)*30;
		}
		else if(timeAnimation < 6.35f){
			cam.zoom = 0.4f+(timeAnimation-6.35f)*30;
		}
		else if(timeAnimation < 8){
			cam.zoom = (timeAnimation - 6.35f)*4;
		}
		else if(timeAnimation < 10)
		{
			res.felicitation.draw(batchI, (timeAnimation - 8)/2);
		}
		else 
			res.felicitation.draw(batchI);

		batchI.end();
		if(Gdx.input.isKeyJustPressed(Keys.B) || Gdx.input.justTouched())	{
			dispose();
			game.setScreen(new Menu(game));

		}
	}
	@Override
	public void render(float delta) 
	{
		if(Gdx.input.justTouched())
		{
			res.click.play();
			Gdx.input.vibrate(100);
		}
		cam.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		for(int i=0; i<particle.length;i++)
		{
			particle[i][7]++;
			if(particle[i][7] >=particle[i][6])
			{
				double nb = Math.random();
				if(nb<0.25f)
				{
					particle[i][0] = 0;
					particle[i][1] = (float) (Math.random()*map[0].length*Piece.width);
				}
				else if(nb<0.5f)
				{
					particle[i][0] = map.length*Piece.width;
					particle[i][1] = (float) (Math.random()*map[0].length*Piece.width);
				}
				else if(nb<0.75f)
				{
					particle[i][0] = (float) (Math.random()*map.length*Piece.width);
					particle[i][1] = 0;
				}
				else
				{
					particle[i][0] = (float) (Math.random()*map.length*Piece.width);
					particle[i][1] = map[0].length*Piece.width;
				}
				
				particle[i][2] = 0;
				particle[i][6] = (float) (Math.random()*1000);
				particle[i][7] = 0;

				particle[i][3] = -(map.length*Piece.width/2 - particle[i][0])/particle[i][6];
				particle[i][4] = -(map[0].length*Piece.width/2 - particle[i][1])/particle[i][6];
				particle[i][5] = (float) (Math.random()*4);
			}
		}

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
			if(pos != "buildScreen")
				if((distance - oldDistance > 0 && cam.zoom >0.6f) || (distance - oldDistance < 0 && cam.zoom <3))
					cam.zoom -= cam.zoom * (distance - oldDistance) / Gdx.graphics.getWidth();
		}
		else
			justTouched1 = false;

		x = cam.position.x + Gdx.input.getX()*cam.zoom - Gdx.graphics.getWidth()/2*(cam.zoom);
		y = cam.position.y + (Gdx.graphics.getHeight() - Gdx.input.getY())*cam.zoom - Gdx.graphics.getHeight()/2*cam.zoom;


		if(pos == "jeu")
			jeu(delta);
		else if (pos == "newPiece1")
			newPiece1(delta);
		else if (pos == "newPiece2")
			newPiece2(delta);	
		else if (pos == "buildPiece")
			buildPiece(delta);
		else 
			endGame(delta);
		
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

		dispose();
	}

	@Override
	public void dispose() {
		
		buildPiece.dispose();
		newPiece.dispose();
		showAll.dispose();
		buildPiece.dispose();
		getBack.dispose(); 
		makeLevel.dispose();
		rightRotation.dispose();
		leftRotation.dispose();
		supprimerP.dispose();
		setAuto.dispose();
		Save.dispose();
		piece.clear(); 
		compatibles.clear();
		for(int i=0; i<resolution.length; i++)
			for(int j=0; j< resolution[0].length;j++)
				resolution[i][j].clear();

	}

}
