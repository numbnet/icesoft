package com.icesoft.icefaces.tutorial.component.outputProgress.style;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import javax.faces.context.FacesContext;

/**
 * Class used to demonstrate the custom styling that can be applied to a progress bar
 * As the styling is done at a page level, this class really just handles the percent
 *  of the progress bar, and starting and stopping it's demonstration process
 */
public class StyleBean extends ProgressBar
{
    public StyleBean() {
        super();
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