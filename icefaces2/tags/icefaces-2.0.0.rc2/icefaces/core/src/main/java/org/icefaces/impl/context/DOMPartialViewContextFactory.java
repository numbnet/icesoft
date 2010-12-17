/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */


package org.icefaces.impl.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.context.PartialViewContext;

public class DOMPartialViewContextFactory extends PartialViewContextFactory  {
    PartialViewContextFactory delegate;

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
     * @param facesContext the {@link FacesContext} for the current request.
     */
    public PartialViewContext getPartialViewContext(FacesContext facesContext)  {
        return new DOMPartialViewContext(delegate.getPartialViewContext(facesContext), facesContext);
    }
    
}
