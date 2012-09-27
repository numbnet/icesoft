package org.icefaces.demo.elementUpdate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;

@ManagedBean(name = "BodyBean")
@SessionScoped
public class BodyBean {

    public void generateBodyUpdate() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewBody");
                writer.startElement("body", null);
                writer.writeAttribute("class", "foo", "class");
                writer.writeAttribute("title", "fooTitle", "title");
                writer.writeAttribute("lang", "fooLang", "lang");
                writer.startElement("span", null);
                writer.writeAttribute("id", "status", "id");
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.startEval();
                writer.write("var body = document.getElementsByTagName('body')[0];");
                writer.write("document.getElementById('status').innerHTML='BODY CLASS:'+body.className+' BODY TITLE:'+body.title+' BODY LANG:'+body.lang");
                writer.endEval();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
