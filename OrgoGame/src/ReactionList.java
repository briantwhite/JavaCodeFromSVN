
public class ReactionList {

	private int[] reactionList;
	private String delimiter = ",";
	
	public ReactionList(String reactionListString) {
		int delimiterCount = 0;
		int i = 0;
		while (true) {
			int location = reactionListString.indexOf(delimiter, i);
			if (location == -1) {
				break;
			}
			delimiterCount++;
			i = location + 1;
		}

		reactionList = new int[delimiterCount + 1];
		
		i = 0;
		delimiterCount = 0;
		while (true) {
			int location = reactionListString.indexOf(delimiter, i);
				if (location == -1) {
				break;
			}
			reactionList[delimiterCount] 
			             = Integer.parseInt(
			            		 reactionListString.substring(i, location));
			i = location + 1;
			delimiterCount++;
		}
		
		//getlast one
		reactionList[delimiterCount] 
		             = Integer.parseInt(
				reactionListString.substring(i, reactionListString.length()));
		
	}
	
	public int[] getList() {
		return reactionList;
	}
}
