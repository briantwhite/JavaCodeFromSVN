package phylogenySurvey;

public class Link {
	
	private SelectableLinkableObject oneLabel;
	private SelectableLinkableObject otherLabel;
	
	public Link(SelectableLinkableObject oneLabel, SelectableLinkableObject otherLabel) {
		this.oneLabel = oneLabel;
		this.otherLabel = otherLabel;
	}

	public SelectableLinkableObject getOneLabel() {
		return oneLabel;
	}

	public SelectableLinkableObject getOtherLabel() {
		return otherLabel;
	}

}
