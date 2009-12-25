
public class LES {
	
	public static final int[] initialAlleles = {1,0,0,0};
	public static final int ageAtDeath = 2;
	public static final int numStudents = 200;
	
	private static Classroom classroom;
		
	public static final int numNeighborsToSearch = 20;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		classroom = new Classroom();
		System.out.println("Created Classroom");
		
		System.out.println("G\t0\t1\t2\t3\t4\tAvg");
		
		for (int i = 0; i < 20; i++) {
			System.out.print(i + "\t");
			int[] result = classroom.report();
			int total = 0;
			int count = 0;
			for (int j = 0; j < result.length; j++) {
				count+= result[j];
				total+= result[j] * j;
				System.out.print(result[j] + "\t");
			}
			float avg = (float)total/count;
			System.out.printf("%f", avg);
			System.out.print("\n\n");
			System.out.println(classroom.nextGeneration() + " survived");
		}
		

	}

}
