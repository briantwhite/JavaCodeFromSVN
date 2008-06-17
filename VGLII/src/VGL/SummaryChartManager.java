package VGL;

public class SummaryChartManager {
	
	private static SummaryChartManager instance;
	
	private SummaryChartManager() {
		
	}

	public static SummaryChartManager getInstance() {
		if (instance == null) {
			instance = new SummaryChartManager();
		}
		return instance;
	}
	
	public void addToSelected(int cageId) {
		
	}
	
	public void removeFromSelected(int cageId) {
		
	}
	
	public void clearAllSelected() {
		
	}
}
