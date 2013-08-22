package edu.umb.jsPedigrees.client.Pelican;
/********************************************************************
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA  02111-1307, USA.
 *
 *  @author Copyright (C) Frank Dudbridge
 *  modified 2012 Brian White
 *
 ********************************************************************/

//package uk.ac.mrc.rfcgr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.umb.jsPedigrees.client.PE.PedigreeExplorer;
import edu.umb.jsPedigrees.client.PE.RandomPedigreeGenerator;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class Pelican extends AbsolutePanel {

	private PedigreeExplorer pedEx;
	public static MenuBar menuBar;
	private PopupPanel popup;
	private PelicanPerson currentPerson;
	private int currentId;
	private boolean pedHasChanged;
	private MenuItem Parents;
	private boolean mergeEnabled;
	private Vector<Vector<PelicanPerson>> history;
	private int historyPosition;
	private int fontAscent;

	private HashSet<String> matingList;

	/* {{{ constructor (popup menu) */

	public Pelican(RootPanel rootPanel) {
		super();

		// set up the popup menu that appears when you click on a person
		popup = new PopupPanel();
		MenuBar popupMenu = new MenuBar(true);

		MenuBar addMenu = new MenuBar(true);
		addMenu.addItem("1 son", new Command() {
			public void execute() {
				addChildren("1 son");
			}
		});
		addMenu.addItem("1 daughter", new Command() {
			public void execute() {
				addChildren("1 daughter");
			}
		});
		addMenu.addItem("2 sons", new Command() {
			public void execute() {
				addChildren("2 sons");
			}
		});
		addMenu.addItem("2 daughters", new Command() {
			public void execute() {
				addChildren("2 daughters");
			}
		});
		addMenu.addItem("3 sons", new Command() {
			public void execute() {
				addChildren("3 sons");
			}
		});
		addMenu.addItem("3 daughters", new Command() {
			public void execute() {
				addChildren("3 daughters");
			}
		});
		addMenu.addItem("Spouse+son", new Command() {
			public void execute() {
				addSpouse("Spouse+son");
			}
		});
		addMenu.addItem("Spouse+daughter", new Command() {
			public void execute() {
				addSpouse("Spouse+daughter");
			}
		});
		Parents = new MenuItem("Parents", new Command() {
			public void execute() {
				addParents();
			}
		});
		addMenu.addItem(Parents);
		popupMenu.addItem("Add", addMenu);

		MenuBar changeMenu = new MenuBar(true);

		MenuBar changeAff = new MenuBar(true);
		changeAff.addItem("Affected", new Command() {
			public void execute() {
				currentPerson.affection = PelicanPerson.affected;
				updateDisplay();
			}
		});
		changeAff.addItem("Unaffected", new Command() {
			public void execute() {
				currentPerson.affection = PelicanPerson.unaffected;
				updateDisplay();
			}
		});
		changeMenu.addItem("Affection", changeAff);

		MenuBar changeSex = new MenuBar(true);
		changeSex.addItem("Male", new Command() {
			public void execute() {
				currentPerson.sex = PelicanPerson.male;
				checkSex();
				updateDisplay();
			}
		});
		changeSex.addItem("Female", new Command() {
			public void execute() {
				currentPerson.sex = PelicanPerson.female;
				checkSex();
				updateDisplay();
			}
		});
		changeMenu.addItem("Sex", changeSex);

		popupMenu.addItem("Change", changeMenu);

		popupMenu.addItem("Merge with...", new Command() {
			public void execute() {
				mergePerson(currentPerson);
			}
		});

		popupMenu.addItem("Delete", new Command() {
			public void execute() {
				deletePerson(currentPerson);
			}
		});

		popup.add(popupMenu);

		// main menu
		MenuBar mainMenu = new MenuBar(true);

		MenuBar editMenu = new MenuBar(true);
		editMenu.addItem("New Pedigree", new Command() {
			public void execute() {
				newPedigree();
				updateDisplay();
			}
		});
		editMenu.addItem("Undo", new Command() {
			public void execute() {
				if (historyPosition > 1) {
					historyPosition--;
					Vector<PelicanPerson> savedPed = (Vector<PelicanPerson>)history.elementAt(historyPosition - 1);
					loadPedigree(savedPed);
					pedHasChanged = true;
					updateDisplay();
				}
			}
		});
		editMenu.addItem("Redo", new Command() {
			public void execute() {
				if (historyPosition < history.size()) {
					historyPosition++;
					Vector<PelicanPerson> savedPed = (Vector<PelicanPerson>)history.elementAt(historyPosition - 1);
					loadPedigree(savedPed);
					pedHasChanged = true;
					updateDisplay();
				}
			}
		});
		editMenu.addItem("Renumber", new Command() {
			public void execute() {
				renumberAll();
				updateDisplay();
			}
		});
		mainMenu.addItem("Edit", editMenu);
		rootPanel.add(mainMenu);

		mergeEnabled = false;
		history = new Vector<Vector<PelicanPerson>>();
		historyPosition = 0;
		matingList = new HashSet<String>();
		newPedigree();
	}

	public void setPedEx(PedigreeExplorer pe) {
		pedEx = pe;
	}

	private void newPedigree() {
		// start out with a single female
		clear();
		currentId = 1;
		add(new PelicanPerson(this, currentId++, PelicanPerson.female, 0));
		savePedigree();
		pedHasChanged = true;
	}

	/* }}} */

	// load pedigree from the history
	private void loadPedigree(Vector<PelicanPerson> savedPed) {
		removeAll();
		HashMap<Integer, PelicanPerson> idMap=new HashMap<Integer, PelicanPerson>();
		for(int i = 0; i < savedPed.size() ; i++) {
			PelicanPerson p=(PelicanPerson)savedPed.elementAt(i);
			PelicanPerson person=new PelicanPerson(p);
			add(person);
			idMap.put(new Integer(p.id),person);
		}
		for(int i=0;i<getComponentCount();i++) {
			PelicanPerson p=(PelicanPerson)getComponent(i);
			if (p.father!=null)
				//gww		p.father=(PelicanPerson)idMap.get(new Integer(p.father.id));
				p.father=(PelicanPerson)idMap.get(p.father.id);
			if (p.mother!=null)
				//gww		p.mother=(PelicanPerson)idMap.get(new Integer(p.mother.id));
				p.mother=(PelicanPerson)idMap.get(p.mother.id);
		}
	}

	/* }}} */

	/* {{{ updateDisplay */

	public void updateDisplay() {
		savePedigree();
		pedHasChanged=true;
		paint(getGraphics());
	}

	/* }}} */

	/* {{{ areSpouses */

	private boolean areSpouses(PelicanPerson person1,PelicanPerson person2) {
		if (person1==null || person2==null) return(false);
		//gww	if (matingList.contains(new Dimension(person1.id,person2.id)) ||
		//gww	    matingList.contains(new Dimension(person2.id,person1.id)))
		// construct unique set entry from the id strings concatenated with a space
		if (matingList.contains(person1.id+" "+person2.id) ||
				matingList.contains(person2.id+" "+person1.id))
			return(true);
		return(false);
	}

	/* }}} */

	/* {{{ isChild */

	private boolean isChild(PelicanPerson child,PelicanPerson person1,PelicanPerson person2) {
		if (child.father==person1 && child.mother==person2 ||
				child.mother==person1 && child.father==person2)
			return(true);
		return(false);
	}

	/* }}} */

	/* {{{ areSibs */

	private boolean areSibs(PelicanPerson person1,PelicanPerson person2) {
		if (person1.father==person2.father && person1.mother==person2.mother)
			return(true);
		return(false);
	}

	/* }}} */

	/* {{{ isAncestor */

	private boolean isAncestor(PelicanPerson parent,PelicanPerson child) {
		if (child.father==parent || child.mother==parent)
			return(true);
		if (child.father!=null) {
			if (isAncestor(parent,child.father)) return(true);
		}
		if (child.mother!=null) {
			if (isAncestor(parent,child.mother)) return(true);
		}
		return(false);
	}

	/* }}} */

	/* {{{ addParents */

	private void addParents() {

		// Nice idea to move connections to the edge of the sibships, but
		// lots of potential complications.  For now, will live with
		// messy node connections...
		//  	// find the spouse of currentPerson (who will be an orphan)
		//  	PelicanPerson spouse=null;
		//  	int spouseIndex=0;
		//  	for(int i=0;i<getComponentCount();i++)
		//  	    if (getComponent(i) instanceof PelicanPerson) {
		//  		if (areSpouses((PelicanPerson)getComponent(i),currentPerson)) {
		//  		    spouseIndex=i;
		//  		    spouse=(PelicanPerson)getComponent(i);
		//  		}
		//  	    }

		//  	if (spouse!=null) {
		//  	    // find out if sibs of the spouse have non-orphaned spouses
		//  	    int nbranch=0; // number of non-orphaned spouses of sibs
		//  	    int firstsib=-1; // index of first sib of the spouse
		//  	    int lastsib=0; // index of last sib of the spouse
		//  	    for(int i=0;i<getComponentCount();i++)
		//  		if (getComponent(i) instanceof PelicanPerson) {
		//  		    PelicanPerson person=(PelicanPerson)getComponent(i);
		//  		    if (person.pid==spouse.pid && person.mid==spouse.mid) {
		//  			// we have a sib, so check its spouses
		//  			if (firstsib==-1) firstsib=i;
		//  			lastsib=i;
		//  			for(int j=0;j<getComponentCount();j++)
		//  			    if (getComponent(j) instanceof PelicanPerson) {
		//  				PelicanPerson sibSpouse=(PelicanPerson)getComponent(j);
		//  				if (areSpouses(sibSpouse,person) && 
		//  				    sibSpouse.pid!=PelicanPerson.unknown &&
		//  				    sibSpouse.mid!=PelicanPerson.unknown)
		//  				    nbranch++;
		//  			    }
		//  		    }
		//  		}
		//  	    System.out.println("nbranch "+String.valueOf(nbranch));
		//  	    // if no other sibs, move sib to the left
		//  	    if (nbranch==0) {
		//  		remove(spouseIndex);
		//  		add(spouse,firstsib);
		//  	    }
		//  	    // if one sib, move sib to the right
		//  	    if (nbranch==1) {
		//  		remove(spouseIndex);
		//  		add(spouse,lastsib);
		//  	    }
		//  	}

		// new father
		currentPerson.father=new PelicanPerson(this, currentId++, PelicanPerson.male, currentPerson.generation-1);
		add(currentPerson.father);
		// new mother
		currentPerson.mother=new PelicanPerson(this, currentId++, PelicanPerson.female, currentPerson.generation-1);
		add(currentPerson.mother);

		updateDisplay();
	}

	/* }}} */

	/* {{{ addChildren */

	private void addChildren(String request) {

		// find spouse for this subject
		PelicanPerson spouse=null;
		for(int i=0;i<getComponentCount() && spouse==null;i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				if (areSpouses(person,currentPerson))
					spouse=person;
			}
		// create a spouse
		if (spouse==null) {
			if (currentPerson.sex==PelicanPerson.female)
				spouse=new PelicanPerson(this, currentId++, PelicanPerson.male, currentPerson.generation);
			else
				spouse=new PelicanPerson(this, currentId++, PelicanPerson.female, currentPerson.generation);
			add(spouse);
		}

		// add in the children
		int generation=Math.max(currentPerson.generation,spouse.generation);
		for(int i=0;i<Integer.parseInt(request.substring(0,1));i++) {
			if (currentPerson.sex==PelicanPerson.female) {
				if (request.charAt(2)=='s')
					add(new PelicanPerson(this, currentId++, spouse, currentPerson, PelicanPerson.male, generation+1));
				else
					add(new PelicanPerson(this, currentId++, spouse, currentPerson, PelicanPerson.female, generation+1));
			}
			else {
				if (request.charAt(2)=='s')
					add(new PelicanPerson(this, currentId++, currentPerson, spouse, PelicanPerson.male, generation+1));
				else
					add(new PelicanPerson(this, currentId++, currentPerson, spouse, PelicanPerson.female, generation+1));
			}
		}

		updateDisplay();
	}

	/* }}} */

	/* {{{ addSpouse */

	private void addSpouse(String request) {

		// create a spouse
		int spouseId=currentId++;
		PelicanPerson spouse;
		if (currentPerson.sex==PelicanPerson.female) {
			spouse=new PelicanPerson(this, spouseId, PelicanPerson.male, currentPerson.generation);
			add(spouse);
		}
		else {
			spouse=new PelicanPerson(this, spouseId, PelicanPerson.female, currentPerson.generation);
			add(spouse);
		}
		// add in the child
		if (currentPerson.sex==PelicanPerson.female) {
			if (request.charAt(7)=='s')
				add(new PelicanPerson(this, currentId++, spouse, currentPerson, PelicanPerson.male, currentPerson.generation+1));
			else
				add(new PelicanPerson(this, currentId++, spouse, currentPerson, PelicanPerson.female, currentPerson.generation+1));
		}
		else {
			if (request.charAt(7)=='s')
				add(new PelicanPerson(this, currentId++, currentPerson, spouse, PelicanPerson.male, currentPerson.generation+1));
			else
				add(new PelicanPerson(this, currentId++, currentPerson, spouse, PelicanPerson.female, currentPerson.generation+1));
		}

		updateDisplay();
	}

	/* }}} */

	/* {{{ changeId */

	// swaps the Id's for two subjects
	private void changeId(int oldId, int newId) {
		for(int i = 0; i < getComponentCount(); i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				if (person.id == oldId) person.id = newId;
				else if (person.id == newId) person.id = oldId;
			}
	}


	// renumber the subjects, top-down, left-right
	public void renumberAll() {
		boolean someChange=false;

		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				int thisId=0;
				for(int j=0;j<getComponentCount();j++)
					if (getComponent(j) instanceof PelicanPerson) {
						PelicanPerson person2=(PelicanPerson)getComponent(j);
						if (person2.generation<person.generation ||
								person2.generation==person.generation &&
								person2.getX()<=person.getX())
							thisId++;
					}
				if (person.id!=thisId) {
					changeId(person.id,thisId);
					someChange=true;
				}
			}
		if (someChange)
			updateDisplay();
	}


	/* }}} */

	/* {{{ changeName */

	// change a subject's name
	private void inputChangeName() {
		//gww    String newName=JOptionPane.showInputDialog(this,"Enter new name for subject "+String.valueOf(currentPerson.id));
		String newName=JOptionPane.showInputDialog(this,"Enter new name for subject "+currentPerson.id);
		if (newName!=null && newName.trim().length()>0) {
			try {
				if (!currentPerson.name.equals(newName)) {
					currentPerson.name=newName;
					updateDisplay();
				}		
			}
			catch(Throwable t) {
				String message=t.getMessage();
				JOptionPane.showMessageDialog(this,message,"PELICAN error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* }}} */


	/* {{{ changeGenotypes */

	private void changeGenotypes() {
		JComboBox genotypes = new JComboBox(GENOTYPE_CHOICES[currentModelNumber]);
		if (!displayGenotypes) {
			JOptionPane.showMessageDialog(this,
					"You must select a particular genetic model\n before assigning genotypes.",
					"Pedigree Explorer error",JOptionPane.ERROR_MESSAGE);			
			return;
		}
		if (JOptionPane.showConfirmDialog(this,
				genotypes,
				"Edit genotypes",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION) {
			String genotypeString = (String) genotypes.getSelectedItem();
			String[] genoParts = genotypeString.split(" ");
			String[] newgeno = new String[2];
			newgeno[0] = genoParts[0];
			newgeno[1] = genoParts[1];
			currentPerson.setWorkingGenotype(currentModelNumber, newgeno);
		}
		pedHasChanged = true;
		updateDisplay();
	}


	/* }}} */




	/* {{{ mergePerson */

	private void mergePerson(PelicanPerson person) {
		if (mergeEnabled) {
			try {
				if (person!=currentPerson) {
					if (person.sex!=currentPerson.sex)
						throw(new Error("Persons to be merged must have the same sex"));
					if (!person.isOrphan() && !currentPerson.isOrphan())
						throw(new Error("One person to be merged must be an orphan"));
					if (isAncestor(person,currentPerson) ||
							isAncestor(currentPerson,person))
						throw(new Error("Cannot merge with a direct ancestor"));
					// remove the orphan, or the second-selected person
					PelicanPerson person1=person;
					PelicanPerson person2=currentPerson;
					if (person1.isOrphan() && !person2.isOrphan()) {
						person1=currentPerson;
						person2=person;
					}
				}
			}
			catch(Throwable t) {
				JOptionPane.showMessageDialog(this,t.getMessage(),"PELICAN error",JOptionPane.ERROR_MESSAGE);
			}
			//setCursor(Cursor.getDefaultCursor());
		}
		else {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		mergeEnabled=!mergeEnabled;
	}

	/* }}} */


	/* {{{ deletePerson */

	// recursively mark a subject and its descendents for deletion
	private void markForDeletion(PelicanPerson person) {
		// remove all children
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson p=(PelicanPerson)getComponent(i);
				if (p.father==person || p.mother==person)
					markForDeletion(p);
			}
		// remove orphan spouses, who have no other spouses of their own
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson p=(PelicanPerson)getComponent(i);
				if (areSpouses(p,person) && p.isOrphan()) {
					boolean soleMate=true;
					for(int j=0;j<getComponentCount();j++)
						if (getComponent(j) instanceof PelicanPerson) {
							PelicanPerson pp=(PelicanPerson)getComponent(j);
							if (areSpouses(p,pp) && pp!=person)
								soleMate=false;
						}
					if (soleMate) p.laidOut=true;
				}
			}
		// remove this subject
		person.laidOut=true;
	}

	// main routine to delete a person
	private void deletePerson(PelicanPerson person) {
		// check if the person has children
		boolean hasChild=false;
		boolean hasPaternalSibs=false;
		boolean hasMaternalSibs=false;
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson p=(PelicanPerson)getComponent(i);
				if (p.father==person || p.mother==person)
					hasChild=true;
				if (p!=person && p.father==person.father)
					hasPaternalSibs=true;
				if (p!=person && p.mother==person.mother)
					hasMaternalSibs=true;
			}
		if (!hasChild && hasPaternalSibs && hasMaternalSibs) {
			remove(person);
			updateDisplay();
			return;
		}

		// verify multiple deletes for internal subjects
		int choice=JOptionPane.OK_OPTION;
		Vector clause=new Vector();
		if (person.hasMother())
			//	    if (getMother(person).isOrphan() && !hasMaternalSibs)
			if (person.mother.isOrphan() && !hasMaternalSibs)
				clause.add("mother");
		if (person.hasFather())
			//	    if (getFather(person).isOrphan() && !hasPaternalSibs)
			if (person.father.isOrphan() && !hasPaternalSibs)
				clause.add("father");
		if (hasChild)
			clause.add("descendents");
		String message="The ";
		if (clause.size()==3)
			message+="mother, father and descendents";
		if (clause.size()==2)
			message+=(String)clause.elementAt(0)+" and "+(String)clause.elementAt(1);
		if (clause.size()==1)
			message+=(String)clause.elementAt(0);
		//gww	message+=" of subject "+String.valueOf(person.id)+" will also be deleted";
		message+=" of subject "+person.id+" will also be deleted";
		if (clause.size()>0)
			choice=JOptionPane.showConfirmDialog(this,message,"Confirm delete",JOptionPane.OK_CANCEL_OPTION);

		if (choice!=JOptionPane.OK_OPTION)
			return;

		// here, laidOut indicates whether a subject is marked for deletion 
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson)
				((PelicanPerson)getComponent(i)).laidOut=false;

		markForDeletion(person);
		if (person.hasMother())
			if (person.mother.isOrphan() && !hasMaternalSibs)
				person.mother.laidOut=true;
		if (person.hasFather())
			if (person.father.isOrphan() && !hasPaternalSibs)
				person.father.laidOut=true;

		boolean empty=true;
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				if (((PelicanPerson)getComponent(i)).laidOut) {
					remove(i);
					i--;
				}
				else empty=false;
			}
		if (empty) newPedigree();
		updateDisplay();
	}

	/* }}} */

	/* {{{ ActionListener(this) */

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String request=source.getText();

		// Popup menu actions

		// Add menu actions
		if(request.equals("Parents"))
			addParents();
		if (request.charAt(0) >='1' && request.charAt(0) <='3' && !request.endsWith("%"))
			addChildren(request);

		if (request.charAt(0)=='S')
			addSpouse(request);


		// Change menu actions
		if (request.equals("Affected")) {
			currentPerson.affection=PelicanPerson.affected;
			updateDisplay();
		}
		if (request.equals("Unaffected")) {
			currentPerson.affection=PelicanPerson.unaffected;
			updateDisplay();
		}
		if (request.equals("Male")) {
			currentPerson.sex = PelicanPerson.male;
			checkSex();
			updateDisplay();
		}
		if (request.equals("Female")) {
			currentPerson.sex = PelicanPerson.female;
			checkSex();
			updateDisplay();
		}
		if (request.equals("name...")) {
			inputChangeName();
		}
		if (request.equals("genotype...")) {
			changeGenotypes();
		}
		if (request.equals("Merge with...")) {
			mergePerson(currentPerson);
		}

		if (request.equals("Delete")) {
			deletePerson(currentPerson);
		}

		// Edit menu actions
		if (request.startsWith("Undo") && historyPosition>1 ||
				request.startsWith("Redo") && historyPosition<history.size()) {
			if (request.startsWith("Undo")) historyPosition--;
			if (request.startsWith("Redo")) historyPosition++;
			Vector<PelicanPerson> savedPed=(Vector<PelicanPerson>)history.elementAt(historyPosition-1);
			loadPedigree(savedPed);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.equals("Renumber")) {
			renumberAll();
			updateDisplay();
		}

		// View menu actions
		if (request.equals("Zoom in")) {
			PelicanPerson.changeScale(1.1111111);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.equals("Zoom out")) {
			PelicanPerson.changeScale(0.9);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.endsWith("%")) {
			double scale=Double.parseDouble(request.substring(0,request.length()-1))/100.0;
			PelicanPerson.setScale(scale);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.startsWith("Vertical")) {
			PelicanPerson.changeVspace(request.endsWith("+")?5:-5);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.startsWith("Horizontal")) {
			PelicanPerson.changeHspace(request.endsWith("+")?5:-5);
			pedHasChanged=true;
			paint(getGraphics());
		}
		if (request.equals("Refresh")) {
			if (autoLayout.isSelected()) pedHasChanged=true;
			paint(getGraphics());
		}

		if (request.equals("Display IDs") || request.equals("Display names")) {
			setVisible(false);
			for(int i=0;i<getComponentCount();i++)
				if (getComponent(i) instanceof PelicanLines) {
					remove(i);
				}
			add(new PelicanLines(this,showId.isSelected(),showName.isSelected(), displayGenotypes),-1);
			pedHasChanged=true;
			paint(getGraphics());
			setVisible(true);
		}

		if (request.equals("Pelican help")) {
			showHelp();
		}

		if (request.equals("About...")) {
			JOptionPane.showMessageDialog(this,
					PedigreeExplorer.ABOUT_MESSAGE + versionMessage,
					"About Pedigree Explorer",
					JOptionPane.INFORMATION_MESSAGE);
		}

		if (request.equals("left")) {
			System.out.println("here");
		}

	}

	/* }}} */

	/* {{{ reorderSelected */

	// this is called after a mouse drag
	// if appropriate, move the selected person to a different position
	// in the list
	private void reorderSelected() {
		if (currentPerson==null) return;
		boolean haveMoved=false;

		// if currentPerson is a child, change its location among
		// its full sibs
		if (!currentPerson.isOrphan()) {
			boolean atCurrentPerson=false;
			boolean moveRight=false;
			for(int i=0;i<getComponentCount() && !haveMoved;i++)
				if (getComponent(i) instanceof PelicanPerson) {
					PelicanPerson sib=(PelicanPerson)getComponent(i);
					// find a sib to the right of currentPerson
					if (sib!=currentPerson && areSibs(sib,currentPerson) &&
							currentPerson.getX()<sib.getX()) {
						// insert currentPerson into this position
						if (!atCurrentPerson) {
							if (moveRight) add(currentPerson,i-1);
							else add(currentPerson,i);
							savePedigree();
						}
						haveMoved=true;
					}
					if (areSibs(sib,currentPerson)) {
						atCurrentPerson=(sib==currentPerson);
						if (atCurrentPerson) moveRight=true;
					}
				}
			// if no full sibs, move currentPerson to the end
			if (!haveMoved && !atCurrentPerson) {
				add(currentPerson);
				savePedigree();
			}
		}
		// if it is a root subject, change location among other roots
		else if (currentPerson.isRoot()) {
			// start by making a shorter list of root subjects
			Vector rootSubjects=new Vector();
			for(int i=0;i<getComponentCount();i++)
				if (getComponent(i) instanceof PelicanPerson) {
					PelicanPerson root=(PelicanPerson)getComponent(i);
					if (root.isRoot()) rootSubjects.add(root);
				}
			// find the root subjects to the left and right of currentPerson
			PelicanPerson currentLeft=null;
			boolean haveLeft=false;
			for(int i=rootSubjects.size()-1;i>=0 && !haveLeft;i--) {
				PelicanPerson root=(PelicanPerson)(rootSubjects.elementAt(i));
				if (root!=currentPerson && !areSpouses(root,currentPerson) &&
						currentPerson.getX()>root.getX()) {
					currentLeft=root;
					haveLeft=true;
				}
			}
			PelicanPerson currentRight=null;
			boolean haveRight=false;
			for(int i=0;i<rootSubjects.size() && !haveRight;i++) {
				PelicanPerson root=(PelicanPerson)(rootSubjects.elementAt(i));
				if (root!=currentPerson && !areSpouses(root,currentPerson) &&
						currentPerson.getX()<root.getX()) {
					currentRight=root;
					haveRight=true;
				}
			}

			// find the root subject to the right of the first spouse
			boolean diffRight=false;
			haveRight=false;
			for(int i=0;i<rootSubjects.size() && !haveRight;i++) {
				PelicanPerson spouse=(PelicanPerson)(rootSubjects.elementAt(i));
				if (spouse!=currentPerson && areSpouses(spouse,currentPerson)) {
					for(int j=0;j<rootSubjects.size() && !haveRight;j++) {
						PelicanPerson root=(PelicanPerson)(rootSubjects.elementAt(j));
						if (root!=spouse && root!=currentPerson &&
								!areSpouses(root,currentPerson) &&
								spouse.getX()<root.getX()) {
							if (root!=currentRight) diffRight=true;
							haveRight=true;
						}
					}
					if (!haveRight && currentRight!=null) diffRight=true;
					haveRight=true;
				}
			}

			// don't allow movements between spouses
			if (areSpouses(currentLeft,currentRight)) return;

			// if movement is outside the current mating,
			// move the whole mating
			if (diffRight) {
				// first remove and save currentPerson and its spouses
				Vector currentMating=new Vector();
				int limit=rootSubjects.size();
				for(int i=0;i<limit;i++) {
					PelicanPerson spouse=(PelicanPerson)(rootSubjects.elementAt(i));
					if (spouse==currentPerson || areSpouses(spouse,currentPerson)) {
						rootSubjects.remove(spouse);
						currentMating.add(spouse);
						i--;
						limit--;
					}
				}
				// then reinsert them in before currentRight
				for(int i=0;i<currentMating.size();i++) {
					PelicanPerson spouse=(PelicanPerson)(currentMating.elementAt(i));
					if (currentRight==null)
						rootSubjects.add(spouse);
					else
						rootSubjects.insertElementAt(spouse,rootSubjects.indexOf(currentRight));
				}
				// then put all the root subjects back into this container
				for(int i=0;i<rootSubjects.size();i++)
					add((PelicanPerson)rootSubjects.elementAt(i));
				savePedigree();
			}
			// otherwise rearrange this mating
			else {
				boolean atCurrentPerson=false;
				boolean moveRight=false;
				for(int i=0;i<getComponentCount() && !haveMoved;i++)
					if (getComponent(i) instanceof PelicanPerson) {
						PelicanPerson spouse=(PelicanPerson)getComponent(i);
						// find a spouse to the right of currentPerson
						if (spouse!=currentPerson && areSpouses(spouse,currentPerson) &&
								currentPerson.getX()<spouse.getX()) {
							// insert currentPerson into this position
							if (!atCurrentPerson) {
								if (moveRight) add(currentPerson,i-1);
								else add(currentPerson,i);
								savePedigree();
							}
							haveMoved=true;
						}
						if (spouse==currentPerson || areSpouses(spouse,currentPerson)) {
							atCurrentPerson=(spouse==currentPerson);
							if (atCurrentPerson) moveRight=true;
						}
					}
				// if no spouses, move currentPerson to the end
				if (!haveMoved && !atCurrentPerson) {
					add(currentPerson);
					savePedigree();
				}
			}
		}
	}

	/* }}} */

	/*
	 * see if you've just made two male parents or two female parents
	 * by changing the sex of the current person
	 */
	private void checkSex() {
		Iterator<String> matingListIt = matingList.iterator();
		HashMap<Integer, Integer> matingsA = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> matingsB = new HashMap<Integer, Integer>();
		while (matingListIt.hasNext()) {
			String[] parts = matingListIt.next().split(" ");
			matingsA.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			matingsB.put(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
		}

		PelicanPerson spouse = null;
		if (matingsA.containsKey(currentPerson.id)) spouse = getPersonById(matingsA.get(currentPerson.id));
		if (matingsB.containsKey(currentPerson.id)) spouse = getPersonById(matingsB.get(currentPerson.id));
		if ((spouse != null) && (spouse.sex == currentPerson.sex)) {
			JOptionPane.showMessageDialog(this, 
					"You can't have two parents of the same gender; undoing change.",
					"Editing Error",
					JOptionPane.ERROR_MESSAGE);
			historyPosition--;
			Vector<PelicanPerson> savedPed=(Vector<PelicanPerson>)history.elementAt(historyPosition-1);
			loadPedigree(savedPed);
			pedHasChanged=true;
			paint(getGraphics());
		}
	}

	private PelicanPerson getPersonById(int id) {
		Vector<PelicanPerson> people = getAllPeople();
		Iterator<PelicanPerson> pIt = people.iterator();
		while (pIt.hasNext()) {
			PelicanPerson p = pIt.next();
			if (p.id == id) return p;
		}
		return null;
	}

	/* {{{ layoutPerson */

	// recursively lay out a person
	// returns: number of horizontal units taken up by its nuclear family
	private double layoutPerson(PelicanPerson person,int across) {
		int offspringSpace=0;
		int spouseSpace=0;
		PelicanPerson spouse=null;
		int verticalOffset;
		if (displayGenotypes) {
			verticalOffset = (1 + (showName.isSelected()?1:0))*fontAscent;
		} else {
			verticalOffset = ((showName.isSelected()?1:0))*fontAscent;
		}


		// lay out with the first spouse
		boolean haveSpouse=false;
		boolean hasSibs=false;
		for(int i=0;i<getComponentCount() && !haveSpouse;i++)
			if (getComponent(i) instanceof PelicanPerson) {
				spouse=(PelicanPerson)getComponent(i);
				PelicanPerson lastChild=null;
				if (areSpouses(spouse,person) /* && spouse.isOrphan() */ /*&&
					     !spouse.laidOut*/) {
					for(int j=0;j<getComponentCount();j++)
						if (getComponent(j) instanceof PelicanPerson) {
							PelicanPerson child=(PelicanPerson)getComponent(j);
							if (isChild(child,person,spouse)) {
								offspringSpace+=layoutPerson(child,across+offspringSpace);
								lastChild=child;
							}
						}
					haveSpouse=true;
				}
				if (areSibs(spouse,person) && spouse!=person) hasSibs=true;
			}
		// place to the left of the spouse
		if (!person.laidOut) {
			if (offspringSpace>1) 
				person.setLocation(PelicanPerson.xSpace*(2*across+offspringSpace-2)/2,(PelicanPerson.ySpace+verticalOffset)*person.generation);
			else
				person.setLocation(PelicanPerson.xSpace*across,(PelicanPerson.ySpace+verticalOffset)*person.generation);
			// move a singleton child across by half a space
			if (!hasSibs && !haveSpouse) {
				if ((person.father==null || !person.father.laidOut) &&
						(person.mother==null || !person.mother.laidOut))
					person.setLocation(person.getX()+PelicanPerson.xSpace/2,person.getY());
			}
			person.laidOut=true;
			spouseSpace++;
		}
		// place the spouse
		if (haveSpouse && !spouse.laidOut && spouse.generation==person.generation) {
			if (offspringSpace>1) 
				spouse.setLocation(PelicanPerson.xSpace*(2*across+offspringSpace
						-2+spouseSpace*2)/2,(PelicanPerson.ySpace+verticalOffset)*person.generation);
			else 
				spouse.setLocation(PelicanPerson.xSpace*(across+spouseSpace),(PelicanPerson.ySpace+verticalOffset)*person.generation);
			spouse.laidOut=true;
			spouseSpace++;
		}

		int maxSpace=(offspringSpace>spouseSpace)?offspringSpace:spouseSpace;

		// lay out with other spouses
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				spouse=(PelicanPerson)getComponent(i);
				spouseSpace=0;
				offspringSpace=0;
				if (areSpouses(spouse,person) &&
						(spouse.isOrphan() || !spouse.laidOut)) {
					for(int j=0;j<getComponentCount();j++)
						if (getComponent(j) instanceof PelicanPerson) {
							PelicanPerson child=(PelicanPerson)getComponent(j);
							if (isChild(child,person,spouse))
								offspringSpace+=layoutPerson(child,across+maxSpace+offspringSpace);
						}
					// place the spouse
					if (!spouse.laidOut && spouse.generation==person.generation) {
						spouse.setLocation(PelicanPerson.xSpace*(2*(across+maxSpace)+offspringSpace-1)/2,(PelicanPerson.ySpace+verticalOffset)*person.generation);
						spouse.laidOut=true;
						spouseSpace++;
					}
					maxSpace+=(offspringSpace>spouseSpace)?offspringSpace:spouseSpace;
				}
			}

		// System.out.println("ID "+person.id+" max "+String.valueOf(maxSpace));

		return(maxSpace);
	}

	/* }}} */

	public int getCurrentModelNumber() {
		return currentModelNumber;
	}

	public Vector<PelicanPerson> getAllPeople() {
		Vector<PelicanPerson> result = new Vector<PelicanPerson>();
		for (int i = 0; i < getComponentCount(); i++) {
			Component c = getComponent(i);
			if (c instanceof PelicanPerson) {
				result.add((PelicanPerson)c);
			}
		}
		return result;
	}

	public HashSet<String> getMatingList() {
		return matingList;
	}

	/* {{{ paintComponent: draw the pedigree */

	// draw the pedigree
	public void paintComponent(Graphics g) {

		super.paintComponent(g); 
		if (!pedHasChanged) return;

		setVisible(false);
		undoMenu.setEnabled(historyPosition>1);
		redoMenu.setEnabled(historyPosition!=history.size());

		Graphics2D g2=(Graphics2D)g;
		fontAscent=g.getFontMetrics().getAscent();

		// Initialise: nobody laid out, orphans are root subjects
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				person.laidOut=false;
				person.root=person.isOrphan();
			}

		// Make list of matings and ensure roots have orphan spouses
		matingList.clear();
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				if (person.father!=null && person.mother!=null)
					//gww                  matingList.add(new Dimension(person.father.id,person.mother.id));
					matingList.add(person.father.id+" "+person.mother.id);
				if (person.father!=null && !person.father.isOrphan())
					if (person.mother!=null && person.mother.generation>=person.father.generation)
						person.mother.root=false;
				if (person.mother!=null && !person.mother.isOrphan())
					if (person.father!=null && person.father.generation>=person.mother.generation)
						person.father.root=false;
			}

		// lay out the root subjects
		// person is a root if it has spouses which are all orphans
		int rootSpace=0;
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)getComponent(i);
				if (!person.laidOut && person.isOrphan() &&
						person.isRoot()) {
					rootSpace+=layoutPerson(person,rootSpace);
				}
			}

		// normalise x-y locations
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanLines) remove(i);
		int minx=0;
		int miny=0;
		int maxx=0;
		int maxy=0;
		for(int i=0;i<getComponentCount();i++) {
			Component c=getComponent(i);
			if (i==0 || c.getX()<minx) minx=c.getX();
			if (i==0 || c.getY()<miny) miny=c.getY();
			if (i==0 || c.getX()>maxx) maxx=c.getX();
			if (i==0 || c.getY()>maxy) maxy=c.getY();
		}

		for(int i=0;i<getComponentCount();i++) {
			Component c=getComponent(i);
			// if pedigree fits in the frame, then centre it
			// otherwise start it at (0,0)
			if (maxx-minx+PelicanPerson.xSpace < pedEx.getPelicanWidth())
				c.setLocation(c.getX()-(maxx + minx - pedEx.getPelicanWidth() + PelicanPerson.symbolSize)/2,
						c.getY()-miny+PelicanPerson.symbolSize/2);
			else
				c.setLocation(c.getX()-minx+PelicanPerson.symbolSize/2,c.getY()-miny+PelicanPerson.symbolSize/2);
		}

		// draw the lines on the graph
		add(new PelicanLines(this,showId.isSelected(),showName.isSelected(),displayGenotypes),-1);

		if (displayGenotypes) {
			setPreferredSize(new Dimension(
					maxx - minx + PelicanPerson.xSpace + PelicanPerson.symbolSize/2,
					maxy-miny + PelicanPerson.ySpace + PelicanPerson.symbolSize/2 + fontAscent));			
		} else {
			setPreferredSize(new Dimension(
					maxx - minx + PelicanPerson.xSpace + PelicanPerson.symbolSize/2,
					maxy - miny + PelicanPerson.ySpace + PelicanPerson.symbolSize/2));
		}


		setVisible(true);

		currentId=0;
		// need to check if this is a valid integer ID 
		for(int i=0;i<getComponentCount();i++)
			if (getComponent(i) instanceof PelicanPerson) {
				// if 'id' is a valid integer
				int id = ((PelicanPerson)getComponent(i)).id;
				if (id > 0)
					currentId=Math.max(currentId,((PelicanPerson)getComponent(i)).id);
			}
		currentId++;
		currentPerson=null;
		pedHasChanged=false;    
	}

	/* }}} */



	/* {{{ checkIntegrity */

	// check that fathers are male, etc
	private int checkIntegrity(Vector pedigree) {
		for(int i=0;i<pedigree.size();i++) {
			PelicanPerson person=(PelicanPerson)pedigree.elementAt(i);
			boolean fatherError=false;
			boolean motherError=false;
			for(int j=0;j<pedigree.size();j++) {
				PelicanPerson parent=(PelicanPerson)pedigree.elementAt(j);
				if (parent==person.father && parent.sex==PelicanPerson.female)
					fatherError=true;
				if (parent==person.mother && parent.sex==PelicanPerson.male)
					motherError=true;
			}
			// perhaps the pid/mid fields are swapped?
			if (fatherError && motherError) {
				//gww		int choice = JOptionPane.showConfirmDialog(this,"Subject "+String.valueOf(person.id)+" has a male mother and female father.  Choose YES to exchange the parental IDs.","Pelican: pedigree structure error",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				int choice = JOptionPane.showConfirmDialog(this,"Subject "+person.id+" has a male mother and female father.  Choose YES to exchange the parental IDs.","Pelican: pedigree structure error",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (choice==JOptionPane.CANCEL_OPTION)
					return(choice);
				if (choice==JOptionPane.YES_OPTION) {
					PelicanPerson temp=person.father;
					person.father=person.mother;
					person.mother=temp;
				}
			}
			// male mother...
			else {
				if (fatherError) {
					//gww		    int choice = JOptionPane.showConfirmDialog(this,"Subject "+String.valueOf(person.id)+" has a female father.","Pelican: pedigree structure error",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
					int choice = JOptionPane.showConfirmDialog(this,"Subject "+person.id+" has a female father.","Pelican: pedigree structure error",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
					if (choice==JOptionPane.CANCEL_OPTION)
						return(choice);
				}
				if (motherError) {
					//gww		    int choice = JOptionPane.showConfirmDialog(this,"Subject "+String.valueOf(person.id)+" has a male mother.","Pelican: pedigree structure error",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
					int choice = JOptionPane.showConfirmDialog(this,"Subject "+person.id+" has a male mother.","Pelican: pedigree structure error",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
					if (choice==JOptionPane.CANCEL_OPTION)
						return(choice);
				}
			}
		}
		return(JOptionPane.OK_OPTION);
	}

	/* }}} */

	/* {{{ openFile */

	private void openFile() {
		openFile(null,null);
	}

	private void openFile(String fileName) {
		openFile(fileName,null);
	}

	private void openFile(String fileName,String pedName) {
		try {
			File file;
			if (fileName==null) {
				JFileChooser dialog=new JFileChooser(currentDirectory);
				dialog.setDialogTitle("Open pedigree file");
				dialog.addChoosableFileFilter(new GeneralFilter("ped"));
				if (dialog.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
					return;
				file=dialog.getSelectedFile();
			}
			else file=new File(fileName);

			String filename=file.getCanonicalPath();
			if (!file.canRead())
				throw(new Error("Cannot read file "+filename));


			// read in this pedigree from the file
			Vector<PelicanPerson> newPedigree = new Vector<PelicanPerson>();
			FileInputStream infile = new FileInputStream(filename);
			int pedSize=0;
			Vector<Integer> pidList=new Vector<Integer>();
			Vector<Integer> midList=new Vector<Integer>();
			HashMap<Integer, Integer> idMap=new HashMap<Integer, Integer>();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(infile);
			infile.close();

			List<Element> people = doc.getRootElement().getChildren();
			Iterator<Element> pIt = people.iterator();
			while (pIt.hasNext()) {
				Element e = pIt.next();
				if (e.getName().equals("Person")) {
					int id = Integer.parseInt(e.getAttributeValue("Id"));
					String name = e.getAttributeValue("Name");
					int sex = Integer.parseInt(e.getAttributeValue("Sex"));
					int affection = Integer.parseInt(e.getAttributeValue("Affection"));

					String[] genotype = new String[2];
					genotype[0] = "?";
					genotype[1] = "?";

					PelicanPerson person = new PelicanPerson(this, id, null, null, sex, affection, name, 0, genotype);
					person.setWorkingGenotype(0, e.getAttributeValue("ARgeno").split(" "));
					person.setWorkingGenotype(1, e.getAttributeValue("ADgeno").split(" "));
					person.setWorkingGenotype(2, e.getAttributeValue("SLRgeno").split(" "));
					person.setWorkingGenotype(3, e.getAttributeValue("SLDgeno").split(" "));
					newPedigree.add(person);

					int father = Integer.parseInt(e.getAttributeValue("Father"));
					pidList.add(new Integer(father));
					int mother = Integer.parseInt(e.getAttributeValue("Mother"));
					midList.add(new Integer(mother));

					idMap.put(new Integer(id),new Integer(pedSize++));

				}
			}


			for(int i=0;i<newPedigree.size();i++) {
				PelicanPerson person=(PelicanPerson)newPedigree.elementAt(i);
				//gww	    if (((Integer)pidList.elementAt(i)).intValue()!=PelicanPerson.unknown) {
				if ((Integer)pidList.elementAt(i) != PelicanPerson.unknownID) {
					if (!idMap.containsKey(pidList.elementAt(i)))
						//gww		    throw(new Error("Father of subject "+String.valueOf(person.id)+" is missing"));
						throw(new Error("Father of subject "+person.id+" is missing"));
					person.father=(PelicanPerson)newPedigree.elementAt(((Integer)idMap.get(pidList.elementAt(i))).intValue());
				}
				//gww	    if (((Integer)midList.elementAt(i)).intValue()!=PelicanPerson.unknown) {
				if ((Integer)midList.elementAt(i) != PelicanPerson.unknownID) {
					if (!idMap.containsKey(midList.elementAt(i)))
						//gww		    throw(new Error("Mother of subject "+String.valueOf(person.id)+" is missing"));
						throw(new Error("Mother of subject "+person.id+" is missing"));
					person.mother=(PelicanPerson)newPedigree.elementAt(((Integer)idMap.get(midList.elementAt(i))).intValue());
				}
			}

			// figure out the generations
			((PelicanPerson)newPedigree.elementAt(0)).laidOut=true;
			boolean someChange=true;
			int nperson=newPedigree.size();
			// repeatedly pass through the pedigree all subjects laid out
			while (someChange) {
				someChange=false;
				for(int i=0;i<nperson;i++) {
					PelicanPerson p=(PelicanPerson)newPedigree.elementAt(i);
					if (!p.laidOut) {
						// try to get it from the parents
						for(int j=0;j<nperson;j++) {
							PelicanPerson parent=(PelicanPerson)newPedigree.elementAt(j);
							if (parent==p.father && parent.laidOut) {
								p.generation=parent.generation+1;
								p.laidOut=true;
								someChange=true;
							}
							if (parent==p.mother && parent.laidOut) {
								p.generation=parent.generation+1;
								p.laidOut=true;
								someChange=true;
							}
						}
					}
					if (p.laidOut) {
						// assign parents generation
						for(int j=0;j<nperson;j++) {
							PelicanPerson parent=(PelicanPerson)newPedigree.elementAt(j);
							if (parent==p.father && !parent.laidOut) {
								parent.generation=p.generation-1;
								parent.laidOut=true;
								someChange=true;
							}
							if (parent==p.mother && !parent.laidOut) {
								parent.generation=p.generation-1;
								parent.laidOut=true;
								someChange=true;
							}
						}
					}
				}
			}

			if (checkIntegrity(newPedigree)==JOptionPane.CANCEL_OPTION)
				return;

			// end
			removeAll();
			displayGenotypes = false;
			currentId=0;
			boolean haveNames=false;
			for(int i=0;i<nperson;i++) {
				PelicanPerson p=(PelicanPerson)newPedigree.elementAt(i);
				add(p);
				if (!p.name.equals("")) haveNames=true;
				//gww need to add in check for valid integer ID else ignore updating of currentId
				if (p.id > 0) {
					//gww           if (i==0 || p.id>currentId) currentId=p.id;
					if (i==0 || p.id > currentId) currentId = p.id;
				}
			}
			currentId++;
			newPedigree.removeAllElements();
			showName.setSelected(haveNames);
			updateDisplay();
			currentDirectory=file.getParent();
		}
		catch(Throwable t) {
			JOptionPane.showMessageDialog(this,t.getMessage(),"PELICAN error",JOptionPane.ERROR_MESSAGE);
		}
		savePedigree();
	}

	/* }}} */


	/* {{{ saveFile */

	private void saveFile() {

		JFileChooser dialog=new JFileChooser(currentDirectory);
		dialog.setDialogTitle("Save pedigree file");
		dialog.addChoosableFileFilter(new GeneralFilter("ped"));
		if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File file = dialog.getSelectedFile();
				String filename = file.getAbsolutePath();
				if (!filename.endsWith(".ped")) filename = filename + ".ped";
				if (file.exists() && !file.canWrite())
					throw(new Error("Cannot write file "+filename));

				FileWriter outfile = new FileWriter(filename, false);
				XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
				outfile.write(outputter.outputString(getXMLDoc()));
				outfile.close();
				currentDirectory=file.getParent();
			}
			catch(Throwable t) {
				JOptionPane.showMessageDialog(this,t.getMessage(),"PELICAN error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* }}} */

	public Document getXMLDoc() {
		Element root = new Element("PedEx");
		Vector<PelicanPerson> people = getAllPeople();
		Iterator<PelicanPerson> it = people.iterator();
		while (it.hasNext()) {
			PelicanPerson p = it.next();
			root.addContent(p.save());
		}
		return new Document(root);
	}

	/* {{{ saveImage */

	// save as a PNG or JPEG image
	private void saveImage() {

		// this only works under java1.4+
		String javaVersion=System.getProperty("java.vm.version");
		if (javaVersion.startsWith("1.") && javaVersion.charAt(2)<'4')
			return;

		BufferedImage image = new BufferedImage(
				getWidth() * SCALE, 
				getHeight() * SCALE,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphics = (Graphics2D)image.getGraphics();
		graphics.scale(SCALE, SCALE);
		if(graphics != null) {
			paintAll(graphics);
		}

		if (askFormat==null || !askFormat.isSelected()) {
			if (askFormat==null)
				askFormat=new JCheckBox("Do not ask again",true);
			GridBagLayout grid = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			JPanel chooseFormat = new JPanel(grid);
			c.weightx=1.0;
			JLabel label=new JLabel("Choose image format: ");
			grid.setConstraints(label,c);
			chooseFormat.add(label);
			String [] formats={"PNG","JPEG"};
			JComboBox formatChoice = new JComboBox(formats);
			c.gridwidth=GridBagConstraints.REMAINDER;
			grid.setConstraints(formatChoice,c);
			chooseFormat.add(formatChoice);

			grid.setConstraints(askFormat,c);
			chooseFormat.add(askFormat);

			if (JOptionPane.showConfirmDialog(this,chooseFormat,"Pelican: image format",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
				askFormat=null;
				return;
			}
			imageFormat=(String)(formatChoice.getSelectedItem());
		}

		JFileChooser dialog=new JFileChooser(currentDirectory);
		dialog.setDialogTitle("Print image file");
		if (imageFormat.equals("PNG"))
			dialog.addChoosableFileFilter(new GeneralFilter("png"));
		if (imageFormat.equals("JPEG")) {
			dialog.addChoosableFileFilter(new GeneralFilter("jpeg"));
			dialog.addChoosableFileFilter(new GeneralFilter("jpg"));
		}
		if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File file=dialog.getSelectedFile();
				String filename=file.getAbsolutePath();
				if (file.exists() && !file.canWrite())
					throw(new Error("Cannot write file "+filename));
				ImageIO.write(image,imageFormat,file);
				currentDirectory=file.getParent();
			}
			catch(Throwable t) {
				JOptionPane.showMessageDialog(this,t.getMessage(),"PELICAN error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* }}} */

	/* {{{ printImage */

	// Thanks to Hugh Morgan for sharing his code for this
	private class MyWriter implements Printable {
		private BufferedImage image=null;
		public MyWriter() {}
		public void setImage(BufferedImage newImage) {
			image=newImage;
		}
		public int print(Graphics g,PageFormat format,int pageIndex) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(format.getImageableX(), format.getImageableY());
			double scale=1.0;
			if (format.getImageableWidth()/image.getWidth()<scale)
				scale=format.getImageableWidth()/image.getWidth();
			if (format.getImageableHeight()/image.getHeight()<scale)
				scale=format.getImageableHeight()/image.getHeight();
			g2d.scale(scale,scale);
			g2d.drawImage(image, null, null);
			return Printable.PAGE_EXISTS;
		}
	}

	private void printImage() {
		if (printerJob==null)
			printerJob = PrinterJob.getPrinterJob();
		if (printerJob.printDialog()) {
			try {
				BufferedImage image = (BufferedImage)createImage(getWidth(), getHeight());
				Graphics graphics = image.getGraphics();
				if(graphics != null) {
					paintAll(graphics);
				}
				MyWriter writer=new MyWriter();
				writer.setImage(image);
				Book book = new Book();
				book.append(writer, new PageFormat());
				printerJob.setPageable(book);
				printerJob.print();
			}
			catch(Throwable t) {
				JOptionPane.showMessageDialog(this,t.getMessage(),"Printing error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* }}} */

	/* {{{ showHelp */

	private void showHelp() {
		try {
			final JFrame helpFrame = new JFrame("Pelican: Help");
			final JEditorPane helpPane=new JEditorPane();
			helpPane.setContentType("text/html");

			JMenuBar helpMenuBar = new JMenuBar();

			JMenu helpFileMenu = new JMenu("File");
			helpFileMenu.setMnemonic(KeyEvent.VK_F);
			helpMenuBar.add(helpFileMenu);
			JMenuItem helpFileMenuHome = new JMenuItem("Home");
			helpFileMenuHome.setMnemonic(KeyEvent.VK_H);
			helpFileMenuHome.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						helpPane.setPage(this.getClass().getResource("Help/pelicanHelp.html"));
					}
					catch(Throwable t) {}
				}
			});
			helpFileMenu.add(helpFileMenuHome);
			JMenuItem helpFileMenuClose = new JMenuItem("Close");
			helpFileMenuClose.setMnemonic(KeyEvent.VK_C);
			helpFileMenuClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					helpFrame.dispatchEvent(new WindowEvent(pedEx.getMasterFrame(), WindowEvent.WINDOW_CLOSING));
				}
			});
			helpFileMenu.add(helpFileMenuClose);
			helpFrame.setJMenuBar(helpMenuBar);

			helpPane.setEditable(false);
			helpPane.addHyperlinkListener(new hyperListener());
			helpPane.setPage(this.getClass().getResource("Help/pelicanHelp.html"));
			JScrollPane scroll=new JScrollPane(helpPane);
			helpFrame.getContentPane().add(scroll);
			helpPane.setPreferredSize(new Dimension(600,400));
			helpFrame.pack();
			scroll.setViewportView(helpPane);
			helpFrame.setLocation(pedEx.getMasterFrame().getX()+300, pedEx.getMasterFrame().getY());
			helpFrame.setVisible(true);
		}
		catch(Throwable t) {
			JOptionPane.showMessageDialog(this,t.getMessage(),"PELICAN error",JOptionPane.ERROR_MESSAGE);
		}
	}

	/* }}} */


	/* {{{ popup menu listener */

	/**
	 *
	 * Popup menu listener
	 *
	 */
	class PopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			if (!popup.isVisible() && autoLayout.isSelected()) {
				reorderSelected();
				pedHasChanged=true;
				paint(getGraphics());
			}
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e)  {

			int x = e.getX();
			int y = e.getY();

			final Component c = getComponentAt(x,y);

			if (c instanceof PelicanPerson) {
				PelicanPerson p=(PelicanPerson)c;
				if (e.isPopupTrigger()) {
					if (p.isOrphan()) Parents.setEnabled(true);
					else Parents.setEnabled(false);
					popup.show(e.getComponent(),x,y);
					currentPerson=(PelicanPerson)c;
				}
				else {
					if (mergeEnabled) {
						mergePerson((PelicanPerson)c);
						updateDisplay();
					}
					else {
						currentPerson=(PelicanPerson)c;
					}
				}
			}
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			mergeEnabled=false;

		}
	}

	/* }}} */

	/* {{{ mouse drag listener */

	class dragListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			if (!popup.isVisible() && currentPerson!=null) {
				int x=e.getX();
				int y=e.getY();
				setVisible(false);
				currentPerson.setLocation(x,y);
				paint(getGraphics());
				setVisible(true);
			}
		}
	}

	/* }}} */

	/* {{{ hyperlink listener */

	class hyperListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				JEditorPane pane = (JEditorPane) e.getSource();
				try {
					pane.setPage(e.getURL());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	/* }}} */

	/* {{{ GeneralFilter */

	class GeneralFilter extends javax.swing.filechooser.FileFilter {
		private String suffix;
		GeneralFilter(String s) {
			suffix=s;
		}
		public boolean accept(File f) {
			if (f.isDirectory())
				return(true);
			if (f.getName().endsWith(suffix))
				return(true);
			return(false);
		}
		public String getDescription() {
			return "*."+suffix;
		}
	}

	/* }}} */

}
