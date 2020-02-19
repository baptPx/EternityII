package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera {

	private boolean controlDev = true;
	public Camera()
	{
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		position.x = Gdx.graphics.getWidth();
		position.y = Gdx.graphics.getHeight();
	}
	public void update()
	{
		
		if(controlDev)
		{
			if(Gdx.input.isKeyPressed(Keys.RIGHT))
				position.x += 10*zoom;
			if(Gdx.input.isKeyPressed(Keys.LEFT))
				position.x -= 10*zoom;
			if(Gdx.input.isKeyPressed(Keys.UP))
				position.y += 10*zoom;
			if(Gdx.input.isKeyPressed(Keys.DOWN))
				position.y -= 10*zoom;
			if(Gdx.input.isKeyPressed(Keys.STAR))
				zoom *= 1.02f;
			if(Gdx.input.isKeyPressed(Keys.PLUS))
				zoom /= 1.02f;
			if(Gdx.input.isKeyPressed(Keys.T))
				zoom = 2;
		}

		super.update();
	}
}
