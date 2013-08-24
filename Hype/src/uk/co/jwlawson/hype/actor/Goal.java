package uk.co.jwlawson.hype.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Goal extends Actor {

	private List<WinListener> winListeners;
	private Hacker hacker;
	private Vector2 pos1, pos2;

	public Goal(Hacker hacker) {
		winListeners = new ArrayList<Goal.WinListener>(2);
		this.hacker = hacker;
		pos1 = new Vector2();
		pos2 = new Vector2();
	}

	public void addWinListener(WinListener lis) {
		winListeners.add(lis);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		pos1.set(8, 8);
		pos2 = hacker.localToStageCoordinates(pos1);
		pos1 = stageToLocalCoordinates(pos2);
		if (hit(pos1.x, pos1.y, false) != null) {
			for (WinListener lis : winListeners) {
				lis.hackerWins(hacker);
			}
		}
	}

	public interface WinListener {
		public void hackerWins(Hacker hacker);
	}
}
