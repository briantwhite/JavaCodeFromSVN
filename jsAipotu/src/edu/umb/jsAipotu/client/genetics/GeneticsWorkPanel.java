package edu.umb.jsAipotu.client.genetics;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.jsAipotu.client.molGenExp.ExpressedAndFoldedGene;
import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismFactory;
import edu.umb.jsAipotu.client.molGenExp.OrganismUI;
import edu.umb.jsAipotu.client.molGenExp.WorkPanel;

public class GeneticsWorkPanel extends WorkPanel {
	
	// settings for laying out grid of offspring
	public static final int NUM_COLS = 8;
	public static final int NUM_ROWS = 4;

	private int trayNum; 	//current tray number
	private String parentInfo;	//info on the parent

	private GeneticsWorkbench gw;
	
	private HTML upperLabel;

	private SimplePanel trayPanel;
	private Grid offspringDisplayGrid;

	private Button crossTwoButton;
	private Button selfCrossButton;
	private Button mutateButton;

	private OrganismFactory organismFactory;
	
	private ArrayList<Organism> offspring;
		
	public GeneticsWorkPanel(String title, GeneticsWorkbench gw) {
		super(title);
		this.gw = gw;
		organismFactory = new OrganismFactory();
		setupUI();
	}

	private void setupUI() {
		
		offspring = new ArrayList<Organism>();

		VerticalPanel mainPanel = new VerticalPanel();
		
		upperLabel = new HTML("Ready...");
		mainPanel.add(upperLabel);
		
		trayPanel = new SimplePanel();
		offspringDisplayGrid = new Grid(NUM_ROWS, NUM_COLS);
		ScrollPanel offspringListScroller = new ScrollPanel(offspringDisplayGrid);
		trayPanel.add(offspringListScroller);
		mainPanel.add(trayPanel);

		HorizontalPanel lowerButtonPanel = new HorizontalPanel();
		crossTwoButton = new Button("Cross Two Organisms");
		crossTwoButton.setEnabled(false);
		lowerButtonPanel.add(crossTwoButton);
		selfCrossButton = new Button("Self-cross One Organism");
		selfCrossButton.setEnabled(false);
		lowerButtonPanel.add(selfCrossButton);
		mutateButton = new Button("Mutate One Organism");
		mutateButton.setEnabled(false);
		lowerButtonPanel.add(mutateButton);
		mainPanel.add(lowerButtonPanel);
		add(mainPanel);

		crossTwoButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Organism o1 = gw.getMGE().getOUI1().getOrganism();
				Organism o2 = gw.getMGE().getOUI2().getOrganism();
				crossTwo(o1, o2);
			}
		});

		selfCrossButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				Organism o = gw.getMGE().getOUI2().getOrganism();
				crossTwo(o, o);
			}
		});

		mutateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				makeMutantsOf(gw.getMGE().getOUI2().getOrganism());
			}
		});
		
	}

	public void crossTwo(Organism o1, Organism o2) {
		
		offspring = new ArrayList<Organism>();
		
		offspringDisplayGrid.clear();
		
		trayNum = gw.getNextTrayNum();		

		ExpressedAndFoldedGene efg1 = null;
		ExpressedAndFoldedGene efg2 = null;

		if (o1.equals(o2)) {
			parentInfo = o1.getName() + " self-cross";
		} else {
			parentInfo = o1.getName() + " X " + o2.getName();
		}
		upperLabel.setHTML("<b>" 
				+ "Tray " + trayNum + ": "
				+ parentInfo
				+ "</b>");

		int count = 20 + Random.nextInt(10);
		int row = 0;
		int col = 0;
		for (int i = 1; i < count; i++) {

			//contribution from first parent
			if (Random.nextInt(2) == 0) {
				efg1 = o1.getGene1();
			} else {
				efg1 = o1.getGene2();
			}

			//contribution from other parent
			if (Random.nextInt(2) == 0) {
				efg2 = o2.getGene1();
			} else {
				efg2 = o2.getGene2(); 
			}

			Organism o = 
				organismFactory.createOrganism(trayNum + "-" + i, efg1, efg2);

			offspring.add(o);
			offspringDisplayGrid.setWidget(row, col, new OrganismUI(o, gw.getMGE()));
			col++;
			if (col == NUM_COLS) {
				col = 0;
				row++;
			}
		}

		// add tray to hist list
		Tray tray = new Tray(trayNum, parentInfo, offspring);
		gw.addToHistoryList(tray);
	}
	
	public void makeMutantsOf(Organism o) {
		trayNum = gw.getNextTrayNum();
		parentInfo = "Mutants of " + o.getName();
		upperLabel.setHTML("<b>" 
				+ "Tray " + trayNum + ": "
				+ parentInfo
				+ "</b>");
		int count = 20 + Random.nextInt(10);
		int row = 0;
		int col = 0;
		for (int i = 1; i < count; i++) {
			Organism mutant = MutantGenerator.getInstance().getMutantOf(o, trayNum + "-" + i);
			offspring.add(mutant);
			offspringDisplayGrid.setWidget(row, col, new OrganismUI(mutant, gw.getMGE()));
			col++;
			if (col == NUM_COLS) {
				col = 0;
				row++;
			}
		}
		// add tray to hist list
		Tray tray = new Tray(trayNum, parentInfo, offspring);
		gw.addToHistoryList(tray);
	}

	public void setCurrentTray(Tray tray) {
		offspringDisplayGrid.clear();
		offspring = new ArrayList<Organism>();
		ArrayList<Organism> organisms = tray.getAllOrganisms();
		int row = 0;
		int col = 0;
		Iterator<Organism> oIt = organisms.iterator();
		while (oIt.hasNext()) {
			Organism orig = oIt.next();
			Organism o = organismFactory.createOrganism(orig.getName(), orig);
			offspring.add(o);
			offspringDisplayGrid.setWidget(row, col, new OrganismUI(o, gw.getMGE()));
			col++;
			if (col == NUM_COLS) {
				col = 0;
				row++;
			}
		}
		upperLabel.setHTML("<b>" 
				+ "Tray " + tray.getNumber() + ": "
				+ tray.getParentInfo()
				+ "</b>");

	}

	public void mutateOrganism(Organism o) {
		//figure out how many mutants to make
//		Random random = new Random();
//		int mutantCount = 10 + random.nextInt(10);
//
//		trayNum = gw.getNextTrayNum();		
//		offspringList.clearList();
//
//		mutantGenerator = new MutantGenerator(
//				o,
//				mutantCount,
//				trayNum,
//				offspringList,
//				gw);
//
//		Thread t = new Thread(mutantGenerator);
//		t.start();
//		timer.start();
//		upperLabel.setText("Making " + mutantCount + " mutant versions of "
//				+ "Organism " + o.getName() + ".");
//		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//		gw.setSelfCrossButtonsEnabled(false);
//		gw.setMutateButtonsEnabled(false);
//
//		gw.getMGE().setStatusLabelText("Making mutants of Organism " 
//				+ o.getName());
//		JProgressBar progressBar = gw.getMGE().getProgressBar();
//		progressBar.setMinimum(0);
//		progressBar.setMaximum(mutantGenerator.getLengthOfTask());
//		progressBar.setValue(0);
	}


	public void setCrossTwoButtonEnabled(boolean b) {
		crossTwoButton.setEnabled(b);
	}

	public void setSelfCrossButtonEnabled(boolean b) {
		selfCrossButton.setEnabled(b);
	}

	public void setMutateButtonEnabled(boolean b) {
		mutateButton.setEnabled(b);
	}

}
