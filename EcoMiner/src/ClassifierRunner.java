import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClassifierRunner extends Thread {
	
	private String command;
	private String speciesName;
	private ArrayList attributes;
	private ArrayList instances;
	private int lineCounter;
	private File resultFile;
	private boolean done;
	
	public ClassifierRunner(String command, 
			ArrayList attributes,
			ArrayList instances,
			String speciesName,
			File resultFile) {
		
		super();
		lineCounter = 0;
		this.speciesName = speciesName;
		this.command = command;
		this.attributes = attributes;
		this.instances = instances;
		this.resultFile = resultFile;
		done = false;
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
	
	int getProgress() {
		return lineCounter;
	}
	
	class Runner {
		Runner() {
			Runtime rt = Runtime.getRuntime();
			BufferedWriter csvOutputFile = null;
			try {
				
				//open the result file
				csvOutputFile = new BufferedWriter(new FileWriter(resultFile));
				//put in all the attributes except the last one 
				// (the dummy "species" attribute needed to make the classifier work)
				for (int i = 0; i < (attributes.size() - 1); i++) {
					csvOutputFile.write((String)attributes.get(i) + ",");
				}
				csvOutputFile.write(speciesName + "\n");
				
				Process runClassifierProcess = rt.exec(command);
				InputStream stdin = runClassifierProcess.getInputStream();
				InputStreamReader isr = new InputStreamReader(stdin);
				BufferedReader br = new BufferedReader(isr);
				String resultLine = null;
				lineCounter = 0;
				while ( (resultLine = br.readLine()) != null) {
					String[] resultLineParts = resultLine.split(" ");
					if (resultLineParts.length > 1) {
						String climateDataLine = (String)instances.get(lineCounter);
						csvOutputFile.write(
								climateDataLine.substring(0, (climateDataLine.length() - 1))
								+ (String)resultLineParts[1]
								                  + "\n");
						lineCounter++;
					}
				}
				runClassifierProcess.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (csvOutputFile != null) {
						csvOutputFile.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			done = true;
		}
	}
}
