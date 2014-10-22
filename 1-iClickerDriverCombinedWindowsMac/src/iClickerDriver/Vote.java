package iClickerDriver;

/**
 * Vote: a piece of vote.
 * @author Junhao
 *
 */

public class Vote {
	/** iClicker remote id, eight characters. */
	private String id;
	/** Button clicked. */
	private ButtonEnum button;
	/** Timestamp in HH:MM:SS.MMM format. */
	private String timeStamp;
	
	/**
	 * Constructor, will generate an eight-characters remote id based on the six-characters id.
	 * @param id first six characters of iClicker remote id.
	 * @param button button pressed.
	 */
	public Vote(String id, ButtonEnum button) {
		int id1 = Integer.valueOf(id.substring(0, 2), 16);
		int id2 = Integer.valueOf(id.substring(2, 4), 16);
		int id3 = Integer.valueOf(id.substring(4, 6), 16);
		int id4 = id1 ^ id2 ^ id3;
		
		String id4Str = (Integer.toHexString(id4)).toUpperCase();
        
        this.id = id + ((Integer.toHexString(id4)).length() == 2 ? id4Str : "0" + id4Str);
		this.id = this.id.toUpperCase();
		
		this.button    = button;
		this.timeStamp = TimeProcess.getTime(TimeEnum.HMSM);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ButtonEnum getButton() {
		return button;
	}

	public void setButton(ButtonEnum button) {
		this.button = button;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
