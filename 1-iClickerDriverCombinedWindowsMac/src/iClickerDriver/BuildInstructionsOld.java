package iClickerDriver;


/**
 * BuildInstructions: construct various instruction packets.
 * @author Junhao
 *
 */
class BuildInstructionsOld {
	/**
	 * Set frequency.
	 * @param freq1: first frequency code.
	 * @param freq2: second frequency code.
	 * @return array of bytes containing the packet.
	 */
	static byte[] setFreq(FrequencyEnum freq1, FrequencyEnum freq2) {
		Integer freq1Int = freq1.ordinal() + 0x21;
		Integer freq2Int = freq2.ordinal() + 0x41;
		String freq1Str  = Integer.toHexString(freq1Int);
		String freq2Str  = Integer.toHexString(freq2Int);
		
		String head = "01 10";
		String tail = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 ";

		String finalString = head + " " + freq1Str + " " + freq2Str + " " + tail;

		return StringProcess.hexString2byte(finalString);
	}

	/**
	 * Set instrcutor i>clicker remote id.
	 * @param instructorID: id of instructor's remote, eight characters.
	 * @return array of bytes containing the packet.
	 */
	static byte[] setInstructor(String instructorID) {
		String head = "01 17 06";
		String tail = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 ";

		String instructorIDSix = instructorID.substring(0, 2) + " " + 
								 instructorID.substring(2, 4) + " " + 
								 instructorID.substring(4, 6);
		
		String finalString = head + " " + instructorIDSix + " " + tail;

		return StringProcess.hexString2byte(finalString);
	}

	/**
	 * Disable voting.
	 * @return array of bytes containing the packet.
	 */
	static byte[] disableVoting() {
		return StringProcess.hexString2byte("01 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	/**
	 * A packet sent during start session whose function is unknown.
	 * @return array of bytes containing the packet.
	 */
	static byte[] startSessionNoName() {
		return StringProcess.hexString2byte("01 17 03 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	/**
	 * Poll to request votes.
	 * @param current: current vote index.
	 * @return array of bytes containing the packet.
	 */
	static byte[] polling(int current) {
		String currentString = java.lang.Integer.toHexString(current);

		if (currentString.length() < 2) {
			currentString = "0" + currentString;
		}

		String head = "01 17 01";
		String tail = "04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00";

		String finalString = head + " " + currentString + " " + tail;
				
		return StringProcess.hexString2byte(finalString);
	}

	/**
	 * Reset base station counter.
	 * @return array of bytes containing the packet.
	 */
	static byte[] resetCounter() {
		return StringProcess.hexString2byte("01 17 05 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	/**
	 * Enable voting, so that students get green lights when they press buttons.
	 * @return array of bytes containing the packet.
	 */
	static byte[] enableVoting() {
		return StringProcess.hexString2byte("01 11 00 05 00 00 00 00 00 00 00 00 00 00 00 00 " +
											"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
											"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	/**
	 * Request summary after a question voting is finished.
	 * @return array of bytes containing the packet.
	 */
	static byte[] requestSummary() {
		return StringProcess.hexString2byte("01 17 04 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	/**
	 * Write characters on the LCD screen.
	 * @param str: String to be written.
	 * @param row: which row on the screen to write, either the 1 for the first row or 2 for the second one. 
	 * @return array of bytes containing the packet.
	 */
	public static byte[] LCDPrint(String str, int row) {
		byte[] result = new byte[64];

		result[0] = 0x01;
		if (row == 1) {
			result[1] = 0x13;
		}
		else if (row == 2) {
			result[1] = 0x14;
		}
		else {
			System.err.println("Bad LCD row");
		}

		for (int i = 2; i < 18; i++) {
			result[i] = 0x20;
		}

		byte[] outputText = str.getBytes();

		System.arraycopy(outputText, 0, result, 2, outputText.length);

		if (StringProcess.isSystemWindows()) {
			byte[] winResult = new byte[65];
			winResult[0] = 0x00;
			System.arraycopy(result, 0, winResult, 1, result.length);
			return winResult;
		}
		else {
			return result;
		}
	}
}
