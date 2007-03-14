package molGenExp;

import javax.swing.DefaultListSelectionModel;

public class CustomListSelectionModel extends DefaultListSelectionModel {
	private int location;
	
	public CustomListSelectionModel(int location) {
		this.location = location;
	}

	public void setSelectionInterval(int index0, int index1) {
//		System.out.println("set interval: " + location + " " + index0 + " " + index1);
		super.setSelectionInterval(index0, index1);
	}
	
	public void addSelectionInterval(int index0, int index1) {
//		System.out.println("add interval: " + location + " " + index0 + " " + index1);
		super.addSelectionInterval(index0, index1);		
	}
	
	public void removeSelectionInterval(int index0, int index1) {
//		System.out.println("remove interval: " + location + " " + index0 + " " + index1);
		super.removeSelectionInterval(index0, index1);
	}
}
