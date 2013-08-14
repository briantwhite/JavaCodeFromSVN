package edu.umb.jsPedigrees.client.PE;

public class ModelDescriptionHTML {
	
	private static String HEAD = "<html>";
	private static String TABLE_TOP =  "<table border=1>"
			+ "<tr><th>Genotype</th><th>Phenotype</th></tr>";
	
	private static String TAIL = "</table></html>";
	
	public static String[] HTML = {
			HEAD 
			+ "Autosomal Recessive<br>"
			+ TABLE_TOP
			+ "<tr><td>A A</td><td>Normal</td></tr>"
			+ "<tr><td>A a</td><td>Normal</td></tr>"
			+ "<tr><td>a a</td><td>Affected</td></tr>"
			+ TAIL,
			
			HEAD 
			+ "Autosomal Dominant<br>"
			+ TABLE_TOP
			+ "<tr><td>B B</td><td>Affected</td></tr>"
			+ "<tr><td>B b</td><td>Affected</td></tr>"
			+ "<tr><td>b b</td><td>Normal</td></tr>"
			+ TAIL,

			HEAD 
			+ "Sex-Linked Recessive<br>"
			+ TABLE_TOP
			+ "<tr><td>XD XD</td><td>Normal Female</td></tr>"
			+ "<tr><td>XD Xd</td><td>Normal Female</td></tr>"
			+ "<tr><td>Xd Xd</td><td>Affected Female</td></tr>"
			+ "<tr><td>XD Y</td><td>Normal Male</td></tr>"
			+ "<tr><td>Xd Y</td><td>Affected Male</td></tr>"
			+ TAIL,

			HEAD 
			+ "Sex-Linked Dominant<br>"
			+ TABLE_TOP
			+ "<tr><td>XE XE</td><td>Affected Female</td></tr>"
			+ "<tr><td>XE Xe</td><td>Affected Female</td></tr>"
			+ "<tr><td>Xe Xe</td><td>Normal Female</td></tr>"
			+ "<tr><td>XE Y</td><td>Affected Male</td></tr>"
			+ "<tr><td>Xe Y</td><td>Normal Male</td></tr>"
			+ TAIL};
}
