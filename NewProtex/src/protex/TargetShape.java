package protex;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class TargetShape {

	private ImageIcon bigPic;
	private ImageIcon thumbPic;
	private String name;
	private String shapeString;
	private int numAcids;
	
	public TargetShape(BufferedImage big, 
			BufferedImage thumb,
			String name,
			String shapeString) {
		bigPic = new ImageIcon(big);
		thumbPic = new ImageIcon(thumb);
		this.name = name;
		this.shapeString = shapeString;
		numAcids = (shapeString.split(";")).length;
	}

	public ImageIcon getBigPic() {
		return bigPic;
	}

	public String getName() {
		return name;
	}

	public String getShapeString() {
		return shapeString;
	}

	public ImageIcon getThumbPic() {
		return thumbPic;
	}
	
	public int getNumAcids() {
		return numAcids;
	}
	
}
