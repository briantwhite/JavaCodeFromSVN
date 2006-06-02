// AminoAcid.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota


/**
 * Model an AminoAcid
 */
public class AminoAcid
{
    /** 
     * The name of this AminoAcid.
     */
    protected String name;

    /**
     * The hydrophobic index tells the folding how much this AminoAcid
     * wants to be on the inside. 
     */
    protected double hydrophobicIndex;

    /**
     * The hydrophobic index normalized to have a value between +-1,
     * so suitable for use in color display.
     */
    private float normalizedHydrophobicIndex;

    public AminoAcid( String name, double hydrophobicIndex )
    {
	this.name = name;
	this.hydrophobicIndex = hydrophobicIndex;
	this.normalizedHydrophobicIndex = (float)hydrophobicIndex;
    }

    /**
     * The default name in the hydroPhobicIndex, as a String.
     */
    public AminoAcid( double hydrophobicIndex )
    {
	this( ""+hydrophobicIndex, hydrophobicIndex );
    }

    public double getHydrophobicIndex() 
    {
	return hydrophobicIndex;
    }

    public float getNormalizedHydrophobicIndex() 
    {
	return normalizedHydrophobicIndex;
    }

    public void setNormalizedHydrophobicIndex( double val ) 
    {
	normalizedHydrophobicIndex = (float)val;
    }

    public String toString()
    {
	return name;
    }

    public String getName()
    {
	return name;
    }
}
