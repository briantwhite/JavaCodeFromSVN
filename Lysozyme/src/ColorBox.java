import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/*
 * Created on Apr 3, 2005
 *
 */

/**
 * @author Brian.White
 */
public class ColorBox extends JPanel {
	public final Color[] colors;
	public final String[] choices;
	
	JComboBox comboBox;
	
	public ColorBox(Color[] colorList, String[] choiceList){ 

		colors = colorList;
		choices = choiceList;
		
		comboBox = new JComboBox(choices);
		
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				String choice = (String)cb.getSelectedItem();
				Color chosenColor = Color.white;
				for (int i = 0; i < choices.length; i++) {
					if (choice.equals(choices[i])) {
						chosenColor = colors[i];
						break;
					}
				}
				comboBox.setBackground(chosenColor);
				comboBox.getParent().setBackground(chosenColor);
			}
		});
		
		
		this.add(comboBox);
		comboBox.setBackground(Color.white);
		comboBox.getParent().setBackground(Color.white);
		comboBox.getParent().setSize(new Dimension(100,100));

	}
	
	

}
