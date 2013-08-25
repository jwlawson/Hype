package uk.co.jwlawson.hype;

import uk.co.jwlawson.hype.screen.GameScreen;
import uk.co.jwlawson.hype.screen.LoadingScreen;
import uk.co.jwlawson.hype.screen.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class MyGame extends Game {

	private LoadingScreen loading;
	private GameScreen game;
	private MainMenu menu;

	@Override
	public void create() {
		loading = new LoadingScreen(this);
		game = new GameScreen();
		menu = new MainMenu(this);
		setScreen(loading);
	}

	public void finishedLoading(AssetManager manager) {
		game.load("1", manager);
		menu.load(manager);
		setScreen(menu);
	}

	public void play() {
		setScreen(game);
	}

	public void levelSelect() {
		// TODO Auto-generated method stub

	}

}
