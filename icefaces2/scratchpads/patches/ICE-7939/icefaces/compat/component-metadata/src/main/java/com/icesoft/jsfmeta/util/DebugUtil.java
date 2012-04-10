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

package com.icesoft.jsfmeta.util;

import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.FacetBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;
import com.sun.rave.jsfmeta.beans.RenderKitBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugUtil {
    
    private static Logger logger = Logger.getLogger(DebugUtil.class.getName());
    
    public DebugUtil() {
    }
    
    public static void print(FacesConfigBean config){
        
        ComponentBean cbs[] = config.getComponents();
        for (int i = 0; i < cbs.length; i++) {
            
            ComponentBean cb = cbs[i];
            if(logger.isLoggable(Level.FINE)){
                logger.log(Level.FINE, "Component( componentType="
                        + cb.getComponentType() + ",componentFamily="
                        + cb.getComponentFamily() + ",rendererType="
                        + cb.getRendererType() + ",baseComponentType="
                        + cb.getBaseComponentType() + ")"+
                        "");
            }
            
            if(cbs[i].getComponentFamily() == null || cbs[i].getComponentType() == null){
                continue;
            }
            
            RendererBean rendererBean = config.getRenderKit("HTML_BASIC").getRenderer(cbs[i].getComponentFamily(), cbs[i].getRendererType());
            
            if(rendererBean == null){
                continue;
            }
            
            if(logger.isLoggable(Level.FINE)){
                logger.log(Level.FINE, " tagName="+rendererBean.getTagName());
            }
            
            PropertyBean[] pbs = cbs[i].getProperties();
            for(int j=0; j< pbs.length;j++){
                
                if(logger.isLoggable(Level.FINE)){
                    logger.log(Level.FINE,"categoryName="+pbs[j].getCategory()+" propertyName="+pbs[j].getPropertyName());
                }
                
            }
        }
        
        for (int i = 0; i < cbs.length; i++) {
            ComponentBean cb = cbs[i];
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Component(componentType="
                        + cb.getComponentType() + ",componentFamily="
                        + cb.getComponentFamily() + ",rendererType="
                        + cb.getRendererType() + ",baseComponentType="
                        + cb.getBaseComponentType() + ")");
            }
            FacetBean fbs[] = cbs[i].getFacets();
            for (int j = 0; j < fbs.length; j++) {
                
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "  Facet(facetName="
                            + fbs[j].getFacetName() + ",displayName="
                            + fbs[j].getDisplayName("") + ")");
                }
            }
        }
        
        RenderKitBean rkbs[] = config.getRenderKits();
        for (int i = 0; i < rkbs.length; i++) {
            RenderKitBean rkb = rkbs[i];
            RendererBean rbs[] = rkb.getRenderers();
            for (int j = 0; j < rbs.length; j++) {
                RendererBean rb = rbs[j];
                if(logger.isLoggable(Level.FINE)){
                    logger.log(Level.FINE, "Renderer(renderKitId="
                            + rkb.getRenderKitId() + ",componentFamily="
                            + rb.getComponentFamily() + ",rendererType="
                            + rb.getRendererType() + ")");
                }
                FacetBean fbs[] = rbs[j].getFacets();
                for (int k = 0; k < fbs.length; k++){
                    
                    if(logger.isLoggable(Level.FINE)){
                        logger.log(Level.FINE, "  Facet(facetName="
                                + fbs[k].getFacetName() + ",displayName="
                                + fbs[k].getDisplayName("") + ")");
                    }
                }
                
            }
            
        }
    }
    
}
