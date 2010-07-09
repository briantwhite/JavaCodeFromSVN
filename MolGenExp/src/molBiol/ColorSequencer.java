package molBiol;
/*
 * Created on Dec 12, 2004
 *
 * cycle thru the three exon colors
 */

/**
 * @author Brian.White
 *
 * 
 */
public class ColorSequencer {

	int exonColor;
	
	public ColorSequencer() {
		exonColor = 1;
	}
	
	//cycle thru the three possible exon colors
    public String getNextColor() {
        switch (exonColor) {
            case 0:
            	exonColor++;
            case 1:
                exonColor++;
                return "exon";
            case 2:
                exonColor++;
                return "next";
            case 3:
                exonColor = 1;
                return "another";
            }
        return "";
    }

}
