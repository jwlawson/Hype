package uk.co.jwlawson.aniguffins;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueuableAnimatedActor extends AnimatableActor implements
		AnimatedActor.AnimationDoneListener {

	private QueuedAnimationPool queuePool = new QueuedAnimationPool();
	private Queue<QueuedAnimation> queue = new ArrayBlockingQueue<QueuedAnimation>(4);

	public QueuableAnimatedActor(TextureRegion tex) {
		super(tex);
	}

	public void queueAnimation(String name, boolean looping) {
		QueuedAnimation ani = queuePool.obtain();
		ani.name = name;
		ani.looping = looping;
		queue.add(ani);
	}

	@Override
	public void animationFinished() {
		if (queue.peek() != null) {
			QueuedAnimation ani = queue.remove();
			activateAnimation(ani.name, ani.looping);
			queuePool.free(ani);
		}
	}

	private class QueuedAnimation {
		String name;
		boolean looping;
	}

	private class QueuedAnimationPool extends Pool<QueuedAnimation> {

		@Override
		protected QueuedAnimation newObject() {
			return new QueuedAnimation();
		}

	}
}
