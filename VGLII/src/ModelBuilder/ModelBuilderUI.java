package ModelBuilder;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import GeneticModels.GeneModel;
import GeneticModels.GeneticModel;
import GeneticModels.PhenotypeProcessor;
import VGL.Messages;

public class ModelBuilderUI extends JDialog {
	
	private JFrame parentFrame;
	private WorkingModel workingModel;
	private GeneticModel geneticModel;
	
	// from saved work file
	public ModelBuilderUI (
			JFrame parentFrame, 
			WorkingModel workingModel, 
			GeneticModel geneticModel) {
		super(parentFrame);
		this.parentFrame = parentFrame;
		this.workingModel = workingModel;
		this.geneticModel = geneticModel;
		setupUI();
	}
	
	// for new problem
	public ModelBuilderUI(
			JFrame parentFrame,
			GeneticModel geneticModel) {
		this(parentFrame, new WorkingModel(geneticModel), geneticModel);
	}
	
	private void setupUI() {
		this.setTitle(Messages.getInstance().getString("VGLII.ModelBuilder"));
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
		outerPanel.add(Box.createRigidArea(new Dimension(1,300)));
		
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel,BoxLayout.Y_AXIS));
		masterPanel.add(Box.createRigidArea(new Dimension(400,1)));
		JTabbedPane tabs = new JTabbedPane();
		GeneModel[] geneModels = new GeneModel[geneticModel.getNumberOfGeneModels()];
		ModelPane[] modelPanes = new ModelPane[geneticModel.getNumberOfCharacters()];
		
		// see if epistasis or complementation is involved
		if (geneticModel.getPhenoTypeProcessor().getInteractionType() 
				== PhenotypeProcessor.NO_INTERACTION) {
			// no epistasis or complementation
			for (int i = 0; i < geneticModel.getNumberOfGeneModels(); i++) {
				geneModels[i] = geneticModel.getGeneModelByIndex(i);
				modelPanes[i] = new ModelPane(i, geneModels[i], 
						geneticModel.getProblemTypeSpecification(), this);
				tabs.addTab(geneModels[i].getCharacter(), modelPanes[i]);
				modelPanes[i].setupActionListeners();
			}

		} else {
			
		}
			
		masterPanel.add(tabs);
		outerPanel.add(masterPanel);
		this.add(outerPanel);
		this.pack();
		
	}

}
