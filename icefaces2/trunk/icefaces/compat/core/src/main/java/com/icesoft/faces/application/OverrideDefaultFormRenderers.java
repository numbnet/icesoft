/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 *
 */

package com.icesoft.faces.application;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class OverrideDefaultFormRenderers implements SystemEventListener {
    private static final Logger Log = Logger.getLogger(OverrideDefaultFormRenderers.class.getName());
    public static final String COMMAND_LINK_HIDDEN_FIELD = "command_link_hidden_field";
    private static final String COMMAND_LINK_HIDDEN_FIELDS_KEY = "com.icesoft.faces.FormRequiredHidden";
    private static final String JAVAX_FACES_FORM_RENDERER_TYPE = "javax.faces.Form";
    private static final String OVERRIDING_FORM_RENDERER_TYPE = "com.icefaces.overriding.Form";
    private Renderer renderer;

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }


        HtmlForm form = (HtmlForm) ((PreRenderComponentEvent) event).getComponent();
        if (JAVAX_FACES_FORM_RENDERER_TYPE.equals(form.getRendererType())) {
            if (renderer == null) {
                RenderKit renderKit = context.getRenderKit();
                Renderer previousRenderer = renderKit.getRenderer(JAVAX_FACES_FORM_RENDERER_TYPE, JAVAX_FACES_FORM_RENDERER_TYPE);
                renderer = new OverridingFormRenderer(previousRenderer);
                renderKit.addRenderer(JAVAX_FACES_FORM_RENDERER_TYPE, OVERRIDING_FORM_RENDERER_TYPE, renderer);
                Log.fine("Extending h:form renderer to provide additional functionality expected by ice:* components.");
            }

            form.setRendererType(OVERRIDING_FORM_RENDERER_TYPE);
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof HtmlForm;
    }

    private static class OverridingFormRenderer extends Renderer {
        private final Renderer previousRenderer;

        public OverridingFormRenderer(Renderer previousRenderer) {
            this.previousRenderer = previousRenderer;
        }

        public void decode(FacesContext context, UIComponent component) {
            previousRenderer.decode(context, component);
        }

        public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
            previousRenderer.encodeBegin(context, component);
        }

        public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
            previousRenderer.encodeChildren(context, component);
        }

        public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            String formClientID = component.getClientId(context);
            writer.startElement("span", component);
            writer.writeAttribute("id", formClientID + "hdnFldsDiv", null);

            Map requestMap = context.getExternalContext().getRequestMap();
            Map map = (Map) requestMap.get(COMMAND_LINK_HIDDEN_FIELDS_KEY);
            if (map != null) {
                Iterator fields = map.entrySet().iterator();
                while (fields.hasNext()) {
                    Map.Entry nextField = (Map.Entry) fields.next();
                    if (COMMAND_LINK_HIDDEN_FIELD.equals(nextField.getValue())) {
                        writer.startElement("input", component);
                        writer.writeAttribute("type", "hidden", null);
                        writer.writeAttribute("name", nextField.getKey().toString(), null);
                        writer.endElement("input");
                    }
                }
            }

            writer.endElement("span");
            previousRenderer.encodeEnd(context, component);
        }

        public String convertClientId(FacesContext context, String clientId) {
            return previousRenderer.convertClientId(context, clientId);
        }

        public boolean getRendersChildren() {
            return previousRenderer.getRendersChildren();
        }

        public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
            return previousRenderer.getConvertedValue(context, component, submittedValue);
        }
    }
}
