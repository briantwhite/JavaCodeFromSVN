/**
 * Chung Ying Yu
 * cs681~3                Fall 2002- Spring 2003
 * Project VGL
 * File: SaveFrame.java
 *
 * @author Chung Ying Yu
 * $Id: SaveFrame.java,v 1.1 2004-09-24 15:46:39 brian Exp $
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 * SaveFrame is a JFrame for user to select a file to save
 */

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class SaveFrame extends JFrame {
	JFileChooser jFileChooser1 = new JFileChooser();

	/**
	 * constructor
	 */
	public SaveFrame() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * set the logo image and add jFileChooser
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("img/logo.gif"));
		this.getContentPane().add(jFileChooser1, BorderLayout.CENTER);
	}
}

