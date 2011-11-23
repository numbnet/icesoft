package org.icefaces.component.utils;

import org.icefaces.util.EnvUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuxUploadResourceHandler extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(AuxUploadResourceHandler.class.getName());
    private static final String RESOURCE_KEY = "javax.faces.resource";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String AUX_PREFIX = "AUX";
    private ResourceHandler wrapped;
    private Resource tokenResource;

    public AuxUploadResourceHandler(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
        tokenResource = createResource("icemobilebud.txt");
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
    
        if ( tokenResource.getRequestPath().equals(
            getResourcePath(facesContext)) )  {
            storeParts(externalContext);
            externalContext.setResponseContentType("text/plain");
            OutputStream out = externalContext.getResponseOutputStream();
            out.write("handled by AuxUploadResourceHandler".getBytes());
            return;
        }
        
        wrapped.handleResourceRequest(facesContext);
        
    }

    private void storeParts(ExternalContext externalContext)  {
        HttpServletRequest request = 
                (HttpServletRequest) externalContext.getRequest();
        HttpSession session = request.getSession();
        try {
            for (Part part : request.getParts()) {
System.out.println(part.getName() + " " + part.getContentType());
if (null == part.getContentType())  {
    System.out.println("getting as attribute "+ request.getParameter(part.getName()));
}
                if ("image/jpeg".equals(part.getContentType())) {
                    String fileName = UUID.randomUUID().toString() + ".jpg";
                    String dirPath = externalContext.getRealPath("/images") + "/";
                    File dirFile = new File(dirPath);
                    if (!dirFile.exists()) {
                        dirFile.mkdir();
                    }
                    String fullPath = dirPath + fileName;
                    part.write(fullPath);
                    String imageURI = "/images/" + fileName;
//                    session.setAttribute(IMAGE_KEY, imageURI);
System.out.println("find it at " + imageURI);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTokenResourcePath()  {
        return tokenResource.getRequestPath();
    }

    private static String getResourcePath(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        String path = externalContext.getRequestServletPath();
        if (null == path)  {
            path = externalContext.getRequestPathInfo();
        }
        return (externalContext.getRequestContextPath() + path);
    }

}
