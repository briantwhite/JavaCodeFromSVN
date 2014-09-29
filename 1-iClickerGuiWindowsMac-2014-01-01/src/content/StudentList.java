package content;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.csvreader.CsvReader;

public class StudentList {
	private Course course;
	
	// An array of students.
	private ArrayList<Student> students;

	// Max lab section supported. If there are more than 12 
	// lab sections in the .csv file, throw an exception.
	private final int SECTIONMAXIMUMAMOUNT = 12;
	
	// Lab section name and its corresponding registered student amount
	// (the total number of students in a certain lab section). For 
	// students who registered in the course and owns more than one
	// clicker, the two clicker Id will be associated with one student.
	private HashMap<String, Integer> modifiedSectionNameToStudentSize;
	
	// Map between clicker Id and student
	private HashMap<String, Student> clickerIdToStudent;
	
	// Map between student Id and student
	private HashMap<String, Student> studentIdToStudent;
	
	// Names of each lab section. Sorted for the ease of display on histogram.
	private ArrayList<String> modifiedSectionNameList;

	// Map between section name and section index in the modifiedSectionNameList.
	private HashMap<String, Integer> sectionNameToSectionIndex;
	
	// Size of each lab section. Corresponds to the order of modifiedSectionNameList.
	private ArrayList<Integer> modifiedSectionSizeList;
	
	// Amount of students in the largest lab section.
	private int largestSectionAmount;

	/**
	 * Constructor.
	 * @param course
	 * @throws Exception 
	 */
	StudentList(Course course) throws Exception {
		this.course = course;
		
		this.students = new ArrayList<Student> ();
		
		this.modifiedSectionNameToStudentSize = new HashMap<String, Integer>();
		
		this.clickerIdToStudent = new HashMap<String, Student> ();
		
		this.studentIdToStudent = new HashMap<String, Student> ();
		
		this.modifiedSectionNameList = new ArrayList<String> ();
		
		this.sectionNameToSectionIndex = new HashMap<String, Integer> ();
		
		this.modifiedSectionSizeList = new ArrayList<Integer> ();
		
		this.largestSectionAmount = 0;
		
		this.readStudentRecord();
	}
	
	/**
	 * Read student record from .csv file and build the student list.
	 * @throws IOException 
	 * @throws Exception 
	 */
	private void readStudentRecord() throws IOException {
		CsvReader csvReader = null;
		
		try {
			csvReader = new CsvReader(course.getStudentRecordCsvPath());
		} catch (FileNotFoundException e) {
			this.course.setHasRegisteredInfo(false);
			return;
		}
		
		// Read first line.
		csvReader.readRecord();
		
		while (csvReader.readRecord()) {
			// Read record.
			String studentId = csvReader.get(0);
			String firstName = csvReader.get(1);
			String lastName = csvReader.get(2);
			String iClickerId = csvReader.get(3).substring(1, 9);
			String section = csvReader.get(4);
			
			if (this.studentIdToStudent.containsKey(studentId)) {	// If we've seen this student.
				// Get this student.
				Student student = this.studentIdToStudent.get(studentId);
				
				// This student has more than one clicker remote, add the new remote.
				student.addClickerId(iClickerId);
				
				// New map between a clicker and a student.
				this.clickerIdToStudent.put(iClickerId, student);
			} else {												// If we haven't seen this student.
				// Create a new student.
				Student student = new Student(studentId, firstName, lastName, iClickerId, section);
				
				// Add it to the list.
				this.students.add(student);
				
				// Map between a clicker and a student.
				this.clickerIdToStudent.put(iClickerId, student);
				
				// Map between student ID to a student.
				this.studentIdToStudent.put(studentId, student);
				
				if (this.modifiedSectionNameToStudentSize.containsKey(section) == false) {						// New lab section.
					if (this.modifiedSectionNameToStudentSize.keySet().size() < this.SECTIONMAXIMUMAMOUNT) {	// Still have room for new lab section.
						this.modifiedSectionNameToStudentSize.put(section, 1);
					} else {																					// Reach the limit.
						System.err.println("Only support at most 12 lab sections.");							// Because we have at most 12 colors defined for labs.
						break;
					}
				} else {																						// Existing lab section.
					this.modifiedSectionNameToStudentSize.put(section, this.modifiedSectionNameToStudentSize.get(section) + 1);
				}
			}
		}
		
		csvReader.close();
		
		// If there are at least one record.
		if (this.students.size() != 0) { 
			computeLargestSectionAmount();
		
			createSectionNameList();
		
			createSectionNameToIndexMap();
			
			createSectionSizeList();
			
			this.course.setHasRegisteredInfo(true);
		} else {
			this.course.setHasRegisteredInfo(false);
		}
	}
	
	/**
	 * Which lab section has the most students.
	 */
	private void computeLargestSectionAmount() {
		Collection<Integer> sectionSizeCollection = this.modifiedSectionNameToStudentSize.values();
		
		this.largestSectionAmount = Collections.max(sectionSizeCollection);
	}
	
	/**
	 * Create sorted section name list.
	 */
	private void createSectionNameList() {
		Set<String> nameSet = this.modifiedSectionNameToStudentSize.keySet();
		
		this.modifiedSectionNameList.addAll(nameSet);
		
		Collections.sort(this.modifiedSectionNameList);
	}

	/**
	 * The index of a lab section given its lab section name.
	 */
	private void createSectionNameToIndexMap() {
		for (int i = 0; i < this.modifiedSectionNameList.size(); i++) {
			this.sectionNameToSectionIndex.put(this.modifiedSectionNameList.get(i), i);
		}
	}
	
	/**
	 * Create section size list.
	 */
	private void createSectionSizeList() {
		Iterator<String> it = this.modifiedSectionNameList.iterator();
		
		while (it.hasNext()) {
			this.modifiedSectionSizeList.add(this.modifiedSectionNameToStudentSize.get(it.next()));
		}
	}
	
	/**
	 * Get the index of lab section given the section name.
	 * @param sectionName
	 * @return index of lab section or -1
	 */
	public int getLabSectionIndexBySection(String sectionName) {
		Integer index = this.sectionNameToSectionIndex.get(sectionName);
		
		return index == null ? -1 : index;
	}
	
	/**
	 * Given a clicker Id, return the student owns that clicker.
	 * @param clickerId
	 * @return student owns that clicker or null if the clickerId is not found.
	 */
	public Student getStudentByClickerId(String clickerId) {
		return this.clickerIdToStudent.get(clickerId);
	}
	
	/**
	 * Get all the section names (sorted).
	 * @return ArrayList of section names.
	 */
	public ArrayList<String> getSectionNameList() {
		return this.modifiedSectionNameList;
	}
	
	/**
	 * Get all the section sizes (according to the order of name list).
	 * @return ArrayList of section sizes.
	 */
	public ArrayList<Integer> getSectionSizeList() {
		return this.modifiedSectionSizeList;
	}

	/**
	 * Number of students in the largest lab section.
	 * @return number of students in the largest lab section.
	 */
	public int getLargestSectionAmount() {
		return this.largestSectionAmount;
	}

	/**
	 * How many lab sections are there in total.
	 * @return number of lab sections.
	 */
	public int getLabSectionAmount() {
		return this.modifiedSectionNameList.size();
	}
}
