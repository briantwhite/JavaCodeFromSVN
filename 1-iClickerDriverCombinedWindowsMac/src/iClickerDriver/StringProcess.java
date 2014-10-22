package iClickerDriver;

import java.lang.String;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * StringProcess: a class that provides various string/characters utilities.
 * @author Junhao
 *
 */

class StringProcess {
	/**
	 * All but Windows requires that the packet sent by the computer 
	 * should be padded with an extra byte, whose values is zero, 
	 * in front of the actual packet. Thus detecting the system 
	 * type is necessary. 
	 * @return true if it is on Windows; false if it is not.
	 */
	static boolean isSystemWindows() {
		String osname = System.getProperty("os.name");
		
		Set<String> windowsNames = new HashSet<String>();
		windowsNames.add("Windows 7");
		windowsNames.add("Windows 8");
		Set<String> osxNames = new HashSet<String>();
		osxNames.add("Mac OS X");
		Set<String> linuxNames = new HashSet<String>();
		linuxNames.add("Linux");
		
		if (windowsNames.contains(osname)) {
			return true;
		} else if (osxNames.contains(osname)) {
			return false;
		} else if (linuxNames.contains(osname)) {
			return false;
		} else {
			System.err.println("Unrecognised OS: " + osname);
			return false;
		}
	}
	
	/**
	 * Convert a hex String to an array of bytes, os type considered.
	 * @param theArray: source String, hex format, with one space between each byte.
	 * @return an array of bytes.
	 */
	static byte[] hexString2byte(String theArray) {
		 String theArrayNoSpace = theArray.replaceAll("\\s", "");
		 String theArrayNoSpacePadded = theArrayNoSpace;
		 
		 if(isSystemWindows()) {
			 theArrayNoSpacePadded = "00" + theArrayNoSpace;
		 }
		 
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(theArrayNoSpacePadded);
	     
	     return bytes;
	}
	
    /**
     * Converts a segment of a byte array (from start to end) to a 
     * String, with start ranging from 0 to end, and end ranging 
     * from start to the length of the array - 1.
     * Example: 
     * 		byte [] buf = {'H', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
     * 		StringProcess.byte2HexString(buf, 3, 6);
     * 	returns:
     * 		"lo w"
     * @param buf: source array of bytes.
     * @param start: start index.
     * @param end: end index.
     * @return hex String.
     */
	static String byte2HexString(byte[] buf, int start, int end) {
    	String response = "";

    	if(start < 0 || end > buf.length - 1 || end - start < 0) {
    		System.err.println("Bad index");
    		return "";
    	}

    	for(int i = start; i <= end; i++) {
    		int character = buf[i];
    		if (character < 0) {
    			character = character + 256;
    		}
    		String hs = Integer.toHexString(character);
    		if (character < 16) { 
    			response = response + "0";
    		}
    		response = response + hs + " ";
    	}
    	
    	return response;
	}
}
