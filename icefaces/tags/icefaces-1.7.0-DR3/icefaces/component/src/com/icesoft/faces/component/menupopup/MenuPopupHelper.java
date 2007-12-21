package com.icesoft.faces.component.menupopup;

import org.w3c.dom.Element;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

/**
 * @author Mark Collette
 */
public class MenuPopupHelper {
    public static void renderMenuPopupHandler(
        FacesContext facesContext, UIComponent comp, Element elem)
    {
        StringBuffer handler = new StringBuffer(256);
        
        boolean needReturnFalse = false;
        String id = (String) comp.getAttributes().get("menuPopup");
        if(id != null && id.trim().length() > 0) {
//System.out.println("MenuPopupHelper.renderMenuPopupHandler()  menuPopup: " + id);
            UIComponent menuPopup = D2DViewHandler.findComponent(id, comp);
            if(menuPopup == null) {
                throw new IllegalArgumentException(
                    "Could not find the MenuPopup UIComponent referenced by " +
                    "attribute menuPopup=\""+id+"\" in UIComponent of type: " +
                    comp.getClass().getName() + " with id: \""+comp.getId()+"\"");
            }
            String menuPopupClientId = menuPopup.getClientId(facesContext);
//System.out.println("MenuPopupHelper.renderMenuPopupHandler()  menuPopupClientId: " + menuPopupClientId);
            handler.append("Ice.Menu.contextMenuPopup(event, '");
            handler.append(menuPopupClientId);
            handler.append("_sub');");
            needReturnFalse = true;
        }
        
        boolean haveMenuContext = checkHasMenuContext(comp);
        if(haveMenuContext) {
//System.out.println("MenuPopupHelper.renderMenuPopupHandler()  haveMenuContext");
            String originatorClientId = comp.getClientId(facesContext);
            handler.append("Ice.Menu.setMenuContext('");
            handler.append(originatorClientId);
            handler.append("');");
            //TODO I don't think we set needReturnFalse = true here, 
            //  since we only want to stop the regular popup menu if
            //  we hit a menuPopup section, not just a menuContext section 
        }
        
        if(needReturnFalse)
            handler.append("return false;");
        if(handler.length() > 0) {
//System.out.println("MenuPopupHelper.renderMenuPopupHandler()  handler: " + handler.toString());
            elem.setAttribute(HTML.ONCONTEXTMENU_ATTR, handler.toString());
        }
        else
            elem.removeAttribute(HTML.ONCONTEXTMENU_ATTR);
        // oncontextmenu="Ice.Menu.contextMenuPopup('iceform:icepnltabset:outDesc');Ice.Menu.contextMenuPopup(event, 'iceform:icepnltabset:menuP_sub');return false;"
    }
    
    public static void decodeMenuContext(FacesContext facesContext, UIComponent comp) {
//System.out.println("MenuPopupHelper.decodeMenuContext()  for: " + comp.getClientId(facesContext));
        String requestMenuContext = (String) facesContext.getExternalContext().
            getRequestParameterMap().get("ice.menuContext");
        if(requestMenuContext == null || requestMenuContext.length() == 0)
            return;
//System.out.println("MenuPopupHelper.decodeMenuContext()    requestMenuContext: " + requestMenuContext);
        boolean haveMenuContext = checkHasMenuContext(comp);
//System.out.println("MenuPopupHelper.decodeMenuContext()    haveMenuContext: " + haveMenuContext);
        if(!haveMenuContext)
            return;
        String originatorClientId = comp.getClientId(facesContext);
//System.out.println("MenuPopupHelper.decodeMenuContext()    originatorClientId: " + originatorClientId);
        if(!requestMenuContext.equals(originatorClientId))
            return;
//System.out.println("MenuPopupHelper.decodeMenuContext()    *** MATCH");
        
        MenuContextEvent inner = new MenuContextEvent(comp, null); 
        UIComponent parent = comp.getParent();
        while(parent != null) {
            boolean parentHasMenuContext = checkHasMenuContext(parent);
            if(parentHasMenuContext) {
                inner = new MenuContextEvent(parent, inner); 
            }
            parent = parent.getParent();
        }
//System.out.println("MenuPopupHelper.decodeMenuContext()  outer-most: " + inner);
        while(inner != null) {
            inner.process(facesContext);
            inner = inner.getInner();
        }
    }
    
    private static boolean checkHasMenuContext(UIComponent comp) {
        javax.faces.el.ValueBinding vb = comp.getValueBinding("menuContext");
        if(vb != null) {
//System.out.println("MenuPopupHelper.checkHasMenuContext()  menuContext: " + vb);
            String vbExp = vb.getExpressionString();
//System.out.println("MenuPopupHelper.checkHasMenuContext()  menuContext.expr: " + vbExp);
            if(vbExp != null && vbExp.length() > 0)
                return true;
        }
        return false;
    }
}
