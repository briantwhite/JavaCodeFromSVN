import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class FileLoader {
	
	/*
	 * expects a file of names, one per line
	 * makes ArrayList of them
	 * 
	 * returns null if file not found
	 */
	public static ArrayList<String> getDoNotCallList(JFrame masterFrame, File doNotCallFile) {
		ArrayList<String> doNotCallList = new ArrayList<String>();
		BufferedReader reader = null;
		String text = null;
		try {
			reader = new BufferedReader(new FileReader(doNotCallFile));
			while ((text = reader.readLine()) != null) {
				if (!text.equals("")) {
					System.out.println("DNC:" + text);
					doNotCallList.add(text);
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(masterFrame, "<html>Can't find \"do not call.txt\", <br>"
					+ "please RESET directories <br>and restart the program.</html>",
					"File not found", JOptionPane.ERROR_MESSAGE);
			doNotCallList = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doNotCallList;
	}
	
	/*
	 * next, the students & their ID #s
	 * from file Roster.txt
	 * where lines are lastName,firstName,ID
	 * 
	 *     hash map is 
	 *     	key = name
	 *      value = student ID (# or username like "brian.white001")
	 *      
	 * return null if file not found
	 */				
	public static TreeMap<String, String> getNamesAndStudentIDs(JFrame masterFrame, File studentFile) {
		TreeMap<String, String> namesAndsStudentIDs = new TreeMap<String, String>();
		BufferedReader reader = null;
		String text = null;
		try {
			reader = new BufferedReader(new FileReader(studentFile));
			while ((text = reader.readLine()) != null) {
				// lines are lastName,firstName,ID
				if (text.contains(",")) {
					String[] parts = text.split(",");
					String name = parts[0] + "," + parts[1];
					if (parts.length == 3) {
						namesAndsStudentIDs.put(name, parts[2]);
						System.out.println(name);
					}
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(masterFrame, "<html>Can't find \"Roster.txt\", <br>"
					+ "please RESET working directory <br>and restart the program.</html>",
					"File not found", JOptionPane.ERROR_MESSAGE);
			namesAndsStudentIDs = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return namesAndsStudentIDs;
	}

	/*
	 * now read in list of ID#s and iClicker #s
	 * from RemoteID.csv
	 * where lines are #iclickerNum,"id num"
	 * 
	 * hash map is:
	 * 		key = student id (# or brian.white001)
	 * 		value = ArrayList of iClicker IDs
	 * 			sometimes, there are multiple registrations (!)
	 * 
	 * return null if file not found
	 */
	public static HashMap<String, ArrayList<String>> getStudentIDsAndClickerIDs(JFrame masterFrame, File idFile) {
		HashMap<String, ArrayList<String>> studentIDsAndClickerIDs = new HashMap<String, ArrayList<String>>();
		BufferedReader reader = null;
		String text = null;
		try {
			reader = new BufferedReader(new FileReader(idFile));
			while ((text = reader.readLine()) != null) {
				// lines are #iclickerNum,"id num"
				if (text.startsWith("#")) {
					String[] parts = text.split(",");
					String id = parts[1].replaceAll("\\\"", "");
					String iClickerNum = parts[0];
					// if it's a new entry, create ArrayList for clicker #s
					if (!studentIDsAndClickerIDs.containsKey(id)) {
						studentIDsAndClickerIDs.put(id, new ArrayList<String>());
					} 
					ArrayList<String> workingSet = studentIDsAndClickerIDs.get(id);
					workingSet.add(iClickerNum);
					studentIDsAndClickerIDs.put(id, workingSet);
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(masterFrame, "<html>Can't find \"RemoteID.csv\", <br>"
					+ "please RESET working directory <br>and restart the program.</html>",
					"File not found", JOptionPane.ERROR_MESSAGE);
			studentIDsAndClickerIDs = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return studentIDsAndClickerIDs;
	}
	
	/*
	 * read newest iClicker log file
	 * to get list of clicker IDs of students present
	 * 
	 * use HashMap to look up names
	 * 
	 * return null if file not found
	 */
	public static ArrayList<String> getNamesOfPresentStudents(
			JFrame masterFrame, 
			File newestLogFile, 
			HashMap<String, String> iClickerIDsAndNames) {
		ArrayList<String> namesOfPresentStudents = new ArrayList<String>();
		BufferedReader reader = null;
		String text = null;
		try {
			reader = new BufferedReader(new FileReader(newestLogFile));
			while ((text = reader.readLine()) != null) {
				if (text.startsWith("#")) {
					String[] parts = text.split(",");
					String iClickerID = parts[0];
					if (iClickerIDsAndNames.containsKey(iClickerID)) {
						namesOfPresentStudents.add(iClickerIDsAndNames.get(iClickerID));
					}
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(masterFrame, "<html>Can't find the iClicker logs, <br>"
					+ "please RESET directories <br>and restart the program.</html>",
					"File not found", JOptionPane.ERROR_MESSAGE);
			namesOfPresentStudents = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return namesOfPresentStudents;
	}

}
