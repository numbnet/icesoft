/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.camera;


import static org.icefaces.mobi.utils.HTML.BUTTON_ELEM;
import static org.icefaces.mobi.utils.HTML.ONCLICK_ATTR;
import static org.icefaces.mobi.utils.HTML.SPAN_ELEM;
import static org.icefaces.mobi.utils.HTML.ANCHOR_ELEM;
import static org.icefaces.mobi.utils.HTML.INPUT_ELEM;
import static org.icefaces.mobi.utils.HTML.ID_ATTR;
import static org.icefaces.mobi.utils.HTML.NAME_ATTR;
import static org.icefaces.mobi.utils.HTML.TYPE_ATTR;
import static org.icefaces.mobi.utils.HTML.INPUT_TYPE_FILE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.mobi.renderkit.RenderUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.BridgeItCommand;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;


public class CameraRenderer extends Renderer {
    //private static final Logger logger = Logger.getLogger(CameraRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Camera camera = (Camera) uiComponent;
        String clientId = camera.getClientId();
        try {
            if (!camera.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                boolean valid =  extractImages(facesContext, map, clientId);
                /* only set map to value if boolean returned from extractImages is true */
                if (valid){
                    if (map !=null){
                       camera.setValue(map);

             //   trigger valueChange and add map as newEvent value old event is NA
                       uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
                            null, map));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param facesContext
     * @param map
     * @param clientId
     * @return   boolean true means validation of the upload passes, false mean it does not.
     * @throws IOException
     *
     * that uploaded this component.
     */
    public boolean extractImages(FacesContext facesContext, Map map, String clientId) throws IOException {
        return MobiJSFUtils.decodeComponentFile(facesContext, clientId, map);
    }

    /*
      rendering markup moved to core renderer for use with JSP and JSF
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        Camera camera = (Camera) uiComponent;
        String oldLabel = camera.getButtonLabel();
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        if (MobiJSFUtils.uploadInProgress(camera))  {
            camera.setButtonLabel(camera.getCaptureMessageLabel()) ;
        } 
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = camera.getClientId();
        writer.startElement(SPAN_ELEM, null);
        writer.writeAttribute(ID_ATTR, clientId + "_wpr", null);
        RenderUtils.writeStyle(uiComponent, writer);
        RenderUtils.writeStyleClassAndBase(uiComponent, writer, "mobi-wrapper");
        if( client.isBridgeItSupportedPlatform(BridgeItCommand.CAMERA) ){
            RenderUtils.startButtonElem(uiComponent, writer);
            
            String script = "bridgeit.camera('" + clientId + "', '', "
                    + "{postURL:'" + AuxUploadSetup.getInstance().getUploadURL() + "', ";
            script += "cookies:{'JSESSIONID':'" + 
                    MobiJSFUtils.getSessionIdCookie(facesContext) +  "'}";
            int maxwidth = camera.getMaxwidth();
            if (maxwidth > 0){
                script += ", maxwidth: " + maxwidth;
            }
            int maxheight = camera.getMaxheight();
            if (maxheight > 0){
                script += ", maxheight:" + maxheight;
            }
            script += "});";
            writer.writeAttribute(ONCLICK_ATTR, script, null);
            
            writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
            
            RenderUtils.writeDisabled(uiComponent, writer);
            RenderUtils.writeTabIndex(uiComponent, writer);
            
            writer.startElement(SPAN_ELEM, camera);
            writer.writeText(camera.getButtonLabel(), null);
            writer.endElement(SPAN_ELEM);
            
            writer.endElement(BUTTON_ELEM);
            camera.setButtonLabel(oldLabel);
        }
        else{
            UIComponent fallbackFacet = uiComponent.getFacet("fallback");
            if( fallbackFacet == null ){
                //link
                writer.startElement(ANCHOR_ELEM, uiComponent);
                writer.writeAttribute(ID_ATTR, clientId, null);
                RenderUtils.writeDisabled(uiComponent, writer);
                RenderUtils.writeStyle(uiComponent, writer);
                RenderUtils.writeStyleClassAndBase(uiComponent, writer, CSSUtils.STYLECLASS_BUTTON);
                RenderUtils.writeTabIndex(uiComponent, writer);
                if( !camera.isDisabled()){
                    writer.writeAttribute(ONCLICK_ATTR, "document.getElementById('" + clientId + "').style.display = 'none'; document.getElementById('" + clientId + "_upload').style.display = 'inline-block';", null);
                }
                writer.writeText(camera.getButtonLabel(), null);
                writer.endElement(ANCHOR_ELEM);
                //file upload
                if( !camera.isDisabled()){
                    writer.startElement(INPUT_ELEM, null);
                    writer.writeAttribute(ID_ATTR, clientId + "_upload", null);
                    writer.writeAttribute("style", "display:none", null);
                    writer.writeAttribute(NAME_ATTR, clientId, null);
                    writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_FILE, null);
                    if( client.isIEBrowser() ){
                        writer.writeAttribute("accept", "image/*;capture=camera", null);
                    }
                    else{
                        writer.writeAttribute("accept", "image/*", null);
                    }
                    writer.endElement(INPUT_ELEM);
                }
            }
            else{
                fallbackFacet.encodeAll(facesContext);
            }
        }
        writer.endElement(SPAN_ELEM);
        
       
    }
    
}
