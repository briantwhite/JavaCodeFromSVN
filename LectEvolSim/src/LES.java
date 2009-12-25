
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
		
		System.out.println("G\t0\t1\t2\t3\t4\t");
		
		for (int i = 0; i < 10; i++) {
			System.out.print(i + "\t");
			int[] result = classroom.report();
			for (int j = 0; j < result.length; j++) {
				System.out.print(result[j] + "\t");
			}
			System.out.print("\n\n");
			System.out.println(classroom.nextGeneration() + " survived");
		}
		

	}

}
