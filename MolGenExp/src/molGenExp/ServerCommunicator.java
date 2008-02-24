package molGenExp;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import preferences.MGEPreferences;

public class ServerCommunicator {

	private MolGenExp mge;

	private MGEPreferences preferences;

	public ServerCommunicator(MolGenExp mge) {
		this.mge = mge;
		preferences = MGEPreferences.getInstance();
	}
	
	public String testServer() {
		return sendSequencesToServer("FFFFF,GGGGG,HHHHH,IIIII,JJJJJ,KKKKK,LLLLL,MMMMM," 
				+ "NNNNN,PPPPP,QQQQQ,RRRRR,SSSSS,TTTTT,VVVVV,WWWWW");
	}

	public String sendSequencesToServer(String sequenceString) {

		URL serverScriptURL = null;
		try {
			serverScriptURL = new URL(preferences.getFoldingServerURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		URLConnection serverConnection = null;
		try {
			serverConnection = serverScriptURL.openConnection();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(mge,
					"Unable to contact the server.\n" + e1.toString(),
					"Connection Error",
					JOptionPane.ERROR_MESSAGE);
		}

		serverConnection.setDoInput(true);
		serverConnection.setDoOutput(true);
		serverConnection.setUseCaches(false);

		serverConnection.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");

		DataOutputStream toServerStream = null;
		try {
			toServerStream = new DataOutputStream(serverConnection.getOutputStream());
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(mge,
					"Unable to contact the server.\n" + e2.toString(),
					"Connection Error",
					JOptionPane.ERROR_MESSAGE);					
		}

		String toServerContent = new String("");
		try {
			toServerContent = "seq=" + URLEncoder.encode(sequenceString, "UTF-8");

		} catch (UnsupportedEncodingException e3) {
			e3.printStackTrace();
		}

		try {
			toServerStream.writeBytes(toServerContent);
			toServerStream.flush();
			toServerStream.close();
		} catch (IOException e4) {
			JOptionPane.showMessageDialog(mge,
					"Unable to send to the server.\n" + e4.toString(),
					"Connection Error",
					JOptionPane.ERROR_MESSAGE);					
		}

		//get response from server
		String serverResponseLine = new String("");
		StringBuffer fromServerBuffer = new StringBuffer();
		BufferedReader fromServerStream;
		try {
			fromServerStream = new BufferedReader(new InputStreamReader(
					serverConnection.getInputStream()));
			while (null != ((serverResponseLine = fromServerStream.readLine()))) {
				fromServerBuffer.append(serverResponseLine);
			}
		} catch (IOException e5) {
			JOptionPane.showMessageDialog(mge,
					"Server not responding to transmission.\n" + e5.toString(),
					"Connection Error",
					JOptionPane.ERROR_MESSAGE);					
		}

		return fromServerBuffer.toString();
	}

}
