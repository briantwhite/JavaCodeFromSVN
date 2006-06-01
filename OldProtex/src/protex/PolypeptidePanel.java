// PolypeptidePanel.java
//
//
// Copyright 2004-2005 MGX Team UMB.  All rights reserved.
/* 
 * License Information
 * 
 * This  program  is  free  software;  you can redistribute it and/or modify it 
 * under  the  terms  of  the GNU General Public License  as  published  by the 
 * Free  Software  Foundation; either version 2  of  the  License,  or (at your 
 * option) any later version.
 */
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
 *     public class AClass {
 *         (public constants)
 *         (public constructors)
 *         (public accessors)
 *         (public mutators)
 *         (nonpublic fields)
 *         (nonpublic auxiliary methods or nested classes)
 *     }
 *
 * Jia also recommends the following design guidelines.
 *
 *     1. Avoid public fields.  There should be no nonfinal public fields, 
 *        except when a class is final and the field is unconstrained.
 *     2. Ensure completeness of the public interface.  The set of public 
 *        methods defined in the class should provide full and convenient 
 *        access to the functionality of the class.
 *     3. Separate interface from implementation.  When the functionality 
 *        supported by a class can be implemented in different ways, it is 
 *        advisable to separate the interface from the implementation.
 * 
 * Created:  25 Feb 2005
 * Modified: 25 Apr 2005 (David Portman/MGX Team UMB)
 */

package protex;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * Holds thumbnail image and identifier (e.g., PP001) associated with one
 * polypeptide chain. For use by JList cells of the HistoryCellRenderer class.
 * 
 * @author David Portman/MGX Team UMB
 */
public class PolypeptidePanel extends JPanel {

	/**
	 * Constructor.
	 *  
	 */
	public PolypeptidePanel(BufferedImage im, ImageIcon ic, Polypeptide pp) {
		fullsize = im;
		thumbnail = ic;
		polypeptide = pp;
	}

	// public accessors

	/**
	 * 
	 * @return BufferedImage Full-size image.
	 */
	public BufferedImage getFullsize() {
		return fullsize;
	}

	/**
	 * 
	 * @return ImageIcon Thumbnail image.
	 */
	public ImageIcon getThumbnail() {
		return thumbnail;
	}

	/**
	 * 
	 * @return Polypepetide.
	 */
	public Polypeptide getPolypeptide() {
		return polypeptide;
	}

	// non-public fields

	private BufferedImage fullsize;

	private ImageIcon thumbnail;

	private Polypeptide polypeptide;
}
