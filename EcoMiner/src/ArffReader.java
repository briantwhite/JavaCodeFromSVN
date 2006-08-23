import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class ArffReader extends LongTask {

	private File arffFile;
	private ArrayList attributes;
	private ArrayList instances;

	public ArffReader(File arffFile) {
		super();
		this.arffFile = arffFile;
		attributes = new ArrayList();
		instances = new ArrayList();
	}

	ArrayList getAttributes() {
		return attributes;
	}

	ArrayList getInstances() {
		return instances;
	}

	int getNumAttributes() {
		return attributes.size();
	}

	int getNumInstances() {
		return instances.size();
	}

	public void doIt() {
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(arffFile));
			boolean inDataRegion = false;
			while ((line = reader.readLine()) != null) {
				if (line.toUpperCase().startsWith("@ATTRIBUTE")) {
					String[] lineParts = line.split(" ");
					attributes.add((String)lineParts[1]);
				}

				if (inDataRegion) {
					if (line.indexOf(",") > 0) {
						instances.add(line);
					}
				}

				if (line.toUpperCase().matches("@DATA")) {
					inDataRegion = true;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		result = attributes.size() 
			+ " climate variables loaded; "
			+ instances.size()
			+ " locations loaded.";
		done = true;
	}
}
