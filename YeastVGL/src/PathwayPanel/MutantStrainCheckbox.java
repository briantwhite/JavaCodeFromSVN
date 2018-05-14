package PathwayPanel;

import javax.swing.JCheckBox;

import Biochemistry.SingleMutantStrain;

public class MutantStrainCheckbox extends JCheckBox {
	
	private SingleMutantStrain mutantStrain;
	
	public MutantStrainCheckbox(SingleMutantStrain mutantStrain) {
		super("<html><font color='red'>Mutation found in strain M" + mutantStrain.getIndex() + 
				": CG " + mutantStrain.getComplementationGroup()
				+ "</font></html>");
		this.mutantStrain = mutantStrain;
	}
	
	public SingleMutantStrain getMutantStrain() {
		return mutantStrain;
	}

}
