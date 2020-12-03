package edu.umb.jsAipotu.client.genetics;/* this is the main class - the enetics workshop application * written by Brian White 2004 *  brian.white@umb.edu *   This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License * as published by the Free Software Foundation; either version 2 * of the License, or (at your option) any later version. * * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * */ import java.awt.BorderLayout;import java.awt.Dimension;import java.awt.GridLayout;import javax.swing.BorderFactory;import javax.swing.Box;import javax.swing.BoxLayout;import javax.swing.DefaultListModel;import javax.swing.JPanel;import javax.swing.JScrollPane;import edu.umb.jsAipotu.client.molGenExp.HistoryList;import edu.umb.jsAipotu.client.molGenExp.MolGenExp;import edu.umb.jsAipotu.client.molGenExp.WorkPanel;import edu.umb.jsAipotu.client.molGenExp.Workbench;public class GeneticsWorkbench extends Workbench {	GeneticsWorkPanel upperGeneticsWorkPanel;	GeneticsWorkPanel lowerGeneticsWorkPanel;//	GeneticsHistoryList geneticsHistoryList;	HistoryList geneticsHistoryList;		JScrollPane histListScrollPane;		int nextTrayNumber;	public GeneticsWorkbench (MolGenExp mge) {		super(mge);		setupUI();		nextTrayNumber = 0;	}	private void setupUI() {		setLayout(new BorderLayout());						JPanel mainPanel = new JPanel();				JPanel leftPanel = new JPanel();		leftPanel.setBorder(				BorderFactory.createTitledBorder("History List"));		geneticsHistoryList = 			new HistoryList(new DefaultListModel(), 					this, 					new GeneticsHistoryCellRenderer(),					false);		histListScrollPane = new JScrollPane(geneticsHistoryList);		histListScrollPane.setMaximumSize(new Dimension(150,1000));		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));		leftPanel.add(Box.createRigidArea(new Dimension(100,1)));		leftPanel.add(histListScrollPane);		JPanel genexPanel = new JPanel();		genexPanel.setLayout(new GridLayout(2,1));		upperGeneticsWorkPanel = new GeneticsWorkPanel("Upper", this);		lowerGeneticsWorkPanel = new GeneticsWorkPanel("Lower", this);		genexPanel.add(upperGeneticsWorkPanel);		genexPanel.add(lowerGeneticsWorkPanel);		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));		mainPanel.add(leftPanel);		mainPanel.add(genexPanel);				add(mainPanel, BorderLayout.CENTER);	}			public void setCrossTwoButtonsEnabled(boolean b) {		upperGeneticsWorkPanel.setCrossTwoButtonEnabled(b);		lowerGeneticsWorkPanel.setCrossTwoButtonEnabled(b);	}		public void setSelfCrossButtonsEnabled(boolean b) {		upperGeneticsWorkPanel.setSelfCrossButtonEnabled(b);		lowerGeneticsWorkPanel.setSelfCrossButtonEnabled(b);	}		public void setMutateButtonsEnabled(boolean b) {		upperGeneticsWorkPanel.setMutateButtonEnabled(b);		lowerGeneticsWorkPanel.setMutateButtonEnabled(b);	}		public int getNextTrayNum() {		nextTrayNumber++;		return nextTrayNumber;	}		public GeneticsWorkPanel getLowerGeneticsWindow() {		return lowerGeneticsWorkPanel;	}		public GeneticsWorkPanel getUpperGeneticsWindow() {		return upperGeneticsWorkPanel;	}		public void setDefaultSelectionSettings() {		upperGeneticsWorkPanel.getGeneticsWorkPanelList().setDefaultSelectionSettings();		lowerGeneticsWorkPanel.getGeneticsWorkPanelList().setDefaultSelectionSettings();	}		public void setCustomSelectionSettings() {		upperGeneticsWorkPanel.getGeneticsWorkPanelList().setCustomSelectionSettings();		lowerGeneticsWorkPanel.getGeneticsWorkPanelList().setCustomSelectionSettings();			}	public void addToHistoryList(Object o) {		geneticsHistoryList.add(o);		histListScrollPane.revalidate();		histListScrollPane.repaint();		mge.clearSelectedOrganisms();	}	public WorkPanel getLowerPanel() {		return lowerGeneticsWorkPanel;	}	public WorkPanel getUpperPanel() {		return upperGeneticsWorkPanel;	}	public void sendToLowerPanel(Object o) {		lowerGeneticsWorkPanel.setCurrentTray((Tray)o);	}		public void sendToUpperPanel(Object o) {		upperGeneticsWorkPanel.setCurrentTray((Tray)o);	}}