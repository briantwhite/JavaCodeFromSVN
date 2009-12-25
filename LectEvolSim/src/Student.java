import java.util.Random;


public class Student {
	private int age;
	private Gene[] genes;
	private Random r;

	public Student() {
		r = new Random();
		genes = new Gene[LES.initialAlleles.length];
		for (int i = 0; i < LES.initialAlleles.length; i++) {
			genes[i] = new Gene(LES.initialAlleles[i]);
		}
		age = 0;
	}
	
	public Student(Gene[] geneArray) {
		r = new Random();
		age = 0;
		genes = new Gene[LES.initialAlleles.length];
		for (int i = 0; i < geneArray.length; i++) {
			genes[i] = new Gene(geneArray[i].getAllele());
		}
	}
	
	//returns true if alive; false if dead
	public boolean nextGeneration() {
		age++;
		// die if too old
		if (age >= LES.ageAtDeath) {
			return false;
		}
		
		//die if too few or too many flagella
		if ((report() == 0)|| (report() == 4)) {
			return false;
		}
		
		return true;
	}
	
	public Gene[] getGenotype() {
		return genes;
	}
	
	public int report() {
		int count = 0;
		for (int i = 0; i < LES.initialAlleles.length; i++) {
			count += genes[i].getAllele();
		}
		return count;
	}
	
	
	public void mutate() {
		int roll = r.nextInt(6) + 1;
		if (roll > 4) {
			return;         // no mutation on 5,6
		}		
		genes[roll - 1].mutate();
	}

		
}
