package LogAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
 * starts as a set of required genetic facts (Dominance Interactions & sex-linkage)
 * as crosses are processed, it checks them off and lets reader konw when 
 * all required info has been collected
 */
public class RequiredResultSet {

	// these are the states for each required result
	private static final int NOT_FOUND = 0;
	private static final int FOUND = 1;

	private HashMap<GeneticResult, Integer> results;

	private HashSet<LinkedGeneticResult> linkedResults;

	private HashSet<PartialNoSexLinkageResult> partialNoSexLinkageResults;

	/*
	 * heterozygote phenotype for incomplete dominance
	 *   used because you can rule out sex-linkage if you 
	 *   find both males and females of this
	 *   you can also be sure of sex-linkage if never find this in one sex
	 */
	private String heterozygotePhenotype;
	private boolean foundMaleHeterozygote;
	private boolean foundFemaleHeterozygote;

	public RequiredResultSet(HashSet<GeneticResult> requiredResults) {
		results = new HashMap<GeneticResult, Integer>();
		Iterator<GeneticResult> grIt = requiredResults.iterator();
		while (grIt.hasNext()) {
			GeneticResult gr = grIt.next();
			results.put(gr, new Integer(NOT_FOUND));
		}
		linkedResults = new HashSet<LinkedGeneticResult>();
		partialNoSexLinkageResults = new HashSet<PartialNoSexLinkageResult>();

		heterozygotePhenotype = null;
		foundMaleHeterozygote = false;
		foundFemaleHeterozygote = false;

		if (VGLLogAnalyzer.debug) System.out.println(toString());
	}

	/*
	 * process a genetic result
	 * 	return false until all requirements met
	 *  then return true
	 */
	public void processResult(GeneticResult result) {
		if (VGLLogAnalyzer.debug) System.out.println(toString());

		if (isHaveAllInfo()) return;

		if (result instanceof LinkedGeneticResult) {
			// a linked result is where you know result A or B
			//  once you find B, you know A and vice-versa
			LinkedGeneticResult lgr = (LinkedGeneticResult)result;

			// see if one or the other part of the linked result has been found
			// if so, check off the other part and you're done with this result
			if (results.get(lgr.getResultA()) != null) {
				if (results.get(lgr.getResultA()).intValue() == FOUND) {
					// result A has been collected; so we also know B
					results.put(lgr.getResultB(), FOUND);
					return;
				}
			}
			if (results.get(lgr.getResultB()) != null) {			
				if (results.get(lgr.getResultB()).intValue() == FOUND) {
					// result B has been collected; so we also know A
					results.put(lgr.getResultA(), FOUND);
					return;
				}
			}
			// if neither has been collected so far, mark as needing a link
			linkedResults.add(lgr);

		} else if (result instanceof PartialNoSexLinkageResult) {
			/*
			 * a partial no sex-linkage result
			 * 	one half of a reciprocal cross 
			 * 	male A X female B -> male and female A and B
			 *  male B X female A -> male and female A and B
			 */
			partialNoSexLinkageResults.add((PartialNoSexLinkageResult)result);

			/*
			 * now process them all to see if we can find a reciprocal pair
			 * which will allow us to cross off any SexLinkageResult(false)'s
			 */
			Iterator<PartialNoSexLinkageResult> it1 = partialNoSexLinkageResults.iterator();
			boolean allDone = false;
			while (it1.hasNext()) {
				PartialNoSexLinkageResult r1 = it1.next();
				Iterator<PartialNoSexLinkageResult> it2 = partialNoSexLinkageResults.iterator();
				while (it2.hasNext()) {
					PartialNoSexLinkageResult r2 = it2.next();
					if (r1.isReciprocalOf(r2)) {
						results.put(new SexLinkageResult(false), FOUND);
						if (VGLLogAnalyzer.debug) System.out.println("    confirmed no sex-linkage!!");
						allDone = true;
						break;
					}
				}
				if (allDone) break;
			}

		} else if (result instanceof FoundHeterozygotePhenotypeResult) {
			/* 
			 * process this - it shows that sex-linkage or not with incomplete dominance
			 * only gets fired if het pheno != null
			 */
			FoundHeterozygotePhenotypeResult fhpr = (FoundHeterozygotePhenotypeResult)result;
			if (fhpr.isMale()) {
				foundMaleHeterozygote = true;
			} else {
				foundFemaleHeterozygote = true;
			}

			if (foundMaleHeterozygote && foundFemaleHeterozygote) {
				if (results.containsKey(new SexLinkageResult(false))) {
					results.put(new SexLinkageResult(false), FOUND);
					if (VGLLogAnalyzer.debug) System.out.println(
							"found het of both sexes; ruled out sex linkage");
				}
			}

		} else {
			// it's a regular result; tag it as found 
			if (results.containsKey(result)) {
				results.put(result, FOUND);
			}

			// and deal with any links
			Iterator<LinkedGeneticResult> lgrIt = linkedResults.iterator();
			LinkedGeneticResult lgrToRemove = null;
			while (lgrIt.hasNext()) {
				LinkedGeneticResult lgr = lgrIt.next();
				if (lgr.getResultA().equals(result)) {
					// we found one result; mark the other as found
					results.put(lgr.getResultB(), FOUND);
					// and we're done with this linked one
					lgrToRemove = lgr;
				}
				if (lgr.getResultB().equals(result)) {
					results.put(lgr.getResultA(), FOUND);
					lgrToRemove = lgr;
				}
			}
			/*
			 * remove the linked one - must do it here since you can't mess
			 * with a Collection while iterating over it
			 */
			if (lgrToRemove != null) linkedResults.remove(lgrToRemove);
		}
	}

	public void setHeterozygotePhenotype(String hete) {
		heterozygotePhenotype = hete;
	}

	public String getHeterozygotePhenotype() {
		return heterozygotePhenotype;
	}

	public boolean isHaveAllInfo() {
		// see if all have been checked off
		Iterator<GeneticResult> grIt = results.keySet().iterator();
		while (grIt.hasNext()) {
			GeneticResult gr = grIt.next();
			if (results.get(gr).intValue() == NOT_FOUND) return false;
		}
		return true;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("***Required Results***\n");
		buf.append("   results\n");
		Iterator<GeneticResult> grIt = results.keySet().iterator();
		while (grIt.hasNext()) {
			GeneticResult gr = grIt.next();
			buf.append(gr.toString() + " code=" + results.get(gr).intValue());
			buf.append("\n");
		}

		buf.append("   het pheno =" + heterozygotePhenotype + "\n");

		buf.append("   linked results:\n");
		Iterator<LinkedGeneticResult> lgrIt = linkedResults.iterator();
		while (lgrIt.hasNext()) {
			buf.append(lgrIt.next().toString() + "\n");
		}

		buf.append("   partial results:\n");
		Iterator<PartialNoSexLinkageResult> pnslrIt = partialNoSexLinkageResults.iterator();
		while (pnslrIt.hasNext()) {
			buf.append(pnslrIt.next().toString() + "\n");
		}

		buf.append("Are we all done? " + isHaveAllInfo() + "\n");

		buf.append("******\n");
		return buf.toString();
	}

}
