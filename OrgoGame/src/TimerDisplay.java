import java.util.TimerTask;


public class TimerDisplay extends TimerTask {

	private Controller controller;
	private int ticks;
	private int seconds;
	private int minutes;

	private boolean paused;

	public TimerDisplay(Controller controller) {
		this.controller = controller;
		seconds = 0;
		minutes = 0;
		paused = false;
	}

	public void run() {
		if (!paused){
			ticks++;
			seconds = ticks % 60;
			minutes = (int)(ticks/60);
			controller.updateVisibleTimers();
		}
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}

	public void reset() {
		ticks = 0;
	}

	public String getMinutes() {
		return String.valueOf(minutes);
	}

	public String getSeconds() {
		if (seconds < 10) {
			return "0" + String.valueOf(seconds);
		}
		return String.valueOf(seconds);
	}

}
