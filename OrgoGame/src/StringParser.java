
public class StringParser {

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

}
