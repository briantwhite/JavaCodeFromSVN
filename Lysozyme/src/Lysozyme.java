import javax.swing.JFrame;

/*
 * Created on Apr 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Brian.White
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Lysozyme {
	static ColorBox colorBox;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Lysozyme Data");
		
		colorBox = new ColorBox();
		frame.getContentPane().add(colorBox);
				
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
