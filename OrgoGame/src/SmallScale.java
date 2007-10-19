import javax.microedition.lcdui.Font;


public class SmallScale extends Scale {

	public SmallScale() {
		super(20, Font.getFont(Font.FACE_SYSTEM,
				Font.STYLE_PLAIN,
				Font.SIZE_SMALL), 
				2, 70, 70);
	}

}
