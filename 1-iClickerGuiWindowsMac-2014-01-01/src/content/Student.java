package content;

import java.util.ArrayList;

/**
 * A class that represents a student.
 * @author Junhao
 *
 */
public class Student {
	private String StudentId;
	private ArrayList<String> clickerIds;
	private String lastName;
	private String firstName;
	private String labSection;
	
	public Student(String studentId, String firstName, String lastName, String clickerId, String labSection) {
		this.StudentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.clickerIds = new ArrayList<String> ();
		this.clickerIds.add(clickerId);
		this.labSection = labSection;
	}

	public String getStudentId() {
		return StudentId;
	}

	public ArrayList<String> getClickerId() {
		return clickerIds;
	}
	
	public void addClickerId(String clickerId) {
		this.clickerIds.add(clickerId);
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLabSection() {
		return labSection;
	}
}
