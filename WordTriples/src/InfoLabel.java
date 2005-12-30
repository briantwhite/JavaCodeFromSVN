import java.awt.Color;

import javax.swing.JLabel;

public class InfoLabel extends JLabel {
	public InfoLabel(String text){
		super();
		this.setOpaque(true);
		this.setBackground(Color.YELLOW);
		this.setForeground(Color.BLUE);
		this.setText(text);
	}
}
