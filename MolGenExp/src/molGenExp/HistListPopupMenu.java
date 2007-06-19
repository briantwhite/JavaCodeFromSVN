package molGenExp;

import genetics.Tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import molBiol.MolBiolWorkbench;

public class HistListPopupMenu extends JPopupMenu {
	
	JMenuItem showProteinItem;
	JMenuItem toUpperItem;
	JMenuItem toLowerItem;
	JMenuItem addNotesItem;
	JMenuItem deleteItem;
	
	public HistListPopupMenu(final JList list, final Workbench workbench, boolean isMolBiol) {
		super();
		if (isMolBiol) {
			showProteinItem = new JMenuItem("Show close-up of protein structure");
		}
		toUpperItem = new JMenuItem("Send to Upper Panel");
		toLowerItem = new JMenuItem("Send to Lower Panel");
		addNotesItem = new JMenuItem("Add Notes...");
		deleteItem = new JMenuItem("Delete from History List");
		
		if(isMolBiol) {
			this.add(showProteinItem);
			this.addSeparator();
		}
		this.add(toUpperItem);
		this.add(toLowerItem);
		this.addSeparator();
		this.add(addNotesItem);
		this.addSeparator();
		this.add(deleteItem);
		
		if(isMolBiol) {
			showProteinItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((MolBiolWorkbench)workbench).showProteinCloseUp((((HistoryList)list).getSelectedValue()));
				}
			});
		}

		toUpperItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workbench.sendToUpperPanel((((HistoryList)list).getSelectedValue()));
			}
		});
		
		toLowerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workbench.sendToLowerPanel((((HistoryList)list).getSelectedValue()));
			}
		});
		
		addNotesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String oldText = 
					(((HistListItem)((HistoryList)list).getSelectedValue()).getToolTipText());
				String newText = (String)JOptionPane.showInputDialog(
	                    workbench,
	                    "Edit the notes below:",
	                    "Add notes to this item",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    oldText);
				if ((newText != null) && (newText.length() > 0)) {
					((HistListItem)((HistoryList)list).getSelectedValue()).setToolTipText(newText);
				}
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((HistoryList)list).deleteSelected();
			}
		});
		
	}
}
