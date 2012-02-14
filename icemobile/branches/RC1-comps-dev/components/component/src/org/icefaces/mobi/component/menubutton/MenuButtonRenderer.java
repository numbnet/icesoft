package org.icefaces.mobi.component.menubutton;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import sun.tools.tree.ThisExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.el.ExpressionFactory;
import javax.el.ELContext;
import javax.faces.FacesException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MenuButtonRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(MenuButtonRenderer.class.getName());
    private static final String JS_NAME = "menubutton.js";
    private static final String JS_MIN_NAME = "menubutton-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.button";


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
         Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
         MenuButton menu = (MenuButton) uiComponent;
         String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
         String clientId = menu.getClientId();
         if (clientId.equals(source)) {
             try {
                 if (!menu.isDisabled()) {
                     /*  currently using submit of menuButtonItem
                     find the item in the list and queue the event for it*/
                 }
             } catch (Exception e) {
                 logger.warning("Error queuing MenuButtonItem event");
             }
         }
     }

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         MenuButton menu = (MenuButton) uiComponent;
      	UIComponent form = Utils.findParentForm(uiComponent);
   		if(form == null) {
			throw new FacesException("MenuButton : \"" + clientId + "\" must be inside a form element");
		}
         // root element
         writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         // apply button type style classes
         StringBuilder baseClass = new StringBuilder(MenuButton.BASE_STYLE_CLASS);
         StringBuilder buttonClass = new StringBuilder(MenuButton.BUTTON_STYLE_CLASS) ;
         StringBuilder selectClass = new StringBuilder(MenuButton.MENU_SELECT_CLASS);
         String userDefinedClass = menu.getStyleClass();
         if (null != userDefinedClass) {
             baseClass.append(userDefinedClass);
             selectClass.append(userDefinedClass);
             buttonClass.append(userDefinedClass);
         }
         // apply disabled style state if specified.
         if (menu.isDisabled()) {
             baseClass.append(MenuButton.DISABLED_STYLE_CLASS);
         }
         writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);

         // should be auto base though
         writer.startElement(HTML.SPAN_ELEM, uiComponent);
         writer.writeAttribute(HTML.CLASS_ATTR, selectClass.toString(), HTML.CLASS_ATTR);
         writer.writeAttribute(HTML.ID_ATTR, clientId+"_btn", HTML.ID_ATTR);
         writer.startElement(HTML.SPAN_ELEM, uiComponent);
         String selectLabel = menu.getButtonLabel();
         writer.write(selectLabel);
         writer.endElement(HTML.SPAN_ELEM);
         writer.endElement(HTML.SPAN_ELEM);
         if (menu.isDisabled()) {
             writer.writeAttribute("disabled", "disabled", null);
             writer.endElement(HTML.DIV_ELEM);
             return;
         }
         //setup config object
         writer.startElement(HTML.SELECT_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId+"_sel", HTML.ID_ATTR);
         writer.writeAttribute(HTML.CLASS_ATTR, selectClass, HTML.CLASS_ATTR);
         writer.writeAttribute(HTML.ONCHANGE_ATTR, "mobi.menubutton.select('"+clientId+"');", HTML.ONCHANGE_ATTR);
         if (null!=menu.getStyle()){
             String style= menu.getStyle();
             if ( style.trim().length() > 0) {
                 writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
             }
         }
         if (menu.getVar() != null) {
            menu.setRowIndex(-1);
            for (int i = 0; i < menu.getRowCount(); i++) {
                //assume that if it's a list of items then it's grouped
                menu.setRowIndex(i);
                // option can't have children tags but can be disabled ...not too sure what to do about that
                /* check to see that only child can be MenuButtonItem?  */
                   renderChildren(facesContext, menu);
            }
            menu.setRowIndex(-1);
            writer.endElement(HTML.SELECT_ELEM);
        }  else {
             //doing it with indiv menuButtonItem tag's
             renderChildren(facesContext, menu);
         }
         writer.endElement(HTML.DIV_ELEM);

     }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
         //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeScript(FacesContext context, UIComponent uiComponent) throws IOException{
            //need to initialize the component on the page and can also
         ResponseWriter writer = context.getResponseWriter();
         MenuButton menu = (MenuButton) uiComponent;
         String clientId = menu.getClientId(context);
         if (BaseLayoutRenderer.menuItemCfg.containsKey(clientId)){
             writer.startElement("span", uiComponent);
             writer.writeAttribute("id", clientId+"_initScr", "id");
             writer.startElement("script", null);
             writer.writeAttribute("text", "text/javascript", null);
             for (Map.Entry<String, StringBuilder> entry: BaseLayoutRenderer.menuItemCfg.entrySet()){
                  writer.write(entry.toString());
             }
             writer.endElement("script");
             writer.endElement("span");
         }
     }

}
