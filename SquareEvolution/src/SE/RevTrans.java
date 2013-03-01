package SE;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class RevTrans {

	/**
	 * @param args
	 * arg0 = filename of genetic code file
	 * arg1 = sequence to reverse translate
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			GeneticCode geneticCode = new GeneticCode();
			if (geneticCode != null) {
				System.out.println(geneticCode.reverseTranslate(args[0]));
			}			
		} else {
			GeneticCode geneticCode = new GeneticCode(args[0]);
			if (geneticCode != null) {
				System.out.println(geneticCode.reverseTranslate(args[1]));
			}
		}
	}

}
