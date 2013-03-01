package LogAnalyzer;


public class ClassifiedCross {
	private int type;
	private int modifier;
	private boolean showsDominance;
	private boolean showsSexLinkage;
	private boolean haveAllInfo;
	
	public ClassifiedCross(
			int type, 
			int modifier,
			boolean showsDominance,
			boolean showsSexLinkage,
			boolean haveAllInfo) {
		this.type = type;
		this.modifier = modifier;
		this.showsDominance = showsDominance;
		this.showsSexLinkage = showsSexLinkage;
		this.haveAllInfo = haveAllInfo;
	}
	
	public int getType() {
		return type;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public String toString() {
		return type + "-" + modifier;
	}

	public boolean showsDominance() {
		return showsDominance;
	}

	public boolean showsSexLinkage() {
		return showsSexLinkage;
	}
	
	public boolean isHaveAllInfo() {
		return haveAllInfo;
	}
	
}
