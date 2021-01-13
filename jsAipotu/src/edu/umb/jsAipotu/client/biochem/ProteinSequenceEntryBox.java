package edu.umb.jsAipotu.client.biochem;

import java.io.IOException;

import com.google.gwt.dom.client.Document;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBox;

/*  uses underlying single-letter code document
 *   edits by entering single letter code
 *   displays in three-letter code
 */
public class ProteinSequenceEntryBox extends ValueBox<String> {
	
	private BiochemistryWorkpanel bwp;
	
	private String aaSeq; // single-letter format
	
	public ProteinSequenceEntryBox() {
		super(Document.get().createTextInputElement(), RENDERER, PARSER);
		//this.bwp = bwp;
		//aaSeq = "";
	}
	
	public void setAminoAcidSequence(String aaSeq) {
		setText(aaSeq);
	}
	
	public String getAminoAcidSequence() {
		return null;
	}
	
    private static final Renderer<String> RENDERER = new Renderer<String>() {
        public String render(String object) {
           return "bob";
        }

        public void render(String object, Appendable appendable) throws IOException {
            appendable.append(render(object));
        }
    };
    
    private static final Parser<String> PARSER = new Parser<String>() {
        public String parse(CharSequence text) {
           return "fred";
        }
    };

	

}
