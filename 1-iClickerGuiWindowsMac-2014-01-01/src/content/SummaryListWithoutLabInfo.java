package content;

import iClickerDriver.Vote;

public class SummaryListWithoutLabInfo extends SummaryList {

	public SummaryListWithoutLabInfo(Question question) {
		super(question);
		
		this.labInfoList = null;
	}

	@Override
	public void add(Vote vote) throws ClassNotFoundException {
		if (this.clickerIdToSummary.get(vote.getId()) == null) {
			this.addNewVote(vote);
		} else {
			this.modifyExistingVote(vote);
		}
		
		if (studentTotal > 0){
			responseAPerc = (double)responseA * 100 / studentTotal;
			responseBPerc = (double)responseB * 100 / studentTotal;
			responseCPerc = (double)responseC * 100 / studentTotal;
			responseDPerc = (double)responseD * 100 / studentTotal;
			responseEPerc = (double)responseE * 100 / studentTotal;
		} else {
			responseAPerc = 0;
			responseBPerc = 0;
			responseCPerc = 0;
			responseDPerc = 0;
			responseEPerc = 0;
		}
		
        updateToolbar();
        updateHistogram();
	}
	
	@Override
	protected void addNewVote(Vote vote) throws ClassNotFoundException {
		Summary summary = new Summary(vote, this.question.getSession().getCourse().getStudents());
		
		// added from SummaryListWithLabInfo
		Student student = summary.getStudent();
		
		for (String clickerId : student.getClickerId()) {
			this.clickerIdToSummary.put(clickerId, summary);
		}
		//

		this.summaryList.add(summary);
		
		this.SummaryToIndex.put(summary, this.summaryList.size() - 1);

		this.increaseCount(summary.getButtonFinal());
		
		// Update response grid.
		this.question.getSession().getCourse().getTest().getResponseGrid().newVote(summary, this.studentTotal - 1);
	}
	
	@Override
	protected void modifyExistingVote(Vote vote) {
		Summary summary = this.clickerIdToSummary.get(vote.getId());
		
		modifyCount(summary.getButtonFinal(), vote.getButton());

		summary.setTimeStampFinal(vote.getTimeStamp());
		summary.setButtonFinal(vote.getButton());
		summary.increaseNumberOfAttempts();
		
		// Update response grid.
        this.question.getSession().getCourse().getTest().getResponseGrid().modifyVote(summary, this.SummaryToIndex.get(summary));
	}
}