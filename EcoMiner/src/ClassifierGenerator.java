import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClassifierGenerator extends Thread {
	
	private String command;
	private boolean done;
	private String result;
	
	public ClassifierGenerator(String command) {
		super();
		this.command = command;
		done = false;
		result = "";
	}
	
	public void go() {
		done = false;
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new Generator();
			}
		};
		worker.start();
	}
	
	boolean done() {
		return done;
	}
	
	String getResult() {
		return result;
	}
	
	class Generator {
		Generator() {
			Runtime rt = Runtime.getRuntime();
			try {
				Process makeClassifierProcess = rt.exec(command);
				InputStream stdin = makeClassifierProcess.getInputStream();
				InputStreamReader isr = new InputStreamReader(stdin);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ( (line = br.readLine()) != null) {
					result = result + line + "\n";
				}
				makeClassifierProcess.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			done = true;
		}
	}
}
