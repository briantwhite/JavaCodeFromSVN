package content;

import app.Test;

/**
 * Keep record of time elapsed when voting goes on.
 * @author Junhao
 *
 */
public class Timer implements Runnable {
	private int seconds;
	private Test test;
	private final int updateTimerDelay = 1000;
	Timer(Test test) {
		seconds = 0;
		this.test = test;
	}
	
	@Override
	public void run() {
		while (test.getCourse().getSession().isVotingEnabled()) {
			Integer second = seconds % 60;
			Integer minute = seconds / 60;
			Integer hour = seconds / 3600;
			
			String secondStr = second >= 10 ? second.toString() : "0" + second.toString();
			String minuteStr = minute >= 10 ? minute.toString() : "0" + minute.toString();
			String hourStr   = hour.toString();
			
			String time = hourStr + ":" + minuteStr + ":" + secondStr;
			
			this.test.getToolbarInstructor().updateTime(time);
			this.test.getToolbarInstructor().updateVotesPerSecond(
					this.test.getCourse().getSession().getCurrentQuestion().getSummaryList().getVotesThisInterval());
			this.test.getToolbarStudent().updateTime(time);
			
			try {
				Thread.sleep(updateTimerDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			seconds++;
		}
	}
}
