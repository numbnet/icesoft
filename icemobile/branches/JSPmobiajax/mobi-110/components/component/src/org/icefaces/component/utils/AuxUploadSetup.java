package org.icefaces.component.utils;

import org.icefaces.util.EnvUtils;

import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
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

    public AuxUploadSetup()  {
        Application application = FacesContext.getCurrentInstance()
                .getApplication();
        ResourceHandler currentHandler = application.getResourceHandler();
        auxHandler = new AuxUploadResourceHandler(currentHandler);
        application.setResourceHandler(auxHandler);
    }

    public String getUploadPath()  {
        return auxHandler.getTokenResourcePath();
    }
    
    public String getUploadURL()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        String urlPrefix = externalContext.getRequestServerName() + ":" +
            externalContext.getRequestServerPort();
        return "http://" + urlPrefix + getUploadPath();
    }
}
