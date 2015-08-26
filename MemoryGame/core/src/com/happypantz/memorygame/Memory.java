package com.happypantz.memorygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Memory extends Game {
	SpriteBatch batch;
	Texture img;

	float r, g, b;
	int flagR, flagG, flagB;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		r = 1;
		g = 0.5f;
		b = 0;
		flagR = 1;
		flagG = 1;
		flagB = 1;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (r <= 0) flagR = 1;
		else if (r >= 1) flagR = -1;
		r += flagR * 0.01;

		if (g <= 0) flagG = 1;
		else if (g >= 1) flagG = -1;
		g += flagG * 0.01;

		if (b <= 0) flagB = 1;
		else if (b >= 1) flagB = -1;
		b += flagB * 0.01;

		batch.begin();

		batch.end();
	}
}
