package content;

import java.util.ArrayList;

import iClickerDriver.*;

/**
 * A thread keeps running, polling the base station and fetching the votes.
 * @author Junhao
 *
 */
public class ClickerControlStation extends ClickerControl {
	private IClickerDriver driver;					// The clicker driver.
	private final int sleepTimeVotingEnabled = 1;		// When voting is enabled, polling every 1 millisecond.
	private final int sleepTimeVotingDisabled = 500;	// When voting is disabled, poling every 500 millisecond (only for votes coming from the instructor's clicker).

	public ClickerControlStation(Session session) throws Exception {
		super(session);
		
		driver = new IClickerDriver(this.session.getCourse().getFrequencyEnum1(), 
												   this.session.getCourse().getFrequencyEnum2(),
												   this.session.getCourse().getInstructorID(),
												   false);
		
//		if (driver.getDriverType() == DriverType.OLD) {
		if (false) {
			this.driver = new IClickerDriverOld(this.session.getCourse().getFrequencyEnum1(), 
												this.session.getCourse().getFrequencyEnum2(),
												this.session.getCourse().getInstructorID(),
												false);
		} else {
			this.driver = new IClickerDriverNew(this.session.getCourse().getFrequencyEnum1(), 
												this.session.getCourse().getFrequencyEnum2(),
												this.session.getCourse().getInstructorID(),
												false);
//												true);		// show packets
		}
		
		
		this.driver.startBaseStation();
	}
	
	@Override
	public void run() {
		ArrayList<Vote> votes = null;
		
		while (true) {
			try {
				votes = this.driver.requestVotes();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (Vote vote : votes) {
				if (vote != null) {
					try {
						this.session.processVote(vote);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				if (this.session.isVotingEnabled()) {
					Thread.sleep(sleepTimeVotingEnabled);
				} else {
					Thread.sleep(sleepTimeVotingDisabled);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startAcceptingVotes() throws Exception {
		this.driver.startAcceptingVotes();
	}
	
	public ArrayList<Vote> stopAccpetingVotes() throws Exception {
		ArrayList<Vote> arrayList = this.driver.stopAcceptingVotes();
		return arrayList;
	}
}