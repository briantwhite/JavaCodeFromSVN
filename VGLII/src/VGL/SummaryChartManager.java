package VGL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import GeneticModels.Organism;
import GeneticModels.OrganismList;
import GeneticModels.Phenotype;
import GeneticModels.Trait;
/**
 * Brian White Summer 2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: SummaryChartManager.java,v 1.7 2008-06-28 02:08:18 brian Exp $
 */

public class SummaryChartManager {

	private static SummaryChartManager instance;

	private TreeSet<CageUI> selectedSet;

	private SummaryChartUI summaryChartUI;

	private SummaryChartManager() {
		selectedSet = new TreeSet<CageUI>();
	}

	public static SummaryChartManager getInstance() {
		if (instance == null) {
			instance = new SummaryChartManager();
		}
		return instance;
	}

	public void addToSelected(CageUI cageUI) {
		selectedSet.add(cageUI);
	}

	public void removeFromSelected(CageUI cageUI) {
		selectedSet.remove(cageUI);
	}

	public void clearSelectedSet() {
		Iterator<CageUI> it = selectedSet.iterator();
		while (it.hasNext()) {
			it.next().setIsSelected(false);
		}
		selectedSet = new TreeSet<CageUI>();
	}

	public int[] getScrambledCharacterOrder() {
		return getOneOrganism().getGeneticModel().getScrambledCharacterOrder();
	}

	public void hideSummaryChart() {
		if (summaryChartUI != null) {
			summaryChartUI.setVisible(false);
		}
	}


	/**
	 * totals up the contents of the selectedSet of cageUIs
	 * split by the phenotypes you're interested in
	 * - these are specified in traitsToCount:
	 *   if you want to sort based on all 3 traits,
	 *   traitsToCount = {0,1,2}
	 *   if you want only traits 1 and 3
	 *   traitsToCount = {1,3}
	 * @param traitsToCount
	 * @return
	 */
	public PhenotypeCount[] calculateTotals(int[] traitsToCount) {
		TreeMap<String, MFTotCounts> totals = new TreeMap<String, MFTotCounts>();

		Iterator<CageUI> cageUIIterator = selectedSet.iterator();
		while (cageUIIterator.hasNext()) {
			TreeMap<String, OrganismList> children = 
				cageUIIterator.next().getCage().getChildren();
			Iterator<String> oListIterator = children.keySet().iterator();
			while (oListIterator.hasNext()) {
				OrganismList oList = children.get(oListIterator.next());
				String customPhenotypeString = 
					oList.getCustomPhenotypeString(traitsToCount);
				if (!totals.containsKey(customPhenotypeString)) {
					totals.put(customPhenotypeString, new MFTotCounts(0,0));
				}
				MFTotCounts oldTotal = totals.get(customPhenotypeString);
				totals.put(customPhenotypeString, oldTotal.add(oList.getMFTotCounts()));
			}
		}

		PhenotypeCount[] result = new PhenotypeCount[totals.keySet().size()];
		Iterator<String> customPhenoIterator = totals.keySet().iterator();
		int i = 0;
		while (customPhenoIterator.hasNext()) {
			String pheno = customPhenoIterator.next();
			result[i] = new PhenotypeCount(pheno, totals.get(pheno));
			i++;
		}

		return result;
	}

	public CageUI[] getSelectedSet() {
		CageUI[] result = new CageUI[selectedSet.size()];
		Iterator<CageUI> it = selectedSet.iterator();
		int i = 0;
		while (it.hasNext()) {
			result[i] = it.next();
		}
		return result;
	}

	public Trait[] getTraitSet() {
		if (selectedSet == null) {
			return null;
		}

		ArrayList<Phenotype> phenoList = getOneOrganism().getPhenotypes();
		Trait[] result = new Trait[phenoList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = phenoList.get(i).getTrait();
		}
		return result;
	}

	//just get one random organism so you can find full
	//  set of phenos
	private Organism getOneOrganism() {
		TreeMap<String, OrganismList> children = 
			selectedSet.first().getCage().getChildren();
		String pheno = children.keySet().iterator().next();
		return children.get(pheno).get(0);	
	}

	public void showSummaryChart(VGLII master) {
		if(selectedSet.size() == 0) {
			JOptionPane.showMessageDialog(master,
					"<html>You have not selected any cages to summarize.<br>" +
					"You should double-click on one or more cages <br>" +
					"to select them; they will turn pale yellow to <br>" +
					"indicate that they are selected.",
					"Summary Chart Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		summaryChartUI = new SummaryChartUI(master);
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		Iterator<CageUI> it = selectedSet.iterator();
		while(it.hasNext()) {
			b.append(it.next().getId() + ",");
		}
		if(b.length() != 0) {
			b.deleteCharAt(b.length() -1);
		}
		return b.toString();
	}
}
