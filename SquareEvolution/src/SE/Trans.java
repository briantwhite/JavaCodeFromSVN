package SE;


public class Trans {

	/**
	 * @param args
	 * arg0 = filename of genetic code
	 * arg1 = sequence to translate
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			GeneticCode geneticCode = new GeneticCode();
			if (geneticCode != null) {
				System.out.println(geneticCode.translateWithCache(args[0]));
			}			
		} else if (args.length == 2){
			GeneticCode geneticCode = new GeneticCode(args[0]);
			if (geneticCode != null) {
				System.out.println(geneticCode.translateWithCache(args[1]));
			}
		} else {
			System.out.println("");
		}
	}

}
