package LogAnalyzer;

public class CrossTally {
	
	private int modelNumber;
	private int total;
	private int fieldXfield;
	private int fieldXother;
	private int sibling;
	private int other;
	private int samePheno;
	private int differentPheno;
	private int showsDominance;
	private int showsSexLinkage;
	private int extraCrosses;
	
	public CrossTally(
			int modelNumber,
			int fieldXfield,
			int fieldXother,
			int sibling,
			int other,
			int samePheno,
			int differentPheno,
			int showsDominance,
			int showsSexLinkage,
			int extraCrosses) {
		this.modelNumber = modelNumber;
		this.fieldXfield = fieldXfield;
		this.fieldXother = fieldXother;
		this.sibling = sibling;
		this.other = other;
		this.samePheno = samePheno;
		this.differentPheno = differentPheno;
		this.showsDominance = showsDominance;
		this.showsSexLinkage = showsSexLinkage;
		this.total = fieldXfield + fieldXother + sibling + other;
		this.extraCrosses = extraCrosses;
	}
	
	public int getModelNumber() {
		return modelNumber;
	}

	public int getFieldXfield() {
		return fieldXfield;
	}

	public int getFieldXother() {
		return fieldXother;
	}

	public int getSibling() {
		return sibling;
	}

	public int getOther() {
		return other;
	}

	public int getSamePheno() {
		return samePheno;
	}

	public int getDifferentPheno() {
		return differentPheno;
	}
	
	public int getShowsDominance() {
		return showsDominance;
	}

	public int getShowsSexLinkage() {
		return showsSexLinkage;
	}


	public int getTotal() {
		return total;
	}

	public int getExtraCrosses() {
		return extraCrosses;
	}
}
