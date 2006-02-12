
public class Word {

	private String text;
	private int count;
	private int group;
	
	public Word(String text) {
		this.text = text;
		count = 0;
		group = 0;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	
	public String getText() {
		return text;
	}

	public boolean equals(Object o) {
		return (o instanceof Word) && (text.equals(((Word)o).getText()));
	}
	
	public int hashCode() {
		return text.hashCode();
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
}
