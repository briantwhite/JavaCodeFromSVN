package tbs.graphanalysis;

import java.util.HashSet;

/*
 * set of bipartitions in binary form (see Graph)
 * of the correct tree
 * used in scoring student trees by Robinson-Foulds distance
 */
public class CorrectTreeBipartitionSet {
	
	public static HashSet<Integer> getSet() {
		HashSet<Integer> set = new HashSet<Integer>();
		set.add(Integer.parseInt("0", 2));
		set.add(Integer.parseInt("11110111111110111111", 2));
		set.add(Integer.parseInt("11111111111111110101", 2));
		set.add(Integer.parseInt("11111111101111111111", 2));
		set.add(Integer.parseInt("11111111111111110111", 2));
		set.add(Integer.parseInt("11110111111111111111", 2));
		set.add(Integer.parseInt("11111111111101111111", 2));
		set.add(Integer.parseInt("1111111111111111111", 2));
		set.add(Integer.parseInt("11010011010010110101", 2));
		set.add(Integer.parseInt("11111111111111111011", 2));
		set.add(Integer.parseInt("10111111111111111111", 2));
		set.add(Integer.parseInt("11111111111110111111", 2));
		set.add(Integer.parseInt("11111011111111111111", 2));
		set.add(Integer.parseInt("11111111111111111101", 2));
		set.add(Integer.parseInt("11111101111111111111", 2));
		set.add(Integer.parseInt("10111111101111101111", 2));
		set.add(Integer.parseInt("11111111011111110101", 2));
		set.add(Integer.parseInt("10111111101111101011", 2));
		set.add(Integer.parseInt("11110111011110110101", 2));
		set.add(Integer.parseInt("11111111111011111111", 2));
		set.add(Integer.parseInt("11111111011111111111", 2));
		set.add(Integer.parseInt("11111111111111101111", 2));
		set.add(Integer.parseInt("11101111111111111111", 2));
		set.add(Integer.parseInt("11111111110111111111", 2));
		set.add(Integer.parseInt("11011111111111111111", 2));
		set.add(Integer.parseInt("11111111111111011111", 2));
		set.add(Integer.parseInt("11111101111111011111", 2));
		set.add(Integer.parseInt("11111110111111111111", 2));
		set.add(Integer.parseInt("10010001000010000001", 2));
		set.add(Integer.parseInt("11101110111111111111", 2));
		return set;
	}

}
