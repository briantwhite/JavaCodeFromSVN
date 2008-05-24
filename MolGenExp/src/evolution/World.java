package evolution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;

import preferences.MGEPreferences;

import utilities.GlobalDefaults;

import molGenExp.MolGenExp;
import molGenExp.Organism;

public class World extends JPanel implements MouseListener {

	private MGEPreferences preferences;

	ThinOrganism[][] organisms;

	public final static int pictureSize = 500;
	private int cellSize ;
	private int selectedCelli = -1;
	private int selectedCellj = -1;

	public World() {
		preferences = MGEPreferences.getInstance();
		resizeWorld();
		this.addMouseListener(this);
	}

	public void resizeWorld() {
		organisms = 
			new ThinOrganism[preferences.getWorldSize()][preferences.getWorldSize()]; 
		cellSize = pictureSize/preferences.getWorldSize();
	}

	public void initialize(Organism[] orgs) {
		Random r = new Random();
		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
				organisms[i][j] = new ThinOrganism(orgs[r.nextInt(orgs.length)]);
			}
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);

		if (preferences.getWorldSize() != organisms.length) {
			resizeWorld();
		}

		for (int i = 0; i < preferences.getWorldSize(); i++) {
			for (int j = 0; j < preferences.getWorldSize(); j++) {
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

	public Organism getSelectedOrganism() {
		if ((selectedCelli < 0) && (selectedCellj < 0)) {
			return null;
		}
		return new Organism(organisms[selectedCelli][ selectedCellj]);
	}

	public void clearSelectedOrganism() {
		selectedCelli = -1;
		selectedCellj = -1;
	}

	public void mouseClicked(MouseEvent e) {
		
		int newCelli = e.getX()/cellSize;
		if (newCelli < preferences.getWorldSize()) {
			selectedCelli = newCelli;
		}
		
		int newCellj = e.getY()/cellSize;
		if (newCellj < preferences.getWorldSize()) {
			selectedCellj = newCellj;
		}
		
		repaint();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}
