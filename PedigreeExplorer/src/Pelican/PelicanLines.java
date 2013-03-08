package Pelican;
/********************************************************************
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA  02111-1307, USA.
 *
 *  @author Copyright (C) Frank Dudbridge
 *
 ********************************************************************/

//package uk.ac.mrc.rfcgr;

import javax.swing.*;

import PE.PedigreeExplorer;

import java.awt.*;
import java.util.*;

public class PelicanLines extends JPanel
{
	private Container panel;
	private boolean showId;
	private boolean showName;
	private boolean displayGenotypes;

	/* {{{ constructor */

	public PelicanLines(Container panel,
			boolean showId,
			boolean showName,
			boolean displayGenotypes) {
		super();
		setBackground(Color.white);
		setOpaque(false);
		setLocation(0,0);
		this.panel=panel;
		this.showId=showId;
		this.showName=showName;
		this.displayGenotypes = displayGenotypes;
		setPreferredSize(panel.getPreferredSize());
	}

	/* }}} */

	/* {{{ adjacent */

	private boolean adjacent(PelicanPerson father,PelicanPerson mother) {
		if (Math.abs(father.getY()-mother.getY()) >= PelicanPerson.ySize)
			return(true);
		for(int i=0;i<panel.getComponentCount();i++)
			if (panel.getComponent(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)panel.getComponent(i);
				if (!(person.id == father.id) &&
						!(person.id == mother.id) &&
						person.getY()>=Math.min(father.getY(),mother.getY()) &&
						person.getY()<=Math.max(father.getY(),mother.getY()) &&
						person.getX()>=Math.min(father.getX(),mother.getX()) &&
						person.getX()<=Math.max(father.getX(),mother.getX()))
					return(false);
			}
		return(true);
	}

	/* }}} */

	/* {{{ areSibs */

	private boolean areSibs(PelicanPerson person1,PelicanPerson person2) {
		if (person1.father==person2.father && person1.mother==person2.mother)
			return(true);
		return(false);
	}

	/* }}} */

	/* {{{ paintComponent: draw the lines */

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);

		setPreferredSize(panel.getPreferredSize());
		g2.setColor(Color.black);
		
		if (PedigreeExplorer.thickLines) {
			// turn this off so they don't look blurry when exported
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		} else {
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}
		
		if (PedigreeExplorer.thickLines) {
			g2.setStroke(new BasicStroke(2)); 
		} else {
			g2.setStroke(new BasicStroke(1)); 
		}

		int fontHeight=g2.getFontMetrics().getHeight();
		int fontAscent=g2.getFontMetrics().getAscent();
		int dropSize=Math.max(2,Math.min(PelicanPerson.symbolSize/2,3*(PelicanPerson.ySpace-PelicanPerson.symbolSize-fontHeight)/4));
		for(int i=0;i<panel.getComponentCount();i++)
			if (panel.getComponent(i) instanceof PelicanPerson) {
				// draw a line from this person to its parents
				PelicanPerson person=(PelicanPerson)panel.getComponent(i);
				if (person.father!=null && person.mother!=null) {
					//System.out.println("HERE "+String.valueOf(i));
					// find the mother and father
					PelicanPerson father=person.father;
					PelicanPerson mother=person.mother;
					if (father!=null && mother!=null) {
						// line between parents
						int fatherX=father.getX()+(father.getX()<mother.getX()?PelicanPerson.symbolSize:0);
						int motherX=mother.getX()+(mother.getX()<father.getX()?PelicanPerson.symbolSize:0);
						int fatherY=father.getY()+PelicanPerson.symbolSize/2;
						int motherY=mother.getY()+PelicanPerson.symbolSize/2;
						int leftX=fatherX;
						int leftY=fatherY;
						int rightX=motherX;
						int rightY=motherY;
						if (motherX<fatherX) {
							leftX=motherX;
							leftY=motherY;
							rightX=fatherX;
							rightY=fatherY;
						}
						int gap=PelicanPerson.xSpace-PelicanPerson.symbolSize;
						// see if any subjects lie between the father and mother
						if (!adjacent(father,mother) && father.generation==mother.generation) {
							// draw lines which avoid other symbols
							g2.drawLine(leftX,leftY,leftX+gap/4,leftY);
							g2.drawLine(rightX,rightY,rightX-gap/2,rightY);
							leftX+=gap/4;
							rightX-=gap/2;
							g2.drawLine(leftX,leftY,leftX,leftY-(PelicanPerson.symbolSize+dropSize)/2);
							g2.drawLine(rightX,rightY,rightX,rightY-(PelicanPerson.symbolSize+dropSize)/2);
							leftY-=(PelicanPerson.symbolSize+dropSize)/2;
							rightY-=(PelicanPerson.symbolSize+dropSize)/2;
						}
						g2.drawLine(leftX,leftY,rightX,rightY);
						// line up from child
						g2.drawLine(person.getX()+PelicanPerson.symbolSize/2,person.getY(),person.getX()+PelicanPerson.symbolSize/2,person.getY()-dropSize);
						// line across from child
						// try to attach to an orphan parent
						int parentX=fatherX;
						if (father.isOrphan() || mother.isOrphan()) {
							parentX=Math.max(fatherX,motherX)-gap/2;
						}
						else {
							// if no orphan parents, go straight up from
							// middle laid out sib
							int nsib=0;
							for(int j=0;j<panel.getComponentCount();j++)
								if (panel.getComponent(j) instanceof PelicanPerson) {
									PelicanPerson sib=(PelicanPerson)panel.getComponent(j);
									if (areSibs(person,sib)) nsib++;
								}
							int sibs=0;
							for(int j=0;j<panel.getComponentCount() && sibs<=nsib/2;j++)
								if (panel.getComponent(j) instanceof PelicanPerson) {
									PelicanPerson sib=(PelicanPerson)panel.getComponent(j);
									if (areSibs(person,sib)) sibs++;
									parentX=sib.getX()+PelicanPerson.symbolSize/2;
								}
							if (nsib>1 && nsib%2==0)
								parentX-=PelicanPerson.xSpace/2;
							if (parentX<=leftX)
								parentX=leftX+PelicanPerson.symbolSize/2;
							if (parentX>=rightX)
								parentX=rightX-PelicanPerson.symbolSize/2;
						}

						g2.drawLine(person.getX()+PelicanPerson.symbolSize/2,person.getY()-dropSize,parentX,person.getY()-dropSize);
						// line up to parents
						// Draw a vertical line up to the line joining the parents
						// if this happens to be not between the parents,
						// change it to a line to the midpoint between the parents
						int parentY=(rightX!=leftX)?
								leftY+(rightY-leftY)*(parentX-leftX)/(rightX-leftX):
									(leftY+rightY)/2;
								if (rightX==leftX || parentY>Math.max(leftY,rightY) ||
										parentY<Math.min(leftY,rightY))
									g2.drawLine(parentX,person.getY()-dropSize,
											(leftX+rightX)/2,(leftY+rightY)/2);
								else
									g2.drawLine(parentX,person.getY()-dropSize,parentX,parentY);
					}
				}

				// write out id
				int verticalPosn=person.getY()+PelicanPerson.symbolSize+fontAscent;
				if (showId) {
					String idString = String.valueOf(person.id);
					int fontWidth = g2.getFontMetrics().stringWidth(idString);
					g2.drawString(idString, 
							person.getX() + PelicanPerson.symbolSize/2 - fontWidth/2,
							verticalPosn);
					verticalPosn+=fontAscent;
				}
				// write out name
				if (showName) {
					int fontWidth=g2.getFontMetrics().stringWidth(person.name);
					g2.drawString(person.name,person.getX()+PelicanPerson.symbolSize/2-fontWidth/2,verticalPosn);
					verticalPosn+=fontAscent;
				}

				// write out genotype
				int maxWidth=0;
				if (displayGenotypes) {
					int fontWidth=g2.getFontMetrics().stringWidth(person.getWorkingGenotypeAsString());
					maxWidth=Math.max(maxWidth,fontWidth);
					g2.drawString(person.getWorkingGenotypeAsString(),
							person.getX() + PelicanPerson.symbolSize/2 - fontWidth/2,
							verticalPosn);
				}

			}
	}

	/* }}} */

}

