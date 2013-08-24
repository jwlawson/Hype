package uk.co.jwlawson.hype.timer;

public interface TimeListener {

	public void secondsDropped(int secondsRemaining);

	public void timeRemaining(float timeRemaining);

	public void timeFinished();
}
