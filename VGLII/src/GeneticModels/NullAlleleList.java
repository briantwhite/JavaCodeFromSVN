package GeneticModels;

import java.util.ArrayList;

public class NullAlleleList extends ArrayList<Allele>{
	
	private Allele nullAllele;
	
	public NullAlleleList() {
		nullAllele = new Allele(new NullTrait("","",""), 0);
	}
	
	public Allele get(int i) {
		return nullAllele;
	}

}
