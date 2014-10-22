package iClickerDriver;


import java.io.IOException;
import java.util.ArrayList;

/**
 * IClickerDriverOld: allow control of old iClicker 
 * base station, including starting base station, 
 * starting/stopping accepting votes, requesting 
 * votes, updating LCD. 
 * @author Junhao
 *
 */
public class IClickerDriverOld extends IClickerDriver {
	// Delays happen between various packets. Do not change these magic numbers.
	// For starting base station.
	private static final long AFTER_FREQUENCY_DELAY_MS  = 140L;
	private static final long AFTER_INSTRUCTOR_DELAY_MS = 240L;
	private static final long BEFORE_NONAME_DELAY_MS    =  70L;
	private static final long AFTER_NONAME_DELAY_MS     = 210L;
	
	// For requesting votes.
	private static final long BEFORE_READ_DELAY_MS      = 100L;
	
	// For starting accepting votes.
	private static final long BEFORE_RESET_DELAY_MS     = 160L;
	private static final long AFTER_RESET_DELAY_MS      = 180L;
	
	// For stopping accepting votes.
	private static final long BEFORE_NONACCEPT_DELAY_MS = 210L;
	private static final long AFTER_NONACCEPT_DELAY_MS  = 130L;
	private static final long BEFORE_SUMMARY_DELAY_MS   = 470L;
	
	private int voteIndex = 0;
	
	/**
	 * Constructor.
	 * @param freq1 first frequency code.
	 * @param freq2 second frequency code.
	 * @param instructorID id of instructor's remote which contains eight characters.
	 * @param ifPrintPacket whether to print out the packet received from the base station.
	 * @throws IOException
	 */
	public IClickerDriverOld(FrequencyEnum freq1, FrequencyEnum freq2, String instructorID, boolean ifPrintPacket) throws IOException {
		super(freq1, freq2, instructorID, ifPrintPacket);
	}
	
	/**
	 * Start base station.
	 * @throws Exception
	 */
	public void startBaseStation() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		{
			//  Set frequency.
			device.write(BuildInstructionsOld.setFreq(freq1, freq2));
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isSetFreqAck(buf) == false) {
				throw new Exception("Fail to receive Set Frequency Ack");
			}
			
			Thread.sleep(AFTER_FREQUENCY_DELAY_MS);
		}
		
		if(instructorID.isEmpty() == false) {
			// Set instructor remote.
			device.write(BuildInstructionsOld.setInstructor(this.instructorID));
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isSetInstructorAck(buf) == false) {
				throw new Exception("Fail to receive Set Instructor Ack");
			}
			
			Thread.sleep(AFTER_INSTRUCTOR_DELAY_MS);
		}
		
		{
			// Disable voting.
			device.write(BuildInstructionsOld.disableVoting());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isDisableVotingAck(buf) == false) {
				throw new Exception("Fail to receive Disable Voting Ack");
			}
			
			Thread.sleep(BEFORE_NONAME_DELAY_MS);
		}
		
		{
			// No name packet.
			device.write(BuildInstructionsOld.startSessionNoName());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isStartSessionNoNameAck(buf) == false) {
				throw new Exception("Fail to receive Start Session No Name Ack");
			}
			
			Thread.sleep(AFTER_NONAME_DELAY_MS);
		}
	}

	/**
	 * Enable voting.
	 * @throws Exception
	 */
	public void startAcceptingVotes() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		voteIndex = 0;
		
		{
			// Reset Counter
			Thread.sleep(BEFORE_RESET_DELAY_MS);
			
			device.write(BuildInstructionsOld.resetCounter());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isResetCounterAck(buf) == false) {
				throw new Exception("Fail to receive reset counter Ack");
			}
			
			Thread.sleep(AFTER_RESET_DELAY_MS);
		}
		
		{
			// Enable voting.
			device.write(BuildInstructionsOld.enableVoting());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isEnableVotingAck(buf) == false) {
				throw new Exception("Fail to receive Enable Voting Ack");
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
		
		{
			// Disable voting.
			Thread.sleep(BEFORE_NONACCEPT_DELAY_MS);
			
			device.write(BuildInstructionsOld.disableVoting());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isDisableVotingAck(buf) == false) {
				throw new Exception("Fail to receive Disable Voting Ack");
			}
			
			Thread.sleep(AFTER_NONACCEPT_DELAY_MS);
		}
		
		{
			// Request votes.
			ArrayList<Vote> voteArray = requestVotes();
			ArrayList<Vote> voteArray2 = requestVotes();
			voteArray.addAll(voteArray2);
			
			Thread.sleep(BEFORE_SUMMARY_DELAY_MS);

			// Request summary
			device.write(BuildInstructionsOld.requestSummary());
			Thread.sleep(BEFORE_READ_DELAY_MS);
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if(ParseInstructionOld.isRequestSummaryAck(buf) == false) {
				throw new Exception("Fail to receive Request Summary Ack");
			}
			
			device.read(buf);
			
			if (this.ifPrintPacket) {
				printBuf(buf);
			}
			
			if (ParseInstructionOld.isRequestSummaryRes(buf) == false) {
				throw new Exception("Fail to receive Request Summary Response");
			}
			
			// TODO: interpret request summary.
			
			return voteArray;
		}
	}

	/**
	 * Request votes.
	 * @return ArrayList of votes.
	 * @throws Exception
	 */
	public ArrayList<Vote> requestVotes() throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		ArrayList<Vote> voteArray = new ArrayList<Vote>(14);

		device.write(BuildInstructionsOld.polling(voteIndex));
		Thread.sleep(BEFORE_READ_DELAY_MS);
		
		device.read(buf);
		
		if (this.ifPrintPacket) {
			printBuf(buf);
		}
		
		if (ParseInstructionOld.isPollingAck(buf) == false) {
			throw new Exception("Fail to receive Polling Ack");
		}
		
		processResults(voteArray);

		return voteArray;
	}
	
	/**
	 * Recursively fetch packet that belongs to polling response.
	 * @param voteArray: ArrayList stores the votes.
	 * @throws Exception
	 */
	private void processResults(ArrayList<Vote> voteArray) throws Exception {
		byte[] buf = new byte[BUFSIZE];
		
		Thread.sleep(BEFORE_READ_DELAY_MS);

		device.read(buf);

		if (this.ifPrintPacket) {
			printBuf(buf);
		}
				
		if(ParseInstructionOld.isPollingRes(buf) == false) {
			throw new Exception("Fail to receive Polling Res");
		}
		else {
			int voteAmountLeft = ParseInstructionOld.getPollingResVoteAmountLeft(buf);
			int voteIndexEnd = ParseInstructionOld.getPollingResVoteIndexEnd(buf);
			
			voteIndex = voteIndexEnd;
			
			if(voteAmountLeft != 0) {
				ParseInstructionOld.getPollingResVotes(buf, voteArray);
				
				if(ParseInstructionOld.isPollingResLastInstruction(buf) == false) {
					processResults(voteArray);
				}			
			}
		}
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
		
		device.write(BuildInstructionsOld.LCDPrint(str, 1));
		
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
		
		device.write(BuildInstructionsOld.LCDPrint(str, 2));
		
		Thread.sleep(LCD_DELAY_MS);
	}
}
