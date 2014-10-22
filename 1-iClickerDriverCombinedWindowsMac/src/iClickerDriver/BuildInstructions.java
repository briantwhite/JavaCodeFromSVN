package iClickerDriver;

public class BuildInstructions {
	static byte[] setFrequency(FrequencyEnum freq1, FrequencyEnum freq2) {
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
	
	static byte[] getDriverTypeInstuction() {
		return StringProcess.hexString2byte("01 15 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
										 	"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
	}
}
