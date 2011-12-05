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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import java.io.IOException;


public abstract class SessionAwareResourceHandlerWrapper extends ResourceHandlerWrapper {
    public boolean isResourceRequest(FacesContext context) {
        EnvUtils.createSessionOnPageLoad(context);
        if (EnvUtils.isSessionInvalid(context)) {
            return getWrapped().isResourceRequest(context);
        } else {
            return isSessionAwareResourceRequest(context);
        }
    }

    public void handleResourceRequest(FacesContext context) throws IOException {
        if (EnvUtils.isSessionInvalid(context)) {
            getWrapped().handleResourceRequest(context);
        } else {
            handleSessionAwareResourceRequest(context);
        }
    }

    public abstract boolean isSessionAwareResourceRequest(FacesContext context);

    public abstract void handleSessionAwareResourceRequest(FacesContext context) throws IOException;
}
