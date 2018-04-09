package YeastVGL;

import com.google.gson.Gson;

import Biochemistry.MutantSet;
import Biochemistry.Pathway;
import ComplementationTestPanel.ComplementationTestPanel;
import PathwayPanel.PathwayPanel;

public class YeastVGL {
	
	private Pathway pathway;
	private static YeastVGL yeastVGL;
	private MutantSet mutantSet;
	private static YeastVGL_GUI gui;
	private ComplementationTestPanel ctp;
	private PathwayPanel pp;
	
	public static final int NUM_MUTANTS = 15;

	public static void main(String[] args) {
		/*
		 * trap apple-Q
		 * 	need to have this early in code 
		 *  https://stackoverflow.com/questions/2061194/swing-on-osx-how-to-trap-command-q
		 */
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");  
		yeastVGL = new YeastVGL();
		gui = new YeastVGL_GUI(yeastVGL);
		gui.setVisible(true);
	}
	
	private YeastVGL() {
		pathway = new Pathway();
		mutantSet = new MutantSet(NUM_MUTANTS, pathway.getNumberOfEnzymes());
		ctp = new ComplementationTestPanel(this);
		pp = new PathwayPanel(this);
	}
	
	public Pathway getPathway() {
		return pathway;
	}
	
	public MutantSet getMutantSet() {
		return mutantSet;
	}
	
	public void setMutantSet(MutantSet ms) {
		mutantSet = ms;
	}
	
	public int getNumMutants() {
		return NUM_MUTANTS;
	}
	
	public YeastVGL_GUI getGUI() {
		return gui;
	}
	
	public ComplementationTestPanel getComplementationTestPanel() {
		return ctp;
	}
	
	public PathwayPanel getPathwayPanel() {
		return pp;
	}

	public String getJsonString() {
		State state = new State(mutantSet, ctp.getData(), ctp.getWorkingSetChoices(), pp.getPathwayDrawingPanel().getState());
		Gson gson = new Gson();
		return gson.toJson(state);
	}
	
	public void restoreSavedState(State state) {
		ctp.restoreSavedState(state);
		pp.updateWorkingSet(ctp.getWorkingSet());
		pp.getPathwayDrawingPanel().restoreSavedState(state);
	}
}
