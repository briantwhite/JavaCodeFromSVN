package edu.umb.jsVGL.client;

public class TextStrings {

	private final static String VERSION = "2.0";

	public static final String NO_FILE_MENU_WELCOME_TEXT = "<html><body>"
			+ "<h3>Welcome to js VGL version " + VERSION + "</h3>"
			+ "<h3>Working a problem</h3>"
			+ "You solve jsVGL problems by crossing organisms - select a male and a female symbol and then click the <b>Cross Two</b> button. "
			+ "jsVGL will then generate the resulting offspring. By choosing your crosses and observing the results carefully, you can determine "
			+ "how the trait(s) in your problem are inherited. You then enter your findings into the <b>Genetic Model</b> tab. Once you are sure"
			+ " of your answer, you can then submit it for grading."
			+ "<h4>Practice Problems</h4>"
			+ "If you choose a new Practice Problem, you will be able to view the correct underlying model in the Genetic Model tab."
			+ "<h4>Graded Problems</h4>"
			+ "If you choose a new graded problem, you will not be able to view the underlying genetic model; you must, instead, "
			+ "determine it for yourself."
			+ "</body></html>";

	public static final String FILE_MENU_WELCOME_TEXT = "<html><body>"
			+ "<h3>Welcome to js VGL version " + VERSION + "</h3>"
			+ "<h4>Working a problem</h4>"
			+ "You solve jsVGL problems by crossing organisms - select a male and a female symbol and then click the <b>Cross Two</b> button. "
			+ "jsVGL will then generate the resulting offspring. By choosing your crosses and observing the results carefully, you can determine "
			+ "how the trait(s) in your problem are inherited. You then enter your findings into the <b>Genetic Model</b> tab."
			+ "<h4>Practice Problems</h4>"
			+ "If you choose a new Practice Problem, you will be able to view the correct underlying model in the Genetic Model tab. Problems in <b>Practice "
			+ " Mode</b> cannot be saved or exported."
			+ "<h4>Graded Problems</h4>"
			+ "If you choose a new graded problem, you will not be able to view the underlying genetic model; you must, instead, "
			+ "determine it for yourself. Problems started in <b>Graded Mode</b> can be saved and exported."
			+ "<h4>Saving Your Work</h4>"
			+ "At any point while you are working on a problem, you can save your work as a .jsvgl file that you can open later to continue "
			+ "your work, share with another student, or submit to your TA. You do this by choosing the <b>Save Work As...</b> option from "
			+ "the <b>File</b> menu. You can re-open your work by first clicking the <b>Clear Workspace</b> button, choosing <b>Load Saved Work...</b>, "
			+ " choosing the file you have previously saved, and clicking <b>OK</b>."
			+ "<h4>Exporting your Data</h4>"
			+ "At any point while you are working a problem, you can export your data and the model you have entered as an .html file that can be opened "
			+ "by a word-processing program. You can then copy and paste your data into a report. Note that these .html files cannot be opened "
			+ "by jsVGL."
			+ "</body></html>";

	public static final String ABOUT_jsVGL = 
			"<h3>About jsVGL</h3>"
					+ "Version " + VERSION + "<br>"
					+ "jsVGL is a javascript version of the "
					+ "<a href=\"http://vgl.umb.edu\"target=\"_blank\">Virtual Genetics Lab</a>. "
					+ "Both are developed by <a href=\"mailto:brian.white@umb.edu\">Brian White</a> "
					+ "at the University of Massachusetts, Boston.";

	public static final String SUPER_CROSS_TEXT = 
			"<h3>Super Cross</h3>"
					+ "This carries out a cross with a large number of offpspring."
					+ "It is useful for getting recombination frequency data.<br><br>"
					+"Choose the desired number of offspring from the list below:<br>";
	
	public static final String FAILED_TO_LOAD = "<div style='background-color: yellow; border: 2px dotted red; width: 300px; text-align: center;'>" +
					"Sorry, there was an error loading the saved problem."
					+ "<br>Unfortunately, it cannot be opened."
					+ "<br>Please consult your system administrator."
					+ "</div>";
}
