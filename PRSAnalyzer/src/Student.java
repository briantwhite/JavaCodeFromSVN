
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Student {
	private Set attendanceRecord;

	public Student() {
		attendanceRecord = new HashSet();
	}

	public void addDateAttended(Date thisDate) {
		attendanceRecord.add(thisDate);
	}

	public boolean getIfAttended(Date thisDate) {
		return attendanceRecord.contains(thisDate);
	}

	public static void main(String[] args) {
		Student testStudent = new Student();
		Date date = new Date();

		testStudent.addDateAttended(date);
		if (testStudent.getIfAttended(date)) {
			System.out.println("he attended on" + date.toString());
		}

	}
}