package content;

import iClickerDriver.ButtonEnum;
import iClickerDriver.Vote;

public class SummaryListWithLabInfo extends SummaryList {

	public SummaryListWithLabInfo(Question question) {
		super(question);
		
		this.labInfoList = new LabInfoList(this.studentList.getLabSectionAmount());
	}
	
	@Override
	public void add(Vote vote) throws ClassNotFoundException {
		Student student = this.studentList.getStudentByClickerId(vote.getId());		// We need to check if this is an
		if (student == null) return;												// ad-hoc remote. Discard if yes.
			
		// Once a student votes, all his/her remotes will be added to clickerIdToSummary. 
		// So the following if branch means if this is the first time this student votes.
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
		
		Student student = summary.getStudent();
		
		for (String clickerId : student.getClickerId()) {
			this.clickerIdToSummary.put(clickerId, summary);
		}
		
		this.summaryList.add(summary);
		
		this.SummaryToIndex.put(summary, this.summaryList.size() - 1);
		
		this.increaseCount(summary.getButtonFinal());
		
		// Get lab section index of this student;
		int labSectionIndex = this.studentList.getLabSectionIndexBySection(summary.getStudent().getLabSection());
		this.labInfoList.newResponse(labSectionIndex, summary.getButtonFinal());
		
		// Update response grid.
		this.question.getSession().getCourse().getTest().getResponseGrid().newVote(summary, this.studentTotal - 1);
	}

	@Override
	protected void modifyExistingVote(Vote vote) {
		Summary summary = this.clickerIdToSummary.get(vote.getId());
		
		ButtonEnum oldChoice = summary.getButtonFinal();
		
		summary.setTimeStampFinal(vote.getTimeStamp());
		summary.setButtonFinal(vote.getButton());
		summary.increaseNumberOfAttempts();

		this.modifyCount(oldChoice, vote.getButton());
		
		int labSection = this.studentList.getLabSectionIndexBySection(summary.getStudent().getLabSection());
		this.labInfoList.modifyResponse(labSection, oldChoice, vote.getButton());
		
		// Update response grid.
		this.question.getSession().getCourse().getTest().getResponseGrid().modifyVote(summary, this.SummaryToIndex.get(summary));
	}
}
