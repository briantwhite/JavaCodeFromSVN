package edu.umb.jsAipotu.client.genetics;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

import edu.umb.jsAipotu.client.molGenExp.Organism;
import edu.umb.jsAipotu.client.molGenExp.OrganismUI;

public class MutateCommand implements RepeatingCommand {

	int count, row, col, i;

	GeneticsWorkbench gw;
	Organism o;
	int trayNum;
	String parentInfo;
	ArrayList<Organism> offspring;
	Grid offspringDisplayGrid;
	
	PopupPanel busyPanel;
	
	public MutateCommand(GeneticsWorkbench gw, Organism o, int trayNum, String parentInfo, ArrayList<Organism> offspring, Grid offspringDisplayGrid) {
		this.gw = gw;
		this.o = o;
		this.trayNum = trayNum;
		this.parentInfo = parentInfo;
		this.offspring = offspring;
		this.offspringDisplayGrid = offspringDisplayGrid;
		count = 20 + Random.nextInt(10);
		row = 0;
		col = 0;
		i = 0;
		busyPanel = new PopupPanel();
		busyPanel.setStyleName("busyPopup");
		busyPanel.setPopupPosition(300, 300);
		busyPanel.setWidget(new HTML("<center><h1>Making " + count + " of " + parentInfo + ".<br> Please be patient.</h1></center>"));
		busyPanel.show();
	}

	public boolean execute() {
		
		Organism mutant = MutantGenerator.getInstance().getMutantOf(o, trayNum + "-" + i);
		offspring.add(mutant);
		offspringDisplayGrid.setWidget(row, col, new OrganismUI(mutant, gw.getMGE()));
		col++;
		if (col == GeneticsWorkPanel.NUM_COLS) {
			col = 0;
			row++;
		}
		i++;
		if (i < count) {
			return true;
		} else {
			// add tray to hist list
			Tray tray = new Tray(trayNum, parentInfo, offspring);
			gw.addToHistoryList(tray);
			busyPanel.hide();
			return false;
		}
	}

}


