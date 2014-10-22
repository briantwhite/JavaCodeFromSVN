package iClickerDriver;

public class ParseInstruction {
	// Length of the buffer size we are interested in.
	protected static final int BUFSIZE = 64;
	
	/**
	 * Test driver type. If one writes the packets (the ones shown in method getDriverType, 
	 * class IClickerDriver) to the base station, both base stations will respond with three
	 * packets. The first and second packet are the same for both base station. However, the
	 * third one is different for different base station. The new one will reply with the 
	 * following packet:
	 * 
	 * 01 15 04 05 21 41 01 00 66 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 
	 * or:
	 * 
	 * 01 15 00 32 04 05 21 41 01 01 66 12 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 
	 * while the old one will reply with the following packet:
	 * 
	 * 01 15 02 03 21 41 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 
	 * 
	 * or:
	 * 
	 * @param buf
	 * @return
	 */
	static DriverType getDriverType(byte[] buf) {
		if (buf[2] == 4 || buf[4] == 4) {
			return DriverType.NEW;
		} else {
			return DriverType.OLD;
		}
	}
}
