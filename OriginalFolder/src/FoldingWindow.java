// FoldingWindow.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FoldingWindow extends Applet 
    implements FoldingObserver, ActionListener, ItemListener
{
    private int width;
    private int height;
    private FoldingManager manager;
    private static boolean blackColoring = false;
    
    private TextField input;
    private Button foldButton;
    private Button plotButton;
    private TextArea messageArea;

    // specifying the algorithm
    private CheckboxGroup algorithms;
    private Checkbox bruteForce;
    private Checkbox incremental;
    private TextField lookAhead;
    private TextField step;

    // specifying the data table
    private CheckboxGroup acidTable;
    private Checkbox virtual;
    private Checkbox standard;

    // specifing the creation of polypeptides
    private CheckboxGroup random;
    private Checkbox isRandom;
    private Checkbox isNotRandom;
    private TextField length;
    private TextField seed;

    // specifying whether to fold or not
    private CheckboxGroup plotted;
    private Checkbox isSolution;
    private Checkbox isNotSolution;

    // specifying the grid
    private CheckboxGroup gridType;
    private Checkbox hexagonal;
    private Checkbox square;
    private Checkbox cubic;
    private Checkbox dodecahedral;
    private CheckboxGroup breakTies;
    private Checkbox straight;
    private Checkbox bent;

    public FoldingWindow( )
    {
    }

    public FoldingWindow(int width, int height )
    {
	this.width  = width;
	this.height = height;
	init( );
    }

    /**
     * Window initialization code here rather than in constructor
     * so that it's invoked by both main and the applet manager.
     */
    public void init()
    {
	manager = FoldingManager.getInstance();
	manager.setBlackColoring( blackColoring);
	manager.attach( this);

	// Initializations
	
	input        = new TextField(60);
	foldButton   = new Button("FOLD");
	plotButton   = new Button("PLOT");
	messageArea  = new TextArea( 10, 60 );

	plotted      = new CheckboxGroup();
	isSolution   = new Checkbox(" plot    ", false, plotted);
	isNotSolution= new Checkbox(" fold    ", true,  plotted);

	random       = new CheckboxGroup();
	isRandom     = new Checkbox("generate randomly  ", false, random);
	isNotRandom  = new Checkbox("use input   "       ,  true, random);
	length       = new TextField("8",  2);
	length.setEnabled( false);
	seed         = new TextField("99", 5);
	seed.setEnabled( false);

	algorithms   = new CheckboxGroup();
	bruteForce   = new Checkbox( "brute force", false, algorithms );
	incremental  = new Checkbox( "incremental",  true, algorithms );
	lookAhead    = new TextField(""+IncrementalFolder.defaultLookAhead, 2);
	step         = new TextField(""+IncrementalFolder.defaultStep,   2);

	acidTable   = new CheckboxGroup();
	virtual     = new Checkbox( "virtual", false, acidTable );
	standard    = new Checkbox( "natural",  true, acidTable );

	gridType    = new CheckboxGroup();
	hexagonal   = new Checkbox( "hexagonal", true, gridType );
	square      = new Checkbox( "square", false, gridType );
	cubic       = new Checkbox( "cubic", false, gridType );
	dodecahedral= new Checkbox( "dodecahedral", false, gridType );
	breakTies   = new CheckboxGroup();
	straight    = new Checkbox( "straight", true, breakTies);
	bent        = new Checkbox( "bent",    false, breakTies);

	foldButton.addActionListener( this);
	plotButton.addActionListener( this);

	isSolution.addItemListener( this);
	isNotSolution.addItemListener( this);
	isRandom.addItemListener( this);
	isNotRandom.addItemListener( this);
	bruteForce.addItemListener( this);
	incremental.addItemListener( this);
	
	// input fields ( entire selection options)
	Panel inputArea   = new Panel();
	inputArea.setLayout( new GridLayout(7, 1) );

	// enter data explicitly, go
	Panel dataEntry = new Panel();
	dataEntry.setLayout(new FlowLayout(FlowLayout.LEFT));
	dataEntry.add(input);
	//dataEntry.add(foldButton);

	// action (fold, or plot previously folded chain)
	Panel actionSelector = new Panel();
	actionSelector.setLayout( new FlowLayout(FlowLayout.LEFT));
	actionSelector.add( new Label( " Action:   " ));
	actionSelector.add( isSolution);
	actionSelector.add( isNotSolution);

	// creation (input field, or random)
	Panel creationMechanism = new Panel();
	creationMechanism.setLayout( new FlowLayout( FlowLayout.LEFT ));
	//creationMechanism.setLayout( new FlowLayout());
	creationMechanism.add( new Label( "Creation: " ));
	creationMechanism.add( isNotRandom);
	creationMechanism.add( isRandom );
	creationMechanism.add( new Label("length"));
	creationMechanism.add( length );
	creationMechanism.add( new Label("seed"));
	creationMechanism.add( seed );

	// algorithm
	Panel algorithmSelector = new Panel();
	algorithmSelector.setLayout(new FlowLayout( FlowLayout.LEFT ));
	algorithmSelector.add( new Label( "Folding algorithm" ));
	algorithmSelector.add( bruteForce );
	algorithmSelector.add( incremental );
	algorithmSelector.add( new Label("lookahead"));
	algorithmSelector.add( lookAhead );
	algorithmSelector.add( new Label("step"));
	algorithmSelector.add( step );

	// AminoAcidTable
	Panel tableSelector = new Panel();
	tableSelector.setLayout(new FlowLayout(FlowLayout.LEFT));
	tableSelector.add( new Label("Amino acid table") );
	tableSelector.add( standard );
	tableSelector.add( virtual );

	// choose grid
	Panel gridSelector = new Panel();
	gridSelector.setLayout( new FlowLayout(FlowLayout.LEFT));
	gridSelector.add( new Label( "Grid: " ));
	gridSelector.add( hexagonal );
	gridSelector.add( square );
	gridSelector.add( cubic );
	gridSelector.add( dodecahedral );
	gridSelector.add( new Label( "Break ties: " ));
	gridSelector.add( straight);
	gridSelector.add( bent);
	
	// action button
	Panel actionButtonPanel = new Panel();
	actionButtonPanel.setLayout( new FlowLayout( FlowLayout.CENTER));
	actionButtonPanel.add( foldButton);

	inputArea.add( actionSelector);
	inputArea.add( creationMechanism);
	inputArea.add( dataEntry);
	
	//inputArea.add(creationMechanism);
	inputArea.add(tableSelector);
	//inputArea.add(dataEntry);
	//inputArea.add(actionSelector);
	inputArea.add(gridSelector);
	inputArea.add(algorithmSelector);
	inputArea.add( actionButtonPanel);

	this.setLayout(new BorderLayout());
	
	this.add( BorderLayout.NORTH, inputArea );

	this.add( BorderLayout.CENTER, messageArea );

	this.setSize(400, 600);
    }

    private void createPolypeptideWindow()
    {
	TextArea textInfo = new TextArea( 10, 60);
	textInfo.append( manager.getPolypeptideString()+ "\n");
	textInfo.append( manager.actionReport()+"\n\n");
	textInfo.append( " folding index: "+manager.getFoldingIndex("###.###")+"\n");
	textInfo.append( " energy:            "+manager.getEnergy("###.###")+"\n");
	textInfo.append( " time:                 "+manager.getTime()+"");
	GridCanvas canvas = manager.createCanvas();
	
	Panel content = new Panel();
	content.setLayout( new BorderLayout());
	content.add( BorderLayout.NORTH, textInfo);
	content.add( BorderLayout.SOUTH, canvas);

	String label ="Folded Polypeptide";
	Frame newWindow = new Frame(label);
	newWindow.setSize( 400, 200);
	newWindow.add( content);
	newWindow.addWindowListener( new Dispose());
	newWindow.pack();
	newWindow.show();
    }
    
    private void createErrorWindow(String ex, String stack){

	TextArea info = new TextArea(15, 60);
	info.append("ERROR:\n");
	info.append(ex + "\n");
	info.append(stack);

	Panel content = new Panel();
	content.setLayout( new BorderLayout());
	content.add( BorderLayout.NORTH, info);

	Frame errWindow = new Frame("ERROR");
	errWindow.setSize( 500, 150);
	errWindow.add( content);
	errWindow.addWindowListener( new Dispose());
	errWindow.pack();
	errWindow.show();
    }

    public void foldingDone()
    {
	//report statistics
	System.out.println( manager.report());
	messageArea.append( manager.report());

	repaint();
	createPolypeptideWindow();
    }

    public void actionPerformed( ActionEvent e)
    {
	try
	    {
		manager.fold( createAttributes());	
	    }
	catch( FoldingException ex)
	    {
		createErrorWindow(ex.toString(),ex.getMessage());
	    }
	catch( Exception ex)
	    {
		createErrorWindow(ex.toString(), ex.getMessage());
		ex.printStackTrace();
	    }
    }

    public void itemStateChanged( ItemEvent e)
    {
	String source = e.getItem().toString();
	//System.out.println("getItem returns: "+source);
	if( source.trim().equals("plot"))
	    if( e.getStateChange() == ItemEvent.SELECTED ){
		foldButton.setLabel( "PLOT");
		//System.out.println("plot selected");
		isRandom.setEnabled( false);
		//isNotRandom.setEnabled( false);
		length.setEnabled( false);
		seed.setEnabled( false);
		bruteForce.setEnabled( false);
		incremental.setEnabled( false);
		lookAhead.setEnabled( false);
		step.setEnabled( false);
		isNotRandom.setState( true);
		input.setEnabled( true);
		//algorithmSelector.setEnabled( false);
		repaint();
	    }
	    else{}
	else 
	    if( source.trim().equals("fold"))
		if( e.getStateChange() == ItemEvent.SELECTED){
		    foldButton.setLabel( "FOLD");
		    //System.out.println(" folding requested");
		    isRandom.setEnabled( true);
		    //isNotRandom.setEnabled( true);
		    length.setEnabled( true);
		    seed.setEnabled( true);
		    bruteForce.setEnabled( true);
		    incremental.setEnabled( true);
		    lookAhead.setEnabled( true);
		    step.setEnabled( true);
		    //algorithmSelector.setEnabled( true);
		    repaint();
		}
		else{}
	else 
	    if( source.trim().equals("use input"))
		if( e.getStateChange() == ItemEvent.SELECTED){
		    length.setEnabled( false);
		    seed.setEnabled( false);
		    input.setEnabled( true);
		}
		else{}
        else
	    if( source.trim().equals("generate randomly"))
		if( e.getStateChange() == ItemEvent.SELECTED){
		    length.setEnabled( true);
		    seed.setEnabled( true);
		    input.setEnabled( false);
		}
		else{}
	else
	    if( source.trim().equals("brute force"))
		if( e.getStateChange() == ItemEvent.SELECTED){
		    lookAhead.setEnabled( false);
		    step.setEnabled( false);

		}
		else{}
	else
	    if( source.trim().equals("incremental"))
		if( e.getStateChange() == ItemEvent.SELECTED){
		   lookAhead.setEnabled( true);
		   step.setEnabled( true); 

		}
		else{}
	else
	    System.out.println("Didn't go through condition!!");

    }

    private Attributes createAttributes()
    {
	Attributes attrib = new Attributes();
	attrib.setIsFolded      ( isSolution.getState());
	attrib.setIsRandom      ( isRandom.getState());
	attrib.setInputString   ( input.getText());
	attrib.setLength        ( length.getText());
	attrib.setSeed          ( seed.getText());
	attrib.setTable         ( (standard.getState() ? "standard" : "virtual" ));
	attrib.setFolder        ( (bruteForce.getState() ? "bruteforce" : "incremental" ) );
	attrib.setLookup        ( lookAhead.getText());
	attrib.setStep          ( step.getText());
	attrib.setBreakTies     ( (straight.getState() ? "straight" : "bent" ));
	if (hexagonal.getState()) {
	    attrib.setGrid("hexagonal");
	}
	else if (square.getState()) {
	    attrib.setGrid("square");
	}
	else if (cubic.getState()) { 
	    attrib.setGrid("cubic");
	}
	else {
	    attrib.setGrid("dodecahedral");	    
	}
	return attrib;
    }



    public static void main( String[] args )
    {
	String arg = null;
	try{
	    arg = args[0];         // there is an argument
	    blackColoring = true;  //  so, it means we want black coloring
	}
	catch( Exception e){
	                           // by default, no arguments mean color coloring
	}
	
	int width  = 600;
        int height = 400;
        Frame frame = new Frame();
        frame.setSize(width, height);
	FoldingWindow fw = new FoldingWindow( width, height );
        frame.add( fw );
        frame.addWindowListener( new ShutDown() );
        frame.pack();
        frame.show();
    }

    ////////////////////////////////
    //        HELPER CLASSES      //
    ////////////////////////////////

    /** A ShutDown instance handles close events generated
     * by the underlying window system with its windowClosing
     * method.
     */
    private static class ShutDown extends WindowAdapter
    {
        public void windowClosing (WindowEvent e)
        {
            System.exit(0);
        }
    }

    /**  Dispose instances handle close events generated
     *	 by the underlying window systemwith its
     *   windowClosing method
     */
    private static class Dispose extends WindowAdapter
    {
	public void windowClosing( WindowEvent e)
	{
	    (e.getWindow()).dispose();
	}
    }


}
