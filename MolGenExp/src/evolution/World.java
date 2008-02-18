package evolution;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import molGenExp.Organism;
import molGenExp.MolGenExp;

public class World extends JPanel {

	ThinOrganism[][] organisms = 
		new ThinOrganism[MolGenExp.worldSize][MolGenExp.worldSize];
	
	private int cellSize = 500/MolGenExp.worldSize;

	public World() {

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
	}
	
	public ThinOrganism getThinOrganism(int i, int j) {
		return organisms[i][j];
	}
	
	public void setOrganisms(ThinOrganism[][] newOrgs) {
		organisms = null;
		organisms = newOrgs;
		repaint();
	}

}
