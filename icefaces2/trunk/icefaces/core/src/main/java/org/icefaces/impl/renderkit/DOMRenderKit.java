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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.renderkit;

import org.icefaces.application.ProductInfo;
import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.impl.event.MainEventListener;
import org.icefaces.util.EnvUtils;
import org.icefaces.impl.util.FormEndRendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.icefaces.render.ExternalScript;
import org.icefaces.render.MandatoryResourceComponent;

public class DOMRenderKit extends RenderKitWrapper {
    private static Logger log = Logger.getLogger(DOMRenderKit.class.getName());
    private MainEventListener mainEventListener = new MainEventListener();
    private RenderKit delegate;
    private boolean deltaSubmit;
    private List mandatoryResourceConfig = null;
    private Renderer modifiedMessagesRenderer = null;
    private static final String MESSAGES = "javax.faces.Messages";
    private static final String MESSAGES_CLASS = 
            "org.icefaces.impl.renderkit.html_basic.MessagesRenderer";
    private ArrayList<ExternalScript> customScriptRenderers = new ArrayList<ExternalScript>();
    private ArrayList<String> mandatoryResourceComponents = new ArrayList<String>();

    //Announce ICEfaces 2.0
    static {
        if (log.isLoggable(Level.INFO)) {
            log.info(new ProductInfo().toString());
        }
    }

    public DOMRenderKit(RenderKit delegate) {
        this.delegate = delegate;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        deltaSubmit = EnvUtils.isDeltaSubmit(facesContext);
        String mandatoryResourceConfigString = 
                EnvUtils.getMandatoryResourceConfig(facesContext);
        if (null != mandatoryResourceConfigString)  {
            mandatoryResourceConfig = Arrays.asList(
                mandatoryResourceConfigString.split("\\s+") );
        }
        try {
            modifiedMessagesRenderer = 
                    (Renderer) Class.forName(MESSAGES_CLASS).newInstance();
        } catch (Throwable t)  {
            log.fine("No override for Messages Renderer " + t.toString());
        }
    }

    public RenderKit getWrapped() {
        return delegate;
    }

    /**
     * Check if renderer has an annotation for adding custom scripts 
     * @param family
     * @param rendererType
     * @param r
     */
    public void addRenderer(String family, String rendererType, Renderer r) {

        Class clazz = r.getClass();
        ExternalScript ec = (ExternalScript) clazz.getAnnotation(ExternalScript.class);
        if (ec != null) {
            if (ec.scriptURL() != null) {
                customScriptRenderers.add(ec);
            }
        }
        MandatoryResourceComponent mrc = (MandatoryResourceComponent)
            clazz.getAnnotation(MandatoryResourceComponent.class);
        if (mrc != null) {
            String compClassName = mrc.value();
            if (compClassName != null && compClassName.length() > 0) {
                if (!mandatoryResourceComponents.contains(compClassName)) {
                    if ((null == mandatoryResourceConfig) || 
                        mandatoryResourceConfig.contains(compClassName)) {
                        mandatoryResourceComponents.add(compClassName);
                    }
                }
            }
        }
//        if (!property.methodExpressionArgument().equals(Property.Null)) {
//					propertyValues.methodExpressionArgument = property.methodExpressionArgument();
//				}

        Renderer renderer = "javax.faces.Form".equals(family) ? new FormBoost(r) : r;
        super.addRenderer(family, rendererType, renderer);
    }

    public Renderer getRenderer(String family, String type)  {
        if ((null != modifiedMessagesRenderer) && MESSAGES.equals(family) && MESSAGES.equals(type))  {
            return modifiedMessagesRenderer;
        }
        return delegate.getRenderer(family, type);
    }

    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding) {
        ResponseWriter parentWriter = delegate.createResponseWriter(writer, contentTypeList, encoding);
        if (!EnvUtils.isICEfacesView(FacesContext.getCurrentInstance())) {
            return parentWriter;
        }
        return new DOMResponseWriter(parentWriter, parentWriter.getCharacterEncoding(), parentWriter.getContentType());
    }

    public List<ExternalScript> getCustomRenderScripts() {
        return customScriptRenderers;
    }
    
    public List<String> getMandatoryResourceComponents() {
        return mandatoryResourceComponents;
    }

    private class FormBoost extends RendererWrapper {
        private FormBoost(Renderer renderer) {
            super(renderer);
        }

        public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
            FormEndRendering.renderIntoForm(context, component);

            if (deltaSubmit && EnvUtils.isICEfacesView(context)) {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("script", component);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeText("ice.calculateInitialParameters('", null);
                writer.writeText(component.getClientId(context), null);
                writer.writeText("');", null);
                writer.endElement("script");
            }
            super.encodeEnd(context, component);
        }
    }
}
