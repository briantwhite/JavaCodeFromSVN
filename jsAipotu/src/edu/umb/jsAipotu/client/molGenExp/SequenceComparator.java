package edu.umb.jsAipotu.client.molGenExp;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	
	public DialogBox resultDialog;
	public HTML html;
	private VerticalPanel dialogPanel;
	private Button closeButton;

	public SequenceComparator(String u,
			String l,
			String s,
			String c) {
		upperSeq = u;
		lowerSeq = l;
		sampleSeq = s;
		probeSeq = c;
		resultDialog = new DialogBox(false);
		resultDialog.setStyleName("comparisonResultDialog");
		dialogPanel = new VerticalPanel();
		html = new HTML();
		dialogPanel.add(html);
		closeButton = new Button("Close");
		closeButton.setStyleName("closeComparisonResultButton");
		dialogPanel.add(closeButton);
		resultDialog.add(dialogPanel);
		resultDialog.hide();
		
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				resultDialog.hide();
			}
		});
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
