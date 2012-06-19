package org.icefaces.mobi.component.layoutmenu;

import org.icefaces.mobi.component.contentpane.ContentPane;
import org.icefaces.mobi.component.contentstack.ContentStack;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class LayoutMenuRenderer extends BaseLayoutRenderer {

    private static Logger logger = Logger.getLogger(LayoutMenuRenderer.class.getName());
    private static final String JS_NAME = "layoutmenu.js";
    private static final String JS_MIN_NAME = "layoutmenu-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.layoutmenu";


  /*  public void decode(FacesContext facesContext, UIComponent uiComponent) {
         Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
         LayoutMenu menu = (LayoutMenu) uiComponent;
         String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
         Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
         String clientId = menu.getClientId();
         String inputStr = params.get(clientId + "_hidden");
         if( null != inputStr) {
             //find the activeIndex and set it
             //going to have to validate on client....
         }
    } */

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        LayoutMenu menu = (LayoutMenu) uiComponent;
      	UIComponent form = Utils.findParentForm(uiComponent);
   		if(form == null) {
			throw new FacesException("LayoutMenu : \"" + clientId + "\" must be inside a form element");
		}
         // root element
         writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         // apply button type style classes
         StringBuilder baseClass = new StringBuilder(LayoutMenu.LAYOUTMENU_CLASS);
         StringBuilder listClass = new StringBuilder(LayoutMenu.LAYOUTMENU_LIST_CLASS);
         String userDefinedClass = menu.getStyleClass();
         if (null != userDefinedClass) {
             baseClass.append(userDefinedClass);
             listClass.append(userDefinedClass);
         }
         writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);

         // should be auto base though
         writer.startElement(HTML.UL_ELEM, uiComponent);
         String selectedPane = null;
         if (null!=menu.getSelectedPane()) {
             selectedPane = menu.getSelectedPane();
         }
         //if still null, then get the first item in the contentStack??
         writer.writeAttribute("data-current",selectedPane , null);
         writer.writeAttribute(HTML.CLASS_ATTR, listClass.toString(), HTML.CLASS_ATTR);
         if (menu.getVar() != null) {
            menu.setRowIndex(-1);
            for (int i = 0; i < menu.getRowCount(); i++) {
                //assume that if it's a list of items then it's grouped
                menu.setRowIndex(i);
                // option can't have children tags but can be disabled ...not too sure what to do about that
                /* check to see that only child can be LayoutMenuItem?  */
                renderChildren(facesContext, menu);
            }
            menu.setRowIndex(-1);
        }  else {
             //doing it with indiv LayoutMenuItem tag's
             renderChildren(facesContext, menu);
         }
        writer.endElement(HTML.UL_ELEM);
        this.encodeHidden(facesContext, uiComponent);
        writer.endElement(HTML.DIV_ELEM);
  //      encodeScript(facesContext,  uiComponent);
    }


    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
         //Rendering happens on encodeEnd
    }


    @Override
    public boolean getRendersChildren() {
        return true;
    }


}
