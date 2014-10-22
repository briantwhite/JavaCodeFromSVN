package iClickerDriver;


import java.util.*;

/**
 * ParseInstruction: a class that interpret the packets received 
 * from the base station by comparing them with the standard ones.
 * @author Junhao
 *
 */
class ParseInstructionOld extends ParseInstruction {

	
	/**
	 * Check if the packet is an ack for setting frequency.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isSetFreqAck(byte[] buf) {
		String setFreqAckStd = "01 10 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String setFreqAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(setFreqAckStd.compareTo(setFreqAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Check if the packet is an ack for setting instructor remote id.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isSetInstructorAck(byte[] buf) {
		String setInstructorAckStd = "01 17 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String setInstructorAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(setInstructorAckStd.compareTo(setInstructorAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is an ack for disabling voting.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isDisableVotingAck(byte[] buf) {
		String disableVotingAckStd = "01 16 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									 "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String disableVotingAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(disableVotingAckStd.compareTo(disableVotingAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is an ack for starting session.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isStartSessionNoNameAck(byte[] buf) {
		String startSessionNoNameAckStd = "01 17 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String startSessionNoNameAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(startSessionNoNameAckStd.compareTo(startSessionNoNameAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is an ack for polling. 
	 * @param buf source packet.
	 * @return true if yes, false otherwise.
	 */
	static boolean isPollingAck(byte[] buf) {
		String pollingAckStd = "01 17 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
							   "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
			
		String pollingAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(pollingAckStd.compareTo(pollingAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is a response to polling.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isPollingRes(byte[] buf) {
		String pollingResStd = "02 18 ";
		
		String pollingResTest = StringProcess.byte2HexString(buf, 0, 1);
		
		if(pollingResStd.compareTo(pollingResTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is the last response in the response set.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isPollingResLastInstruction(byte[] buf) {
		String pollingResLastInstructionStd = "00 ";
		String pollingResNotLastInstructionStd = "01 ";
		
		String pollingResLastInstructionTest = StringProcess.byte2HexString(buf, 2, 2);
		
		if(pollingResLastInstructionStd.compareTo(pollingResLastInstructionTest) == 0) {
			return true;
		}
		else if(pollingResNotLastInstructionStd.compareTo(pollingResLastInstructionTest) == 0) {
			return false;
		}
		else {
			System.err.println("Bad Last Instruction flag");
			return false;
		}
	}
	
	/**
	 * Get how many votes remain to be read.
	 * @param buf source.
	 * @return total amount of votes unread.
	 */
	static int getPollingResVoteAmountLeft(byte[] buf) {
		String pollingResVoteAmountLeftTest = StringProcess.byte2HexString(buf, 3, 3);
		pollingResVoteAmountLeftTest = pollingResVoteAmountLeftTest.replaceAll("\\s", "");
		return Integer.parseInt(pollingResVoteAmountLeftTest, 16);
	}
	
	/**
	 * Get the index of the first vote in this packet.
	 * @deprecated
	 * @param buf source.
	 * @return index of first unread vote in this packet.
	 */
	static int getPollingResVoteIndexStart(byte[] buf) {
		String pollingResVoteIndexStartTest = StringProcess.byte2HexString(buf, 4, 4);
		pollingResVoteIndexStartTest = pollingResVoteIndexStartTest.replaceAll("\\s", "");
		return Integer.parseInt(pollingResVoteIndexStartTest, 16) + 1;
	}

	/**
	 * Get the index of the last vote in this packet.
	 * @param buf source.
	 * @return index of last unread vote in this packet.
	 */
	static int getPollingResVoteIndexEnd(byte[] buf) {
		String PollingResVoteIndexEndTest = StringProcess.byte2HexString(buf, 5, 5);
		PollingResVoteIndexEndTest = PollingResVoteIndexEndTest.replaceAll("\\s", "");
		return Integer.parseInt(PollingResVoteIndexEndTest, 16);
	}
	
	/**
	 * Fetch votes from a single packet.
	 * @param buf source.
	 * @param voteArray ArrayList that stores votes.
	 * @return true if the current packet contains vote(s); false otherwise.
	 */
	static boolean getPollingResVotes(byte[] buf, ArrayList<Vote> voteArray) {
		int voteAmountCurrent = getPollingResVoteAmountCurrent(buf);
		
		if(voteAmountCurrent == 0) {
			return false;
		}
		else {
			for(int i = 0; i < voteAmountCurrent; i++) {
				Vote vote = getVote(buf, i);
				voteArray.add(vote);
			}
			
			return true;
		}
	}
	
	/**
	 * Get the total amount of votes in this packet.
	 * @param buf source packet.
	 * @return total amount of votes in this packet.
	 */
	private static int getPollingResVoteAmountCurrent(byte[] buf) {
		String getPollingResVoteAmountCurrentTest = StringProcess.byte2HexString(buf, 6, 6);
		getPollingResVoteAmountCurrentTest = getPollingResVoteAmountCurrentTest.replaceAll("\\s", "");
		return Integer.parseInt(getPollingResVoteAmountCurrentTest, 16);
	}
	
	/**
	 * Get a piece of vote.
	 * @param buf source packet.
	 * @param index position of target vote.
	 * @return target vote.
	 */
	private static Vote getVote(byte[] buf, int index) {
		// Vote information starts at the 8th byte, whose index is 7.
		int base = 7;
		// One piece of vote information needs 4 bytes.
		int voteLength = 4;
		
		String voteButtonString = StringProcess.byte2HexString(buf, base + index * voteLength, base + index * voteLength);
		voteButtonString = voteButtonString.replaceAll("\\s", "");
		int voteButtonInt = Integer.parseInt(voteButtonString, 16);
		
		ButtonEnum button;
		
		switch (voteButtonInt) {
		case 97:  button = ButtonEnum.A; break;
		case 98:  button = ButtonEnum.B; break;
		case 99:  button = ButtonEnum.C; break;
		case 100: button = ButtonEnum.D; break;
		case 101: button = ButtonEnum.E; break;
		default:  button = ButtonEnum.A; System.err.println("Bad button pressed");
		}
		
		String id = StringProcess.byte2HexString(buf, base + index * voteLength + 1, base + index * voteLength + 3);
		id = id.replaceAll("\\s", "");
		
		Vote vote = new Vote(id, button);
		return vote;
	}
	
	/**
	 * Check if the packet is an ack for resetting counter.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isResetCounterAck(byte[] buf) {
		String resetCounterAckStd = "01 17 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String resetCounterAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(resetCounterAckStd.compareTo(resetCounterAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is an ack for enabling voting.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isEnableVotingAck(byte[] buf) {
		String enableVotingAckStd = "01 11 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String resetCounterAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(enableVotingAckStd.compareTo(resetCounterAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the packet is an ack for requesting summary.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isRequestSummaryAck(byte[] buf) {
		String requestSummaryAckStd = "01 17 aa 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
									  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String requestSummaryAckTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(requestSummaryAckStd.compareTo(requestSummaryAckTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	static boolean isRequestSummaryRes(byte[] buf) {
		//TODO
		return true;
	}
}
