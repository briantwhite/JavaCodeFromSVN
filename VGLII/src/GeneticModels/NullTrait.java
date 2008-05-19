package GeneticModels;


//trait for the W or Y chromosome
//  no contribution to phenotype
public class NullTrait extends Trait {

	public NullTrait(String traitName, String type, String bodyPart) {
		super(traitName, type, bodyPart);
	}

}
