import java.util.Vector;


public class Utilities {

	public static int[] parseToIntegerArray(String stringToParse, String delimiter) {
		
		if (stringToParse.startsWith(delimiter) ||
				stringToParse.endsWith(delimiter) ||
				(stringToParse.indexOf(delimiter + delimiter) != -1)) {
			System.out.println("bad one" + stringToParse);
			return null;
		}
		
		int delimiterCount = 0;
		int i = 0;
		while (true) {
			int location = stringToParse.indexOf(delimiter, i);
			if (location == -1) {
				break;
			}
			delimiterCount++;
			i = location + 1;
		}

		int[] pieces = new int[delimiterCount + 1];

		i = 0;
		delimiterCount = 0;
		while (true) {
			int location = stringToParse.indexOf(delimiter, i);
			if (location == -1) {
				break;
			}
			pieces[delimiterCount] 
			             = Integer.parseInt(
			            		 stringToParse.substring(i, location));
			i = location + 1;
			delimiterCount++;
		}

		//getlast one
		pieces[delimiterCount] 
		             = Integer.parseInt(
		            		 stringToParse.substring(i, stringToParse.length()));
		
		return pieces;

	}
	
	// ignores delimiters inside quotes
	public static String[] parseToStringArray(String stringToParse, String delim) {
		
		if (stringToParse.startsWith(delim) ||
				stringToParse.endsWith(delim) ||
				(stringToParse.indexOf(delim + delim) != -1)) {
			System.out.println("bad one" + stringToParse);
			return null;
		}
		
		char delimiter = delim.charAt(0);
		
		Vector pieceVector = new Vector();
		boolean insideQuotes = false;
		int lastDelimiter = 0;
		for (int i = 0; i < stringToParse.length(); i++){
			if (stringToParse.charAt(i) == '\"') {
				if (insideQuotes) {
					insideQuotes = false;
				} else {
					insideQuotes = true;
				}
			}
			if ((stringToParse.charAt(i) == delimiter) && !insideQuotes) {
				pieceVector.addElement(stringToParse.substring(lastDelimiter, i + 1));
				lastDelimiter = i;
			}
		}
		
		//get last one
		pieceVector.addElement(stringToParse.substring(lastDelimiter));
		
		String[] pieces = new String[pieceVector.size()];
		for (int i = 0; i < pieceVector.size(); i++) {
			pieces[i] = ((String)pieceVector.elementAt(i)).trim();
		}
		return pieces;
	}
	
	public static String extractFromWithinQuotes(String input) {
		int firstQuoteIndex = input.indexOf("\"");
		int secondQuoteIndex = input.indexOf("\"", firstQuoteIndex + 1);
		return input.substring(firstQuoteIndex + 1, secondQuoteIndex);
	}
	
	public static int parseStringToBigInt(String s, int numDecimalPlaces) {
		int decimalLocation = s.indexOf(".");
		StringBuffer buf = new StringBuffer(s);
		buf.append("00000000");
		buf = buf.delete(decimalLocation + numDecimalPlaces + 1, buf.length() + 1);
		buf = buf.deleteCharAt(decimalLocation);
		return Integer.parseInt(buf.toString());
	}
	
	public static int sqrt(int x) {
		int guess = 1;
		while (Math.abs((guess * guess) - x) > 3) {
			guess = (guess + (x/guess))/2;
			System.out.print(guess + ",");
		}
		System.out.println();
		return guess;
	}

}
