package edu.umb.jsPedigrees.client.Pelican;
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

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class PelicanLines {

	/* {{{ adjacent */

	private static boolean adjacent(AbsolutePanel panel, PelicanPerson father, PelicanPerson mother) {
		if (Math.abs(panel.getWidgetTop(father) - panel.getWidgetTop(mother)) >= PelicanPerson.ySize)
			return(true);
		for(int i=0;i<panel.getWidgetCount();i++)
			if (panel.getWidget(i) instanceof PelicanPerson) {
				PelicanPerson person=(PelicanPerson)panel.getWidget(i);
				if (!(person.id == father.id) &&
						!(person.id == mother.id) &&
						panel.getWidgetTop(person) >= Math.min(panel.getWidgetTop(father), panel.getWidgetTop(mother)) &&
						panel.getWidgetTop(person) <= Math.max(panel.getWidgetTop(father), panel.getWidgetTop(mother)) &&
						panel.getWidgetLeft(person) >= Math.min(panel.getWidgetLeft(father), panel.getWidgetLeft(mother)) &&
						panel.getWidgetLeft(person) <= Math.max(panel.getWidgetLeft(father), panel.getWidgetLeft(mother)))
					return(false);
			}
		return(true);
	}

	/* }}} */

	/* {{{ areSibs */

	private static boolean areSibs(PelicanPerson person1,PelicanPerson person2) {
		if (person1.father==person2.father && person1.mother==person2.mother)
			return(true);
		return(false);
	}

	/* }}} */

	/* {{{ paintComponent: draw the lines */

	public static Canvas drawLines(AbsolutePanel panel) {

		Canvas canvas = Canvas.createIfSupported();
		Context2d ctx = canvas.getContext2d();

		canvas.setCoordinateSpaceHeight(panel.getOffsetHeight());
		canvas.setCoordinateSpaceWidth(panel.getOffsetWidth());

		ctx.setStrokeStyle(CssColor.make("0,0,0"));
		ctx.setLineWidth(1.0f);
		ctx.setFont("12px sans-serif");

		int fontHeight = 15;
		int fontAscent = 15;

		int dropSize=Math.max(2,Math.min(PelicanPerson.symbolSize/2,3*(PelicanPerson.ySpace-PelicanPerson.symbolSize-fontHeight)/4));
		for(int i=0;i<panel.getWidgetCount();i++)
			if (panel.getWidget(i) instanceof PelicanPerson) {
				// draw a line from this person to its parents
				PelicanPerson person=(PelicanPerson)panel.getWidget(i);
				if (person.father!=null && person.mother!=null) {
					//System.out.println("HERE "+String.valueOf(i));
					// find the mother and father
					PelicanPerson father=person.father;
					PelicanPerson mother=person.mother;
					if (father!=null && mother!=null) {
						// line between parents
						int fatherX = panel.getWidgetLeft(father) + ((panel.getWidgetLeft(father) < panel.getWidgetLeft(mother))?PelicanPerson.symbolSize:0);
						int motherX = panel.getWidgetLeft(mother) + ((panel.getWidgetLeft(mother) < panel.getWidgetLeft(father))?PelicanPerson.symbolSize:0);
						int fatherY = panel.getWidgetTop(father) + PelicanPerson.symbolSize/2;
						int motherY = panel.getWidgetTop(mother) + PelicanPerson.symbolSize/2;
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
						if (!adjacent(panel, father, mother) && father.generation==mother.generation) {
							// draw lines which avoid other symbols

							// g2.drawLine(leftX,leftY,leftX+gap/4,leftY);
							ctx.beginPath();
							ctx.moveTo(leftX,leftY);
							ctx.lineTo(leftX+gap/4,leftY);
							ctx.closePath();
							ctx.stroke();

							// g2.drawLine(rightX,rightY,rightX-gap/2,rightY);
							ctx.beginPath();
							ctx.moveTo(rightX,rightY);
							ctx.lineTo(rightX-gap/2,rightY);
							ctx.closePath();
							ctx.stroke();

							leftX+=gap/4;
							rightX-=gap/2;

							// g2.drawLine(leftX,leftY,leftX,leftY-(PelicanPerson.symbolSize+dropSize)/2);
							ctx.beginPath();
							ctx.moveTo(leftX,leftY);
							ctx.lineTo(leftX,leftY-(PelicanPerson.symbolSize+dropSize)/2);
							ctx.closePath();
							ctx.stroke();

							// g2.drawLine(rightX,rightY,rightX,rightY-(PelicanPerson.symbolSize+dropSize)/2);
							ctx.beginPath();
							ctx.moveTo(rightX,rightY);
							ctx.lineTo(rightX,rightY-(PelicanPerson.symbolSize+dropSize)/2);
							ctx.closePath();
							ctx.stroke();

							leftY-=(PelicanPerson.symbolSize+dropSize)/2;
							rightY-=(PelicanPerson.symbolSize+dropSize)/2;
						}

						// g2.drawLine(leftX,leftY,rightX,rightY);
						ctx.beginPath();
						ctx.moveTo(leftX,leftY);
						ctx.lineTo(rightX,rightY);
						ctx.closePath();
						ctx.stroke();

						// line up from child
						// g2.drawLine(person.getX()+PelicanPerson.symbolSize/2,person.getY(),person.getX()+PelicanPerson.symbolSize/2,person.getY()-dropSize);
						ctx.beginPath();
						ctx.moveTo(panel.getWidgetLeft(person) + PelicanPerson.symbolSize/2, panel.getWidgetTop(person));
						ctx.lineTo(panel.getWidgetLeft(person) + PelicanPerson.symbolSize/2, panel.getWidgetTop(person) - dropSize);
						ctx.closePath();
						ctx.stroke();

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
							for(int j=0;j<panel.getWidgetCount();j++)
								if (panel.getWidget(j) instanceof PelicanPerson) {
									PelicanPerson sib=(PelicanPerson)panel.getWidget(j);
									if (areSibs(person,sib)) nsib++;
								}
							int sibs=0;
							for(int j=0;j<panel.getWidgetCount() && sibs<=nsib/2;j++)
								if (panel.getWidget(j) instanceof PelicanPerson) {
									PelicanPerson sib=(PelicanPerson)panel.getWidget(j);
									if (areSibs(person,sib)) sibs++;
									parentX = panel.getWidgetLeft(sib) + PelicanPerson.symbolSize/2;
								}
							if (nsib>1 && nsib%2==0)
								parentX-=PelicanPerson.xSpace/2;
							if (parentX<=leftX)
								parentX=leftX+PelicanPerson.symbolSize/2;
							if (parentX>=rightX)
								parentX=rightX-PelicanPerson.symbolSize/2;
						}

						// g2.drawLine(person.getX()+PelicanPerson.symbolSize/2,person.getY()-dropSize,parentX,person.getY()-dropSize);
						ctx.beginPath();
						ctx.moveTo(panel.getWidgetLeft(person) + PelicanPerson.symbolSize/2, panel.getWidgetTop(person) - dropSize);
						ctx.lineTo(parentX, panel.getWidgetTop(person) - dropSize);
						ctx.closePath();
						ctx.stroke();						

						// line up to parents
						// Draw a vertical line up to the line joining the parents
						// if this happens to be not between the parents,
						// change it to a line to the midpoint between the parents
						int parentY=(rightX!=leftX)?
								leftY+(rightY-leftY)*(parentX-leftX)/(rightX-leftX):
									(leftY+rightY)/2;
								if (rightX==leftX || parentY>Math.max(leftY,rightY) ||
										parentY<Math.min(leftY,rightY)) {
									// g2.drawLine(parentX,person.getY()-dropSize, (leftX+rightX)/2,(leftY+rightY)/2);
									ctx.beginPath();
									ctx.moveTo(parentX, panel.getWidgetTop(person) - dropSize);
									ctx.lineTo((leftX+rightX)/2, (leftY+rightY)/2);
									ctx.closePath();
									ctx.stroke();						

								} else {
									// g2.drawLine(parentX,person.getY()-dropSize,parentX,parentY);
									ctx.beginPath();
									ctx.moveTo(parentX, panel.getWidgetTop(person) - dropSize);
									ctx.lineTo(parentX, parentY);
									ctx.closePath();
									ctx.stroke();						

								}
					}
				}

				// write out id
				int verticalPosn = panel.getWidgetTop(person) + PelicanPerson.symbolSize + fontAscent;
				String idString = String.valueOf(person.id);
				int fontWidth = (int)ctx.measureText(idString).getWidth();
				// g2.drawString(idString, 
				//		person.getX() + PelicanPerson.symbolSize/2 - fontWidth/2,
				//		verticalPosn);
				ctx.fillText(idString, 
						panel.getWidgetLeft(person) + PelicanPerson.symbolSize/2 - fontWidth/2, 
						verticalPosn);
				verticalPosn += fontAscent;
			}
		return canvas;
	}

}

