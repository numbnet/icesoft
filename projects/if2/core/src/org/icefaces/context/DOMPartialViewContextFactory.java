
package org.icefaces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.context.PartialViewContext;

public class DOMPartialViewContextFactory extends PartialViewContextFactory  {
    DOMPartialViewContextFactory delegate;

    public DOMPartialViewContextFactory(PartialViewContextFactory delegate)  {
        this.delegate = delegate;
    }

    public PartialViewContextFactory getWrapped()  {
        return delegate;
    }


    /**
     * <p>Create (if needed)
     * and return a {@link PartialViewContext} instance that is initialized
     * using the current {@link FacesContext} instance.</p>
     *
     * @param context the {@link FacesContext} for the current request. 
     */
    public PartialViewContext getPartialViewContext(FacesContext facesContext)  {
        return new DOMPartialViewContext(facesContext);
    }
    
}
