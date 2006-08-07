import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClassifierRunner extends Thread {
	
	private String command;
	private ArrayList instances;
	private File resultFile;
	private boolean done;
	private ArrayList results;
	
	public ClassifierRunner(String command, 
			ArrayList instances,
			File resultFile) {
		
		super();
		this.command = command;
		this.instances = instances;
		this.resultFile = resultFile;
		done = false;
		results = new ArrayList();
	}
	
	public void go() {
		done = false;
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new Runner();
			}
		};
		worker.start();
	}
	
	boolean done() {
		return done;
	}
	
	ArrayList getResults() {
		return results;
	}
	
	int getProgress() {
		return results.size();
	}
	
	class Runner {
		Runner() {
			Runtime rt = Runtime.getRuntime();
			try {
				Process runClassifierProcess = rt.exec(command);
				InputStream stdin = runClassifierProcess.getInputStream();
				InputStreamReader isr = new InputStreamReader(stdin);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ( (line = br.readLine()) != null) {
					results.add(line);
				}
				runClassifierProcess.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			done = true;
		}
	}
}
