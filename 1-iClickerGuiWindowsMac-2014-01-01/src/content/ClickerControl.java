package content;

import java.util.ArrayList;

import iClickerDriver.Vote;

/**
 * A thread keeps running, polling the base station and fetching the votes. Votes can come directly from the base station, or from socket.
 * @author Junhao
 *
 */
abstract class ClickerControl implements Runnable{
	protected Session session;
	
	public ClickerControl(Session session) throws Exception {
		this.session = session;
	}
	
	@Override
	abstract public void run();
	
	/**
	 * Start accepting votes.
	 * @throws Exception
	 */
	abstract public void startAcceptingVotes() throws Exception;
	
	/**
	 * Stop accepting votes.
	 * @return
	 * @throws Exception
	 */
	abstract public ArrayList<Vote> stopAccpetingVotes() throws Exception;
}