import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClassifierGenerator extends LongTask {

	private String[] commands;
	
	ClassifierGenerator(String[] commands) {
		this.commands = commands;
	}

	public void doIt() {
		Runtime rt = Runtime.getRuntime();
		try {
			for (int i = 0; i < commands.length; i++){
				Process proc = rt.exec(commands[i]);
				InputStream stdin = proc.getInputStream();
				InputStreamReader isr = new InputStreamReader(stdin);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ( (line = br.readLine()) != null) {
					result = result + line + "\n";
				}
				proc.waitFor();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		done = true;

	}
}
