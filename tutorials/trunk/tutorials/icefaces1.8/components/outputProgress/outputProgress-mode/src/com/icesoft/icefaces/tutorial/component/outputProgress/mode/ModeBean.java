package com.icesoft.icefaces.tutorial.component.outputProgress.mode;

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;

/**
 * Class used to allow the toggling of the two progress bar modes
 * Basically just track whether we should be determinate or indeterminate
 */
public class ModeBean extends ProgressBar
{
    private static final SelectItem[] AVAILABLE_MODES = {new SelectItem(Boolean.FALSE, "Determinate"),
                                                         new SelectItem(Boolean.TRUE, "Indeterminate")};
    private Boolean isIndeterminate = (Boolean)AVAILABLE_MODES[0].getValue();
    
    public ModeBean() {
        super();
    }
    
    public SelectItem[] getAvailableModes() {
        return AVAILABLE_MODES;
    }
    
    public Boolean getIsIndeterminate() {
        return isIndeterminate;
    }
    
    public boolean getIsIndeterminatePrimitive() {
        return isIndeterminate.booleanValue();
    }
    
    public void setIsIndeterminate(Boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
    }
    
    /**
     * Method to start the progress bar
     *
     *@return "startProgress" String for faces-config navigation
     */
    public String startProgress() {
        start();
        
        return "startProgress";
    }
    
    /**
     * Method to stop the progress bar
     *
     *@return "stopProgress" String for faces-config navigation
     */
    public String stopProgress() {
        stop();
        
        return "stopProgress";
    }
    
    /**
     * Method called when the radio buttons are changed between the modes
     * This will just reset the percentage so the new mode has a fresh start
     *
     *@param vce event of the change
     */
    public void modeChanged(ValueChangeEvent vce) {
        percent = 0;
    }
}