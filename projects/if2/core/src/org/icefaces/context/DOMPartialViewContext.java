
package org.icefaces.context;

import java.util.Collection;

import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;


public class DOMPartialViewContext extends com.sun.faces.context.PartialViewContextImpl  {
    FacesContext facesContext;

    public DOMPartialViewContext(FacesContext facesContext)  {
        super(facesContext);
        this.facesContext = facesContext;
    }
/*
    public abstract Collection<String> getExecuteIds();
    
    public abstract Collection<String> getRenderIds();

    public abstract PartialResponseWriter getPartialResponseWriter();

    public abstract boolean isAjaxRequest();

    public abstract boolean isPartialRequest();

    public abstract boolean isExecuteAll();

    public abstract boolean isRenderAll();

    public abstract void setRenderAll(boolean renderAll);
    
    public abstract void setPartialRequest(boolean isPartialRequest);

    public abstract void release();

    public abstract void processPartial(PhaseId phaseId);
*/

}
