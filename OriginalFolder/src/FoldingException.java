// FoldingException.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota



/**
* Parent Exception for the folding package.
*/
public class FoldingException extends Exception
{
    public FoldingException( String message )
    {
	super( message );
    }
}

class InputFormatFoldingException 
    extends FoldingException
{
    public InputFormatFoldingException(String s)
    {
	super( s);
    }
}

class IntegerFormatFoldingException 
    extends InputFormatFoldingException
{
    public IntegerFormatFoldingException(String s)
    {
	super( s);
    }
}
