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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

public class Memory extends Game {
	Stage stage;

	ImageButton[] img = new ImageButton[4];
	ImageButton overlay;
	Label roundDisplay;
	Skin skin = new Skin();

	Drawable[] drawables = new Drawable[8];

	float screenWidth, screenHeight;

	Boolean computerTurn = false;
	ArrayList<Integer> memSequence;
	int index;
	int length;
	int playerChoice;
	int compChoice;

	long lightOn = 500;
	long lightOff = 200;
	long time = 0;
	boolean light = false;
	float fade = 0.2f;

	
	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		skin = 	new Skin(Gdx.files.internal("skins/skin.json"),
				new TextureAtlas(Gdx.files.internal("skins/memory.pack"))
		);

		index = 0;
		length = 2;

		setDrawables();
		memSequence = new ArrayList<Integer>();

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		addButtons();
		addOverlay();
		addRoundDisplay();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Animate the computer's turn
		if (computerTurn) {
			animateComputerTurn();
		}

		// If player reaches end of round reset and increase sequence length
		if (!computerTurn) {
			incrementRound();
		}

		System.out.println(index + " : " + length);

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

	private void addRoundDisplay() {
		roundDisplay = new Label("" + length + "", skin);
		roundDisplay.setFontScale(screenHeight * 0.003f);
		roundDisplay.setPosition(screenWidth/2 - roundDisplay.getWidth()/2 , screenHeight/2 - roundDisplay.getHeight() * 7/16);
		roundDisplay.setAlignment(Align.center);
		roundDisplay.setTouchable(Touchable.disabled);
		roundDisplay.setVisible(false);
		stage.addActor(roundDisplay);
	}

	private void addButtons() {
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
					if (!computerTurn) {
						playerChoice = I;
						if (playerChoice == memSequence.get(index)) { // correct choice: continue
							index++;
						}
						else { // wrong choice: restart
							index = 0;
							length = 2;
							memSequence.clear();
							memSequence = new ArrayList<Integer>();

							// Animate round label
							roundDisplay.addAction(Actions.sequence(
									Actions.fadeOut(fade),
									Actions.run(new Runnable() {
										@Override
										public void run() {
											roundDisplay.setText("" + length + "");
										}
									}),
									Actions.fadeIn(fade),
									Actions.run(new Runnable() {
										@Override
										public void run() {
											computerTurn = true;
										}
									})
							));

						}
					}
				}
			});
			stage.addActor(img[i]);
		}
	}

	private void addOverlay() {
		overlay = new ImageButton(skin);
		overlay.setSize(screenWidth, screenHeight);
		overlay.setPosition(0, 0);
		overlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				overlay.setVisible(false);
				computerTurn = true;

				// show round Label
				roundDisplay.addAction(Actions.visible(true));
				roundDisplay.addAction(Actions.fadeIn(fade));

			}
		});
		stage.addActor(overlay);
	}

	private void animateComputerTurn() {
		if (memSequence.size() < length) {
			if (!light && TimeUtils.millis() > time + lightOff) {
				time = TimeUtils.millis();
				compChoice = new Random().nextInt(4);

				img[compChoice].getStyle().imageUp = drawables[compChoice * 2 + 1];
				light = true;
			} else if (light && TimeUtils.millis() > time + lightOn) {
				time = TimeUtils.millis();
				memSequence.add(compChoice);

				img[compChoice].getStyle().imageUp = drawables[compChoice * 2];
				img[compChoice].getStyle().imageDown = drawables[compChoice * 2 + 1];
				light = false;
			}
			for (int i=0; i < 4; i++) {
				img[i].setTouchable(Touchable.disabled);
			}
		}
		else if (memSequence.size() == length) {
			computerTurn = false;
			for (int i=0; i < 4; i++) {
				img[i].setTouchable(Touchable.enabled);
			}
		}

	}

	private void incrementRound() {

		if (index == length) {
			index = 0;
			length++;
			memSequence.clear();
			memSequence = new ArrayList<Integer>();

			// Animate round label
			roundDisplay.addAction(Actions.sequence(
					Actions.fadeOut(fade),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							roundDisplay.setText("" + length + "");
						}
					}),
					Actions.fadeIn(fade),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							computerTurn = true;
						}
					})
			));
		}
	}
}
