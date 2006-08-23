import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClassifierRunner extends LongTask {

	private String[] commands;
	private String speciesName;
	private ArrayList attributes;
	private ArrayList instances;
	private int lineCounter;
	private File resultFile;

	public ClassifierRunner(String[] commands, 
			ArrayList attributes,
			ArrayList instances,
			String speciesName,
			File resultFile) {

		super();
		lineCounter = 0;
		this.speciesName = speciesName;
		this.commands = commands;
		this.attributes = attributes;
		this.instances = instances;
		this.resultFile = resultFile;
		done = false;
	}


	public void doIt() {
		Runtime rt = Runtime.getRuntime();
		BufferedWriter csvOutputFile = null;
		try {
			//make the temp version of the .arff without 
			//  the site num, lat, and long
			Process proc = rt.exec(commands[0]);
			proc.waitFor();
			
			//open the result file
			csvOutputFile = new BufferedWriter(new FileWriter(resultFile));
			//put in all the attributes except the last one 
			// (the dummy "species" attribute needed to make the classifier work)
			for (int i = 0; i < (attributes.size() - 1); i++) {
				csvOutputFile.write((String)attributes.get(i) + ",");
			}
			csvOutputFile.write(speciesName + "\n");

			Process runClassifierProcess = rt.exec(commands[1]);
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
		result = lineCounter + " locations classified.";
		done = true;
	}

}
