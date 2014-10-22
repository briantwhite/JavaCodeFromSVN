package iClickerDriver;

/**
 * BuildInstructions: construct various instruction packets, 
 * with function names matching the ones in the document.
 * @author Junhao
 *
 */
class BuildInstructionsNew {
	static byte[] PCC1(FrequencyEnum freq1, FrequencyEnum freq2) {
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

	static byte[] PCC2(FrequencyEnum freq1, FrequencyEnum freq2) {
		Integer freq1Int = freq1.ordinal() + 0x21;
		Integer freq2Int = freq2.ordinal() + 0x41;
		String freq1Str  = Integer.toHexString(freq1Int);
		String freq2Str  = Integer.toHexString(freq2Int);
		
		String head = "01 2A";
		String tail = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 ";

		String finalString = head + " " + freq1Str + " " + freq2Str + " 05 " + tail;

		return StringProcess.hexString2byte(finalString);
	}	
	
	static byte[] PCC3() {
		return StringProcess.hexString2byte("01 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
				 						 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
				 						 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}	
	
	static byte[] PCC4() {
		return StringProcess.hexString2byte("01 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC5(String instructorID) {
		String head = "01 1E";
		String tail = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
					  "00 00 00 00 00 00 00 00 00 00 00 ";

		String instructorIDSix = instructorID.substring(0, 2) + " " + 
								 instructorID.substring(2, 4) + " " + 
								 instructorID.substring(4, 6);
		
		String finalString = head + " " + instructorIDSix + " " + tail;

		return StringProcess.hexString2byte(finalString);
	}

	static byte[] PCC6() {
		return StringProcess.hexString2byte("01 15 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC7() {
		return StringProcess.hexString2byte("01 2D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC8() {
		return StringProcess.hexString2byte("01 29 A1 8F 96 8D 99 97 8F 80 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}	
	
	static byte[] PCC9() {
		return StringProcess.hexString2byte("01 17 04 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
											"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
											"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
											"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC10() {
		return StringProcess.hexString2byte("01 17 03 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}

	static byte[] PCC11() {
		return StringProcess.hexString2byte("01 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC21() {
		return StringProcess.hexString2byte("01 19 66 0A 01 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC22() {
		return StringProcess.hexString2byte("01 17 03 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC23() {
		return StringProcess.hexString2byte("01 17 05 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC24() {
		return StringProcess.hexString2byte("01 19 66 0A 01 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC25() {
		return StringProcess.hexString2byte("01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC31() {
		return StringProcess.hexString2byte("01 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC32() {
		return StringProcess.hexString2byte("01 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC33() {
		return StringProcess.hexString2byte("01 17 01 00 04 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC34() {
		return StringProcess.hexString2byte("01 17 03 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
	
	static byte[] PCC35() {
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

		for (int i = 2; i < 19; i++) {
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
