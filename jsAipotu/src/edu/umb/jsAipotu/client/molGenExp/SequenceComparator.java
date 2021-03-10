package edu.umb.jsAipotu.client.molGenExp;

public abstract class SequenceComparator {

	public final static int UPPER = 0;
	public final static int LOWER = 1;
	public final static int SAMPLE = 2;
	public final static int PROBE = 3;
	
	public final static String UPPER_LABEL = 		"Upper Sequence:    ";
	public final static String LOWER_LABEL = 		"Lower Sequence:    ";
	public final static String SAMPLE_LABEL = 		"Sample Sequence:   ";
	public final static String PROBE_LABEL = 	"Probe Sequence:";
	public final static String DIFFERENCE_LABEL = 	"Difference:         ";

	private String upperSeq;
	private String lowerSeq;
	private String sampleSeq;
	private String probeSeq;

	public SequenceComparator(String u,
			String l,
			String s,
			String c) {
		upperSeq = u;
		lowerSeq = l;
		sampleSeq = s;
		probeSeq = c;
	}

	public abstract void compareSequences(int seqID1, int seqID2);

	public String getUpperSeq(){
		return upperSeq;
	}

	public String getLowerSeq() {
		return lowerSeq;
	}

	public String getSampleSeq() {
		return sampleSeq;
	}

	public String getProbeSeq() {
		return probeSeq;
	}

	public String getSequence(int id) {
		switch (id)
		{
		case UPPER:
			return upperSeq;
		case LOWER:
			return lowerSeq;
		case SAMPLE:
			return sampleSeq;
		case PROBE:
			return probeSeq;
		default:
			return "";
		}
	}
	
	public String getSequenceLabel(int id) {
		switch (id)
		{
		case UPPER:
			return UPPER_LABEL;
		case LOWER:
			return LOWER_LABEL;
		case SAMPLE:
			return SAMPLE_LABEL;
		case PROBE:
			return PROBE_LABEL;
		default:
			return "";
		}
	}
}
