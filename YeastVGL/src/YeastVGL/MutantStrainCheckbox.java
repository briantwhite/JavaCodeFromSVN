package YeastVGL;

import javax.swing.JCheckBox;

public class MutantStrainCheckbox extends JCheckBox {
	
	private MutantStrain mutantStrain;
	
	public MutantStrainCheckbox(MutantStrain mutantStrain) {
		super("Mutation found in strain M" + mutantStrain.getIndex() + 
				"; Complementation Group: " + mutantStrain.getComplementationGroup());
		this.mutantStrain = mutantStrain;
	}
	
	public MutantStrain getMutantStrain() {
		return mutantStrain;
	}

}
