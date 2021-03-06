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

public class ArrowTile extends ConnectorTile {

	private int type;

//	private URL bfaImageURL = YeastVGL.class.getResource("images/BigForkedArrow.png");
//	private ImageIcon bigForkedArrow = new ImageIcon(bfaImageURL);
//	private URL sfaImageURL = YeastVGL.class.getResource("images/SmallForkedArrow.png");
//	private ImageIcon smallForkedArrow = new ImageIcon(sfaImageURL);
//	private URL bbaImageURL = YeastVGL.class.getResource("images/BigBentArrow.png");
//	private ImageIcon bigBentArrow = new ImageIcon(bbaImageURL);
//	private URL sbaImageURL = YeastVGL.class.getResource("images/SmallBentArrow.png");
//	private ImageIcon smallBentArrow = new ImageIcon(sbaImageURL);
	private URL bsaImageURL = YeastVGL.class.getResource("images/BigStraightArrow.png");
	private ImageIcon bigStraightArrow = new ImageIcon(bsaImageURL);
	private URL ssaImageURL = YeastVGL.class.getResource("images/SmallStraightArrow.png");
	private ImageIcon smallStraightArrow = new ImageIcon(ssaImageURL);
	private URL beImageURL = YeastVGL.class.getResource("images/BigEmpty.png");
	private ImageIcon bigEmpty = new ImageIcon(beImageURL);
	private URL seImageURL = YeastVGL.class.getResource("images/SmallEmpty.png");
	private ImageIcon smallEmpty = new ImageIcon(seImageURL);

	public ArrowTile(YeastVGL yeastVGL, int row, int col) {
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
		JMenuItem straightItem = new JMenuItem(smallStraightArrow);
		straightItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = STRAIGHT;
				updateSelectedTile(type);
			}		
		});
		popupMenu.add(straightItem);
//		JMenuItem forkedItem = new JMenuItem(smallForkedArrow);
//		forkedItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				type = FORKED_ARROW;
//				updateSelectedTile(type);
//			}		
//		});
//		popupMenu.add(forkedItem);
//		JMenuItem bentItem = new JMenuItem(smallBentArrow);
//		bentItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				type = BENT_ARROW;
//				updateSelectedTile(type);
//			}		
//		});
//		popupMenu.add(bentItem);
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
			add(new JLabel(bigStraightArrow));
//		} else if (type == FORKED) {
//			add(new JLabel(bigForkedArrow));
//		} else {
//			add(new JLabel(bigBentArrow));
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
