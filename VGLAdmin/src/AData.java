/**
 * Chung Ying Yu
 * cs681~3                Fall 2002- Spring 2003
 * Project VGL
 * File: Data.java
 *
 * @author Chung Ying Yu
 * $Id: AData.java,v 1.1 2004-09-24 15:46:39 brian Exp $
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
 * The AData class is used for save the setting values
 */

public class AData {
	String dominance;

	String sexlinked;

	String chance;

	String practicemode;

	/**
	 * constructor
	 */
	public AData() {
	}

	/**
	 * set the dominance value
	 * 
	 * @param d
	 *            String
	 */
	public void setDominance(String d) {
		dominance = d;
	}

	/**
	 * set the sexlinked value
	 * 
	 * @param s
	 *            String
	 */
	public void setSexlinked(String s) {
		sexlinked = s;
	}

	/**
	 * set the Chance value
	 * 
	 * @param c
	 *            String
	 */
	public void setChance(String c) {
		chance = c;
	}

	/**
	 * set the Practice value
	 * 
	 * @param p
	 *            String
	 */
	public void setPracticemode(String p) {
		practicemode = p;
	}

	/**
	 * get the Dominance value
	 * 
	 * @return dominance
	 */
	public String getDominance() {
		return dominance;
	}

	/**
	 * get the Sexlinked value
	 * 
	 * @return Sexlinked
	 */
	public String getSexlinked() {
		return sexlinked;
	}

	/**
	 * get the Chance value
	 * 
	 * @return Chance
	 */
	public String getChance() {
		return chance;
	}

	/**
	 * get the Practicemode value
	 * 
	 * @return Practicemode
	 */
	public String getPracticemode() {
		return practicemode;
	}
}