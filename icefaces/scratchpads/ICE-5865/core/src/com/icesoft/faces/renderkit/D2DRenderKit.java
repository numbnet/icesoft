/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.renderkit;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;
import java.util.HashMap;
import java.util.Map;
import java.io.Writer;
import java.io.OutputStream;

/**
 * Instead of loading all of our Renderers into the standard one with the
 *  renderKitId of RenderKitFactory.HTML_BASIC_RENDER_KIT = "HTML_BASIC",
 *  we're now defining our own RenderKit for ICEfaces applications to use. 
 * 
 */
public class D2DRenderKit extends RenderKit {
    public static final String ICEFACES_RENDER_KIT = "ICEfacesRenderKit";
    
    // Map<String family, Map<String rendererType, Renderer>>
    private Map family2rendererType;
    private RenderKit delegateRenderKit;
    
    public D2DRenderKit() {
        super();
        family2rendererType = new HashMap(64);
//System.out.println("D2DRenderKit.ctor()");
    }
    
    public D2DRenderKit(RenderKit delegateRenderKit)  {
        this();
        this.delegateRenderKit = delegateRenderKit;
    }
    
    public void addRenderer(String family, String rendererType, Renderer renderer) {
//System.out.println("D2DRenderKit.addRenderer()\n  family: " + family + "\n  rendererType: " + rendererType + "\n  renderer: " + renderer.getClass().getName());
        if (family == null || rendererType == null || renderer == null) {
            throw new NullPointerException("All parameters to "+
                "RenderKit.addRenderer(String family, String rendererType, "+
                "Renderer renderer) must be non-null.  family: " + family +
                "  rendererType: " + rendererType + "  renderer: " + renderer);
        }
        // JSF 1.2 can load faces-config.xml files on multiples threads
        synchronized(this) {
            Map rendererType2renderer = (Map) family2rendererType.get(family);
            if (rendererType2renderer == null) {
                rendererType2renderer = new HashMap(64);
                family2rendererType.put(family, rendererType2renderer);
            }
            rendererType2renderer.put(rendererType, renderer);
        }
    }
    
    public Renderer getRenderer(String family, String rendererType) {
        if (family == null || rendererType == null) {
            throw new NullPointerException("All parameters to "+
                "RenderKit.getRenderer(String family, String rendererType) "+
                "must be non-null.  family: " + family + "  rendererType: " +
                rendererType);
        }
        Renderer renderer = null;
        synchronized(this) {
            Map rendererType2renderer = (Map) family2rendererType.get(family);
            if (rendererType2renderer != null) {
                renderer = (Renderer) rendererType2renderer.get(rendererType);
            }
        }
        if (renderer == null) {
            renderer = getDelegate().getRenderer(family, rendererType);
//System.out.println("D2DRenderKit.getRenderer()  DELEGATE\n  family: " + family + "\n  rendererType: " + rendererType + "\n  renderer: " + renderer.getClass().getName());
        }
//System.out.println("D2DRenderKit.getRenderer()\n  family: " + family + "\n  rendererType: " + rendererType + "\n  renderer: " + renderer.getClass().getName());
        return renderer;
    }

    public ResponseStateManager getResponseStateManager() {
        return getDelegate().getResponseStateManager();
    }

    public ResponseWriter createResponseWriter(Writer writer, String string, String string1) {
        return getDelegate().createResponseWriter(writer, string, string1);
    }

    public ResponseStream createResponseStream(OutputStream outputStream) {
        return getDelegate().createResponseStream(outputStream);
    }
    
    private RenderKit getDelegate() {
        if (delegateRenderKit == null) {
            RenderKitFactory renderKitFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            if (renderKitFactory != null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                RenderKit delegate = renderKitFactory.getRenderKit(
                    facesContext, RenderKitFactory.HTML_BASIC_RENDER_KIT);
                delegateRenderKit = delegate;
            }
            if (delegateRenderKit == null) {
                throw new UnsupportedOperationException(
                    "Could not access standard RenderKit");
            }
        }
        return delegateRenderKit;
    }
}
