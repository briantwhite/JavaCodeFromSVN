/**
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File: XmlFilter.java
 * 
 * @author Chung Ying Yu $Id: XmlFilter.java,v 1.1 2004-09-24 15:46:39 brian Exp $
 *         This program is free software; you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation; either version 2 of the License, or (at
 *         your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFilter extends FileFilter {
	/**
	 * Accept all directories and prb file.
	 * 
	 * @param f
	 * @return true if file is directory or prb file, otherwiae return false
	 */
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.prb))
				return true;
			else
				return false;
		}
		return false;
	}

	/**
	 * The description of this filter
	 * 
	 * @return description
	 */
	public String getDescription() {
		return "prb files(*.prb)";
	}
}