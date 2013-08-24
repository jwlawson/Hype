package uk.co.jwlawson.hype.timer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Timer extends Actor {

	private List<TimeListener> listeners;
	private float timeDefault = 10;
	private float timeLeft = 10;
	private int nextSecond = 9;
	private boolean running = false;

	public Timer() {
		listeners = new ArrayList<TimeListener>();
	}

	public void addTimeListener(TimeListener listener) {
		listeners.add(listener);
	}

	public void reset() {
		running = false;
		timeLeft = timeDefault;
	}

	public void start() {
		running = true;
	}

	public void stop() {
		running = false;
	}

	private void finished() {
		running = false;
		for (TimeListener lis : listeners) {
			lis.timeFinished();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (running) {
			timeLeft -= delta;

			for (TimeListener lis : listeners) {
				lis.timeRemaining(timeLeft);
			}

			if (timeLeft < nextSecond) {
				for (TimeListener lis : listeners) {
					lis.secondsDropped(nextSecond);
				}
				nextSecond--;

				if (timeLeft < 0) {
					finished();
				}
			}
		}
	}

}
