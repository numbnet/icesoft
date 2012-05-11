package org.icefaces.impl.event;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

class JavascriptResourceOutput extends UIOutputWriter {
    private final static String JAVAX_FACES_RESOURCE_SCRIPT = "javax.faces.resource.Script";
    protected String script;

    public JavascriptResourceOutput() {
    }

    public JavascriptResourceOutput(ResourceHandler resourceHandler,
                                    String name, String library,
                                    String version) {
        Resource r = resourceHandler.createResource(name, fixResourceParameter(library));
        String path = r.getRequestPath();
        if (version == null) {
            script = path;
        } else {
            if (path.contains("?")) {
                script = path + "&v=" + version;
            } else {
                script = path + "?v=" + version;
            }
        }
        Map attributes = getAttributes();
        //save parameters in attributes since they are checked by the code replacing the @ResourceDepencency components
        attributes.put("name", name);
        if (library != null) {
            attributes.put("library", library);
        }
        if (version != null) {
            attributes.put("version", version);
        }
        this.setTransient(true);
    }

    public void encode(ResponseWriter writer, FacesContext context) throws IOException {
        writer.startElement("script", this);
        //define potential script entries
        //encode URL, some portals are rewriting the URLs radically
        writer.writeAttribute("src", context.getExternalContext().encodeResourceURL(script), null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.endElement("script");
    }

    //Convince PortletFaces Bridge that this is a valid script for
    //inserting into the Portal head
    public String getRendererType() {
        return JAVAX_FACES_RESOURCE_SCRIPT;
    }

    private static String fixResourceParameter(String value) {
        return value == null || "".equals(value) ? null : value;
    }
}