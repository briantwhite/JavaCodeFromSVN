import javax.microedition.lcdui.Font;


public class MediumScale extends Scale {

	public MediumScale() {
		super(30, Font.getFont(Font.FACE_SYSTEM,
				Font.STYLE_PLAIN,
				Font.SIZE_MEDIUM), 
				3, 75, 75);
	}

	public String toString() {
		return "Medium";
	}

}
