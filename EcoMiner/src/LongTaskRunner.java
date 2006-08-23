import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextPane;
import javax.swing.Timer;

public class LongTaskRunner extends Thread {

	private LongTask longTask;
	private EcoMiner em;
	private String whileRunningText;
	private String allDoneText;
	private String result;

	public LongTaskRunner(LongTask longTask,
			EcoMiner em,
			String whileRunningText,
			String allDoneText) {
		super();
		this.longTask = longTask;
		this.em = em;
		this.whileRunningText = whileRunningText;
		this.allDoneText = allDoneText;
		result = "";
	}

	public void go() {
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				longTask.doIt();
				return longTask;
			}
		};
		worker.start();
	}

	boolean done() {
		return longTask.done();
	}

	private Timer longTaskTimer = new Timer(500, new ActionListener() {
		double time = 0;
		int minutes;
		int seconds;
		boolean whichVersion = false;
		public void actionPerformed(ActionEvent e) {
			
			time = time + 0.5;
			minutes = (int) time/60;
			seconds = (int) time%60;
			
			if(whichVersion) {
				em.updateResultPane(EcoMiner.htmlStart
						+ "<font color=blue>" + whileRunningText + "</font><br>"
						+ "Elapsed time = " + minutes + " minutes and " + seconds + " seconds."
						+ EcoMiner.htmlEnd);
				whichVersion = false;
			} else {
				em.updateResultPane(EcoMiner.htmlStart
						+ "<font color=purple>" + whileRunningText + "</font><br>"
						+ "Elapsed time = " + minutes + " minutes and " + seconds + " seconds."
						+ EcoMiner.htmlEnd);		
				whichVersion = true;
			}
			
			if (done()){
				Toolkit.getDefaultToolkit().beep();
				longTaskTimer.stop();
				em.updateResultPane(EcoMiner.htmlStart
						+ "<font color=green>" + allDoneText + "<br>"
						+ "Elapsed time = " + minutes + " minutes and " + seconds + " seconds.<br>"
						+ "Click &quot;NEXT&quot; to continue.</font>"
						+ "<hr>"
						+ "<pre>" + longTask.getResult() + "</pre>"
						+ EcoMiner.htmlEnd);
				em.resetButtonStatus();
				em.setResultPaneTextString();
				
				File tempFile = new File("workspace/temp.arff");
				tempFile.delete();
			}
		}
	});
	
	public Timer getLongTaskTimer() {
		return longTaskTimer;
	}

}
