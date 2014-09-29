package gui.enumoption;

/**
 * OK this is complicated. This enum type is used for controlling whether to load a list question or not.
 * We set the following rule. If the Question List frame is hidden, the next question will not be a list
 * question. If the Question List frame is shown, and the user has selected one of the question by mouse
 * in the past, the next question will not be a list question. If the Question List frame is shown, and 
 * the user has never selected a question, whether the next question will be a list question depends on 
 * whether the question list has been exhausted. When polling during a list question, it will automatically 
 * jumps to the next question when polling stops. 
 * @author Junhao
 *
 */
public enum EnumQuestionListState {
	PLAY,
	PAUSE
}
