package molGenExp;

import genetics.Tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class HistListPopupMenu extends JPopupMenu {
	
	JMenuItem toUpperItem;
	JMenuItem toLowerItem;
	JMenuItem addNotesItem;
	JMenuItem deleteItem;
	
	public HistListPopupMenu(final JList list, final Workbench workbench) {
		super();
		toUpperItem = new JMenuItem("Send to Upper Panel");
		toLowerItem = new JMenuItem("Send to Lower Panel");
		addNotesItem = new JMenuItem("Add Notes...");
		deleteItem = new JMenuItem("Delete from History List");
		this.add(toUpperItem);
		this.add(toLowerItem);
		this.addSeparator();
		this.add(addNotesItem);
		this.addSeparator();
		this.add(deleteItem);
		
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
				System.out.println(((HistListItem)((HistoryList)list).getSelectedValue()).getToolTipText());
			}
		});
		
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((HistoryList)list).deleteSelected();
			}
		});
		
	}

}
