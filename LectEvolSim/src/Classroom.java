import java.util.Random;


public class Classroom {
	
	private Student[] students = new Student[LES.numStudents];
	private Random r;
	
	public Classroom() {
		r = new Random();
		for (int i = 0; i < LES.numStudents; i++) {
			students[i] = new Student();
		}
	}
	
	private Student get(int i) {
		if (i < 0) {
			return students[LES.numStudents + i];
		}
		if (i >= LES.numStudents) {
			return students[i - LES.numStudents];
		}
		return students[i];
	}
	
	public int nextGeneration() {
		int numThatSurvived = 0;
		for (int i = 0; i < LES.numStudents; i++) {
			
			// first, decide what to do
			// if 0 or 4 flagella, die
			// if 1,2,3 then roll dice
			//  1, 2, 3, 4 = survive
			//  5, 6 = die
			int firstRoll = r.nextInt(6) + 1;
			int numFlagella = students[i].report();
			
			if ((firstRoll < 5) && 
					(numFlagella != 0) && 
					(numFlagella != 4)) {
				numThatSurvived++;
			} else {
				// student has died; make a new one
				// 1) pick best local genotype
				int start = i - LES.numNeighborsToSearch/2;
				int end = i + LES.numNeighborsToSearch/2;
				
				int indexOfBestGeno = start;
				int scoreOfBestGeno = 0;
				
				for (int j = start; j < end; j++) {
					Student s = get(j);
					int score = s.report();
					if ((score != 0) && (score != 4)) {
						if (score > scoreOfBestGeno) {
							scoreOfBestGeno = score;
							indexOfBestGeno = j;
						}
					}
				}
				
				Student newStudent = 
					new Student(get(indexOfBestGeno).getGenotype());
				
				// 2) mutate it
				newStudent.mutate();
				
				// 3) put it back in place of old one
				students[i] = newStudent;
			}
		}
		return numThatSurvived;
	}
	
	public int[] report() {
		int[] result = new int[5];
		for (int i = 0; i < LES.numStudents; i++) {
			result[students[i].report()]++;
		}
		return result;
	}

}
