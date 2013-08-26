package uk.co.jwlawson.hype;

import uk.co.jwlawson.hype.screen.GameOver;
import uk.co.jwlawson.hype.screen.GameOver.Message;
import uk.co.jwlawson.hype.screen.GameScreen;
import uk.co.jwlawson.hype.screen.LevelSelect;
import uk.co.jwlawson.hype.screen.LoadingScreen;
import uk.co.jwlawson.hype.screen.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class MyGame extends Game {

	private LoadingScreen loading;
	private GameScreen game;
	private MainMenu menu;
	private GameOver gameOver;
	private LevelSelect levelSelector;
	private AssetManager mAssetManager;

	private int level = 1;
	private int numberLevels = 16;

	@Override
	public void create() {
		loading = new LoadingScreen(this);
		game = new GameScreen(this);
		menu = new MainMenu(this);
		gameOver = new GameOver(this);
		levelSelector = new LevelSelect(this);
		levelSelector.setNumberMaps(numberLevels);
		setScreen(loading);
	}

	public void finishedLoading(AssetManager manager) {
		menu.load(manager);
		gameOver.load(manager);
		levelSelector.load(manager);
		setScreen(menu);

		mAssetManager = manager;
	}

	public void gameOver(Message message) {
		gameOver.setMessage(message);
		setScreen(gameOver);
	}

	public void play() {
		setScreen(game);
	}

	public void levelSelect() {
		setScreen(levelSelector);
	}

	public void showMenu() {
		setScreen(menu);
	}

	public void loadLevel(int level) {
//		if (level > numberLevels) {
//			showMenu();
//		}
		this.level = level;
		game.load(String.valueOf(level), mAssetManager);
		setScreen(game);
	}

	public void restartLevel() {
		loadLevel(level);
	}

	public void nextLevel() {
		loadLevel(++level);
	}

}
