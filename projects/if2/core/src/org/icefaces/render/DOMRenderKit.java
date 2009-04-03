package org.icefaces.render;

import java.util.Iterator;
import java.io.Writer;
import java.io.OutputStream;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;
import javax.faces.render.ResponseStateManager;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.render.ClientBehaviorRenderer;

import org.icefaces.context.DOMResponseWriter;

public class DOMRenderKit extends RenderKit {
    RenderKit delegate;

    public DOMRenderKit(RenderKit delegate)  {
System.out.println("DOMRenderKit wrapping " + delegate);
        this.delegate = delegate;
    }

    public DOMRenderKit()  {
System.out.println("DOMRenderKit without decoration constructor");
    RenderKitFactory factory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        delegate = factory.getRenderKit(null, RenderKitFactory.HTML_BASIC_RENDER_KIT);
System.out.println("        delegate is " + delegate);
        }
    
    public RenderKit getWrapped()  {
        return delegate;
    }

    public  void addRenderer(String family, String type, Renderer renderer)  {
        delegate.addRenderer(family, type, renderer);
    }

    public Renderer getRenderer(String family, String type)  {
//        System.out.println("getRenderer " + family + " " + type);
        return delegate.getRenderer(family, type);
    }

    public ResponseStateManager getResponseStateManager()  {
        return delegate.getResponseStateManager();
    }

    public  ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding)  {
        System.out.println("creating DOMResponseWriter wrapping " + writer.getClass());
        ResponseWriter responseWriter = new DOMResponseWriter(writer);
//        return delegate.createResponseWriter(writer, contentTypeList, encoding);
        return responseWriter;
    }

    public ResponseStream createResponseStream(OutputStream out)  {
        return delegate.createResponseStream(out);
    }
    
    public Iterator getComponentFamilies()  {
        System.out.println("getComponentFamilies ");
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