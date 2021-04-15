package edu.umb.jsAipotu.client.evolution;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;

/**
* NumberSpinner Custom Control
* 
* @author Pavan Andhukuri
* modified by Brian White
* 
*/
public class NumberSpinner extends Composite {

    private int RATE = 1;
    private IntegerBox integerBox;

    public NumberSpinner() {
        this(1);
    }

    public NumberSpinner(int defaultValue) {
        AbsolutePanel absolutePanel = new AbsolutePanel();
        initWidget(absolutePanel);
        absolutePanel.setSize("55px", "23px");

        integerBox = new IntegerBox();
        absolutePanel.add(integerBox, 0, 0);
        integerBox.setSize("30px", "16px");
        integerBox.setValue(defaultValue);

        Button upButton = new Button();
        upButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setValue(getValue() + RATE);
            }
        });
        upButton.setStyleName("dp-spinner-upbutton");

        absolutePanel.add(upButton, 34, 1);
        upButton.setSize("12px", "10px");

        Button downButton = new Button();
        downButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (getValue() == 0)
                    return;
                setValue(getValue() - RATE);
            }
        });
        downButton.setStyleName("dp-spinner-downbutton");
        absolutePanel.add(downButton, 34, 11);
        downButton.setSize("12px", "10px");
    }

    /**
     * Returns the value being held.
     * 
     * @return
     */
    public int getValue() {
        return integerBox.getValue() == null ? 0 : integerBox.getValue();
    }

    /**
     * Sets the value to the control
     * 
     * @param value
     *            Value to be set
     */
    public void setValue(int value) {
        integerBox.setValue(value);
    }

    /**
     * Sets the rate at which increment or decrement is done.
     * 
     * @param rate
     */
    public void setRate(int rate) {
        this.RATE = rate;
    }
}