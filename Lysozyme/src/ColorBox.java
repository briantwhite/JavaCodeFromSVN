import java.awt.Color;
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
	final Color[] ColorList 
	              = {Color.white, Color.red, Color.green, Color.blue};
	final String[] ChoiceList 
	              = {"-", "R", "G", "B"};
	
	JComboBox comboBox;
	
	public ColorBox(){ 
		comboBox= new JComboBox(ChoiceList);
		
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				String choice = (String)cb.getSelectedItem();
				Color chosenColor = Color.white;
				for (int i = 0; i < ChoiceList.length; i++) {
					if (choice.equals(ChoiceList[i])) {
						chosenColor = ColorList[i];
						break;
					}
				}
				comboBox.getParent().setBackground(chosenColor);
			}
		});
		
		
		this.add(comboBox);
	}
	
	

}
