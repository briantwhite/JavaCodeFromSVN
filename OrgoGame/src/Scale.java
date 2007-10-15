
public abstract class Scale {

	private int bondLength;
	private int fontSize;
	private int multiBondOffset;
	
	public Scale(int bondLength,
			int fontSize,
			int multiBondOffset) {
		this.bondLength = bondLength;
		this.fontSize = fontSize;
		this.multiBondOffset = multiBondOffset;
	}

	public int getBondLength() {
		return bondLength;
	}

	public int getFontSize() {
		return fontSize;
	}

	public int getMultiBondOffset() {
		return multiBondOffset;
	}
	
	
}
