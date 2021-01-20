/* displays gene with annotations and selected base(s)
 * collects keypresses and sends update requests to it's MoBoWorkpanel
 */

package edu.umb.jsAipotu.client.molBiol;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

public class GeneDisplayPanel extends FocusPanel {

	private final MolBiolWorkpanel mbwp;

	private HTML html;

	public GeneDisplayPanel(final MolBiolWorkpanel mbwp) {
		this.mbwp = mbwp;
		html = new HTML();
		this.add(html);
		sinkEvents(Event.ONKEYDOWN);
		sinkEvents(Event.ONCLICK);
	}

	public void setHTML(String html) {
		this.html.setHTML(html);
	}

	public void onBrowserEvent(Event e) {
		int type = e.getTypeInt();

		if (type == Event.ONCLICK) {
			int pixelX = e.getClientX() - this.getAbsoluteLeft();
			int baseNum = (int)((pixelX * 0.128) - 3.32);
			mbwp.handleClickEvent(baseNum);
		}

		if (type == Event.ONKEYDOWN) {
			e.preventDefault();
			int kc = e.getKeyCode();

			// left arrow
			if (kc == 37) {
				mbwp.moveCursorLeft();
			} 

			// right arrow
			if (kc == 39) {
				mbwp.moveCursorRight();
			} 

			// delete
			if (kc == 8) {
				mbwp.deleteBaseAtCursor();
			}

			// deal with character entries
			String s = Character.toString((char)kc);

			if (s.equals("A") || s.equals("G") || s.equals("C") || s.equals("T") ) {
				if (e.getShiftKey()) {
					mbwp.insertBaseAtCursor(s);
				} else {
					mbwp.changeBaseAtCursor(s);
				}
			}
		}
	}


}
