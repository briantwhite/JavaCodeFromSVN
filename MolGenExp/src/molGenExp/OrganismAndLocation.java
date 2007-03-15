package molGenExp;

//a class to hold info on an organism and the list it was found in
//so that when it needs to be deselected, you know where it is

public class OrganismAndLocation {
	private Organism o;
	private CustomListSelectionModel listLocation;
	private int index;

	public OrganismAndLocation(Organism o, 
			CustomListSelectionModel listLocation,
			int index) {
		this.o = o;
		this.listLocation = listLocation;
		this.index = index;
	}

	public Organism getOrganism() {
		return o;
	}

	public CustomListSelectionModel getListLocation() {
		return listLocation;
	}

	public int getIndex() {
		return index;
	}
}
