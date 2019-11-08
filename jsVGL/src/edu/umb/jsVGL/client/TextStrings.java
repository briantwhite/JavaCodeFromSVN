package edu.umb.jsVGL.client;

public class TextStrings {

	private final static String VERSION = "1.3";

	public static final String WELCOME_TEXT = "<html><body>"
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
