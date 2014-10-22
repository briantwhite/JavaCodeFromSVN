package app;

import iClickerDriver.DriverType;
import iClickerDriver.FrequencyEnum;
import iClickerDriver.IClickerDriver;
import iClickerDriver.IClickerDriverNew;
import iClickerDriver.IClickerDriverOld;
import iClickerDriver.Vote;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
	public static void main(String[] args) throws IOException {
		// For Windows 32 system. 
		System.loadLibrary("lib/hidapi-jni");
		
		// For Windows 64 system.
		// System.loadLibrary("lib/hidapi-jni-64");
		
		// For Max OS.
		// System.loadLibrary("lib/libhidapi-jni);
		
		FrequencyEnum channel1 = FrequencyEnum.A;
		FrequencyEnum channel2 = FrequencyEnum.A;
		String instructorID = "14A7F94A";
		
		try {
			
			IClickerDriver driver = new IClickerDriver(channel1, channel2, instructorID, false);
			if (driver.getDriverType() == DriverType.OLD) {
				System.out.println("Old");
				driver = new IClickerDriverOld(channel1, channel2, instructorID, false);
			} else {
				System.out.println("New");
				driver = new IClickerDriverNew(channel1, channel2, instructorID, false);
			}
			
			ArrayList<Vote> votes = new ArrayList<Vote>();
			
			driver.startBaseStation();
			System.out.println("Device opened and session started.");
			
			driver.startAcceptingVotes();
			System.out.println("Start accepting votes.");
			
			driver.updateLCDRow1("  Hello world!  ");
			driver.updateLCDRow2(" Bonjour monde! ");
			
			for (int i = 0; i < 1000000; i++) {
				votes = driver.requestVotes();
				
				for (Vote vote : votes) {
					if (vote != null) {
						System.out.println("Id: " + vote.getId() + " Button: " + vote.getButton());
					}
				}
				
				votes.clear();
			}
			
			votes = driver.stopAcceptingVotes();
			System.out.println("Stop accepting votes.");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
