
package org.icefaces.context;

import java.util.Iterator;
import java.io.IOException;
import java.io.Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.icefaces.util.DOMUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.PartialResponseWriter;


public class DOMPartialViewContext extends com.sun.faces.context.PartialViewContextImpl  {
    FacesContext facesContext;
    PartialResponseWriter partialWriter = null;

    public DOMPartialViewContext(FacesContext facesContext)  {
        super(facesContext);
        this.facesContext = facesContext;
    }
/*
    public abstract Collection<String> getExecuteIds();
    
    public abstract Collection<String> getRenderIds();
*/

    public PartialResponseWriter getPartialResponseWriter()  {
        if (null == partialWriter)  {
            try {
                //TODO: ensure this can co-exist with other PartialViewContext implementations
                Writer outputWriter = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
                ResponseWriter basicWriter = new com.sun.faces.renderkit.html_basic.HtmlResponseWriter(outputWriter, "text/html", "utf-8");
                partialWriter  = new PartialResponseWriter(basicWriter);
            } catch (Exception e)  {
                e.printStackTrace();
            }
        }
        return partialWriter;
    }
    
/*

    public abstract boolean isAjaxRequest();

    public abstract boolean isPartialRequest();

    public abstract boolean isExecuteAll();

    public abstract boolean isRenderAll();

    public abstract void setRenderAll(boolean renderAll);
    
    public abstract void setPartialRequest(boolean isPartialRequest);

    public abstract void release();

*/
    public void processPartial(PhaseId phaseId)  {
        if (isRenderAll() && (phaseId == PhaseId.RENDER_RESPONSE))  {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                ResponseWriter orig = facesContext.getResponseWriter();
                //TODO: understand why the original writer must be restored
                //it may be for error handling cases
                //facesContext.getAttributes().put(ORIGINAL_WRITER, orig);
                Writer outputWriter = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
                DOMResponseWriter writer = new DOMResponseWriter(outputWriter);
                facesContext.setResponseWriter(writer);

                ExternalContext exContext = facesContext.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");
                
                Document oldDOM = writer.getOldDocument();

                UIViewRoot viewRoot = facesContext.getViewRoot();
                writer.startDocument();
                Iterator<UIComponent> itr = viewRoot.getFacetsAndChildren();
                while (itr.hasNext()) {
                    UIComponent kid = itr.next();
                    kid.encodeAll(facesContext);
                }
                Document document = writer.getDocument();
                writer.endDocument();
                
                //the valid old document from the current pass is the new document 
                Document newDOM = writer.getOldDocument();

                Node[] diffs = DOMUtils.domDiff(oldDOM, newDOM);
                if ((null != diffs) && (/*strange bug for now*/ diffs.length == 1))  {
                    partialWriter.startDocument();
                    for (int i=0; i <diffs.length; i++)  {
                        Element element = (Element) diffs[i];
                        partialWriter.startUpdate(element.getAttribute("id"));
                        DOMUtils.printNode(element, outputWriter);
                        partialWriter.endUpdate();
                    }
                } else {
                    partialWriter.startDocument();
                    partialWriter.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
                    DOMUtils.printNode(document, outputWriter);
                    partialWriter.endUpdate();
                }
                renderState(facesContext);
                partialWriter.endDocument();
                return;
            } catch (IOException ex) {
                    //should put back the original ResponseWriter
//                this.cleanupAfterView();
            } catch (RuntimeException ex) {
//                this.cleanupAfterView();
                // Throw the exception
                throw ex;
            }


        } else {
            super.processPartial(phaseId);
        }
    }

    private void renderState(FacesContext context) throws IOException {

        // Get the view state and write it to the response..
        PartialResponseWriter writer = getPartialResponseWriter();
        writer.startUpdate(PartialResponseWriter.VIEW_STATE_MARKER);
        String state = context.getApplication().getStateManager().getViewState(context);
        writer.write(state);
        writer.endUpdate();

    }

}
