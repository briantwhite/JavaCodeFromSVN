package app;

import gui.*;
import content.Course;

/**
 * Driver class.
 * @author Junhao
 *
 */

public class Test {
	// All panels and resources.
    private static Welcome welcome = null;
    private static NewCourse newCourse = null;
    private static EditCourse editCourse = null;
    private static DeleteCourse deleteCourse = null;
    private static StartSession startSession = null;
    private static MySettings mySettings = null;
    private static QuestionList questionList = null;
    private static About about = null;
    private static ResponseGrid responseGrid = null;
    private static ToolbarStudent toolbarStudent = null;
    private static ToolbarInstructor toolbarInstructor = null;
    private static AdHocQuestionEditor adHocQuestionEditor = null;
    private static RunQuestionList runQuestionList = null;
    private static QuestionPanel questionPanel = null;
    private static HistogramStudent histogramStudent = null;
    private static HistogramInstructor histogramInstructor = null;
    private static SelectCorrect selectCorrect = null;
    
    private static LabColor labColor = null;
    private static ChoiceColor choiceColor = null;
    
    private static Course course = null;
    
    // Singleton style of generating all panels and resources.
    
    public Welcome getWelcome() {
        if (Test.welcome == null) { 
        	Test.welcome = new Welcome(this);
        }
        
        return welcome;
    }
    
    public NewCourse getNewCourse() {
        if (Test.newCourse == null) {
        	Test.newCourse = new NewCourse(this);
        }
    	
    	return newCourse;
    }
    
    public EditCourse getEditCourse() {
    	if (Test.editCourse == null) {
    		Test.editCourse = new EditCourse(this);
    	}
    	
        return editCourse;
    }
    
    public DeleteCourse getDeleteCourse() {
    	if (Test.deleteCourse == null) {
    		Test.deleteCourse = new DeleteCourse(this);
    	}
    	
        return deleteCourse;
    }
    
    public StartSession getStartSession() {
        if (Test.startSession == null) {
        	Test.startSession = new StartSession(this);
        }
    	
    	return startSession;
    }
    
    public MySettings getMySettings() {
    	if (Test.mySettings == null) {
    		Test.mySettings = new MySettings(this);
    	}
    	
    	return mySettings;
    }
    
    public QuestionList getQuestionList() {
    	if (Test.questionList == null) {
    		Test.questionList = new QuestionList(this);
    	}
    	
    	return questionList;
    }
    
    public About getAbout() {
    	if (Test.about == null) {
    		Test.about = new About();
    	}
    	
    	return about;
    }
    
    public ResponseGrid getResponseGrid() {
    	if (Test.responseGrid == null) {
    		Test.responseGrid = new ResponseGrid(this);
    	}
    	
        return responseGrid;
    }
    
    public ToolbarStudent getToolbarStudent() {
    	if(Test.toolbarStudent == null) {
    		Test.toolbarStudent = new ToolbarStudent(this);
    	}
    	
        return toolbarStudent;
    }
    
    public ToolbarInstructor getToolbarInstructor() {
    	if (Test.toolbarInstructor == null) {
    		Test.toolbarInstructor = new ToolbarInstructor(this);
    	}
    	
        return toolbarInstructor;
    }
    
    public AdHocQuestionEditor getAdHocQuestionEditor() {
    	if (Test.adHocQuestionEditor == null) {
    		Test.adHocQuestionEditor = new AdHocQuestionEditor(this);
    	}
    	return adHocQuestionEditor;
    }
    
    public RunQuestionList getRunQuestionList() {
    	if (Test.runQuestionList == null) {
    		Test.runQuestionList = new RunQuestionList(this);
    	}
    	
    	return runQuestionList;
    }
    
    public QuestionPanel getQuestionPanel() {
    	if (Test.questionPanel == null) {
    		Test.questionPanel = new QuestionPanel(this);
    	}
    	
    	return questionPanel;
    }
    
    public HistogramStudent getHistogramStudent() {
    	if (Test.histogramStudent == null) {
    		Test.histogramStudent = new HistogramStudent(this);
    	}
    	
        return histogramStudent;
    }

    public HistogramInstructor getHistogramInstructor() {
    	if (Test.histogramInstructor == null) {
    		Test.histogramInstructor = new HistogramInstructor(this);
    	}
    	
        return histogramInstructor;
    }
    
    public SelectCorrect getSelectCorrect() {
    	if (Test.selectCorrect == null) {
    		Test.selectCorrect = new SelectCorrect(this);
    	}
    	
        return selectCorrect;
    }
    
    public LabColor getLabColor() {
    	if (Test.labColor == null) {
    		Test.labColor = new LabColor(this);
    	}
    	
		return labColor;
	}
    
    public ChoiceColor getChoiceColor() {
    	if (Test.choiceColor == null) {
    		Test.choiceColor = new ChoiceColor(this);
    	}
    	
    	return choiceColor;
    }
    
    /**
     * Load a course.
     * @param name course name.
     * @throws Exception
     */
    public void LoadCourse(String name) throws Exception {
    	course = new Course(name, this);
    }
    
    public Course getCourse() {
    	return course;
    }
    
    public static void main(String args[]) {
		System.loadLibrary("hidapi-jni");
		
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        final Test test = new Test();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Welcome welcome = test.getWelcome();
                
                welcome.setVisible(true);
            }
        });
    }
}