
public class Word {

	private String text;
	private int count;
	private String group;
	private int code;
	
	public Word(String text) {
		this.text = text;
		count = 0;
		group = "";
		code = 0;
	}
	
	public Word(String text, int count, String group, int code) {
		this.text = text;
		this.count = count;
		this.group = group;
		this.code = code;
	}
		
	public int getCount() {
		return count;
	}
	
	public String getText() {
		return text;
	}

	public int getCode() {
		return code;
	}
	
	
	public boolean equals(Object o) {
		return (o instanceof Word) && (text.equals(((Word)o).getText()));
	}
	
	public int hashCode() {
		return text.hashCode();
	}

	public String getGroup() {
		return group;
	}
	
}
