package com.bapt.eternity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

import Tools.res;

public class Eternity extends Game {

	
	@Override
	public void create () {
		res.build();
		Gdx.input.vibrate(1500);
//		DisplayMode mode = Gdx.graphics.getDisplayMode();
//		Gdx.graphics.setFullscreenMode(mode);
		setScreen(new Menu(this));
		
	}

	@Override
	public void render () {
		
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		res.dispose();
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}
}
