package content.enumoption;

/**
 * State of the histogram display.
 * Careful: HistogramDisplayOption should not be mixed with another enum type 
 * CorrectAnswerDisplay, which appears in the setting of the standard i>clicker 
 * software. In this version of Clicker++, CorrectAnswerDisplay is not used. 
 * HistogramDisplayOption controls how the 'B' vote sent by the instructor's 
 * clicker will change the histogram (whether show or hide; if show, whether 
 * with correct answer revealed or not).
 * @author Junhao
 *
 */

public enum HistogramDisplayOption {
	HIDE,
	SHOWWITHOUTCORRECTANSWER,
	SHOWWITHCORRECTANSWER
}
