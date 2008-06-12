package GeneticModels;

public class ProblemTypeSpecification {
	
	// all are probabilities 'ch' is short for 'chance of'
	private float chZZ_ZW;
	
	private float gene1_chSexLinked;
	private float gene1_ch3Alleles;
	private float gene1_chIncDom;
	
	private float gene2_chPresent;
	private float gene2_chSameChrAsGene1;
	private float gene2_minRfToGene1;
	private float gene2_maxRfToGene1;
	private float gene2_ch3Alleles;
	private float gene2_chIncDom;
	
	private float gene3_chPresent;
	private float gene3_chSameChrAsGene1;
	private float gene3_minRfToGene1;
	private float gene3_maxRfToGene1;
	private float gene3_ch3Alleles;
	private float gene3_chIncDom;
	
	public ProblemTypeSpecification() {
		chZZ_ZW = 0.0f;
		
		gene1_chSexLinked = 0.0f;
		gene1_ch3Alleles = 0.0f;
		gene1_chIncDom = 0.0f;
		
		gene2_chPresent = 0.0f;
		gene2_chSameChrAsGene1 = 0.0f;
		gene2_minRfToGene1 = 0.0f;
		gene2_maxRfToGene1 = 0.0f;
		gene2_ch3Alleles = 0.0f;
		gene2_chIncDom = 0.0f;
		
		gene3_chPresent = 0.0f;
		gene3_chSameChrAsGene1 = 0.0f;
		gene3_minRfToGene1 = 0.0f;
		gene3_maxRfToGene1 = 0.0f;
		gene3_ch3Alleles = 0.0f;
		gene3_chIncDom = 0.0f;		
	}

	public float getChZZ_ZW() {
		return chZZ_ZW;
	}

	public void setChZZ_ZW(float chZZ_ZW) {
		this.chZZ_ZW = chZZ_ZW;
	}

	public float getGene1_chSexLinked() {
		return gene1_chSexLinked;
	}

	public void setGene1_chSexLinked(float gene1_chSexLinked) {
		this.gene1_chSexLinked = gene1_chSexLinked;
	}

	public float getGene1_ch3Alleles() {
		return gene1_ch3Alleles;
	}

	public void setGene1_ch3Alleles(float gene1_ch3Alleles) {
		this.gene1_ch3Alleles = gene1_ch3Alleles;
	}

	public float getGene1_chIncDom() {
		return gene1_chIncDom;
	}

	public void setGene1_chIncDom(float gene1_chIncDom) {
		this.gene1_chIncDom = gene1_chIncDom;
	}

	public float getGene2_chPresent() {
		return gene2_chPresent;
	}

	public void setGene2_chPresent(float gene2_chPresent) {
		this.gene2_chPresent = gene2_chPresent;
	}

	public float getGene2_chSameChrAsGene1() {
		return gene2_chSameChrAsGene1;
	}

	public void setGene2_chSameChrAsGene1(float gene2_chSameChrAsGene1) {
		this.gene2_chSameChrAsGene1 = gene2_chSameChrAsGene1;
	}

	public float getGene2_minRfToGene1() {
		return gene2_minRfToGene1;
	}

	public void setGene2_minRfToGene1(float gene2_minRfToGene1) {
		this.gene2_minRfToGene1 = gene2_minRfToGene1;
	}

	public float getGene2_maxRfToGene1() {
		return gene2_maxRfToGene1;
	}

	public void setGene2_maxRfToGene1(float gene2_maxRfToGene1) {
		this.gene2_maxRfToGene1 = gene2_maxRfToGene1;
	}

	public float getGene2_ch3Alleles() {
		return gene2_ch3Alleles;
	}

	public void setGene2_ch3Alleles(float gene2_ch2Alleles) {
		this.gene2_ch3Alleles = gene2_ch2Alleles;
	}

	public float getGene2_chIncDom() {
		return gene2_chIncDom;
	}

	public void setGene2_chIncDom(float gene2_chIncDom) {
		this.gene2_chIncDom = gene2_chIncDom;
	}

	public float getGene3_chPresent() {
		return gene3_chPresent;
	}

	public void setGene3_chPresent(float gene3_chPresent) {
		this.gene3_chPresent = gene3_chPresent;
	}

	public float getGene3_chSameChrAsGene1() {
		return gene3_chSameChrAsGene1;
	}

	public void setGene3_chSameChrAsGene1(float gene3_chSameChrAsGene1) {
		this.gene3_chSameChrAsGene1 = gene3_chSameChrAsGene1;
	}

	public float getGene3_minRfToGene1() {
		return gene3_minRfToGene1;
	}

	public void setGene3_minRfToGene1(float gene3_minRfToGene1) {
		this.gene3_minRfToGene1 = gene3_minRfToGene1;
	}

	public float getGene3_maxRfToGene1() {
		return gene3_maxRfToGene1;
	}

	public void setGene3_maxRfToGene1(float gene3_maxRfToGene1) {
		this.gene3_maxRfToGene1 = gene3_maxRfToGene1;
	}

	public float getGene3_ch3Alleles() {
		return gene3_ch3Alleles;
	}

	public void setGene3_ch3Alleles(float gene3_ch2Alleles) {
		this.gene3_ch3Alleles = gene3_ch2Alleles;
	}

	public float getGene3_chIncDom() {
		return gene3_chIncDom;
	}

	public void setGene3_chIncDom(float gene3_chIncDom) {
		this.gene3_chIncDom = gene3_chIncDom;
	}

}
