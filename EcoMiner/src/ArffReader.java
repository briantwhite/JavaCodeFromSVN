import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class ArffReader extends Thread {
	
	private File arffFile;
	private boolean done;
	private ArrayList attributes;
	private ArrayList instances;
	
	public ArffReader(File arffFile) {
		super();
		this.arffFile = arffFile;
		done = false;
		attributes = new ArrayList();
		instances = new ArrayList();
	}
	
	public void go() {
		done = false;
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return new Reader();
			}
		};
		worker.start();
	}
	
	boolean done() {
		return done;
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
	
	class Reader {
		Reader() {
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
			
			done = true;
		}
	}
}
