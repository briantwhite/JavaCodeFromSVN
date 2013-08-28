package edu.umb.jsPedigrees.client.PE;

public class Genetics {
	
	/*
	 * string arrays for possible genotypes for pedigree solver
	 * eg SLR_M_A = sex-linked recessive; male; affected
	 */
	public static String[][] AR_A = {{"a", "a"}};
	public static String[][] AR_U = {{"A", "A"}, {"A", "a"}};
	
	public static String[][] AD_A = {{"B", "B"}, {"B", "b"}};
	public static String[][] AD_U = {{"b", "b"}};
	
	public static String[][] SLR_M_A = {{"Xd", "Y"}};
	public static String[][] SLR_F_A = {{"Xd", "Xd"}};
	public static String[][] SLR_M_U = {{"XD", "Y"}};
	public static String[][] SLR_F_U = {{"XD", "XD"}, {"XD", "Xd"}};
	
	public static String[][] SLD_M_A = {{"XE", "Y"}};
	public static String[][] SLD_F_A = {{"XE", "XE"}, {"XE", "Xe"}};
	public static String[][] SLD_M_U = {{"Xe", "Y"}};
	public static String[][] SLD_F_U = {{"Xe", "Xe"}};

}
