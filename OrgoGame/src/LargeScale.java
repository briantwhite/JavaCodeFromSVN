import javax.microedition.lcdui.Font;


public class LargeScale extends Scale {

	public LargeScale() {
		super(50, Font.getFont(Font.FACE_SYSTEM,
				Font.STYLE_PLAIN,
				Font.SIZE_LARGE), 
				4, 100, 100);
	}

}
