package evolution;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class World extends JPanel {

	public final static int worldSize = 100;
	ThinOrganism[][] organisms = new ThinOrganism[worldSize][worldSize];

	public World() {
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				organisms[i][j] = new ThinOrganism(Color.GREEN);
			}
		}
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				g.setColor(organisms[i][j].getColor());
				g.fillRect((5 * i), (5 * j), 4, 4);
			}
		}

	}

}
