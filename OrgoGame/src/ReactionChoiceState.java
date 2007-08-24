import javax.microedition.lcdui.List;


public class ReactionChoiceState extends ListState {
	
	OrgoGame game;

	String[] reactionChoices = {"one", "two", "three" };
	
	public ReactionChoiceState(OrgoGame game) {
		super(game, "Choose a Reaction", List.IMPLICIT);
		this.game = game;
		for (int i = 0; i < reactionChoices.length; i++) {
			this.append("", this.game.reactions[i]);
		}
	}

}
