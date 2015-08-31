package com.happypantz.memorygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.Random;

public class Memory extends Game {
	Stage stage;

	ImageButton[] img = new ImageButton[4];
	ImageButton overlay;
	Skin skin = new Skin();

	Drawable[] drawables = new Drawable[8];
	Drawable bot_right_up, bot_right_down;
	Drawable top_left_up, top_left_down;
	Drawable top_right_up, top_right_down;

	Boolean computerTurn = false;
	ArrayList<Integer> memSequence;
	int index = -1;
	int length = 2;
	int playerChoice;

	long lightOn = 300;
	long lightOff = 100;
	long time = 0;
	
	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = 	new Skin(Gdx.files.internal("skins/skin.json"),
				new TextureAtlas(Gdx.files.internal("skins/memory.pack"))
		);

		setDrawables();
		memSequence = new ArrayList<Integer>();

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		img[0] = new ImageButton(skin, "bot_left");
		img[1] = new ImageButton(skin, "bot_right");
		img[2] = new ImageButton(skin, "top_left");
		img[3] = new ImageButton(skin, "top_right");

		for(int i = 0; i < img.length; i++) {
			img[i].setSize(screenWidth / 2, screenHeight / 2);
			img[i].setPosition((i % 2) * (screenWidth / 2), (float) Math.floor(i / 2) * (screenHeight / 2));
			final int I = i;
			img[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					playerChoice = I;
					index++;
				}
			});
			stage.addActor(img[i]);
		}

		overlay = new ImageButton(skin);
		overlay.setSize(screenWidth, screenHeight);
		overlay.setPosition(0, 0);
		overlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				overlay.setVisible(false);
				computerTurn = true;
			}
		});
		stage.addActor(overlay);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// If player reaches end of round reset and increase sequence length
		if (index == memSequence.size()) {
			index = -1;
			length ++;
			computerTurn = true;
			memSequence = new ArrayList<Integer>();
		}

	//	Drawable temp = img[2].getStyle().imageUp;
	//	img[2].getStyle().imageUp = img[2].getStyle().imageDown;

		stage.act();
		stage.draw();
	}

	private void setDrawables() {
		drawables[0] = skin.getDrawable("bot_left_up");
		drawables[1] = skin.getDrawable("bot_left_down");
		drawables[2] = skin.getDrawable("bot_right_up");
		drawables[3] = skin.getDrawable("bot_right_down");
		drawables[4] = skin.getDrawable("top_left_up");
		drawables[5] = skin.getDrawable("top_left_down");
		drawables[6] = skin.getDrawable("top_right_up");
		drawables[7] = skin.getDrawable("top_right_down");
	}
}
