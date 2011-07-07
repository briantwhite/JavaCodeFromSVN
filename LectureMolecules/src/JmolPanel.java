import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolStatusListener;
import org.jmol.api.JmolViewer;


public 	class JmolPanel extends JPanel 
implements JmolStatusListener {
	JmolViewer viewer;
	JmolAdapter adapter;
	JmolPanel() {
		adapter = new SmarterJmolAdapter(null);
		viewer = org.jmol.viewer.Viewer.allocateViewer(this, adapter);
		viewer.setJmolStatusListener(this);
	}
	
	public JmolViewer getViewer() {
		return viewer;
	}
	
	final Dimension currentSize = new Dimension();
	final Rectangle rectClip = new Rectangle();
	
	public void paint(Graphics g) {
		getSize(currentSize);
		g.getClipBounds(rectClip);
		viewer.renderScreenImage(g, currentSize, rectClip);
	}
	
	//jmol status listener methods
	
	public void scriptStatus(String statusString) {
		
	}
	
	public void notifyAtomPicked(int arg0, String atomInfo) {
		
	}
	
	public void notifyFileLoaded(String arg0, String arg1, String arg2, Object arg3, String arg4) {}
	public void scriptEcho(String arg0) {}
	public void setStatusMessage(String arg0) {}
	public void notifyScriptTermination(String arg0, int arg1) {}
	public void handlePopupMenu(int arg0, int arg1) {}
	public void notifyMeasurementsChanged() {}
	public void notifyFrameChanged(int arg0) {}
	public void showUrl(String arg0) {}
	public void showConsole(boolean arg0) {}
}

