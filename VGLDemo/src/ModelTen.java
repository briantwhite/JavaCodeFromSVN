
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
 * @version 1.0 $Id: ModelTen.java,v 1.1 2005-06-01 13:30:44 brian Exp $
 */

public class ModelTen extends ModelHigh implements Model {

	/**
	 * Constructor.
	 */
	public ModelTen() {
		super();
	}

	/**
	 * Constructor for reopening the file.
	 * 
	 * @param root
	 *            the model information in JDom Element object
	 */
	public ModelTen(Element root) {
		super(root);
	}

	/**
	 * Constructor for testing purpose.
	 */
	public ModelTen(String c, String hoA, String hoB, String hoC) {
		super(c, hoA, hoB, hoC);
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
			int num = random.nextInt(100); // sex
			int num1 = random.nextInt(100); // phenotype
			int num2 = random.nextInt(100); // genotype

			if (num < 50) { // female (XX)
				if ((num1 >= 0) && (num1 < 33)) {  //phenotype A
					if ((num2 >= 0) && (num2 < 50)) {  // AA
						c.addOrganism(
								new Organism(1, homoA, homoA, homoA, 10), 
								homoA);
					} else { // AB
						c.addOrganism(
								new Organism(1, homoA, homoB, homoA, 10), 
								homoA);						
					}
				} else if ((num1 >= 33) && (num1 < 66)) { //phenotype B
					if ((num2 >= 0) && (num2 < 50)) {  // BB
						c.addOrganism(
								new Organism(1, homoB, homoB, homoB, 10), 
								homoB);
					} else { // BC
						c.addOrganism(
								new Organism(1, homoB, homoC, homoB, 10), 
								homoB);						
					}					
				} else {   //phenotype C
					if ((num2 >= 0) && (num2 < 50)) {  // CC
						c.addOrganism(
								new Organism(1, homoC, homoC, homoC, 10), 
								homoC);
					} else { // AC
						c.addOrganism(
								new Organism(1, homoA, homoC, homoC, 10), 
								homoC);						
					}

				}
			} else { // males (XO)
				if ((num1 >= 0) && (num1 < 33)) {  //phenotype A
					c.addOrganism(new Organism(0, homoA, homoA, 10), homoA);					
				} else if ((num1 >=33) && (num1 < 66)) {  //phenotype B
					c.addOrganism(new Organism(0, homoB, homoB, 10), homoB);
				} else { // phenotype C
					c.addOrganism(new Organism(0, homoC, homoC, 10), homoC);
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

			if ((genotype1 == null) && (genotype2 != null)) // O* (male)
				c.addOrganism(new Organism(0, genotype2, genotype2, 10),
						genotype2);
			else if ((genotype1 != null) && (genotype2 == null)) // *O (male)
				c.addOrganism(new Organism(0, genotype1, genotype1, 10),
						genotype1);
			else { // ** (female)
				if (genotype1.equals(genotype2))  //homozygote (AA, BB, CC)
					c.addOrganism(new Organism(1, genotype1,
							genotype2, genotype1, 10), genotype1);
				else if ((genotype1.equals(homoA) && genotype2.equals(homoB)) // AB
						|| (genotype1.equals(homoB) && genotype2.equals(homoA))) 
					c.addOrganism(new Organism(1, genotype1,
							genotype2, homoA, 10), homoA);
				else if ((genotype1.equals(homoB) && genotype2.equals(homoC)) // BC
						|| (genotype1.equals(homoC) && genotype2.equals(homoB))) 
					c.addOrganism(new Organism(1, genotype1,
							genotype2, homoB, 10), homoB);
				else         // can only be AC
					c.addOrganism(new Organism(1, genotype1,
							genotype2, homoC, 10), homoC);
			}
		}

		return c;
	}

	/**
	 * Return the model name.
	 * 
	 * @return the model name
	 */
	public String getModelName() {
		return "model 10: Sex-linked (XX = Female; XY = Male); "
		        + "\n3-allele; Circular Dominance" 
			    + "\nA dominant to B; B dominant to C; C dominant to A";
	}

	/**
	 * Return the model number.
	 * 
	 * @return the model number
	 */
	public int getModelNo() {
		return 10;
	}
}