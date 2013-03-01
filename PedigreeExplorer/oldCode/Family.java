package PE;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import Pelican.PelicanPerson;

public class Family implements Comparable {
	
	private PelicanPerson father;
	private PelicanPerson mother;
	private Vector<PelicanPerson> actualChildren;
	private HashSet<String> possibleChildren;
	
	public Family(PelicanPerson father, PelicanPerson mother) {
		this.father = father;
		this.mother = mother;
		actualChildren = new Vector<PelicanPerson>();
		possibleChildren = new HashSet<String>();
		possibleChildren.add(father.genotype[0] + " " + mother.genotype[0]);
		possibleChildren.add(father.genotype[0] + " " + mother.genotype[1]);
		possibleChildren.add(father.genotype[1] + " " + mother.genotype[0]);
		possibleChildren.add(father.genotype[1] + " " + mother.genotype[1]);
	}
	
	public PelicanPerson getFather() {
		return father;
	}
	
	public PelicanPerson getMother() {
		return mother;
	}
	
	public void addChild(PelicanPerson child) {
		actualChildren.add(child);
	}
	
	public Vector<PelicanPerson> getChildren() {
		return actualChildren;
	}
	
	public void updatePossibleChildren() {
		possibleChildren = new HashSet<String>();
		possibleChildren.add(father.genotype[0] + " " + mother.genotype[0]);
		possibleChildren.add(father.genotype[0] + " " + mother.genotype[1]);
		possibleChildren.add(father.genotype[1] + " " + mother.genotype[0]);
		possibleChildren.add(father.genotype[1] + " " + mother.genotype[1]);
	}
	
	public boolean isChildPossible(PelicanPerson child) {
		return ((possibleChildren.contains(child.genotype[0] + " " + child.genotype[1])) ||
				(possibleChildren.contains(child.genotype[1] + " " + child.genotype[0])));
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Father:" + father.id + " Mother:" + mother.id);
		b.append(" Children:");
		Iterator<PelicanPerson> i = actualChildren.iterator();
		while (i.hasNext()) {
			PelicanPerson p = i.next();
			b.append(p.id + ",");
		}
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}

	/*
	 * sort by lowest of the indices you have
	 *   allows finding root family
	 */
	public int compareTo(Object arg0) throws ClassCastException {
		if(!(arg0 instanceof Family)) throw new ClassCastException("Expected Family in comparison");
		int ourIndex = Math.min(father.id, mother.id);
		int theirIndex = Math.min(((Family)arg0).father.id, ((Family)arg0).mother.id);
		return ourIndex - theirIndex;
	}

}
