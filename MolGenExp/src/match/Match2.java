package match;

//Test all seven alignment algorithms

public class Match2 {
	public static void main(String[] args) {
		Output out = new SystemOut();
		String seq1 = args[0], seq2 = args[1];
		
		Substitution sub = new Blosum50();
		(new NW      (sub, 8,     seq1, seq2)).domatch(out, "GLOBAL ALIGNMENT");
		(new SW      (sub, 8,     seq1, seq2)).domatch(out, "LOCAL ALIGNMENT");
		(new RM      (sub, 8, 20, seq1, seq2)).domatch(out, "REPEATED MATCHES");
		(new OM      (sub, 8,     seq1, seq2)).domatch(out, "OVERLAP MATCH");
		(new NWAffine(sub, 12, 2, seq1, seq2)).domatch(out, "AFFINE GLOBAL");
		(new NWSmart (sub, 8,     seq1, seq2)).domatch(out, "SMART GLOBAL");
		(new SWSmart (sub, 8,     seq1, seq2)).domatch(out, "SMART LOCAL");
		(new SWSmartAffine (sub, 12, 2, seq1, seq2)).domatch(out, 
		"SMART AFFINE LOCAL");
	}
}
