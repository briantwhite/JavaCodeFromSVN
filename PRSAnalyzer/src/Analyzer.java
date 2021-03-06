// this is a program to take the session files generated by PRS software
// and convert them to a table of dates (columns) and id #s (rows)
//
// it requires Analyzer.class & Student.class in the same directory
//
// the session files (and only session files) are to be saved in
// a sub-directory of the directory with Analyzer.class & Student.class
// called "datafiles/"
//
// the result then saved in a file in the same diredctory as this program
// as outfile.csv - suitable for importing into excel
//
// if a student answered a question (right or wrong) on a given day
// there is a 1 in the corresponding cell; if not, a 0.
//
// as it runs, it outputs a list of the dates of the various session
// files to the terminal
//
// since different classes use different ranges of transmitter id #s
// it only outputs (to outfile.csv) data on id#s from minIdNum
// to maxIdNum which are calculated on the fly as it analyzes
// the session files.
// Brian White brian.white@umb.edu 12/2003

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Analyzer {
	private int minIdNum; //the lowest ID # with any attendance

	private int maxIdNum; //the highest ID # with any attendance

	private TreeSet allDates; //the set of all session file dates

	// that is, all class dates when a
	// prs question was asked

	private Map wholeClass; //hashmap of students listed by ID#

	public Analyzer() //constructor
	{
		wholeClass = new HashMap();

		allDates = new TreeSet();
		minIdNum = Integer.MAX_VALUE;
		maxIdNum = 0;
	}

	public void processFile(File currentFile, JTextArea messagePane)
	// reads in a session file (currentFile) and inserts the date of
	// that class (dateOfClass) into the appropriate Student[id#]
	//
	{
		BufferedReader inStream = null;
		String firstLine;
		String secondLine;
		String line;
		String[] firstLineParts = new String[10];
		String dateString;
		DateFormat df;
		df = DateFormat.getDateInstance(DateFormat.LONG);
		Date dateOfClass = new Date();
		String[] lineParts = new String[9];
		int idNum;

		try {
			inStream = new BufferedReader(new FileReader(currentFile));

			//get header lines first line is useful, 2nd is not
			firstLine = inStream.readLine();
			secondLine = inStream.readLine();

			//extract date from first line: the month is [4],
			//  the day is [5], an the year is [6]
			firstLineParts = firstLine.split(" ");

			//be sure it's the right kind of file
			if (!firstLineParts[0].equals("Session")) {
				messagePane.append("File " + currentFile.toString()
						+ " is not in the right format; ignoring it.\n");
				return;
			}

			//convert date in frist line to proper format
			dateString = firstLineParts[4] + " " + firstLineParts[5] + ", "
					+ firstLineParts[6];

			//make it a true date object
			try {
				dateOfClass = df.parse(dateString);
			} catch (ParseException e) {
				System.err.println(e);
			}

			messagePane.append("Analyzing data from: " + dateOfClass.toString()
					+ "\n");

			//save the date in list of all class meeting dates
			allDates.add(dateOfClass);

			// now read the individual student listings & tally them
			// in the individual student HashSets
			while ((line = inStream.readLine()) != null) {
				lineParts = line.split(",");

				//if it's the first line of the 2nd section; all done
				if (lineParts[0].equals("Question")) {
					break;
				}

				//update the min & max ID#'s if necessary
				idNum = Integer.parseInt(lineParts[4]);

				if (idNum < minIdNum) {
					minIdNum = idNum;
				}

				if (idNum > maxIdNum) {
					maxIdNum = idNum;
				}

				Integer idNumAsObject = new Integer(idNum);

				//if that student doesn't exist yet, make one
				if (!wholeClass.containsKey(idNumAsObject)) {
					wholeClass.put(idNumAsObject, new Student());
				}

				//then tally this student's attendance on this date
				Student thisStudent = (Student) wholeClass.get(idNumAsObject);
				thisStudent.addDateAttended(dateOfClass);

			}
		}

		catch (FileNotFoundException e) {
			System.err.println(e);
		}

		catch (IOException e) {
			System.err.println(e);
		}

		catch (NullPointerException e) {
			return;
		}

		finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (Exception e) {
				System.err.println("Unable to close infile");
			}
		}
	}

	public int getNumberOfStudents() {
		return (maxIdNum - minIdNum);
	}

	public int getMinId() {
		return minIdNum;
	}

	public int getMaxId() {
		return maxIdNum;
	}

	public boolean isEmpty() {
		return wholeClass.isEmpty();
	}

	public void saveAll(String outFile)
	//save to output file
	{
		Iterator dateIterator = allDates.iterator();
		Date workingDate = new Date();

		BufferedWriter outStream = null;

		try {
			outStream = new BufferedWriter(new FileWriter(outFile));

			outStream.write("\"ID#");

			while (dateIterator.hasNext()) {
				workingDate = (Date) dateIterator.next();
				outStream.write("\",\""
						+ workingDate.toString().substring(4, 10));
			}
			outStream.write("\"");
			outStream.newLine();

			for (int i = minIdNum; i < maxIdNum + 1; i++) {

				outStream.write("\"" + Integer.toString(i));

				Iterator dateIterator2 = allDates.iterator();

				while (dateIterator2.hasNext()) {
					workingDate = (Date) dateIterator2.next();

					Integer iAsObject = new Integer(i);

					if (!wholeClass.containsKey(iAsObject)) {
						wholeClass.put(iAsObject, new Student());
					}

					Student thisStudent = (Student) wholeClass.get(iAsObject);
					outStream
							.write(((thisStudent.getIfAttended(workingDate)) ? "\",\"1"
									: "\",\"0"));
				}
				outStream.write("\"");
				outStream.newLine();
			}
		}

		catch (FileNotFoundException e) {
			System.err.println(e);
		}

		catch (IOException e) {
			System.err.println(e);
		}

		finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e) {
				System.err.println("Unable to close output stream");
			}
		}

	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("PRS Session File Analyzer");
		final JTextArea messages = new JTextArea(
				"First, select a directory for analysis.\n", 20, 50);

		JButton openDirButton = new JButton(
				"Select Directory with session files");
		JButton saveOutFileButton = new JButton("Save result file");

		final JFileChooser inputDirectoryChooser = new JFileChooser();
		inputDirectoryChooser.setApproveButtonText("Analyze");
		inputDirectoryChooser
				.setApproveButtonToolTipText("Analyze files in selected directory.");
		inputDirectoryChooser
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		inputDirectoryChooser
				.setDialogTitle("Select directory of session files and click Analyze");
		inputDirectoryChooser.setMultiSelectionEnabled(false);

		final JFileChooser outFileChooser = new JFileChooser();
		outFileChooser.setSelectedFile(new File("outfile.csv"));
		outFileChooser.setApproveButtonToolTipText("Save result file.");
		outFileChooser.setDialogTitle("Save result file");

		frame.getContentPane().add(openDirButton, BorderLayout.NORTH);
		frame.getContentPane().add(new JScrollPane(messages),
				BorderLayout.CENTER);
		frame.getContentPane().add(saveOutFileButton, BorderLayout.SOUTH);

		final Analyzer analyzer = new Analyzer();

		openDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = inputDirectoryChooser.showOpenDialog(messages);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					inputDirectoryChooser.setVisible(false);
					String[] fileList = inputDirectoryChooser.getSelectedFile()
							.list();

					for (int i = 0; i < fileList.length; i++) {
						File currentFile = new File(inputDirectoryChooser
								.getSelectedFile().toString()
								+ "/" + fileList[i]);

						if (!fileList[i].equalsIgnoreCase("sessam.csv")) {
							analyzer.processFile(currentFile, messages);
						} else {
							messages.append("Ignoring file " + fileList[i]
									+ "\n");
						}
					}

					messages.append("Lowest ID number = " + analyzer.getMinId()
							+ "\n");

					messages.append("Highest ID number = "
							+ analyzer.getMaxId() + "\n");

					messages.append("Total number of students = "
							+ analyzer.getNumberOfStudents() + "\n");

					messages
							.append("Click the button below to save the result file.\n");
					messages
							.append("It will be saved in Excel-readable format.\n");

				}
			}
		});

		saveOutFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = outFileChooser.showSaveDialog(messages);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (!analyzer.isEmpty()) {
						outFileChooser.setVisible(false);
						analyzer.saveAll(outFileChooser.getSelectedFile()
								.toString());
						messages.append("Output file saved as "
								+ outFileChooser.getSelectedFile().toString()
								+ "\n");
					} else {
						messages.append("No files have been analyzed yet! \n");
					}
				}
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

}