package SE;
import java.util.ArrayList;

/*
 * shows a protein and ligand structure 
 * if no ligand given, then it just shows protein
 */
public class DisplayStructure {


	/**
	 * @param args
	 * arg0 = protein sequence
	 * arg1 = protein structure
	 * arg2 = ligand sequence
	 * arg3 = ligand structure
	 * arg4 = ligand rotamer
	 * arg5 = ligand x
	 * arg6 = ligand y
	 */
	public static void main(String[] args) {
		DisplayStructure ds = new DisplayStructure();
		if (args.length == 2) {
			System.out.println(ds.getStructure(
					args[0],
					args[1],
					"",
					"",
					0,
					0,
					0));
		}

		if (args.length == 7) {
			System.out.println(ds.getStructure(
					args[0],
					args[1],
					args[2],
					args[3],
					Integer.parseInt(args[4]),
					Integer.parseInt(args[5]),
					Integer.parseInt(args[6])));
		}
	}


	public static String getStructure(String protSeq,
			String protStr,
			String ligSeq,
			String ligStr,
			int ligandRotamer,
			int ligX,
			int ligY) {		
		
		if (protStr.equals("None") || protSeq.equals("")) return "No structure";
		
		/*
		 *  if we're binding the protein to itself, 
		 *    we do this by setting the ligand seq to "*"
		 *    and selecting the deisred rotamer
		 *  so, if ligand seq = "*"
		 *    then we should use the protein as the ligand
		 */
		if (ligSeq.equals("*")) {
			ligSeq = protSeq;
			ligStr = protStr;
		}
		
		Structure structure = new Structure();
		
		// figure out where protein goes
		int x = 0;
		int y = 0;
		structure.addSymbol(new Symbol(x, y, protSeq.substring(0, 1)));
		for (int i = 0; i < protStr.length(); i++) {
			String dir = protStr.substring(i, i + 1);
			DirectionVector dv = new DirectionVector(dir);
			x = x + dv.dX;
			y = y + dv.dY;
			structure.addSymbol(new Symbol(x, y, getConnector(dir)));
			x = x + dv.dX;
			y = y + dv.dY;
			structure.addSymbol(new Symbol(x, y, protSeq.substring(i + 1, i + 2)));
		}

		if (!ligSeq.equals("")) {
			// figure out where ligand goes
			x = ligX * 2; // 2x because each element takes 2 spaces (aa & connector)
			y = ligY * 2;
			structure.addSymbol(new Symbol(x, y, ligSeq.substring(0, 1).toLowerCase()));
			for (int i = 0; i < ligStr.length(); i++) {
				String dir = ligStr.substring(i, i + 1);
				DirectionVector dv = new DirectionVector(dir,ligandRotamer);
				x = x + dv.dX;
				y = y + dv.dY;
				structure.addSymbol(
						new Symbol(x, y, getConnector(
								DirectionVector.rotateCW(dir, ligandRotamer))));
				x = x + dv.dX;
				y = y + dv.dY;
				structure.addSymbol(
						new Symbol(x, y, ligSeq.substring(i + 1, i + 2).toLowerCase()));			
			}
		}
		// lay it out for printing
		String[][] canvas = new String[structure.maxX - structure.minX + 1]
		                               [structure.maxY - structure.minY + 1];
		ArrayList<Symbol> symbols = structure.getSymbols();
		for (int i = 0; i < symbols.size(); i++) {
			Symbol s = symbols.get(i);
			canvas[s.x - structure.minX][s.y - structure.minY] = s.s;
		}

		//print it
		StringBuffer b = new StringBuffer();
		for (int j = (structure.maxY - structure.minY); j > -1 ; j--) {
			for (int i = 0; i < (structure.maxX - structure.minX + 1); i++) {
				if (canvas[i][j] == null) {
					b.append(" ");
				} else {
					b.append(canvas[i][j]);
				}
			}
			b.append("\n");
		}
		return b.toString();
	}

	private static String getConnector(String direction) {
		if (direction.equals("U") || direction.equals("D")) return "|";
		if (direction.equals("L") || direction.equals("R")) return "-";
		return "";
	}

}
