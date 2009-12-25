import java.util.Random;


public class Student {
	private Gene[] genes;
	private Random r;

	public Student() {
		r = new Random();
		genes = new Gene[LES.initialAlleles.length];
		for (int i = 0; i < LES.initialAlleles.length; i++) {
			genes[i] = new Gene(LES.initialAlleles[i]);
		}
	}
	
	public Student(Gene[] geneArray) {
		r = new Random();
		genes = new Gene[LES.initialAlleles.length];
		for (int i = 0; i < geneArray.length; i++) {
			genes[i] = new Gene(geneArray[i].getAllele());
		}
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
