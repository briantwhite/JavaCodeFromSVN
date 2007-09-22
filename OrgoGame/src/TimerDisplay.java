import java.util.TimerTask;


public class TimerDisplay extends TimerTask {
	
	private ProblemSet problemSet;
	private OrgoGame orgoGame;
	private Controller controller;
	private int ticks;
	private int seconds;
	private int minutes;
	
	public TimerDisplay() {
		seconds = 0;
		minutes = 0;
	}

	public void run() {
		ticks++;
		seconds = ticks % 60;
		minutes = (int)(ticks/60);
		controller.updateTimers();
	}
	
	public void reset() {
		ticks = 0;
	}
	
	public void setOrgoGame(OrgoGame og) {
		orgoGame = og;
	}
	
	public void setProblemSet(ProblemSet ps) {
		problemSet = ps;
	}
	
	public void setController(Controller c) {
		controller = c;
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
