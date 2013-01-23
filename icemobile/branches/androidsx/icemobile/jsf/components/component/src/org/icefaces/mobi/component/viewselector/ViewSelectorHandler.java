/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobi.component.viewselector;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.ClientDescriptor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.*;
import java.util.Map;
import java.util.logging.Logger;

public class ViewSelectorHandler extends ComponentHandler {
    private static final Logger logger = Logger.getLogger(ViewSelectorHandler.class.getName());

    public ViewSelectorHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
    //    logger.info("CPH.onComponentCreated() for component =" + c.getClass().getName());
        super.onComponentCreated(ctx, c, parent);
    }

    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c)
            throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {

        //logger.info("CPH.applyNextHandler()");
        if (c instanceof ViewSelector) {
            String facetName = returnFacetNameToInclude( ctx, c);
            if (this.nextHandler instanceof CompositeFaceletHandler){
                FaceletHandler[] h = ((CompositeFaceletHandler)this.nextHandler).getHandlers();
                for (int i=0; i< h.length; i++){
                    if (h[i] instanceof FacetHandler){
                        FacetHandler faceHand = (FacetHandler)h[i];
                        String name = faceHand.getFacetName(ctx);
                        if (facetName.equals(name)){
                            h[i].apply(ctx, c);
                            break;
                        }
                    }
                    else {
                        logger.warning("ViewSelector must have children that are facets - see docs");
                   }
                }
            } else {
                logger.warning(" ViewSelector requires 2 facets to be defined - see docs");
            }
        }
    }


    protected String returnFacetNameToInclude(
            FaceletContext ctx, UIComponent c) {
 /*       logger.info("returnFacetNameToInclude()");
        logger.info("    c: " + c);
        logger.info("    c.id: " + c.getId());
        if (c instanceof ViewSelector){
            ViewSelector v = (ViewSelector)c;
            if (v.getDefaultView() !=null){
                logger.info("    c.defaultValue: "+ v.getDefaultView());
            }
            logger.info("     no of facets: "+v.getFacetCount());
        }    */
        FacesContext facesContext = ctx.getFacesContext();
        Map<Object, Object> contextMap = facesContext.getAttributes();
        if ((String)contextMap.get(ViewSelector.VIEW_TYPE_KEY)!=null){
            return (String)contextMap.get(ViewSelector.VIEW_TYPE_KEY);
        }
        String name= null;
        String defaultView = null;
        ViewSelector v = (ViewSelector)c;
        Object defView = v.getDefaultView();
        if (defView !=null){
            defaultView = String.valueOf(defView);
            if (defaultView.equals(ViewSelector.LARGE_FACET) || defaultView.equals(ViewSelector.SMALL_FACET)){
                if (defaultView.equals(ViewSelector.LARGE_FACET)) {
                    name=ViewSelector.LARGE_FACET;
                }else {
                    name=ViewSelector.SMALL_FACET;
                }
                contextMap.put(ViewSelector.VIEW_TYPE_KEY, name);
            }else {
                logger.warning("Must have 2 facets for ViewSelector named see docs");
            }
        } else {
            name= (String)contextMap.get(ViewSelector.VIEW_TYPE_KEY);
            if (name ==null){
                ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
                if (client.isTabletBrowser() || client.isDesktopBrowser()) {
                    name = ViewSelector.LARGE_FACET;
                } else if (client.isHandheldBrowser()) {
                    name = ViewSelector.SMALL_FACET;
                }
                if (name!=null){
                    contextMap.put(ViewSelector.VIEW_TYPE_KEY, name);
                }else{
                    logger.warning("Cannot determine device type. problem with View selection");
                }

            }
        }

        return name;
    }
}
