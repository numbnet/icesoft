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

package org.icefaces.impl.renderkit;

import org.icefaces.application.ProductInfo;
import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.impl.event.MainEventListener;
import org.icefaces.util.EnvUtils;
import org.icefaces.impl.util.FormEndRendering;
import org.icefaces.impl.event.BridgeSetup;

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
import org.icefaces.render.SpecialResourceComponent;

public class DOMRenderKit extends RenderKitWrapper {
    private static Logger log = Logger.getLogger(DOMRenderKit.class.getName());
    private MainEventListener mainEventListener = new MainEventListener();
    private RenderKit delegate;
    private boolean deltaSubmit;
    private Renderer modifiedMessageRenderer = null;
    private static final String MESSAGE = "javax.faces.Message";
    private static final String MESSAGE_CLASS = 
            "org.icefaces.impl.renderkit.html_basic.MessageRenderer";
    private Renderer modifiedMessagesRenderer = null;
    private static final String MESSAGES = "javax.faces.Messages";
    private static final String MESSAGES_CLASS = 
            "org.icefaces.impl.renderkit.html_basic.MessagesRenderer";
    private ArrayList<ExternalScript> customScriptRenderers = new ArrayList<ExternalScript>();
    private ArrayList<MandatoryResourceComponent> mandatoryResourceComponents = new ArrayList<MandatoryResourceComponent>();
	private ArrayList<String> specialResourceComponents = new ArrayList<String>();

    //Announce ICEfaces
    static {
        if (log.isLoggable(Level.INFO)) {
            log.info(new ProductInfo().toString());
        }
    }

    public DOMRenderKit(RenderKit delegate) {
        this.delegate = delegate;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        deltaSubmit = EnvUtils.isDeltaSubmit(facesContext);
        try {
            modifiedMessageRenderer = 
                    (Renderer) Class.forName(MESSAGE_CLASS).newInstance();
        } catch (Throwable t)  {
            log.fine("No override for Message Renderer " + t.toString());
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
                if (!mandatoryResourceComponents.contains(mrc)) {
                    mandatoryResourceComponents.add(mrc);
                }
            }
        }
		
        SpecialResourceComponent srd = (SpecialResourceComponent)
            clazz.getAnnotation(SpecialResourceComponent.class);
        if (srd != null) {
            String compClassName = srd.value();
            if (compClassName != null && compClassName.length() > 0) {
                if (!specialResourceComponents.contains(compClassName)) {
                    specialResourceComponents.add(compClassName);
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
        Renderer renderer = delegate.getRenderer(family, type);
        if (renderer == null) {
            return renderer;
        }
        String className = renderer.getClass().getName();
        if (className.equals("com.sun.faces.renderkit.html_basic.MessageRenderer"))  {
            return modifiedMessageRenderer;
        }
        if (className.equals("com.sun.faces.renderkit.html_basic.MessagesRenderer"))  {
            return modifiedMessagesRenderer;
        }
        return renderer;
    }

    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResponseWriter parentWriter = delegate.createResponseWriter(writer, contentTypeList, encoding);
        if (facesContext.getPartialViewContext().isPartialRequest())  {
            return parentWriter;
        }
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return parentWriter;
        }

        return new DOMResponseWriter(parentWriter, parentWriter.getCharacterEncoding(), parentWriter.getContentType());
    }

    public List<ExternalScript> getCustomRenderScripts() {
        return customScriptRenderers;
    }
    
    public List<MandatoryResourceComponent> getMandatoryResourceComponents() {
        return mandatoryResourceComponents;
    }
	
    public List<String> getSpecialResourceComponents() {
        return specialResourceComponents;
    }

    private class FormBoost extends RendererWrapper {
        private FormBoost(Renderer renderer) {
            super(renderer);
        }

        public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        
            if (component instanceof BridgeSetup.ShortIdForm)  {
                //do not augment BridgeSetup form
                super.encodeEnd(context, component);
                return;
            }

            FormEndRendering.renderIntoForm(context, component);

            if (EnvUtils.isICEfacesView(context)) {
                ResponseWriter writer = context.getResponseWriter();

                if (deltaSubmit) {
                    writer.startElement("script", component);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.writeText("ice.calculateInitialParameters('", null);
                    writer.writeText(component.getClientId(context), null);
                    writer.writeText("');", null);
                    writer.endElement("script");
                }

                super.encodeEnd(context, component);

                //render BridgeSetup immediately after form if missing body
                if (!EnvUtils.hasHeadAndBodyComponents(context)) {
                    BridgeSetup bridgeSetup = BridgeSetup.getBridgeSetup(context);
                    List<UIComponent> bodyResources = bridgeSetup.getBodyResources(context);
                    for (UIComponent bodyResource : bodyResources)  {
                        bodyResource.encodeBegin(context);
                        bodyResource.encodeEnd(context);
                    }
                }
    
            } else {
                super.encodeEnd(context, component);
            }

        }
    }
}
