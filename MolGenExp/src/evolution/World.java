package evolution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import molGenExp.MolGenExp;
import molGenExp.Organism;

public class World extends JPanel implements MouseListener {

	ThinOrganism[][] organisms = 
		new ThinOrganism[MolGenExp.worldSize][MolGenExp.worldSize];
	
	private int cellSize = 500/MolGenExp.worldSize;
	private int selectedCelli = -1;
	private int selectedCellj = -1;

	public World() {
		this.addMouseListener(this);
	}

	public void initialize(Organism o) {
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				organisms[i][j] = new ThinOrganism(o);
			}
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < MolGenExp.worldSize; i++) {
			for (int j = 0; j < MolGenExp.worldSize; j++) {
				if (organisms[i][j] == null) {
					g.setColor(Color.DARK_GRAY);
				} else {
					g.setColor(organisms[i][j].getColor());
				}
				g.fillRect((cellSize * i), (cellSize * j), (cellSize - 1), (cellSize - 1));
			}
		}
		//show the selected cell
		if ((selectedCelli > -1) && (selectedCellj > -1)) {
			g.setColor(Color.black);
			g.drawRect((cellSize * selectedCelli) - 1, 
					(cellSize * selectedCellj) - 1, 
					cellSize, cellSize);
		}
	}
	
	public ThinOrganism getThinOrganism(int i, int j) {
		return organisms[i][j];
	}
	
	public void setOrganisms(ThinOrganism[][] newOrgs) {
		organisms = null;
		organisms = newOrgs;
		repaint();
	}
	
	public Point getSelectedCell() {
		return new Point(selectedCelli, selectedCellj);
	}

	public void mouseClicked(MouseEvent e) {
		selectedCelli = e.getX()/cellSize;
		selectedCellj = e.getY()/cellSize;
		repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}
