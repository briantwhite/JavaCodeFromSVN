package PathwayPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import YeastVGL.YeastVGL;

public class LineTile extends ConnectorTile {
	
	private int type;

	private URL bflImageURL = YeastVGL.class.getResource("images/BigForkedLine.png");
	private ImageIcon bigForkedLine = new ImageIcon(bflImageURL);
	private URL sflImageURL = YeastVGL.class.getResource("images/SmallForkedLine.png");
	private ImageIcon smallForkedLine = new ImageIcon(sflImageURL);
	private URL bblImageURL = YeastVGL.class.getResource("images/BigBentLine.png");
	private ImageIcon bigBentLine = new ImageIcon(bblImageURL);
	private URL sblImageURL = YeastVGL.class.getResource("images/SmallBentLine.png");
	private ImageIcon smallBentLine = new ImageIcon(sblImageURL);
	private URL bslImageURL = YeastVGL.class.getResource("images/BigStraightLine.png");
	private ImageIcon bigStraightLine = new ImageIcon(bslImageURL);
	private URL sslImageURL = YeastVGL.class.getResource("images/SmallStraightLine.png");
	private ImageIcon smallStraightLine = new ImageIcon(sslImageURL);
	private URL beImageURL = YeastVGL.class.getResource("images/BigEmpty.png");
	private ImageIcon bigEmpty = new ImageIcon(beImageURL);
	private URL seImageURL = YeastVGL.class.getResource("images/SmallEmpty.png");
	private ImageIcon smallEmpty = new ImageIcon(seImageURL);

	public LineTile(YeastVGL yeastVGL, int row, int col) {
		super(yeastVGL, row, col);
		BLANK_BACKGROUND_COLOR = new Color(255,255,240);
		ACTIVE_BACKGROUND_COLOR = new Color(255,255,220);
		setBackground(ACTIVE_BACKGROUND_COLOR);
		setOpaque(true);

		type = BLANK;

		JMenuItem emptyItem = new JMenuItem(smallEmpty);
		emptyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = BLANK;
				updateSelectedTile(type);
			}		
		});
		popupMenu.add(emptyItem);
		JMenuItem straightItem = new JMenuItem(smallStraightLine);
		straightItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = STRAIGHT;
				updateSelectedTile(type);
			}		
		});
		popupMenu.add(straightItem);
		JMenuItem forkedItem = new JMenuItem(smallForkedLine);
		forkedItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = FORKED;
				updateSelectedTile(type);
			}		
		});
		popupMenu.add(forkedItem);
		JMenuItem bentItem = new JMenuItem(smallBentLine);
		bentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = BENT;
				updateSelectedTile(type);
			}		
		});
		popupMenu.add(bentItem);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}
			private void showPopup(MouseEvent e) {
				popupMenu.show(e.getComponent(),
						e.getX(), e.getY());
			}
		});
	}

	public void updateSelectedTile(int type) {
		this.type = type;
		removeAll();
		if (type == BLANK) {
			add(new JLabel(bigEmpty));
		} else if (type == STRAIGHT) {
			add(new JLabel(bigStraightLine));
		} else if (type == FORKED) {
			add(new JLabel(bigForkedLine));
		} else {
			add(new JLabel(bigBentLine));
		}
		revalidate();
		repaint();
	}

	public int getSelection() {
		return type;
	}

	public void setSelection(int s) {
		type = s;
		updateSelectedTile(type);
	}
}
