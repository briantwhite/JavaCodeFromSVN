package iClickerDriver;


import java.io.IOException;
import java.util.ArrayList;

/**
 * IClickerDriverNew: allow control of new iClicker 
 * base station including starting base station, 
 * starting/stopping accepting votes, requesting 
 * votes, updating LCD. 
 * @author Junhao
 *
 */
public class IClickerDriverNew extends IClickerDriver {
	
	/**
	 * Constructor.
	 * @param freq1 first frequency code.
	 * @param freq2 second frequency code.
	 * @param instructorID id of instructor's remote which contains eight characters.
	 * @param ifPrintPacket whether to print out the packet received from the base station.
	 * @throws Exception
	 */
	public IClickerDriverNew(FrequencyEnum freq1, FrequencyEnum freq2, String instructorID, boolean ifPrintPacket) throws IOException {
		super(freq1, freq2, instructorID, ifPrintPacket);
	}
	
	/**
	 * Start base station.
	 * @throws Exception
	 */
	public void startBaseStation() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		{
			device.write(BuildInstructionsNew.PCC1(this.freq1, this.freq2));
			Thread.sleep(22L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}			
		}
		
		{
			device.write(BuildInstructionsNew.PCC2(this.freq1, this.freq2));
			Thread.sleep(5071L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{
			device.write(BuildInstructionsNew.PCC3());
			Thread.sleep(16L);

			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{
			device.write(BuildInstructionsNew.PCC4());
			Thread.sleep(17L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		if(instructorID.isEmpty() == false) {
			device.write(BuildInstructionsNew.PCC5(this.instructorID));
			Thread.sleep(15);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{		
			device.write(BuildInstructionsNew.PCC6());
			Thread.sleep(16L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}

		{		
			device.write(BuildInstructionsNew.PCC7());
			Thread.sleep(15L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{		
			device.write(BuildInstructionsNew.PCC8());
			Thread.sleep(17L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{		
			device.write(BuildInstructionsNew.PCC9());
			Thread.sleep(94L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{		
			device.write(BuildInstructionsNew.PCC10());
			Thread.sleep(304L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{		
			device.write(BuildInstructionsNew.PCC11());
			Thread.sleep(100L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{
			// To deal with BSR X
			device.read(buf);
			
			Thread.sleep(100L);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
	}

	/**
	 * Enable voting.
	 * @throws Exception
	 */
	public void startAcceptingVotes() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		{	
			device.write(BuildInstructionsNew.PCC21());
			Thread.sleep(282L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC22());
			Thread.sleep(156L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC23());
			Thread.sleep(94L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC24());
			Thread.sleep(78L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC25());
			Thread.sleep(100L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		

		{
			// To deal with BSR X
			device.read(buf);
			
			Thread.sleep(100L);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
	}
	
	/**
	 * Disable voting.
	 * @return ArrayList of votes.
	 * @throws Exception
	 */
	public ArrayList<Vote> stopAcceptingVotes() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		ArrayList<Vote> votes = new ArrayList<Vote>();
		
		{	
			device.write(BuildInstructionsNew.PCC31());
			Thread.sleep(16L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC32());
			Thread.sleep(16L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC33());
			Thread.sleep(94L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC33());
			Thread.sleep(94L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC34());
			Thread.sleep(95L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		{	
			device.write(BuildInstructionsNew.PCC35());
			Thread.sleep(100L);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
		}
		
		do {
			device.read(buf);
			Thread.sleep(100L);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if (ParseInstructionNew.isVote(buf) == true) {
				Vote vote = ParseInstructionNew.getVote(buf);
				vote.setId(vote.getId().toUpperCase());
				votes.add(vote);
			}
		}
		while (ParseInstructionNew.isSummary(buf) == false);
		
		return votes;
	}

	/**
	 * Request vote.
	 * @return A piece of vote if there is vote, or null if there is no vote.
	 * @throws Exception
	 */
	public ArrayList<Vote> requestVotes() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		Vote vote = null;
		
		device.read(buf);
		
		if (this.ifPrintPacket && ParseInstructionNew.isAllZero(buf) == false) {
			printBuf(buf);
		}
		
		vote = ParseInstructionNew.getVote(buf);
		
		if (vote != null) {
			vote.setId(vote.getId().toUpperCase());
		}
		
		ArrayList<Vote> votes = new ArrayList<Vote> ();
		votes.add(vote);
		return votes;
	}

	/**
	 * Write on first row of LCD screen.
	 * @param str String needs to be write, must be shorter than or equal to 16 characters.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void updateLCDRow1(String str) throws IOException, InterruptedException {
		if(str.length() > 16) {
			System.err.println("Bad string length");
			return;
		}
		
		device.write(BuildInstructionsNew.LCDPrint(str, 1));
		
		Thread.sleep(LCD_DELAY_MS);
	}

	/**
	 * Write on second row of LCD screen.
	 * @param str String needs to be write, must be shorter than or equal to 16 characters.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void updateLCDRow2(String str) throws IOException, InterruptedException {
		if(str.length() > 16) {
			System.err.println("Bad string length");
			return;
		}
		
		device.write(BuildInstructionsNew.LCDPrint(str, 2));
		
		Thread.sleep(LCD_DELAY_MS);
	}
}
