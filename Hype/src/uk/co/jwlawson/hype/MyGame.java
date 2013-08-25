package uk.co.jwlawson.hype;

import uk.co.jwlawson.hype.screen.GameScreen;
import uk.co.jwlawson.hype.screen.LoadingScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class MyGame extends Game {

	private LoadingScreen loading;
	private GameScreen game;

	@Override
	public void create() {
		loading = new LoadingScreen(this);
		game = new GameScreen();
		setScreen(loading);
	}

	public void finishedLoading(AssetManager manager) {
		game.load("1", manager);
		setScreen(game);
	}

}
