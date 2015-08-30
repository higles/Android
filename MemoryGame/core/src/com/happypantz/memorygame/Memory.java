package com.happypantz.memorygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Memory extends Game {
	Stage stage;

	ImageButton[] img = new ImageButton[4];
	Skin skin = new Skin();
	
	@Override
	public void create () {
		stage = new Stage();
		skin = 	new Skin(Gdx.files.internal("skins/skin.json"),
				new TextureAtlas(Gdx.files.internal("skins/memory.pack"))
		);

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		img[0] = new ImageButton(skin, "bot_left");
		img[1] = new ImageButton(skin, "bot_right");
		img[2] = new ImageButton(skin, "top_left");
		img[3] = new ImageButton(skin, "top_right");

		for(int i = 0; i < img.length; i++) {
			img[i].setSize(screenWidth / 2, screenHeight / 2);
			//img[i].setPosition(((i % 2)*(screenWidth/2)), ((i / 2)*(screenHeight/2)));

			stage.addActor(img[i]);
		}

		img[0].setPosition(0 , 0);
		img[1].setPosition(screenWidth / 2, 0);
		img[2].setPosition(0, screenHeight / 2);
		img[3].setPosition(screenWidth / 2, screenHeight / 2);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.input.setInputProcessor(stage);

	//	Drawable temp = img[2].getStyle().imageUp;
	//	img[2].getStyle().imageUp = img[2].getStyle().imageDown;

		stage.act();
		stage.draw();
	}
}
