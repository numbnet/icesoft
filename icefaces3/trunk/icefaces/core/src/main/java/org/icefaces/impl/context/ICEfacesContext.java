package org.icefaces.impl.context;

import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import java.net.URI;

public class ICEfacesContext extends FacesContextWrapper {
    private FacesContext wrapped;

    public ICEfacesContext(FacesContext wrapped) {
        this.wrapped = wrapped;
    }

    public FacesContext getWrapped() {
        return wrapped;
    }

    public ExternalContext getExternalContext() {
        return new ICEfacesExternalContext(wrapped.getExternalContext());
    }

    public static class ICEfacesExternalContext extends ExternalContextWrapper {
        private ExternalContext wrapped;

        public ICEfacesExternalContext(ExternalContext wrapped) {
            this.wrapped = wrapped;
        }

        public ExternalContext getWrapped() {
            return wrapped;
        }

        public String encodeResourceURL(String url) {
            if (EnvUtils.instanceofPortletRequest(wrapped.getRequest())) {
                return wrapped.encodeResourceURL(url);
            }
            if (url.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                return url;
            }
            String mimeType = wrapped.getMimeType(url);
            if (mimeType != null && (
                    mimeType.startsWith("image/") ||
                            mimeType.startsWith("video/") ||
                            mimeType.startsWith("audio/") ||
                            mimeType.equals("text/css") ||
                            mimeType.equals("text/javascript"))) {
                return url;
            }

            return wrapped.encodeResourceURL(url);
        }
    }
}
