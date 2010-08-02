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

package org.icefaces.render;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.application.ProductInfo;
import org.icefaces.context.DOMResponseWriter;
import org.icefaces.event.MainEventListener;
import org.icefaces.push.Configuration;
import org.icefaces.util.EnvUtils;
import org.icefaces.util.FormEndRendering;

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

public class DOMRenderKit extends RenderKitWrapper {
    private static Logger log = Logger.getLogger(DOMRenderKit.class.getName());
    private MainEventListener mainEventListener = new MainEventListener();
    private RenderKit delegate;
    private boolean deltaSubmit;

    //Announce ICEfaces 2.0
    static {
        if (log.isLoggable(Level.INFO)) {
            log.info(new ProductInfo().toString());
        }
    }

    public DOMRenderKit(RenderKit delegate) {
        this.delegate = delegate;
        Configuration configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
        deltaSubmit = configuration.getAttributeAsBoolean("deltaSubmit", false);
    }

    public RenderKit getWrapped() {
        return delegate;
    }

    public void addRenderer(String family, String rendererType, Renderer r) {
        Renderer renderer = deltaSubmit && "javax.faces.Form".equals(family) ? new FormBoost(r) : r;
        super.addRenderer(family, rendererType, renderer);
    }

    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding) {
        ResponseWriter parentWriter = delegate.createResponseWriter(writer, contentTypeList, encoding);
        if (!EnvUtils.isICEfacesView(FacesContext.getCurrentInstance())) {
            return parentWriter;
        }
        return new DOMResponseWriter(parentWriter, parentWriter.getCharacterEncoding(), parentWriter.getContentType());
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