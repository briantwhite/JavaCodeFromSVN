package PathwayPanel;

import javax.swing.JCheckBox;

import Biochemistry.SingleMutantStrain;

public class MutantStrainCheckbox extends JCheckBox {
	
	private SingleMutantStrain mutantStrain;
	
	public MutantStrainCheckbox(SingleMutantStrain mutantStrain) {
		super("Mutation found in strain M" + mutantStrain.getIndex() + 
				"; Complementation Group: " + mutantStrain.getComplementationGroup());
		this.mutantStrain = mutantStrain;
	}
	
	public SingleMutantStrain getMutantStrain() {
		return mutantStrain;
	}

}
