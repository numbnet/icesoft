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

package com.icesoft.faces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

public class CompatDOMPartialViewContextFactory extends PartialViewContextFactory {
    PartialViewContextFactory delegate;

    public CompatDOMPartialViewContextFactory(PartialViewContextFactory delegate) {
        this.delegate = delegate;
    }

    public PartialViewContextFactory getWrapped() {
        return delegate;
    }

    /**
     * <p>Create (if needed)
     * and return a {@link javax.faces.context.PartialViewContext} instance that is initialized
     * using the current {@link javax.faces.context.FacesContext} instance.</p>
     *
     * @param facesContext the {@link javax.faces.context.FacesContext} for the current request.
     */
    public PartialViewContext getPartialViewContext(FacesContext facesContext) {
        return new CompatDOMPartialViewContext(delegate.getPartialViewContext(facesContext), facesContext);
    }

}
