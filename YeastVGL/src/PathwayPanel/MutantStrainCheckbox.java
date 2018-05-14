package PathwayPanel;

import javax.swing.JCheckBox;

import Biochemistry.SingleMutantStrain;

public class MutantStrainCheckbox extends JCheckBox {
	
	private SingleMutantStrain mutantStrain;
	
	public MutantStrainCheckbox(SingleMutantStrain mutantStrain) {
		super("<html>Mutation found in strain <font color='purple'>M" + mutantStrain.getIndex() + 
				"</font>: CG <font color='blue'>" + mutantStrain.getComplementationGroup()
				+ "</font></html>");
		this.mutantStrain = mutantStrain;
	}
	
	public SingleMutantStrain getMutantStrain() {
		return mutantStrain;
	}

}
