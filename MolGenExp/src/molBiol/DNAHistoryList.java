package molBiol;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import molGenExp.MolGenExp;

import biochem.FoldedPolypeptide;

public class DNAHistoryList extends JList implements Serializable {
	
	DefaultListModel histListDataModel;
	
	public DNAHistoryList(ListModel dataModel, final MolGenExp mge) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new DNAHistoryCellRenderer());
		histListDataModel = (DefaultListModel)dataModel;
		this.setFixedCellWidth(20);
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					ExpressedGene eg = 
						(ExpressedGene)getSelectedValue();
					FoldedPolypeptide fp = eg.getFoldedPolypeptide();
					JLabel colorChipForDialog = new JLabel("     ");
					colorChipForDialog.setOpaque(true);
					colorChipForDialog.setBackground(fp.getColor());
					colorChipForDialog.setBorder(new LineBorder(Color.BLACK));

					Object[] options = {"OK",
							new JLabel("Color:"),
							colorChipForDialog
					};
					JOptionPane.showOptionDialog(
							mge,
							"",
							"Protein Structure",
							JOptionPane.YES_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							fp.getFullSizePic(),
							options,
							options[0]);
				}
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
	}
	
	public void add(ExpressedGene vg) {
		histListDataModel.addElement(vg);
	}
	
	public void deleteSelected() {
		if (getSelectedIndex() != -1 ) {
			histListDataModel.removeElementAt(getSelectedIndex());
		} else {
			JOptionPane.showMessageDialog(null, "You have not selected an "
					+ "item to delete.",
					"None Selected", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void clearList() {
		histListDataModel.removeAllElements();
	}
	
	public Object[] getAll() {
		return histListDataModel.toArray();
	}

}
