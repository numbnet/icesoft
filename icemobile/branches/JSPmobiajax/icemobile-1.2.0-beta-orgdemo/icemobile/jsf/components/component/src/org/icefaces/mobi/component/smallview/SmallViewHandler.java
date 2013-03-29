package org.icefaces.mobi.component.smallview;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.ClientDescriptor;

public class SmallViewHandler  extends ComponentHandler {

    public SmallViewHandler(ComponentConfig componentConfig) {
        super(componentConfig);
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        super.onComponentCreated(ctx, c, parent);
    }

    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c)
            throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {

        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        if (c instanceof SmallView && client.isHandheldBrowser()) {
            this.nextHandler.apply(ctx, c);
        }
    }
}

