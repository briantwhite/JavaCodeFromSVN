import javax.microedition.lcdui.List;


public abstract class ListState extends List {
	
	OrgoGame game;

	public ListState(OrgoGame game, String title, int type) {
		super(title, type);
		this.game = game;
	}
	

}
