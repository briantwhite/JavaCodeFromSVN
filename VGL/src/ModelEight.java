
import org.jdom.Element;

/**
 * Brian White Summer 2004 Project VGL File:
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: ModelEight.java,v 1.1 2004-09-24 15:30:16 brian Exp $
 */

public class ModelEight extends ModelHigh implements Model {

	/**
	 * Constructor.
	 */
	public ModelEight() {
		super();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	public ModelEight(Element root) {
		super(root);
	}

	/**
	 * Constructor for testing purpose.
	 */
	public ModelEight(String c, String hoA, String hoB, String hoC) {
		super(c, hoA, hoB, hoC);
	}

	/**
	 * Return the sex type randomly chosen.
	 * 
	 * @return the sex type
	 */
	private int getRandomSexType() {
		int num = random.nextInt(100);
		if (num < 50)
			return 0; // male
		else
			return 1; // female
	}

	/**
	 * Create the field population cage and return it.
	 * 
	 * @return the field population cage
	 */
	public Cage populate() {
		Cage c = new Cage(0);

		// get the random number for amount of organism, 20 - 40
		int count = random.nextInt(20); // 0 - 19
		count = count + 20;

		for (int i = 0; i < count; i++) {
			int num1 = random.nextInt(100); // selects phenotype
			int num2 = random.nextInt(100); // selects among possible genotypes

			if ((num1 >= 0) && (num1 < 33)) {  // phenotype = A
				if ((num2 >= 0) && (num2 < 50)) {  // AA homozygote
					c.addOrganism(new Organism(getRandomSexType(), homoA, homoA,
							homoA, 8), homoA);
				} else {                          // AB heterozygote	
					c.addOrganism(new Organism(getRandomSexType(), homoA, homoB,
							homoA, 8), homoA);
				}
			} else if ((num1 >= 33) && (num1 < 66)) {  //phenotype B
				if ((num2 >= 0) && (num2 < 50)) {      //BB homozygote
					c.addOrganism(new Organism(getRandomSexType(), homoB, homoB,
							homoB, 8), homoB);
				} else {                              //BC heterozygote
					c.addOrganism(new Organism(getRandomSexType(), homoB, homoC,
							homoB, 8), homoB);
				}
			} else {                                // phenotype C 
				if ((num2 >= 0) && (num2 < 50)) {      //CC homozygote
					c.addOrganism(new Organism(getRandomSexType(), homoC, homoC,
							homoC, 8), homoC);
				} else {                              //CA heterozygote
					c.addOrganism(new Organism(getRandomSexType(), homoC, homoA,
							homoC, 8), homoC);
				}
			}
		}

		return c;
	}

	/**
	 * Cross the given two organisms and return the offspring cage.
	 * 
	 * @param id
	 *            cage's id
	 * @param o1
	 *            organism to be crossed
	 * @param o2
	 *            organism to be crossed
	 * @return the offspring cage
	 */
	public Cage crossTwo(int id, Organism o1, Organism o2) {
		if (o1.getSexType() == o2.getSexType()) // same sex error
			return null;

		Cage c = new Cage(id, o1, o2);

		// get the random number for amount of organism, 20 - 40
		int count = random.nextInt(20); // 0 - 19
		count = count + 20;

		String genotype1 = null;
		String genotype2 = null;
		for (int i = 0; i < count; i++) {
			int num = random.nextInt(100);
			if (num < 50)
				genotype1 = o1.getAllele1();
			else
				genotype1 = o1.getAllele2();

			num = random.nextInt(100);
			if (num < 50)
				genotype2 = o2.getAllele1();
			else
				genotype2 = o2.getAllele2();

			if (genotype1.equals(genotype2))  //homozygote (AA, BB, CC)
				c.addOrganism(new Organism(getRandomSexType(), genotype1,
						genotype2, genotype1, 8), genotype1);
			else if ((genotype1.equals(homoA) && genotype2.equals(homoB)) // AB
					|| (genotype1.equals(homoB) && genotype2.equals(homoA))) 
				c.addOrganism(new Organism(getRandomSexType(), genotype1,
						genotype2, homoA, 8), homoA);
			else if ((genotype1.equals(homoB) && genotype2.equals(homoC)) // BC
					|| (genotype1.equals(homoC) && genotype2.equals(homoB))) 
				c.addOrganism(new Organism(getRandomSexType(), genotype1,
						genotype2, homoB, 8), homoB);
			else         // can only be AC
				c.addOrganism(new Organism(getRandomSexType(), genotype1,
						genotype2, homoC, 8), homoC);
		}
		return c;
	}

	/**
	 * Return the model name.
	 * 
	 * @return the model name
	 */
	public String getModelName() {
		return "model 8: Autosomal; 3-allele; Circular Dominance" 
			+ "\nA dominant to B; B dominant to C; C dominant to A";
	}

	/**
	 * Return the model number.
	 * 
	 * @return the model number
	 */
	public int getModelNo() {
		return 8;
	}
}