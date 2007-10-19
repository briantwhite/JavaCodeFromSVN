import javax.microedition.lcdui.Font;


public abstract class Scale {

	private int bondLength;
	private Font font;
	private int multiBondOffset;
	private int xOffset;
	private int yOffset;
	
	public Scale(int bondLength,
			Font font,
			int multiBondOffset,
			int xOffset,
			int yOffset) {
		this.bondLength = bondLength;
		this.font = font;
		this.multiBondOffset = multiBondOffset;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public int getBondLength() {
		return bondLength;
	}

	public Font getFont() {
		return font;
	}

	public int getMultiBondOffset() {
		return multiBondOffset;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	
}
