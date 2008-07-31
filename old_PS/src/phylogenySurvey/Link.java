package phylogenySurvey;

public class Link {
	
	private SelectableLabel oneLabel;
	private SelectableLabel otherLabel;
	
	public Link(SelectableLabel oneLabel, SelectableLabel otherLabel) {
		this.oneLabel = oneLabel;
		this.otherLabel = otherLabel;
	}

	public SelectableLabel getOneLabel() {
		return oneLabel;
	}

	public SelectableLabel getOtherLabel() {
		return otherLabel;
	}

}
