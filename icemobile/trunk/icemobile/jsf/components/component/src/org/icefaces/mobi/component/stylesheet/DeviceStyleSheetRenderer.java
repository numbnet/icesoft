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

package org.icefaces.mobi.component.stylesheet;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.render.Renderer;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icemobile.util.CSSUtils;


/**
 * Device style sheet render is mobile specific and by default tries to load
 * a css theme for the device making the initial request.  Devices can be either
 * Android, Blackberry or iPhone as that is the initial component theme offering.
 * <p/>
 * If the developer does not wish to have the component auto detect the calling
 * device the library and name attribute can be set to either specify a specific
 * theme by name or a new theme by library and name.
 *
 * @since 0.0.1 alpha.
 */
@ListenerFor(systemEventClass = javax.faces.event.PostAddToViewEvent.class)
public class DeviceStyleSheetRenderer extends Renderer implements javax.faces.event.ComponentSystemEventListener {

    private static final Logger log = Logger.getLogger(DeviceStyleSheetRenderer.class.getName());

    // empty string
    public static final String EMPTY_STRING = "";

    // CSS style extension circa 2011
    private static final String CSS_EXT = ".css";
    
    // iPhone style sheet name found in jar.
    public static final String IPHONE_CSS = "iphone.css";
    // iPad style sheet name found in jar.
    public static final String IPAD_CSS = "ipad.css";
    // Blackberry style sheet name found in jar.
    public static final String BBERRY_CSS = "bberry.css";

    // View types, small or large
    public static final String VIEW_TYPE = "view";
    public static final String VIEW_TYPE_SMALL = "small";
    public static final String VIEW_TYPE_LARGE = "large";


    // default resource library for a default themes,  if not specified in
    // component definition this library will be loaded.
  //  private static final String DEFAULT_LIBRARY = "org.icefaces.component.skins";
    // url of a resource that could not be resolved, danger Will Robertson.
    public static final String RESOURCE_URL_ERROR = "RES_NOT_FOUND";
    // key to store if algorithm to detect device type has run. If a device
    // was detected this key used to store the device name in session scope
    // for later retrial on subsequent component renders.
    public static final String MOBILE_DEVICE_TYPE_KEY = "mobile_device_type";

    protected void startElement(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement(HTML.STYLE_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.STYLE_TYPE_TEXT_CSS, HTML.TYPE_ATTR);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Map contextMap = context.getAttributes();
        Map attributes = uiComponent.getAttributes();
        DeviceStyleSheet stylesheet = (DeviceStyleSheet) uiComponent;
        String name = (String)uiComponent.getAttributes().get("name");
        String view = (String)uiComponent.getAttributes().get("view");
        if( name == null || "".equals(name)){
            name = CSSUtils.deriveTheme(view, JSFUtils.getRequest()).fileName();
        }
          
        if( name != null ){
        	contextMap.put(MOBILE_DEVICE_TYPE_KEY, name);
        	
        	String css = name + CSS_EXT;
            String library = deriveLibrary(attributes);

            // create URL that the resource can be loaded from.
        	Resource resource = context.getApplication().getResourceHandler()
                    .createResource(css, library);
            String resourceUrl = RESOURCE_URL_ERROR;
            if (resource != null) {
                resourceUrl = context.getExternalContext().encodeResourceURL(resource.getRequestPath());
            } else if (log.isLoggable(Level.WARNING)) {
                log.warning("Warning could not load resource " + library + "/" + name);
            }
            // final write out the style element to the component tree.
            writer.startElement(HTML.LINK_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.LINK_TYPE_TEXT_CSS, HTML.TYPE_ATTR);
            writer.writeAttribute(HTML.REL_ATTR, HTML.STYLE_REL_STYLESHEET, HTML.REL_ATTR);
            // pass though
            PassThruAttributeWriter.renderNonBooleanAttributes(
                    writer, uiComponent, stylesheet.getPASS_THOUGH_ATTRIBUTES());
            writer.writeURIAttribute(HTML.HREF_ATTR, resourceUrl, HTML.HREF_ATTR);
            writer.endElement(HTML.LINK_ELEM);
            encodeScript(writer,name);
        }
    }
    
    private String deriveLibrary(Map attributes){
    	String library = (String) attributes.get(HTML.LIBRARY_ATTR);
        if( library == null ){
        	library = DeviceStyleSheet.DEFAULT_LIBRARY;
        }
    	return library;
    }
    
    
    public void encodeScript(ResponseWriter writer, String name) throws IOException {
    	writer.startElement("script", null);
    	writer.writeAttribute("type", "text/javascript", null);
    	writer.writeText(String.format("document.documentElement.className == '' ? document.documentElement.className = '%1$s' : document.documentElement.className = document.documentElement.className+' %1$s';", name),null);
    	writer.endElement("script");
    }

    public void processEvent(ComponentSystemEvent event)
            throws AbortProcessingException {
        // http://javaserverfaces.java.net/nonav/docs/2.0/pdldocs/facelets/index.html
        // Finally make sure the component is only rendered in the header of the
        // HTML document.
        UIComponent component = event.getComponent();
        FacesContext context = FacesContext.getCurrentInstance();
        if (log.isLoggable(Level.FINER)) {
            log.finer("processEvent for component = " + component.getClass().getName());
        }
        context.getViewRoot().addComponentResource(context, component, HTML.HEAD_ELEM);
    }

}
