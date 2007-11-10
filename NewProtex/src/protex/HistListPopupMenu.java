package protex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class HistListPopupMenu extends JPopupMenu {
	
	JMenuItem showProteinItem;
	JMenuItem toUpperItem;
	JMenuItem toLowerItem;
	JMenuItem addNotesItem;
	JMenuItem deleteItem;
	
	public HistListPopupMenu(final JList list, final Protex protex) {
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
				protex.sendToUpperPanel(
						(FoldedPolypeptide)(((HistoryList)list).getSelectedValue()));
			}
		});
		
		toLowerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				protex.sendToLowerPanel(
						(FoldedPolypeptide)(((HistoryList)list).getSelectedValue()));
			}
		});
		
		addNotesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String oldText = 
					(((FoldedPolypeptide)(
							(HistoryList)list).getSelectedValue()).getToolTipTextString());
				String newText = (String)JOptionPane.showInputDialog(
	                    protex,
	                    "Edit the notes below:",
	                    "Add notes to this item",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    oldText);
				if ((newText != null) && (newText.length() > 0)) {
					((FoldedPolypeptide)(
							(HistoryList)list).getSelectedValue()).setToolTipTextString(newText);
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
