package com.icesoft.icefaces.tutorial.component.outputProgress.label;

import javax.faces.model.SelectItem;
import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import javax.faces.context.FacesContext;

/**
 * Class used to allow the toggling of label position, and setting of the label text
 */
public class LabelBean extends ProgressBar
{
    private SelectItem[] AVAILABLE_POSITIONS = {new SelectItem("left"),
                                                new SelectItem("right"),
                                                new SelectItem("top"),
                                                new SelectItem("topcenter"),
                                                new SelectItem("topright"),
                                                new SelectItem("bottom"),
                                                new SelectItem("bottomcenter"),
                                                new SelectItem("bottomright"),
                                                new SelectItem("embed")};
    private String label = null;
    private String labelComplete = null;
    private String labelPosition = AVAILABLE_POSITIONS[AVAILABLE_POSITIONS.length-1].getValue().toString();
    
    public LabelBean() {
        super();
    }
    
    public SelectItem[] getAvailablePositions() {
        return AVAILABLE_POSITIONS;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getLabelComplete() {
        return labelComplete;
    }
    
    public String getLabelPosition() {
        return labelPosition;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setLabelComplete(String labelComplete) {
        this.labelComplete = labelComplete;
    }
    
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }
    
    /**
     * Method to start the progress bar
     *
     *@return "startProgress" String for faces-config navigation
     */
    public String startProgress() {
		PushRenderer.addCurrentSession("all");   
   		PortableRenderer renderer = PushRenderer.getPortableRenderer(FacesContext.getCurrentInstance());
        start(renderer);
        
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
}