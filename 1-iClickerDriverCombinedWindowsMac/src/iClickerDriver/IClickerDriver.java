package iClickerDriver;

import java.io.IOException;
import java.util.ArrayList;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import com.codeminders.hidapi.HIDManagerTest;

/**
 * IClickerDriverNew: allow control of new iClicker 
 * base station including starting base station, 
 * starting/stopping accepting votes, requesting 
 * votes, updating LCD. 
 * @author Junhao
 *
 */
public class IClickerDriver {
	// i>Clicker base station vendor and product id.
	protected static final int VENDOR_ID  = 6273;
	protected static final int PRODUCT_ID = 336;
	
	// For updating LCD.
	protected static long LCD_DELAY_MS = 5L;

	// Channel.
	protected FrequencyEnum freq1, freq2;
	
	// Id of instructor's remote.
	protected String instructorID;
	
	protected static int BUFSIZE = 64;
	
	protected HIDDevice device;
	
	// Whether to print out packet received from base station
	protected boolean ifPrintPacket = false;
	
	/**
	 * Constructor.
	 * @param freq1 first frequency code.
	 * @param freq2 second frequency code.
	 * @param instructorID id of instructor's remote which contains eight characters.
	 * @param ifPrintPacket whether to print out the packet received from the base station.
	 * @throws Exception
	 */
	public IClickerDriver(FrequencyEnum freq1, FrequencyEnum freq2, String instructorID, boolean ifPrintPacket) throws IOException {
		new HIDManagerTest();
		
		device = HIDManager.openById(VENDOR_ID, PRODUCT_ID, null);
		device.disableBlocking();
		
		this.freq1 = freq1;
		this.freq2 = freq2;
		this.instructorID = instructorID;
		this.ifPrintPacket = ifPrintPacket;
	}
	
	public DriverType getDriverType() throws IOException, InterruptedException {
		byte[] buf = new byte[BUFSIZE];
		
		{
			device.write(BuildInstructions.setFrequency(freq1, freq2));
			Thread.sleep(22L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{
			device.write(BuildInstructions.getDriverTypeInstuction());
			Thread.sleep(15L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		return ParseInstruction.getDriverType(buf);
	}
	
	/**
	 * Start base station.
	 * @throws Exception
	 */
	public void startBaseStation() throws Exception {}

	/**
	 * Enable voting.
	 * @throws Exception
	 */
	public void startAcceptingVotes() throws Exception {}
	
	/**
	 * Disable voting.
	 * @return ArrayList of votes.
	 * @throws Exception
	 */
	public ArrayList<Vote> stopAcceptingVotes() throws Exception {
		return null;
	}

	/**
	 * Request vote.
	 * @return A piece of vote if there is vote, or null if there is no vote.
	 * @throws Exception
	 */
	public ArrayList<Vote> requestVotes() throws Exception {
		return null;
	}
	
	/**
	 * Write on first row of LCD screen.
	 * @param str String needs to be write, must be shorter than or equal to 16 characters.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void updateLCDRow1(String str) throws IOException, InterruptedException {}

	/**
	 * Write on second row of LCD screen.
	 * @param str String needs to be write, must be shorter than or equal to 16 characters.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void updateLCDRow2(String str) throws IOException, InterruptedException {}
	
	/**
	 * Print buffer.
	 * @param buf buffer to be printed.
	 */
	protected static void printBuf(byte[] buf) {
		System.out.println(StringProcess.byte2HexString(buf, 0, BUFSIZE-1));
	}
}
