package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class zoneInterface   {
	
	private int positionX, positionY, largeur, hauteur, cursor;
	private Color couleur;
	private Pixmap pixmap;
	private Sprite sprite;
	
	private boolean isTouch, isJustTouched;
	private float touchX, touchY;
	private float timeTouch, timeLastTouch;
	private BitmapFont font;
	
	private boolean middleH=false, middleV=false, right=false, bottom=false, onRight=false;
	private float posTextX, posTextY;
	private String text = "";
	
	/**
	 * les largeurs / longueurs et positions sont en pourcentage 
	 *
	 */
	public zoneInterface(float positionX, float positionY, float largeur, float hauteur, Color couleur) 
	{
		
		this.positionX=(int)(positionX * Gdx.graphics.getWidth()/100);
		this.positionY=(int)(positionY * Gdx.graphics.getHeight()/100);
		this.largeur=(int)(largeur * Gdx.graphics.getWidth()/100);
		this.hauteur=(int)(hauteur * Gdx.graphics.getHeight()/100);
		this.couleur=couleur;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		//font.getData().setScale(InGameScreen.ratioWidth, InGameScreen.ratioHeight);
		font.getData().setScale(1, 1);
		Create();
	}
	public zoneInterface(float positionX, float positionY, float largeur, float hauteur, TextureRegion region) 
	{

		this.positionX=(int)(positionX * Gdx.graphics.getWidth()/100);
		this.positionY=(int)(positionY * Gdx.graphics.getHeight()/100);
		this.largeur=(int)(largeur * Gdx.graphics.getWidth()/100);
		this.hauteur=(int)(hauteur * Gdx.graphics.getHeight()/100);
		
		sprite = new Sprite(region);
		sprite.setPosition(positionX, positionY);
		sprite.setSize(this.largeur, this.hauteur);
		font = new BitmapFont();
		font.setColor(Color.WHITE);
	}
	public zoneInterface(int positionX, int positionY, int largeur, int hauteur)
	{
		this(positionX, positionY, largeur, hauteur, new Color(Color.rgba8888(0, 0, 0, 0)));
	}
	public zoneInterface(int positionX, int positionY, int largeur, int hauteur, float r,  float g, float b, float a)
	{
		this(positionX, positionY, largeur, hauteur, new Color(Color.rgba8888(r, g, b, a)));
	}
	private void Create()
	{
		pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(couleur);
		pixmap.fillRectangle(0, 0, largeur, hauteur );
		sprite = new Sprite(new TextureRegion(new Texture(pixmap), largeur, hauteur));
		sprite.setPosition(positionX, positionY );
	}
	public void draw(Batch batch)
	{
		sprite.draw(batch);
		if(!text.isEmpty())
			font.draw(batch, text, posTextX, posTextY);

	}
	public void setColorAlpha(float a)
	{
		sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, a);
	}
	public void update(float deltaTime)
	{
//		sprite.setScale(InGameScreen.camera.zoom);
		sprite.setPosition(positionX, positionY );
				
		if(!middleH && !right && !onRight)
			posTextX = positionX;
		else if(middleH)	// a refaire
			posTextX = positionX - font.getCapHeight()*text.length()/2 + largeur/2;
		else if(onRight)	
			posTextX = positionX + largeur + 5;
		
		if(!middleV && !bottom)
			posTextY =  positionY + hauteur;
		else if(middleV)
			posTextY =  positionY + hauteur/2 + font.getCapHeight()/2;
		else
			posTextY =  positionY + font.getCapHeight();
		isTouch=false;
		isJustTouched=false;
		
		
//			for(int i=0; i<4; i++)
//			{
//				if(Gdx.input.isTouched(i))
//				{
//					if(Gdx.input.getX(i) > positionX / InGameScreen.ratioWidth && 
//				    Gdx.input.getX(i) < positionX / InGameScreen.ratioWidth + largeur / InGameScreen.ratioWidth  && 	
//				    Gdx.input.getY(i) > Gdx.graphics.getHeight() - positionY / InGameScreen.ratioHeight - hauteur / InGameScreen.ratioHeight &&
//				    Gdx.input.getY(i) < Gdx.graphics.getHeight() - positionY / InGameScreen.ratioHeight)
//					{	
//						isTouch=true;
//						cursor=i;
//					}
//				
//				}
//			}
		for(int i=0; i<4; i++)
		{
			if(Gdx.input.justTouched())
			{
				if(Gdx.input.getX(i) > positionX && 
			    Gdx.input.getX(i) < positionX + largeur && 	
			    Gdx.input.getY(i) > Gdx.graphics.getHeight() - positionY - hauteur &&
			    Gdx.input.getY(i) < Gdx.graphics.getHeight() - positionY )
				{	
					isJustTouched=true;
				}
			
			}
			if(Gdx.input.isTouched(i))
			{
				if(Gdx.input.getX(i) > positionX && 
			    Gdx.input.getX(i) < positionX + largeur && 	
			    Gdx.input.getY(i) > Gdx.graphics.getHeight() - positionY - hauteur &&
			    Gdx.input.getY(i) < Gdx.graphics.getHeight() - positionY )
				{	
					isTouch=true;
					cursor=i;
				}
			
			}

		}
		if(!isTouch)
		{
			timeTouch=0;
			timeLastTouch+=deltaTime;
			
		}
		else
		{
			timeTouch+=deltaTime;
			timeLastTouch=0;
		}
	}
	public float getTimeLastTouch() {
		return timeLastTouch;
	}
	public boolean isJustTouched() {
		return isJustTouched;
	}
	public void setJustTouched(boolean isJustTouched) {
		this.isJustTouched = isJustTouched;
	}
	public void setTimeLastTouch(float timeLastTouch) {
		this.timeLastTouch = timeLastTouch;
	}
	public void setTextMidH()
	{
		right=false;
		middleH=true;
		onRight=false;
	}
	public void setTextMidV()
	{
		bottom=false;
		middleV=true;
	}
	public void setTextOnRight()
	{
		bottom=false;
		middleV=false;
		onRight=true;
	}
	public void setTextOnBottom()
	{
		middleV=false;
		bottom=true;
	}

	public float getTouchX() {
		if(isTouch())
		{
			touchX = (int)((Gdx.input.getX(cursor) - positionX )/ (float)largeur  * 100);
		}
		return touchX;
	}

	public float getTouchY() {
		if(isTouch())
		{
			touchY = Gdx.graphics.getHeight() - Gdx.input.getY(cursor) - positionY ;		
			touchY = touchY/ (hauteur) ;
			touchY*=100;
		}
		return touchY;
	}

	public void dispose()
	{
		try {
			pixmap.dispose();
		} catch (Exception e) {
			// TODO: handle exception
		}
//		if(pixmap!=null)
//			pixmap.dispose();
		if(sprite.getTexture() != null)
			sprite.getTexture().dispose();
		font.dispose();
	}
	public float getTimeTouch() {
		return timeTouch;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public boolean isTouch() {
		return isTouch;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public Pixmap getPixmap() {
		return pixmap;
	}

	public void setPixmap(Pixmap pixmap) {
		this.pixmap = pixmap;
	}
	
	public float getWidth()
	{
		return sprite.getWidth();
	}
	public void setWidth(float width)
	{
		sprite.setSize(largeur*width/100, sprite.getHeight());
	}
	public float getHeight()
	{
		return sprite.getHeight();
	}
	public void setHeight(float height)
	{
		sprite.setSize(sprite.getWidth(), height);
	}
	public void setTextColor(Color color) {

		font.setColor(color);
	}


}
