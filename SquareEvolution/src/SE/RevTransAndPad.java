package SE;

import java.util.Random;


public class RevTransAndPad {
	
	private static String[] bases = {"A", "G", "C", "T"};
	private static Random r;

	/**
	 * 
	 * used in generating starting DNA lines for simulations
	 * 
	 * @param args
	 * arg0 = filename of genetic code file
	 * arg1 = sequence to reverse translate
	 * arg2 = base to pad with; or "R" for random
	 * arg3 = the length to pad to
	 */
	public static void main(String[] args) {
		r = new Random();
		if (args.length != 4) return;
		int desiredLength = Integer.parseInt(args[3]);

		GeneticCode geneticCode = new GeneticCode(args[0]);
		if (geneticCode != null) {
			String rawCodingSequence = geneticCode.reverseTranslate(args[1]);
			String codingWithStop = rawCodingSequence + geneticCode.getRandomStopCodon();
			
			StringBuffer b = new StringBuffer(codingWithStop);
			int prePadding = (desiredLength - codingWithStop.length())/2;
			// see if random or fixed padding
			if (args[2].equals("R")) {
				boolean isOK = false;
				while (!isOK) {
					// make random sequence
					b = new StringBuffer(codingWithStop);
					for (int i = 0; i < prePadding; i++) {
						b.insert(0, bases[r.nextInt(4)]);
					}
					while (b.length() < desiredLength) {
						b.append(bases[r.nextInt(4)]);
					}		
					String result = b.toString();
					// see if it's OK
					isOK = ((result.length() == desiredLength) && (geneticCode.translateWithCache(result).equals(args[1])));
				}
				
			} else {
				for (int i = 0; i < prePadding; i++) {
					b.insert(0, args[2]);
				}
				while (b.length() < desiredLength) {
					b.append(args[2]);
				}
			}
			String result = b.toString();
			if ((result.length() == desiredLength) && (geneticCode.translateWithCache(result).equals(args[1]))) {
				System.out.println("<StartingDNA>" + result + "</StartingDNA>");
			} else {
				System.out.println("Failed");
			}
			
		}

	}

}
