package iClickerDriver;

/**
 * ParseInstruction: a class that interpret the packets received 
 * from the base station by comparing them with the standard ones.
 * @author Junhao
 *
 */
class ParseInstructionNew extends ParseInstruction {
	// Length of the buffer size we are interested in.
	private static final int BUFSIZE = 64;
	
	/**
	 * Check if the packet only contains 64 zeros.  
	 * @param buf
	 * @return true if yes and false otherwise.
	 */
	public static boolean isAllZero(byte[] buf) {
		String allZeroStd = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
				   			"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
				   			"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
				   			"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
		
		String allZeroTest = StringProcess.byte2HexString(buf, 0, BUFSIZE - 1);
		
		if(allZeroStd.compareTo(allZeroTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Fetch a piece of vote from a given array of bytes.
	 * @param buf source
	 * @return a piece of vote if there is a vote, or null if there is no vote.
	 */
	static Vote getVote(byte[] buf) {
		// If it is an empty buffer whose bytes are all zeros.
		if (isAllZero(buf) == true) {
			return null;
		}
		
		boolean first = false;
		boolean second = false;
		Vote voteFirst = null;
		Vote voteSecond = null;
		int indexFirst = -1;
		int indexSecond = -1;
		
		// If the first half contains vote.
		if (StringProcess.byte2HexString(buf, 0, 1).compareTo("02 13 ") == 0 ) {
			first = true;
			
			String voteButtonString = StringProcess.byte2HexString(buf, 2, 2);
			voteButtonString = voteButtonString.replaceAll("\\s", "");
			int voteButtonInt = Integer.parseInt(voteButtonString, 16);
			
			ButtonEnum button;
			
			switch (voteButtonInt) {
			case 129:  button = ButtonEnum.A; break;
			case 130:  button = ButtonEnum.B; break;
			case 131:  button = ButtonEnum.C; break;
			case 132:  button = ButtonEnum.D; break;
			case 133:  button = ButtonEnum.E; break;
			default:   button = ButtonEnum.A; System.err.println("Bad button pressed");
			}
			
			String id = StringProcess.byte2HexString(buf, 3, 5);
			id = id.replaceAll("\\s", "");
			
			voteFirst = new Vote(id, button);
			
			String indexFirstStr = StringProcess.byte2HexString(buf, 6, 6);
			indexFirstStr = indexFirstStr.replaceAll("\\s", "");
			indexFirst = Integer.parseInt(indexFirstStr, 16);
		}
		
		// If the second half contains vote.
		if (StringProcess.byte2HexString(buf, 32, 33).compareTo("02 13 ") == 0 ) {
			second = true;
			
			String voteButtonString = StringProcess.byte2HexString(buf, 34, 34);
			voteButtonString = voteButtonString.replaceAll("\\s", "");
			int voteButtonInt = Integer.parseInt(voteButtonString, 16);
			
			ButtonEnum button;
			
			switch (voteButtonInt) {
			case 129:  button = ButtonEnum.A; break;
			case 130:  button = ButtonEnum.B; break;
			case 131:  button = ButtonEnum.C; break;
			case 132:  button = ButtonEnum.D; break;
			case 133:  button = ButtonEnum.E; break;
			default:   button = ButtonEnum.A; System.err.println("Bad button pressed");
			}
			
			String id = StringProcess.byte2HexString(buf, 35, 37);
			id = id.replaceAll("\\s", "");
			
			voteSecond = new Vote(id, button);
			
			String indexSecondStr = StringProcess.byte2HexString(buf, 38, 38);
			indexSecondStr = indexSecondStr.replaceAll("\\s", "");
			indexSecond = Integer.parseInt(indexSecondStr, 16);
		}
		
		if (first == true && second == false) {					// Only first half contains vote.
			return voteFirst;
		}
		else if (first == false && second == true) {			// Only second half contains vote.
			return voteSecond;
		}
		else if (first == true && second == true) {				// If both first and second half contain vote.
			// If both first and second half contains vote. 
			if (indexFirst == 0 && indexSecond == 255) {		// The one in the first half is fresher.
				return voteFirst;
			}
			else if (indexFirst == 255 && indexSecond == 0) {	// The one in the second half is fresher.
				return voteSecond;
			}
			else {
				if (indexFirst > indexSecond) {					// The one in the first half is fresher.
					return voteFirst;
				}
				else if (indexFirst < indexSecond) {			// The one in the second half is fresher.
					return voteSecond;
				}
				else {											// Equally fresher, error occurs.
					System.err.println("Equal index for two votes");
					return null;
				}
			}
		}
		else {													// Neither contains vote.
			return null;
		}
	}
	
	/**
	 * Check if the packet is a summary.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isSummary(byte[] buf) {
		String headStd = "02 18 1a ";

		String headTest = StringProcess.byte2HexString(buf, 0, 2);
		
		if(headStd.compareTo(headTest) == 0) {
			return true;
		}
		else {
			return false;
		}		
	}

	/**
	 * Check if the packet may contain vote.
	 * @param buf source.
	 * @return true if yes, false otherwise.
	 */
	static boolean isVote(byte[] buf) {
		String headStd = "02 13 ";
		
		String headTest = StringProcess.byte2HexString(buf, 0, 1);
		
		if(headStd.compareTo(headTest) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}
