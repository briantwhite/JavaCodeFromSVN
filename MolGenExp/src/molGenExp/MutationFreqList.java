package molGenExp;

public class MutationFreqList {
	
	private float pointFreq;
	private float deleteFreq;
	private float insertFreq;
	
	public MutationFreqList(float point, float delete, float insert) {
		pointFreq = point;
		deleteFreq = delete;
		insertFreq = insert;
	}

	public float getPointFreq() {
		return pointFreq;
	}

	public float getDeleteFreq() {
		return deleteFreq;
	}

	public float getInsertFreq() {
		return insertFreq;
	}

}
