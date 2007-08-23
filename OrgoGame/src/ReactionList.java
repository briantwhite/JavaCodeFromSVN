
public class ReactionList {

	private int[] reactionList;
	private String delimiter = ",";
	
	public ReactionList(String reactionListString) {
		reactionList = StringParser.parseToIntegerArray(reactionListString, ",");
	}
	
	public int[] getList() {
		return reactionList;
	}
}
