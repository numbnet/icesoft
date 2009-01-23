package com.icesoft.faces.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpSession;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.context.View;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 *
 */
public class ViewRootStateManagerImpl extends StateManager {

    protected static Log log = LogFactory.getLog(ViewRootStateManagerImpl.class);

    private StateManager delegate;

    public ViewRootStateManagerImpl(StateManager delegate) {
        if (log.isDebugEnabled()) {
            log.debug("ViewRootStateManagerImpl constructed with Delegate: " + delegate);
        }
        this.delegate = delegate;
    }

    /**
     * Override the restoreView method to fetch the ViewRoot from the session
     * and restore it. The UIViewRoot in the session Map is keyed by ICEfaces viewNumber 
     * @param context
     * @param viewId
     * @param renderKitId
     * @return The restored ViewRoot, null if none saved for this ICEfaces viewNumber
     */
   public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {

        if ( !(context instanceof BridgeFacesContext) ) {
            throw new IllegalStateException("FacesContext not instance of BridgeFacesContext");
        }

        BridgeFacesContext bfc = (BridgeFacesContext) context;
        String viewNumber = bfc.getViewNumber();
        if (log.isDebugEnabled()) {
            log.debug("RestoreView called for View: " + bfc.getIceFacesId() + ", viewNumber: " + viewNumber );
        } 

        HttpSession session = (HttpSession) bfc.getExternalContext().getSession(false);
        Map stateMap = (Map) session.getAttribute(  View.ICEFACES_STATE_MAPS );
        if (stateMap == null) {
            stateMap  = new HashMap();
            session.setAttribute( View.ICEFACES_STATE_MAPS, stateMap );
        }

        UIViewRoot root;

        root = (UIViewRoot) stateMap.get( viewNumber );
        if (root == null) {
            log.error("Missing ViewRoot in restoreState, ice.session: " + bfc.getIceFacesId() + ", viewNumber: " + viewNumber );
            return null;
        }
        return root;
    }


    /**
     * Defer to the current strategy for saving the View
     * @param context
     * @return
     */
    public Object saveView(FacesContext context ) {

        UIViewRoot root = context.getViewRoot();

        if ( !(context instanceof BridgeFacesContext) ) {
            throw new IllegalStateException("FacesContext not instance of BridgeFacesContext");
        }

        BridgeFacesContext bfc = (BridgeFacesContext) context;
        String viewNumber = bfc.getViewNumber();

        HttpSession session = (HttpSession) bfc.getExternalContext().getSession(false);
        Map stateMap = (Map) session.getAttribute(  View.ICEFACES_STATE_MAPS );
        if (stateMap == null) {
            stateMap  = new HashMap();
            session.setAttribute(View.ICEFACES_STATE_MAPS, stateMap );
        }
        stateMap.put( viewNumber, root );

        StateManager sm = context.getApplication().getStateManager();
        return sm.new SerializedView( viewNumber, null);
    }

    public SerializedView saveSerializedView(FacesContext context) {
        return (SerializedView) saveView(context);
    }



    /*
        The following methods come from the eventual ResponseStateManager.
        Standard boilerplate disclaimer. We want to eventually support fully
        the RenderKit implementation. There's no way to 'use' the existing JSF
        implementations without linking in the jsf-ri jars at compile time, so
        to keep myfaces compatibility we are forced to copy it and implement
        the portions we want.
     */
    final java.lang.String VIEW_STATE_PARAM = "javax.faces.ViewState";

    final char[] STATE_FIELD_START =
            ("<input type=\"hidden\" name=\""
             + VIEW_STATE_PARAM
             + "\" id=\""
             + VIEW_STATE_PARAM
             + "\" value=\"").toCharArray();

    final char[] STATE_FIELD_END =
            "\" />".toCharArray();


    /**
     * Do this here until we implement the full RenderKit deal.  
     *
     * @param context
     * @param view
     * @throws java.io.IOException
     */
    public void writeState(FacesContext context, SerializedView view)
    throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        writer.write(STATE_FIELD_START);
        writer.write( view.getStructure().toString());
        writer.write(STATE_FIELD_END);
        writeRenderKitIdField(context, writer);
    }



    /**
     * Write the renderKit id to the stream as well.
     * @param context
     * @param writer
     * @throws java.io.IOException
     */
    public static void writeRenderKitIdField(FacesContext context,
                                              ResponseWriter writer)
            throws IOException {
        String result = context.getApplication().getDefaultRenderKitId();
        if (result != null &&
            !RenderKitFactory.HTML_BASIC_RENDER_KIT.equals(result)) {
            writer.startElement("input", context.getViewRoot());
            writer.writeAttribute("type", "hidden", "type");
            writer.writeAttribute("name",
                                  "javax.faces.RenderKitId",
                                  "name");
            writer.writeAttribute("value",
                                  result,
                                  "value");
            writer.endElement("input");
        }
    }

    public void restoreComponentState(FacesContext context,
                                      UIViewRoot viewRoot,
                                      String renderKitId) {
    }

    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId,
                                              String renderKitId) {
        return null;
    }

    protected Object getComponentStateToSave(FacesContext context) {
        return null;
    }

    protected Object getTreeStructureToSave(FacesContext context) {
        return null;
    }
}
