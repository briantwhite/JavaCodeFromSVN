package content;

import iClickerDriver.ButtonEnum;
import iClickerDriver.Vote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A thread keeps running, and fetching votes from socket.
 * @author Junhao
 *
 */
public class ClickerControlSocket extends ClickerControl {
	private static final int SOCKET_PORT = 4448;		// Socket port. TODO make it editable in Settings.
	private final int sleepTimeVotingEnabled = 1;		// When voting is enabled, polling every 1 millisecond.
	private final int sleepTimeVotingDisabled = 500;	// When voting is disabled, poling every 100 millisecond (only for votes coming from the instructor's clicker).
	
	public ClickerControlSocket(Session session) throws Exception {
		super(session);
	}

	@Override
	public void run() {
		Socket socket;
		BufferedReader inputReader = null;
		
		boolean connected = false;
		
		try {
			socket = new Socket("127.0.0.1", SOCKET_PORT);
		}
		catch(Exception e) {
			return;
		}
		
		while(!connected) {
			try {
				inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				connected = true;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		//Loop for clicks
		try {
			while(connected) {
				String response;

				response = (String) inputReader.readLine();
				System.out.println(response);
				
				ButtonEnum buttonEnum;

				switch (response.substring(9, 10)) {
				case "A": buttonEnum = ButtonEnum.A; break;
				case "B": buttonEnum = ButtonEnum.B; break;
				case "C": buttonEnum = ButtonEnum.C; break;
				case "D": buttonEnum = ButtonEnum.D; break;
				default:  buttonEnum = ButtonEnum.E;
				}
				
				Vote vote = new Vote(response.substring(0, 6), buttonEnum);
				
				this.session.processVote(vote);
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
		} catch (Exception e) {}
	}

	@Override
	public void startAcceptingVotes() throws Exception {
		
	}

	@Override
	public ArrayList<Vote> stopAccpetingVotes() throws Exception {
		return null;
	}

}
