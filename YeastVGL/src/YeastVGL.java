
public class YeastVGL {
	
	private static Pathway pathway;

	public static void main(String[] args) {
		pathway = new Pathway();
		
		for (int i = 0; i < pathway.getNumberOfEnzymes(); i++) {
			pathway.activateAllEnzymes();
			pathway.inactivateEnzyme(i);
			System.out.println("-------------------------");
			System.out.println("Inactivating enzyme " + i);
			boolean[] result = pathway.getOutputs();
			for (int j = 0; j < result.length; j++) {
				if (result[j]) {
					System.out.println("\tMolecule " + j + " is produced.");
				} else {
					System.out.println("\tMolecule " + j + " is NOT produced.");
				}
			}
		}
	}

}
