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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.render;

import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Writer;
import java.io.OutputStream;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;
import javax.faces.render.ResponseStateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.event.PostAddToViewEvent;

import org.icefaces.context.DOMResponseWriter;
import org.icefaces.event.MainEventListener;
import org.icefaces.util.EnvUtils;
import org.icefaces.application.ProductInfo;

public class DOMRenderKit extends RenderKit {

    private static Logger log = Logger.getLogger(DOMRenderKit.class.getName());

    MainEventListener mainEventListener = new MainEventListener();
    RenderKit delegate;

    //Announce ICEfaces 2.0
    static {
        if (log.isLoggable(Level.INFO)) {
            log.info(new ProductInfo().toString());
        }
    }

    public DOMRenderKit(RenderKit delegate)  {
        this.delegate = delegate;
    }

    public DOMRenderKit() {
        RenderKitFactory factory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        delegate = factory.getRenderKit(null, RenderKitFactory.HTML_BASIC_RENDER_KIT);
    }

    public RenderKit getWrapped()  {
        return delegate;
    }

    public  void addRenderer(String family, String type, Renderer renderer)  {
        delegate.addRenderer(family, type, renderer);
    }

    public Renderer getRenderer(String family, String type)  {
        return delegate.getRenderer(family, type);
    }

    public ResponseStateManager getResponseStateManager()  {
        return delegate.getResponseStateManager();
    }

    public  ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding)  {
        if (!EnvUtils.isICEfacesView(FacesContext.getCurrentInstance())) {
            return delegate.createResponseWriter(writer, contentTypeList, encoding);
        }
        ResponseWriter responseWriter = new DOMResponseWriter(writer);
        return responseWriter;
    }

    public ResponseStream createResponseStream(OutputStream out)  {
        return delegate.createResponseStream(out);
    }
    
    public Iterator getComponentFamilies()  {
        return delegate.getComponentFamilies();
    }

    public Iterator getRendererTypes(String types)  {
        return delegate.getRendererTypes(types);
    }

    public void addClientBehaviorRenderer(String name, ClientBehaviorRenderer renderer)  {
        delegate.addClientBehaviorRenderer(name, renderer);
    }

    public ClientBehaviorRenderer getClientBehaviorRenderer(String name)  {
        return delegate.getClientBehaviorRenderer(name);
    }

    public Iterator getClientBehaviorRendererTypes()  {
        return delegate.getClientBehaviorRendererTypes();
    }

}