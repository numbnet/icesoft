/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name="auxUpload", eager=true)
@ApplicationScoped
public class AuxUploadSetup {
    private AuxUploadResourceHandler auxHandler;
    private static String AUX_UPLOAD = "auxUpload";

    public AuxUploadSetup()  {
        Application application = FacesContext.getCurrentInstance()
                .getApplication();
        ResourceHandler currentHandler = application.getResourceHandler();
        auxHandler = new AuxUploadResourceHandler(currentHandler);
        application.setResourceHandler(auxHandler);
    }

    public static AuxUploadSetup getCurrentInstance()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        AuxUploadSetup auxUpload = (AuxUploadSetup) externalContext
            .getApplicationMap().get(AUX_UPLOAD);
        return auxUpload;
    }

    public String getUploadPath()  {
        return auxHandler.getTokenResourcePath();
    }
    
    public String getUploadURL()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        String serverName = externalContext.getRequestHeaderMap().
                get("x-forwarded-host");
        if (null == serverName)  {
            serverName = externalContext.getRequestServerName() + ":" +
                    externalContext.getRequestServerPort();
        }
        return "http://" + serverName + getUploadPath();
    }
    
    public boolean getEnabled()  {
        return EnvUtils.isAuxUploadBrowser(FacesContext.getCurrentInstance());
    }
    
    public String getCloudPushId()  {
        return auxHandler.getCloudPushId();
    }
}
