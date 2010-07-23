package org.icefaces.component.tab;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.ARIA;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;

public class TabSetRenderer extends Renderer{
    private static String YUI_TABSET_INDEX = "yti";
    public boolean getRendersChildren() {
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey(YUI_TABSET_INDEX)) {
            String[] info = String.valueOf(requestParameterMap.get(YUI_TABSET_INDEX)).split("=");
            String clientId = uiComponent.getClientId(facesContext);
            TabSet tabSet = (TabSet) uiComponent;
            if (clientId.equals(info[0])) {
                try {
                    Integer index = new Integer(info[1]);
                    if (tabSet.getSelectedIndex()!= index.intValue()) { 
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, 
                                       new Integer(tabSet.getSelectedIndex()), index));
                    }
                } catch (Exception e) {}
            }
        }
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        TabSet tabSet = (TabSet) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
    }
    
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        TabSet tabSet = (TabSet) uiComponent;   
        boolean isBottom = "bottom".equals(tabSet.getOrientation());
        
        if (isBottom) {
            writer.startElement(HTML.DIV_ELEM, uiComponent);
                writer.writeAttribute(HTML.TABINDEX_ATTR, 0, HTML.TABINDEX_ATTR);
                writer.writeAttribute(HTML.ID_ATTR, clientId+"cnt", HTML.ID_ATTR);
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-content", HTML.CLASS_ATTR);
                renderTab(facesContext, uiComponent, false);
            writer.endElement(HTML.DIV_ELEM); 
        
            writer.startElement(HTML.UL_ELEM, uiComponent);
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-nav", HTML.CLASS_ATTR);
                if (EnvUtils.isAriaEnabled(facesContext)) {
                    writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.TABLIST_ROLE, ARIA.ROLE_ATTR);  
                }
                renderTab(facesContext, uiComponent, true);
            writer.endElement(HTML.UL_ELEM);
                
        } else {
            writer.startElement(HTML.UL_ELEM, uiComponent);
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-nav", HTML.CLASS_ATTR);
                if (EnvUtils.isAriaEnabled(facesContext)) {
                    writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.TABLIST_ROLE, ARIA.ROLE_ATTR);  
                }                
                renderTab(facesContext, uiComponent, true);
            writer.endElement(HTML.UL_ELEM);
            
            
            writer.startElement(HTML.DIV_ELEM, uiComponent);
                writer.writeAttribute(HTML.ID_ATTR, clientId+"cnt", HTML.ID_ATTR);
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-content", HTML.CLASS_ATTR);
                renderTab(facesContext, uiComponent, false);
            writer.endElement(HTML.DIV_ELEM);
        }
    }  
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        TabSet tabSet = (TabSet) uiComponent;        
        String clientId = uiComponent.getClientId(facesContext);
        String userDefinedClass = String.valueOf(tabSet.getAttributes().get("styleClass"));       
        String styleClass = userDefinedClass != null ? 
                userDefinedClass +" yui-navset": "yui-navset";
        String orientation = tabSet.getOrientation();
        if ("top".equalsIgnoreCase(orientation)) {
            styleClass+= " yui-navset-top";
        } else if ("left".equalsIgnoreCase(orientation)) {
            styleClass+= " yui-navset-left";
        } else if ("right".equalsIgnoreCase(orientation)) {
            styleClass+= " yui-navset-right";
        } else if ("bottom".equalsIgnoreCase(orientation)) {
            styleClass+= " yui-navset-bottom";
        } 
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        boolean isClientSide = tabSet.isClientSide();
        boolean singleSubmit = tabSet.isSingleSubmit();        
        int selectedIndex = tabSet.getSelectedIndex();
        String onupdate = tabSet.getOnupdate();
        boolean effectOnHover = tabSet.isEffectOnHover();

        
        StringBuilder call = new StringBuilder();
        call.append("ice.component.tabset.updateProperties('")
        .append(clientId)
        .append("', ")
        .append(
	        JSONBuilder.create().beginMap()
	        .entry("orientation", orientation)
	        .endMap().toString())
        .append(", ")
        .append(
	        JSONBuilder.create().beginMap()
	        .entry("isSingleSubmit", singleSubmit)
	        .entry("isClientSide", isClientSide)
	        .entry("aria", EnvUtils.isAriaEnabled(facesContext))
	        .entry("hover", effectOnHover)
	        .entry("selectedIndex", selectedIndex).endMap().toString())
        .append(");");
       
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "call", HTML.ID_ATTR); 
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none", HTML.STYLE_ATTR);         
        writer.write(call.toString());
        writer.endElement(HTML.DIV_ELEM);
     
        if (isClientSide) {
            ScriptWriter.insertScript(facesContext, uiComponent, call.toString());
        } else {
            writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, call.toString(), HTML.ONMOUSEOVER_ATTR); 
        }
        writer.endElement(HTML.DIV_ELEM);  
    }    

    
    private void renderTabNav(FacesContext facesContext, TabSet tabSet, UIComponent tab, int index) throws IOException {
        String clientId = tab.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.LI_ELEM, tab);
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.PRESENTATION_ROLE, ARIA.ROLE_ATTR);  
        }

        UIComponent labelFacet = ((Tab)tab).getLabelFacet();
        if (tabSet.getSelectedIndex() == index) {
            writer.writeAttribute(HTML.CLASS_ATTR, "selected", HTML.CLASS_ATTR);
        }
        writer.startElement(HTML.DIV_ELEM, tab);  
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.TAB_ROLE, ARIA.ROLE_ATTR);  
        }
    
        writer.writeAttribute(HTML.TABINDEX_ATTR, "0", HTML.TABINDEX_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "yui-navdiv", HTML.CLASS_ATTR);           
        writer.startElement("em", tab);
        writer.writeAttribute(HTML.ID_ATTR, clientId+ "Lbl", HTML.ID_ATTR); 
        writer.writeAttribute(HTML.ONCLICK_ATTR, "if(Ice.isEventSourceInputElement(event)) event.cancelBubble = true;", HTML.ONCLICK_ATTR);            
        
        if (labelFacet!= null)
            Utils.renderChild(facesContext, ((Tab)tab).getLabelFacet());
        else
            writer.write(String.valueOf(tab.getAttributes().get("label")));
        writer.endElement("em");
        writer.endElement(HTML.DIV_ELEM);        
   
        writer.startElement(HTML.ANCHOR_ELEM, tab);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", HTML.STYLE_ATTR); 
        writer.endElement(HTML.ANCHOR_ELEM);               
               

        
        writer.endElement(HTML.LI_ELEM);
    }    
    private void renderTabBody(FacesContext facesContext, 
            TabSet tabSet, UIComponent tab, int index) throws IOException {
        String clientId = tab.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.DIV_ELEM, tab);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.TABINDEX_ATTR, 0, HTML.TABINDEX_ATTR);
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.TABPANEL_ROLE, ARIA.ROLE_ATTR);  
        }        
        boolean isClientSide = tabSet.isClientSide();
        if (isClientSide) {
            Utils.renderChild(facesContext, tab);
        } else {
            if (tabSet.getSelectedIndex() == index) {
                Utils.renderChild(facesContext, tab);
            } else {
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-hidden iceOutConStatActv", HTML.CLASS_ATTR);
                writer.write("&nbsp;");
            }
        }
        writer.endElement(HTML.DIV_ELEM);
    }
    
    private void renderTab(FacesContext facesContext, UIComponent uiComponent, boolean isLabel) throws IOException{
        TabSet tabSet = (TabSet) uiComponent;
        Iterator children = tabSet.getChildren().iterator();
        int index = -1;
        while (children.hasNext()) {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof Tab) {
                if (child.isRendered()) {
                    index++;
                    if(isLabel) {
                        renderTabNav(facesContext, tabSet, child, index);                        
                    } else {
                        renderTabBody(facesContext, tabSet, child, index);
                    }

                } else {
                    //write script node to remove this tab
                }
            } else if (child instanceof Tabs) {
                Tabs uiList = (Tabs) child;
                int rowIndex = uiList.getFirst();
                int numberOfRowsToDisplay = uiList.getRows();
                int countOfRowsDisplayed = 0;
                while (  ( numberOfRowsToDisplay == 0 ) ||
                         ( (numberOfRowsToDisplay > 0) &&
                           (countOfRowsDisplayed < numberOfRowsToDisplay) )  )
                {
                     uiList.setRowIndex(rowIndex);
                     if(!uiList.isRowAvailable()){
                        break;
                    }
                    Iterator childs;
                    if (uiList.getChildCount() > 0) {
                        childs = uiList.getChildren().iterator();
                        while (childs.hasNext()) {
                            UIComponent nextChild = (UIComponent) childs.next();
                            if (nextChild.isRendered()) {
                                index++;
                                if(isLabel) {
                                    renderTabNav(facesContext, tabSet, nextChild, index);                        
                                } else {
                                    renderTabBody(facesContext, tabSet, nextChild, index);
                                }
                            }
                        }
                    }
                    rowIndex++;
                    countOfRowsDisplayed++;
                }
                uiList.setRowIndex(-1);
            }
        }
    }
}   
