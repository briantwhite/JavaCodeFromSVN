// FoldingObserver.java
//
//
// Copyright 2004, Ethan Bolker and Bogdan Calota
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
/*
 * The following organization of a public class is recommended by X. Jia [2004:
 * Object Oriented Software Development Using Java(TM). Addison Wesley, Boston,
 * 677 pp.]
 * 
 * public class AClass { (public constants) (public constructors) (public
 * accessors) (public mutators) (nonpublic fields) (nonpublic auxiliary methods
 * or nested classes) }
 * 
 * Jia also recommends the following design guidelines.
 * 
 * 1. Avoid public fields. There should be no nonfinal public fields, except
 * when a class is final and the field is unconstrained. 2. Ensure completeness
 * of the public interface. The set of public methods defined in the class
 * should provide full and convenient access to the functionality of the class.
 * 3. Separate interface from implementation. When the functionality supported
 * by a class can be implemented in different ways, it is advisable to separate
 * the interface from the implementation.
 * 
 * Modified: 08 Mar 2005 (D. A. Portman/MGX Team UMB)
 */

package protex;

public interface FoldingObserver {

	public void doneFolding(Attributes a);

}
