package evolution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import biochem.FoldingException;

import molGenExp.Organism;
import molGenExp.OrganismFactory;
import preferences.GlobalDefaults;
import preferences.MGEPreferences;

public class World extends JPanel implements MouseListener {

	private MGEPreferences preferences;

	private ThinOrganismFactory thinOrganismFactory;
	private OrganismFactory organismFactory;

	private ThinOrganism[][] organisms;
	private ColorCountsRecorder colorCountsRecorder;

	public final static int pictureSize = 500;
	private int cellSize ;
	private int selectedCelli = -1;
	private int selectedCellj = -1;

	public World() {
		preferences = MGEPreferences.getInstance();
		thinOrganismFactory = new ThinOrganismFactory();
		organismFactory = new OrganismFactory();
		colorCountsRecorder = ColorCountsRecorder.getInstance();
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
				organisms[i][j] = 
					thinOrganismFactory.createThinOrganism(
							orgs[r.nextInt(orgs.length)]);
			}
		}
	}

	public void updateCounts() {
		//first, be sure that there are organisms in the world
		if (getThinOrganism(0,0) != null) {
			colorCountsRecorder.setAllToZero();
			for (int i = 0; i < preferences.getWorldSize(); i++) {
				for (int j = 0; j < preferences.getWorldSize(); j++) {
					colorCountsRecorder.incrementCount(
							getThinOrganism(i, j).getOverallColor());
				}
			}
		}
	}

	public void paint(Graphics g) {

		int worldSize = preferences.getWorldSize();

		if (worldSize != organisms.length) {
			resizeWorld();
		}

		g.setColor(new Color(160,160,160));
		g.fillRect(0, 0, worldSize * cellSize, worldSize * cellSize);

		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				if (organisms[i][j] == null) {
					g.setColor(Color.DARK_GRAY);
				} else {
					g.setColor(organisms[i][j].getOverallColor());
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

		//if enabled, show the colors of both alleles in upper left corner of cell
		if (preferences.isShowBothAllelesInWorld()) {
			for (int i = 0; i < worldSize; i++) {
				for (int j = 0; j < worldSize; j++) {
					if (organisms[i][j] == null) {
						return;
					} else {
						ThinOrganism o = organisms[i][j];
						g.setColor(o.getColor1());
						g.fillRect((cellSize * i), (cellSize * j), 
								(cellSize/4), (cellSize/8));
						g.setColor(o.getColor2());
						g.fillRect((cellSize * i), ((cellSize * j) + (cellSize/8)),
								(cellSize/4), (cellSize/8));
					}
				}
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

	public Organism getSelectedOrganism() throws FoldingException {
		if ((selectedCelli < 0) && (selectedCellj < 0)) {
			return null;
		}
		ThinOrganism to = organisms[selectedCelli][selectedCellj];
		if (to.getOverallColor().equals(GlobalDefaults.DEAD_COLOR)) {
			JOptionPane.showMessageDialog(null, 
					"Unable to load that organism because it is not viable.\n"
					+ "It is inviable because one of its proteins cannot be\n"
					+ "Folded properly. Please choose another organism.", 
					"Error Folding Protein", 
					JOptionPane.WARNING_MESSAGE);
		}
		return organismFactory.createOrganism(to);
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
