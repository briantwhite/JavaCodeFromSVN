package protex.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Event;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.canvas.client.Canvas;
import protex.client.java.awt.Dimension;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ProtexGWT implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	//Amino Acid Sequence TextBox
	TextBox psTextBox;
	int AA_LENGTH = 16; //Max # of AA allowed in text box => # chars = 4 * MAX_AA_LENGTH
	CaptionPanel pstbPanel;
	private StandardTable table = StandardTable.getInstance();
	private TripleLetterCodeDocument tlcDoc = new TripleLetterCodeDocument();
	final String allowedLetters = "ACDEFGHIKLMNPQRSTVWY";
	//Amino Acid Palette
	AminoAcidPalette aaPalette;
	CaptionPanel aapPanel;
	//Output
	FoldingManager manager;
	Attributes attributes;
	boolean ssBondsOn;
	boolean customEnergy = false;
	GridCanvas gridCanvas; //We draw on GridCanvas buffer then copy it to displayCanvas
	GridCanvas foldedGridCanvas;
	Canvas foldedCanvas;
	CaptionPanel gcPanel;
	boolean proteinFolded = false;
	boolean proteinEntered = false;
	//Button and dropdown
	HorizontalPanel buttonPanel;
	Button foldButton;
	ListBox ssBondChoice = new ListBox();
	Label progressLabel;
	//Shape matcher
	private String[] targetShapes = {"E;E;E;E;E;E;none;", "E;NE;NW;W;SW;E;none;", "E;E;E;NW;E;SE;SW;W;none",
			                        "E;E;E;NW;E;SE;SW;W;SW;SW;none;", "E;NE;NW;W;SW;W;W;W;W;W;SE;W;NW;NE;E;none;"};
	private String targetShapeString = targetShapes[0];
	GridCanvas targetGridCanvas;
	Canvas targetCanvas;
	CaptionPanel targetPanel;
	int paddingLeft = 15, paddingTop = 10;
	int width= 225;
	int height = 200;
	ScrollPanel scrollPanel;
	AbsolutePanel absolutePanel;
	
	/*
	1. Stick, E;E;E;E;E;E;none
	2. Dinner Plate, E;NE;NW;W;SW;E;none
	3. Spoon, E;E;E;NW;E;SE;SW;W;none
	4. Crab, E;E;E;NW;E;SE;SW;W;SW;SW;none
	5. Earring, E;NE;NW;W;SW;W;W;W;W;W;SE;W;NW;NE;E;none
 	6. NOT USED: Pan, E;E;E;NW;E;SE;SW;W;none
	7. NOT USED: Donut, E;NE;NW;W;SW;none
	8. NOT USED: Padlock, E;NE;NW;W;SW;SW;SE;W;NW;NE;none
	*/
	
	public void onModuleLoad() {
		
		//The main panel of the application
		absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("818px", "510px"); //818px is the maximum content size in the edX platform
		DOM.setElementAttribute(absolutePanel.getElement(), "id", "protex-panel");
		absolutePanel.setStyleName("protex-absolute-panel");
		
		//Left column
		//Amino Acid Palette, top left
		aaPalette = new AminoAcidPalette(225, 180, 4, 5, false);
		aaPalette.paint();
		aapPanel = new CaptionPanel("Amino Acids");
		aapPanel.setSize("225px", "200px");
		DOM.setElementAttribute(aapPanel.getElement(), "id", "aap-panel");
		aapPanel.setStyleName("protex-caption-panel");
		aapPanel.add(aaPalette.canvas);
		
		//Target shape, bottom right
		targetCanvas = Canvas.createIfSupported();
		targetCanvas.addStyleName("protex-canvas");
		targetPanel = new CaptionPanel("Target Shape");
		//setTargetShape(targetShapes[2]);
		targetPanel.setSize("225px", "180px");
		DOM.setElementAttribute(targetPanel.getElement(), "id", "target-panel");
		targetPanel.setStyleName("protex-caption-panel");
		targetPanel.add(targetCanvas);
		
		//Add left column
		absolutePanel.add(aapPanel, paddingLeft, paddingTop);
		absolutePanel.add(targetPanel, paddingLeft, 250);
		
		//Amino Acid Sequence TextBox with Paste disabled
		psTextBox = new TextBox() {
			@Override
			public void onBrowserEvent(Event event ) {  
		        super.onBrowserEvent(event);  
		        switch (event.getTypeInt()) {
		            case Event.ONPASTE: {
		                event.stopPropagation();  
		                event.preventDefault();  
		                break;  
		            }  
		        }  
		    }  
		};
		psTextBox.sinkEvents(Event.ONPASTE);
		
		psTextBox.setWidth(Integer.toString(818-width-93) + "px"); //Inside edX
		//psTextBox.setWidth(Integer.toString(818-width-101) + "px"); //Outside edX
		DOM.setElementAttribute(psTextBox.getElement(), "id", "ps-textbox");
		//Turn spelling check off -- otherwise red dotted line appear under Amino Acids!
		psTextBox.getElement().setAttribute("spellCheck", "false");
		psTextBox.setStyleName("protex-textbox");
		
		psTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				boolean deleteKey = (event.getNativeKeyCode() == KeyCodes.KEY_DELETE);
				boolean backspaceKey = (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE);
				boolean enterKey = (event.getNativeKeyCode() == KeyCodes.KEY_ENTER);
				
				if (enterKey) {
					foldProtein();
				}
				else if (deleteKey || backspaceKey) {
					((TextBox)event.getSource()).cancelKey();
					int offset = psTextBox.getCursorPos();
					int len = (psTextBox.getSelectedText()).length();
					if (len == 0) {
						len = 1;
					}	
					int placeInAaName = offset % 4;
					int numOfAAsSelected = ((offset + (len - 1))/4) - (offset/4) + 1;
					String aaSeq = psTextBox.getText();
	    			StringBuffer sb = new StringBuffer(aaSeq);
	    			int newOffset = offset - placeInAaName;
	    			
	    			if (backspaceKey && numOfAAsSelected == 1 && placeInAaName == 0 && offset != 0) {
	    				sb.delete(newOffset - 4 * numOfAAsSelected, newOffset);
						psTextBox.setText(sb.toString());
						psTextBox.setCursorPos(newOffset - 4 * numOfAAsSelected);
	    			}
	    			else {
	    				sb.delete(newOffset, newOffset + 4 * numOfAAsSelected);
						psTextBox.setText(sb.toString());
						psTextBox.setCursorPos(newOffset);
	    			}
	    			aaSeqChanged();
	    		}	  
			}
		});
		
		psTextBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				//First, check out if we have reached max allowed length
				if (psTextBox.getText().length() == 4 * AA_LENGTH && !(event.getCharCode() == 0))
					((TextBox)event.getSource()).cancelKey();
				else if (!(Character.isLetter(event.getCharCode()))) {
					if (!(event.getCharCode() == 0))
						((TextBox)event.getSource()).cancelKey();
				}
				//We have a letter
				else {
					//Get the character and check if it is allowed
					Character character = Character.valueOf(event.getCharCode());
					character = Character.toUpperCase(character);
					String str = character.toString();
					((TextBox)event.getSource()).cancelKey();
					if ((allowedLetters.indexOf(str) != -1)) {
						int offset = psTextBox.getCursorPos();
						int newOffset;
						if ((offset % 4) > 1) {
							newOffset = offset - (offset % 4) + 4;
	  				   	} 
						else {
							newOffset = offset - (offset % 4);
	  				  	}
	    			  
						String name = table.getFromAbName(str).getName();
						name += " "; //Append a space
						String aaSeq = psTextBox.getText();
						StringBuffer sb = new StringBuffer(aaSeq);
						sb.insert(newOffset, name);
						psTextBox.setText(sb.toString());
						psTextBox.setCursorPos(newOffset + 4);
					}
					aaSeqChanged();
				}	  
			}
	    });

		pstbPanel = new CaptionPanel("Amino Acid Sequence");
		pstbPanel.add(psTextBox);
		DOM.setElementAttribute(pstbPanel.getElement(), "id", "pstb-panel");
		pstbPanel.setStyleName("protex-caption-panel");
		absolutePanel.add(pstbPanel, paddingLeft + width + 40, paddingTop);
		
		//Folding Manager
		manager = FoldingManager.getInstance();
		
		foldedCanvas = Canvas.createIfSupported();
		foldedCanvas.addStyleName("protex-canvas");
		scrollPanel = new ScrollPanel(foldedCanvas);
		scrollPanel.setSize(Integer.toString(818-width-93) + "px", "306px"); //Inside edX
		//scrollPanel.setSize(Integer.toString(818-width-93) + "px", "310px"); //Outside edX
		gcPanel = new CaptionPanel("Folded Protein");
		gcPanel.add(scrollPanel);
		DOM.setElementAttribute(gcPanel.getElement(), "id", "gc-panel");
		gcPanel.setStyleName("protex-caption-panel");
		absolutePanel.add(gcPanel, paddingLeft + width + 40, 100);
		
		//Button and dropdown
		buttonPanel = new HorizontalPanel();
		foldButton = new Button("Fold");
		foldButton.addStyleName("protex-button");
		
		foldButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				foldProtein();
			}
		});
		DOM.setElementAttribute(foldButton.getElement(), "id", "fold-button");
		buttonPanel.add(foldButton);
		
		ssBondChoice = new ListBox();
		ssBondChoice.addItem("Disulfide Bonds OFF");
		ssBondChoice.addItem("Disulfide Bonds ON");
		//Setting this to 1 turns it into a drop-down list
		ssBondChoice.setVisibleItemCount(1);
		ssBondChoice.addChangeHandler(new ChangeHandler(){
		    public void onChange(ChangeEvent event){
		    	aaSeqChanged();
		    }
		});
		DOM.setElementAttribute(ssBondChoice.getElement(), "id", "ssbond-choice");
		ssBondChoice.addStyleName("protex-listbox");
		buttonPanel.add(ssBondChoice);
		
		progressLabel = new Label("");
		DOM.setElementAttribute(progressLabel.getElement(), "id", "progress-label");
		progressLabel.addStyleName("protex-label");
		buttonPanel.add(progressLabel);
		
		absolutePanel.add(buttonPanel, paddingLeft + width + 40, 465);
		
		RootPanel.get("protex_container").add(absolutePanel);
		
		//Export the GWT Java functions that will be accessible from outside JavaScript
		exportSetTargetShape(this);
		exportCheckAnswer(this);
		
		//Using a deferred command ensures that notifyHostpage() is called after
		//GWT initialization is finished.
		Scheduler.get().scheduleDeferred(new Command() { 
			public void execute() {
		      notifyHostpage();
		    }
		});
	}
	
	public static native void alert(String msg)
	/*-{
		$wnd.alert(msg);
	}-*/;
	
	public static native void notifyHostpage() /*-{
		if (typeof $wnd.protexIsReady === 'function')
			$wnd.protexIsReady();
	}-*/;
	
	public void aaSeqChanged() {
		foldButton.setEnabled(true);
		proteinEntered = true;
		proteinFolded = false;	
	}
	
	public void progress(int value) {
		progressLabel.setText(Integer.toString(value) + "/5 direction done.");
	}
	
	private void foldProtein() {
		foldButton.setEnabled(false);
		String ssBondIndex = "";
		if (ssBondChoice.getSelectedIndex() == 0) {
			ssBondsOn = false;
			ssBondIndex = "0.0";
		} else {
			ssBondsOn = true;
			ssBondIndex = "1.5";
		}
		
		// see if using regular energy calculation
		// or custom (with ContactEnergies.txt file)
		/*boolean customEnergy = false;
		if (protex.custom != null) {
			if (protex.custom.isSelected()) {
				customEnergy = true;
			}
		}*/
		customEnergy = false;
		
		if (!psTextBox.getText().isEmpty()) {
			proteinEntered = true;
		}
		else {
			proteinEntered = false;
		}

		if (proteinEntered) {
			
			attributes = new Attributes(psTextBox.getText().trim(), customEnergy,
									3, ssBondIndex, "straight", "test");

			//fold the polypeptide
			manager.protexGWT = this;
			try {
				manager.fold(attributes);
				proteinFolded = true;
			} catch (FoldingException e) {
				e.printStackTrace();
			}
		
			//display it
			/*foldedGridCanvas = manager.createCanvas();
			Dimension d = foldedGridCanvas.getRequiredCanvasSize();
			int width = d.width;
			int height = d.height;
			foldedGridCanvas.setCanvas(width, height);
			foldedGridCanvas.ssBondsOn = getssBondsOn(); //Change later on
			foldedGridCanvas.paint();
			//Now copy the content to displayPanel
			foldedCanvas.setPixelSize(width, height);
			foldedCanvas.setCoordinateSpaceWidth(width);
			foldedCanvas.setCoordinateSpaceHeight(height);
			foldedCanvas.getContext2d().clearRect(0, 0, width, height);
			foldedCanvas.getContext2d().drawImage(foldedGridCanvas.canvas.getCanvasElement(), 0, 0);
			if (foldedGridCanvas.ssBondsOn) {
				gcPanel.setStyleName("protex-caption-panel-ss-bonds-on");
			}
			else {
				gcPanel.setStyleName("protex-caption-panel");
			}
			foldButton.setEnabled(false);*/
		}
		else {
			foldedCanvas.setPixelSize(width, height);
			foldedCanvas.setCoordinateSpaceWidth(width);
			foldedCanvas.setCoordinateSpaceHeight(height);
			foldedCanvas.getContext2d().clearRect(0, 0, width, height);
		}
	}
	
	public void drawProtein() {
		progressLabel.setText("");
		//display it
		foldedGridCanvas = manager.createCanvas();
		Dimension d = foldedGridCanvas.getRequiredCanvasSize();
		int width = d.width;
		int height = d.height;
		foldedGridCanvas.setCanvas(width, height);
		foldedGridCanvas.ssBondsOn = getssBondsOn(); //Change later on
		foldedGridCanvas.paint();
		//Now copy the content to displayPanel
		foldedCanvas.setPixelSize(width, height);
		foldedCanvas.setCoordinateSpaceWidth(width);
		foldedCanvas.setCoordinateSpaceHeight(height);
		foldedCanvas.getContext2d().clearRect(0, 0, width, height);
		foldedCanvas.getContext2d().drawImage(foldedGridCanvas.canvas.getCanvasElement(), 0, 0);
		if (foldedGridCanvas.ssBondsOn) {
			gcPanel.setStyleName("protex-caption-panel-ss-bonds-on");
		}
		else {
			gcPanel.setStyleName("protex-caption-panel");
		}
		foldButton.setEnabled(false);
		proteinIsFolded();
	}
	
	public boolean getssBondsOn() {
		return ssBondsOn;
	}
	
	public void setssBondsOn(boolean b) {
		ssBondsOn = b;
	}
	
    //The following makes static and non-static methods visible to JavaScript outside of GWT,
	//by attaching them to the global scope
	
	//Method which declares static method in JavaScript
	//private static native void exportCheckAnswer() /*-{
    	//$wnd.checkAnswer = @protex.client.ProtexGWT::checkAnswer();
    //}-*/;
	
	//Method which declares non-static method in JavaScript
    private native void exportCheckAnswer(ProtexGWT pr) /*-{
		$wnd.protexCheckAnswer = $entry(function() {return pr.@protex.client.ProtexGWT::checkAnswer()();});
    }-*/;
    
    public static native void proteinIsFolded() /*-{
	if (typeof $wnd.protexProteinIsFolded === 'function')
		$wnd.protexProteinIsFolded();
	}-*/;
 
	private String checkAnswer() {
		//Check for errors
		if ((targetShapeString == null) || (targetShapeString.equals(""))) {
			return "ERROR: No target shape specified.";
		}
		if (!proteinEntered) {
			return "ERROR: There is no folded protein to check.";
		}
		if (!proteinFolded) {
			return "ERROR: The protein sequence you typed in has not been folded. Click the FOLD button and re-submit.";
		}
		//OK to score
		ShapeMatcher shapeMatcher = new ShapeMatcher(targetShapeString, false);
		if (shapeMatcher.matchesTarget(foldedGridCanvas.getGrid().getPP().getDirectionSequence())) {
			return "CORRECT";
		} else {
			return "INCORRECT";
		}
	}
	
	//http://refcardz.dzone.com/refcardz/html/gwt-style-configuration-and/gwt-style-configuration-and-2
	private native void exportSetTargetShape(ProtexGWT pr) /*-{
		$wnd.protexSetTargetShape = $entry(function(str) {return pr.@protex.client.ProtexGWT::setTargetShape(Ljava/lang/String;)(str);});
	}-*/;
		
	private void setTargetShape(String str) {
		targetShapeString = str; //Any checking that the string is valid?
		targetGridCanvas = ProteinImageFactory.buildProtein(targetShapeString, false);
		targetGridCanvas.setssBondsOn(ssBondsOn);
		targetGridCanvas.setBuildTargetShapeMode(true);
		targetGridCanvas.paint();
		//Scale everything by 0.5
		int canWidth = targetGridCanvas.getWidth()/2;
		int canHeight = targetGridCanvas.getHeight()/2;
		targetCanvas.setPixelSize(canWidth, canHeight);
		targetCanvas.setCoordinateSpaceWidth(canWidth);
		targetCanvas.setCoordinateSpaceHeight(canHeight);
		//Now copy the content to displayPanel
		targetCanvas.getContext2d().clearRect(0, 0, width, height);
		//Draw a scaled version of the image
		targetCanvas.getContext2d().drawImage(targetGridCanvas.canvas.getCanvasElement(), 0, 0, canWidth, canHeight);
	};
}