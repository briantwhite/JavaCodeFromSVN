// Attributes.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


/**
 *  Class Attributes contains the attributes
 *    needed to fold or plot a polypeptide.
 *  
 *  Primary Usage: transfering information between
 *            Observers and FoldingManager
 *
 *  Downfall : Attributes is mutable. 
 *      Fix  : - a constractor taking 11 parameters, much harder to work with.
 *             - implementing Cloneable.
 */
public class Attributes
{

    private boolean isFolded     = false;
    private boolean isRandom     = false;
    private String  inputString  = "";
    private String  length       = "";
    private String  seed         = "";
    private String  table        = "";
    private String  grid         = "";
    private String  breakTies    = "";
    private String  folder       = "";
    private String  lookup       = "";
    private String  step         = "";

    public Attributes()
    {	
    }

    //////////////////
    //    SETTERS   //
    //////////////////
    public void setIsFolded( boolean b)
    {
	isFolded = b;
    }
    public void setIsRandom( boolean b)
    {
	isRandom = b;
    }
    public void setInputString( String s)
    {
	inputString = s;
    }
    public void setLength( String s)
    {
	length = s;
    }
    public void setSeed( String s)
    {
	seed = s;
    }
    public void setTable( String s)
    {
	table = s;
    }
    public void setGrid( String s)
    {
	grid = s;
    }
    public void setBreakTies( String s)
    {
	breakTies = s;
    }
    public void setFolder( String s)
    {
	folder = s;
    }
    public void setLookup( String s)
    {
	lookup = s;
    }
    public void setStep( String s)
    {
	step = s;
    }

    ////////////////////
    //     GETTERS    //
    ////////////////////
    public boolean getIsFolded()
    {
	return isFolded;
    }
    public boolean getIsRandom()
    {
	return isRandom;
    }
    public String getInputString()
    {
	return inputString;
    }
    public String getLength()
    {
	return length;
    }
    public String getSeed()
    {
	return seed;
    }
    public String getTable()
    {
	return table;
    }
    public String getGrid()
    {
	return grid;
    }
    public String getBreakTies()
    {
	return breakTies;
    }
    public String getFolder()
    {
	return folder;
    }
    public String getLookup()
    {
	return lookup;
    }
    public String getStep()
    {
	return step;
    }
}

