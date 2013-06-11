package edu.umb.bio.jsGenex.client.gx;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.umb.bio.jsGenex.client.problems.GenexState;
import edu.umb.bio.jsGenex.client.problems.Problem;
import edu.umb.bio.jsGenex.client.requirements.IntronNumberRequirement;
import edu.umb.bio.jsGenex.client.requirements.LongerProteinRequirement;
import edu.umb.bio.jsGenex.client.requirements.NoProteinRequirement;
import edu.umb.bio.jsGenex.client.requirements.NomRNARequirement;
import edu.umb.bio.jsGenex.client.requirements.ProteinLengthRequirement;
import edu.umb.bio.jsGenex.client.requirements.Requirement;
import edu.umb.bio.jsGenex.client.requirements.ShorterProteinRequirement;
import edu.umb.bio.jsGenex.client.requirements.ShortermRNARequirement;
import edu.umb.bio.jsGenex.client.requirements.SingleMutationRequirement;

public class GenexGWT implements EntryPoint {
	
	GenexParams params = new GenexParams();
	Gene workingGene;
	 
	String defaultDNA; //Sequence loaded from the edX xml file
	String DNA;
	String promoterSequence;
	int promoterSpacing;
	String terminatorSequence;
	String intronStartSequence;
	String intronEndSequence;
	String polyATail;

	//length (as actually displayed) of the text 
	// & labels before the start of the top DNA strand
	int headerLength; 
	
	int DNASequenceLength;

	String defaultProtein ="";
	String defaultMaturemRNA = "";

	String caption = ""; //the caption at the bottom of the frame

	String previousProteinString = ""; //the last protein sequence displayed
	String currentProteinString = "";

	int caretPosition = 0; //the number of the selected DNA base

	//Printing is disabled
	//DocumentRenderer docRenderer; // for printing

	boolean allowPrinting = false; //parameter for allowing printing
	// if false, it will not bother you by asking 
	// if you want to allow printing
	
	//GUI elements
	ScrollPanel scrollPanel;
	HorizontalPanel footerPanel;
	Button resetButton;
	Button newSequenceButton;
	Label infoLabel;
	HTML html;
	DialogBox dialogBox;
	VerticalPanel dialogBoxContents;
	HTML message;
	TextBox dnaTextBox;
	Button cancelButton;
	Button okButton;
	HorizontalPanel holder;
	
	boolean dnaStrandWasClicked = false;
	
	//Problems
	private Problem prob;
	
	public void onModuleLoad() {
		 		
		html = new HTML();
		scrollPanel = new ScrollPanel(html);
		scrollPanel.setSize("818px", "325px"); //Inside edX, 818px is the maximum content size in the edX platform
		scrollPanel.setStyleName("genex-scrollpanel");
		RootPanel.get("genex_container").add(scrollPanel);
		
		// Create a DialogBox with a button to close it
	    dialogBox = new DialogBox(false);
	    dialogBox.addStyleName("genex-dialogbox");
	    dialogBoxContents = new VerticalPanel();
	    dialogBox.setText("New DNA Sequence");
	    message = new HTML("Enter new DNA Sequence");
	    message.setStyleName("genex-dialogbox-message");
	    
	    dnaTextBox = new TextBox();
	    
	    cancelButton = new Button("Cancel");
	    cancelButton.addStyleName("genex-button");
	    cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				dnaTextBox.setText("");
			}
		});
	    
	    okButton = new Button("OK");
	    okButton.addStyleName("genex-button");
	    okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				previousProteinString = currentProteinString;
				String newDNA = dnaTextBox.getText();
				newDNA = newDNA.toUpperCase();
				newDNA = newDNA.replaceAll("[^AGCT]","");
				DNA = newDNA;
				caretPosition = -1;
				VisibleGene currentGene = expressGene(DNA, -1);
				refreshDisplay(currentGene, -1);
				currentProteinString = currentGene.getGene().getProteinString();
				headerLength = currentGene.getGene().getHeaderLength();
				DNASequenceLength = currentGene.getGene().getDNASequenceLength();  
				headerLength = currentGene.getGene().getHeaderLength();
				dialogBox.hide();
				checkAnswer(); //Automatic grading
			}
		});
	    
	    holder = new HorizontalPanel();
	    holder.add(cancelButton);
	    holder.add(okButton);
	    dialogBoxContents.add(message);
	    dialogBoxContents.add(dnaTextBox);
	    dialogBoxContents.add(holder);
	    dialogBox.setWidget(dialogBoxContents);
		
		resetButton = new Button("Reset DNA Sequence");
		resetButton.addStyleName("genex-button");
		resetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setDNASequence(defaultDNA);
			}
		});
		
		newSequenceButton = new Button("Enter New DNA Sequence");
		newSequenceButton.addStyleName("genex-button");
		newSequenceButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.center();
			}
		});
		
		infoLabel = new Label("Selected Base = ");
		infoLabel.addStyleName("genex-label");
		
		footerPanel = new HorizontalPanel();
		footerPanel.add(resetButton);
		footerPanel.add(newSequenceButton);
		footerPanel.add(infoLabel);
		RootPanel.get("genex_container").add(footerPanel);
		
		//Using a deferred command ensures that notifyHostpage() is called after
		//GWT initialization is finished.
		class DeferredCommand implements Command {
			GenexGWT gn;
			
			public DeferredCommand(GenexGWT gn) {
				super();
				this.gn = gn;
			}
			
			public void execute() {
				//Export the GWT Java functions that will be accessible from outside JavaScript
				exportSetDefaultDNASequence(gn);
				exportSetDNASequence(gn);
				exportSetClickEvent(gn);
				exportSetKeyEvent(gn);
				exportSetProblemNumber(gn);
				exportGetDNASequence(gn);
				notifyHostpage();
			}
		}
		
		Scheduler.get().scheduleDeferred(new DeferredCommand(this));
	}
	
	public static native void notifyHostpage() /*-{
	if (typeof $wnd.genexIsReady === 'function') 
		$wnd.genexIsReady();
	}-*/;
	
	public static native void storeAnswer(String str) /*-{
	if (typeof $wnd.genexStoreAnswer === 'function')
		$wnd.genexStoreAnswer(str);
	}-*/;
	
	public void setDefaultDNASequence(String dnaSequence) {
	    String inputDNAString = dnaSequence;
	    
	    if (inputDNAString != null) {
	        params.setDefaultDNA(inputDNAString);
	    }
	    
	    String inputPromoterString = "TATAA"; //TO DO getParameter("PROMOTER");
	    if (inputPromoterString != null) {
	        params.setPromoterSequence(inputPromoterString);
	    }
	    
	    String inputTerminatorString = "GGGGG"; //TO DO getParameter("TERMINATOR");
	    if (inputTerminatorString != null) {
	        params.setTerminatorSequence(inputTerminatorString);
	    }
	    
	    String inputIntronStartString = "GUGCG"; //TO DO getParameter("INTRON_START");
	    if (inputIntronStartString != null) {
	        params.setIntronStartSequence(inputIntronStartString);
	    }
	    
	    String inputIntronEndString = "CAAAG"; //TO DO getParameter("INTRON_END");
	    if (inputIntronEndString != null) {
	        params.setIntronEndSequence(inputIntronEndString);
	    }
	    
	    String inputPolyAString = "AAAAAAAAAAAAA"; //TO DO getParameter("POLY_A_TAIL");
	    if (inputPolyAString != null) {
	        params.setPolyATail(inputPolyAString);
	    }
	    
	    //Printing is disabled
	    String inputPrintingString = null; //TO DO getParameter("PRINTING");
	    if (inputPrintingString != null) {
	    	    params.setAllowPrinting(true);
	    }
	    
	    //Setup GUI
	    //This part is redundant as we have blended GenexGUI and GenexApplet. Keep for the moment.
	    defaultDNA = params.getDefaultDNA();
		DNA = params.getDefaultDNA();
		DNASequenceLength = DNA.length();

		promoterSequence = params.getPromoterSequence();
		promoterSpacing = params.getPromoterSpacing();
		terminatorSequence = params.getTerminatorSequence();
		intronStartSequence = params.getIntronStartSequence();
		intronEndSequence = params.getIntronEndSequence();
		polyATail = params.getPolyATail();
		allowPrinting = params.isAllowPrinting();
		
		//if it's a prokaryote, no poly A tail
		if (intronStartSequence.equals("none") || intronEndSequence.equals("none")) {
			polyATail = "";
		}
		
		//display the default gene
		VisibleGene currentGene = expressGene(defaultDNA, -1);
		defaultMaturemRNA = currentGene.getGene()._mRNASequence;
		defaultProtein = currentGene.getGene()._proteinSequence;
		//textPane.setText(currentGene.getColorHTML() + caption + "</pre></body></html>");
		headerLength = currentGene.getGene().getHeaderLength();
		DNASequenceLength = currentGene.getGene().getDNASequenceLength();
		currentProteinString = currentGene.getGene().getProteinString();
		
		html.setHTML(currentGene.getColorHTML() + caption + "</pre></body></html>");
	}
	
	public void setDNASequence(String dnaSequence) {
		DNA = dnaSequence;
		VisibleGene currentGene = expressGene(DNA, -1);
		refreshDisplay(currentGene, -1);
		currentProteinString = currentGene.getGene().getProteinString();
		headerLength = currentGene.getGene().getHeaderLength();
		DNASequenceLength = currentGene.getGene().getDNASequenceLength(); 
		headerLength = currentGene.getGene().getHeaderLength();
		checkAnswer(); //Automatic grading
	}
	
	public String getDNASequence() {
		return this.DNA;
	}
	
	public static native void alert(String msg)
	/*-{
		$wnd.alert(msg);
	}-*/;
	
	public static native void setClickEvent(GenexGWT gn)
	/*-{
		function getCharOffsetRelativeTo(container, node, offset) {
    		var range = document.createRange();
    		range.selectNodeContents(container);
    		range.setEnd(node, offset);
    		return range.toString().length;
		}
		
		var dnaStrandSpan = $doc.getElementById("dna-strand");
		dnaStrandSpan.style.cursor="pointer";
		
		$doc.onclick = function(e) {
			var e = $wnd.event || e; //window.event in IE only
			var src =  e.target || e.srcElement; //target in Firefox only
			var id = src.id;
			if (id !== null && id === "dna-strand") {
				var sel = $wnd.getSelection();
				var offset = getCharOffsetRelativeTo(dnaStrandSpan, sel.anchorNode, sel.anchorOffset);
				gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::mouseClicked(I)(offset);
				gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked = true;
				//Stop propagation of event
				if (e.stopPropagation) {
					e.stopPropagation();
				}	
 				if (e.cancelBubble!=null) {
 					e.cancelBubble = true;
 				}	
			}
			else {
				if (gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked) {
					gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked = false;
					//Refresh the display to erase the blue selection
					gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::eraseBlueSelection()();
				}	
			}	
		}		
	}-*/;
	
	public void eraseBlueSelection() {
		VisibleGene currentGene = expressGene(DNA, -1);
		refreshDisplay(currentGene, -1);
	}
	
	/*The following was causing problems: ie a click on an input field would alter the DNA sequence.
	  We now append the onclick to the window element.
	dnaStrandSpan.onclick = function() {
		var sel = $wnd.getSelection();
		var offset = getCharOffsetRelativeTo(this, sel.anchorNode, sel.anchorOffset);
		gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::mouseClicked(I)(offset);
		gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked = true;
	};
	*/
	
	public static native void setKeyEvent(GenexGWT gn)
	/*-{
		$doc.onkeypress = function(e) {
			if (gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked) {
				var e = $wnd.event || e;
 				var keyTyped = String.fromCharCode(e.charCode);
 				var keyNum = e.charCode;
 				gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::keyPressed(Ljava/lang/String;I)(keyTyped, keyNum);
			}
		};
		
		$doc.onkeydown = function(e) {
			if (gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::dnaStrandWasClicked) {
				var e = $wnd.event || e;
 				var keyCode = e.keyCode;
 				gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::keyDown(I)(keyCode);
 				//Added to disable default backspace behavior in Chrome & Firefox
 				if (e.keyCode === 8) {
 					e.preventDefault();
 				}	
			}
		};
	}-*/;
	
	public void mouseClicked(int pos) {
		int clickSite = pos; //- headerLength;
		if ((clickSite >= 0) && (clickSite <= DNASequenceLength)) {
			VisibleGene vg = expressGene(DNA, clickSite);
			refreshDisplay(vg, clickSite);
			DNASequenceLength = vg.getGene().getDNASequenceLength();
			caretPosition = clickSite;
			headerLength = vg.getGene().getHeaderLength();
			checkAnswer(); //Automatic grading
		}  
	}
	
	public void keyPressed(String keyTyped, int keyNum) {
			if (keyTyped.equals("A")
					|| keyTyped.equals("G")
					|| keyTyped.equals("C")
					|| keyTyped.equals("T") ) {
				previousProteinString = currentProteinString;
				StringBuffer workingDNAbuffer = new StringBuffer(DNA);
				workingDNAbuffer.insert(caretPosition, keyTyped);
				DNA = workingDNAbuffer.toString();
				caretPosition++;
				VisibleGene vg = expressGene(DNA, caretPosition);
				refreshDisplay(vg, caretPosition);
				currentProteinString = vg.getGene().getProteinString();
				DNASequenceLength = vg.getGene().getDNASequenceLength();
				headerLength = vg.getGene().getHeaderLength() + 1;
				checkAnswer(); //Automatic grading
				// need the +1 otherwise, when you click on a base after moving + or -
				// the selected base is n+1 - why??            
			}

			if (keyTyped.equals("a")
					|| keyTyped.equals("g")
					|| keyTyped.equals("c")
					|| keyTyped.equals("t") ) {
				previousProteinString = currentProteinString;
				StringBuffer workingDNAbuffer = new StringBuffer(DNA);
				workingDNAbuffer.replace(caretPosition, 
						caretPosition + 1,
						keyTyped.toUpperCase());
				DNA = workingDNAbuffer.toString();
				VisibleGene vg = expressGene(DNA, caretPosition);
				refreshDisplay(vg, caretPosition);
				currentProteinString = vg.getGene().getProteinString();
				DNASequenceLength = vg.getGene().getDNASequenceLength();
				headerLength = vg.getGene().getHeaderLength() + 1;
				checkAnswer(); //Automatic grading
				// need the +1 otherwise, when you click on a base after moving + or -
				// the selected base is n+1 - why??            
			}

			if (keyTyped.equals("+") 
					|| keyTyped.equals("-")
					|| keyTyped.equals("=") || keyTyped.equals("_")) { //Added keyTyped.equals("_")
				if (keyTyped.equals("+") || keyTyped.equals("=")) {
					caretPosition++;
					if (caretPosition > (DNA.length() - 1)) {
						caretPosition = DNA.length() - 1;
					}
				} else {
					caretPosition--;
					if (caretPosition < 0) {
						caretPosition = 0;
					}
				}
				VisibleGene vg = expressGene(DNA, caretPosition);
				refreshDisplay(vg, caretPosition);
				DNASequenceLength = vg.getGene().getDNASequenceLength();
				headerLength = vg.getGene().getHeaderLength() + 1;
				checkAnswer(); //Automatic grading
				// need the +1 otherwise, when you click on a base after moving + or -
				// the selected base is n+1 - why??            
			}
			
			if (keyNum == 39) {
				caretPosition++;
				if (caretPosition > (DNA.length() - 1)) {
					caretPosition = DNA.length() - 1;
				}
				VisibleGene vg = expressGene(DNA, caretPosition);
				refreshDisplay(vg, caretPosition);
				DNASequenceLength = vg.getGene().getDNASequenceLength();
				headerLength = vg.getGene().getHeaderLength() + 1;
				checkAnswer(); //Automatic grading
				// need the +1 otherwise, when you click on a base after moving + or -
				// the selected base is n+1 - why??            

			}
			
			if (keyNum == 37){
				caretPosition--;
				if (caretPosition < 0) {
					caretPosition = 0;
				}      	
				VisibleGene vg = expressGene(DNA, caretPosition);
				refreshDisplay(vg, caretPosition);
				DNASequenceLength = vg.getGene().getDNASequenceLength();
				headerLength = vg.getGene().getHeaderLength() + 1;
				checkAnswer(); //Automatic grading
				// need the +1 otherwise, when you click on a base after moving + or -
				// the selected base is n+1 - why??            
			}
	}
	
	public void keyDown(int keyCode) {
		//Right arrow
		if (keyCode == 39) {
			caretPosition++;
			if (caretPosition > (DNA.length() - 1)) {
				caretPosition = DNA.length() - 1;
			}
			VisibleGene vg = expressGene(DNA, caretPosition);
			refreshDisplay(vg, caretPosition);
			DNASequenceLength = vg.getGene().getDNASequenceLength();
			headerLength = vg.getGene().getHeaderLength() + 1;
			checkAnswer(); //Automatic grading
			// need the +1 otherwise, when you click on a base after moving + or -
			// the selected base is n+1 - why??            
		}
		//Left arrow
		if (keyCode == 37){
			caretPosition--;
			if (caretPosition < 0) {
				caretPosition = 0;
			}      	
			VisibleGene vg = expressGene(DNA, caretPosition);
			refreshDisplay(vg, caretPosition);
			DNASequenceLength = vg.getGene().getDNASequenceLength();
			headerLength = vg.getGene().getHeaderLength() + 1;
			checkAnswer(); //Automatic grading
			// need the +1 otherwise, when you click on a base after moving + or -
			// the selected base is n+1 - why??            
		}
		//Backspace and Delete
		if (keyCode == 8 || keyCode == 46){
			previousProteinString = currentProteinString;
			StringBuffer workingDNAbuffer = new StringBuffer(DNA);
			workingDNAbuffer.deleteCharAt(caretPosition);
			DNA = workingDNAbuffer.toString();
			if (caretPosition >= 0) {
				caretPosition--;
			}
			VisibleGene vg = expressGene(DNA, caretPosition);
			refreshDisplay(vg, caretPosition);
			currentProteinString = vg.getGene().getProteinString();
			DNASequenceLength = vg.getGene().getDNASequenceLength();
			headerLength = vg.getGene().getHeaderLength();
			checkAnswer(); //Automatic grading
		}
	}
	
	public void refreshDisplay(VisibleGene vg, int selectedBase) {
		if (selectedBase != -1) {
			infoLabel.setText("Selected Base = " + selectedBase);
		}  else {
			infoLabel.setText("Selected Base = ");
		}
		
		html.setHTML(vg.getColorHTML() 
				+ "<font color=blue>" + previousProteinString 
				+ "</font></pre><br><br><br><font size=+1>" + caption 
				+ "</font></body></html>");
		workingGene = vg.theGene;
		setClickEvent(this);
		//html.setCaretPosition(0);
	}
	
	public VisibleGene expressGene(String currentDNA, int selectedDNABase) {

		// set up the gene for transcription, etc.
		Gene currentGene = new Gene(currentDNA, 
				promoterSequence, 
				promoterSpacing,
				terminatorSequence,
				intronStartSequence, 
				intronEndSequence, 
				polyATail);

		//process the gene before displaying it
		currentGene.transcribe();
		currentGene.process();
		currentGene.translate();
		//currentGene.showItAll();

		//generate the html & return it
		return new VisibleGene(currentGene.generateHTML(selectedDNABase), currentGene);
	}
	
	public void setProblemNumber(int problemNumber) {
		prob = new Problem();
		
		Requirement r1, r2, r3;
		ProteinLengthRequirement r11;
		IntronNumberRequirement r22;
		
		//For some strange reason, switch/case wasn't working here!
		if (problemNumber == 1) {
			prob.setNumber(1);
			prob.setName("Shorter mRNA");
			prob.setDescription("Start by Resetting the DNA sequence. Then, make a single base substitution so that the mature mRNA is shorter.");
			r1 = new SingleMutationRequirement();
			r1.setFailureString("You did not make a single base substitution.");
			prob.addRequirement(r1);
			r2 = new ShortermRNARequirement();
			r2.setFailureString("Your change does not make the mature mRNA shorter.");
			prob.addRequirement(r2);
		}
		else if (problemNumber == 2) {
			prob.setNumber(2);
	        prob.setName("Longer Protein");
	        prob.setDescription("Start by Resetting the DNA sequence. Then, make a single base substitution so that the protein is longer.");
	        r1 = new SingleMutationRequirement();
	        r1.setFailureString("You did not make a single base substitution.");
	        prob.addRequirement(r1);
	        r2 = new LongerProteinRequirement();
	        r2.setFailureString("Your change does not make the protein longer.");
	        prob.addRequirement(r2);
		}    
		else if (problemNumber == 3) {
			prob.setNumber(3);
	        prob.setName("Shorter Protein");
	        prob.setDescription("Start by Resetting the DNA sequence. Then, make a single base substitution so that the protein is shorter.");
	        r1 = new SingleMutationRequirement();
	        r1.setFailureString("You did not make a single base substitution.");
	        prob.addRequirement(r1);
	        r2 = new ShorterProteinRequirement();
	        r2.setFailureString("Your change does not make the protein shorter.");
	        prob.addRequirement(r2);
		}
		else if (problemNumber == 4) {
			prob.setNumber(4);
	        prob.setName("No mRNA; no Protein");
	        prob.setDescription("Start by Resetting the DNA sequence. Then, make a single base substitution so that the gene neither makes mRNA nor protein.");
	        r1 = new SingleMutationRequirement();
	        r1.setFailureString("You did not make a single base substitution.");
	        prob.addRequirement(r1);
	        r2 = new NomRNARequirement();
	        r2.setFailureString("Your change does not prevent mRNA from being made.");
	        prob.addRequirement(r2);
	        r3 = new NoProteinRequirement();
	        r3.setFailureString("Your change does not prevent protein from being made");
	        prob.addRequirement(r3);
		}    
		else if (problemNumber == 5) {
			prob.setNumber(5);
	        prob.setName("Build a Gene");
	        prob.setDescription("Use the Enter New DNA Sequence button. Then, type in DNA to make a gene that encodes a protein of 5 amino acids and has one intron.");
	        r11 = new ProteinLengthRequirement();
	        r11.setLength(15);
	        r11.setFailureString("Your protein does not have 5 amino acids.");
	        prob.addRequirement(r11);
	        r22 = new IntronNumberRequirement();
	        r22.setNumber(1);
	        r22.setFailureString("Your gene does not contain one intron.");
	        prob.addRequirement(r22);
		}  	
	}
	
	public GenexState getState() {
		if (workingGene == null) {
			return null;
		}
		return new GenexState(
				defaultDNA,
				defaultMaturemRNA,
				defaultProtein,
				caretPosition,
				DNA,
				workingGene._numberOfExons,
				workingGene._mRNASequence,
				workingGene._proteinSequence);
	}
	
	public void checkAnswer() {
		GenexState state = getState();
		if (state == null) {
			storeAnswer("INCORRECT");
			return;
		}
		String result = prob.evaluate(state);
		if (result.equals("OK")) {
			storeAnswer("CORRECT");
		} else {
			storeAnswer("INCORRECT");
		}
	}
	
	private native void exportSetDefaultDNASequence(GenexGWT gn) /*-{ 
		$wnd.genexSetDefaultDNASequence = $entry(function(str) {return gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::setDefaultDNASequence(Ljava/lang/String;)(str);});
	}-*/;
	
	private native void exportSetDNASequence(GenexGWT gn) /*-{ 
		$wnd.genexSetDNASequence = $entry(function(str) {return gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::setDNASequence(Ljava/lang/String;)(str);});
	}-*/;
	
	private native void exportSetClickEvent(GenexGWT gn) /*-{ 
		$wnd.genexSetClickEvent = $entry(function() {@edu.umb.bio.jsGenex.client.gx.GenexGWT::setClickEvent(Ledu/umb/bio/jsGenex/client/gx/GenexGWT;)(gn);});
	}-*/;
	
	private native void exportSetKeyEvent(GenexGWT gn) /*-{ 
		$wnd.genexSetKeyEvent = $entry(function() {@edu.umb.bio.jsGenex.client.gx.GenexGWT::setKeyEvent(Ledu/umb/bio/jsGenex/client/gx/GenexGWT;)(gn);});
	}-*/;
	
	private native void exportSetProblemNumber(GenexGWT gn) /*-{ 
		$wnd.genexSetProblemNumber = $entry(function(problemNumber) {return gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::setProblemNumber(I)(problemNumber);});
	}-*/;
	
	private native void exportGetDNASequence(GenexGWT gn) /*-{ 
	$wnd.genexGetDNASequence = $entry(function() {return gn.@edu.umb.bio.jsGenex.client.gx.GenexGWT::getDNASequence()();});
}-*/;
}
