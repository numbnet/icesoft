/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.component.tab;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.component.animation.ClientBehaviorContextImpl;
import org.icefaces.component.animation.AnimationBehavior;
import org.icefaces.component.utils.ARIA;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent("org.icefaces.component.tab.TabSet")
public class TabSetRenderer extends Renderer{
    private static String YUI_TABSET_INDEX = "yti";
    public boolean getRendersChildren() {
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        //one field per form will be use to send tabindex info
        if (requestParameterMap.containsKey(YUI_TABSET_INDEX)) {
        	//the value of yti should look something like this "clientId=tabindex"
            String[] info = String.valueOf(requestParameterMap.get(YUI_TABSET_INDEX)).split("=");
            String clientId = uiComponent.getClientId(facesContext);
            TabSet tabSet = (TabSet) uiComponent;
            //info[0] is containing a clientId of a tabset
            if (clientId.equals(info[0])) {
                try {
                	//info[1] is containing a tabindex
                    Integer index = new Integer(info[1]);
                    if (tabSet.getSelectedIndex()!= index.intValue()) { 
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, 
                                       new Integer(tabSet.getSelectedIndex()), index));
                    }
                } catch (Exception e) {}
            }
        }
        //The decode of the component's behavior should be invoked by the component's decode
        Utils.iterateEffects(new AnimationBehavior.Iterator(uiComponent) {
			public void next(String name, AnimationBehavior effectBehavior) {
				effectBehavior.decode(FacesContext.getCurrentInstance(), this.getUIComponent());				
			}
		});
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        TabSet tabSet = (TabSet) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        //tabset's toot div
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        String style = tabSet.getStyle();
        if(style != null){
        	writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }        
    }
    
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        TabSet tabSet = (TabSet) uiComponent;   
        boolean isBottom = "bottom".equals(tabSet.getOrientation());
        
        //As per YUI's contract if the orientation is set to bottom, the contents of the tab
        //should ger rendered first, and then tabs
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
        final TabSet tabSet = (TabSet) uiComponent;        
        String clientId = uiComponent.getClientId(facesContext);
        // ICE-6703: default style classes should be for the top orientation of tabs. (Plus space at the end.)
        String styleClass = "yui-navset yui-navset-top ";
        
        String orientation = tabSet.getOrientation();
        // ICE-6703: top, invalid or unspecified orientation should all use default style classes defined above.
        if ("left".equalsIgnoreCase(orientation)) {
            styleClass= "yui-navset yui-navset-left ";
        } else if ("right".equalsIgnoreCase(orientation)) {
            styleClass= "yui-navset yui-navset-right ";
        } else if ("bottom".equalsIgnoreCase(orientation)) {
            styleClass= "yui-navset yui-navset-bottom ";
        } 
        Object userDefinedClass = tabSet.getAttributes().get("styleClass"); 
        if (userDefinedClass != null ) 
        		styleClass+= userDefinedClass.toString() ;
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        boolean isClientSide = tabSet.isClientSide();
        boolean singleSubmit = tabSet.isSingleSubmit(); 

        int selectedIndex = tabSet.getSelectedIndex();
        //see what the selectedIndex is
        if (selectedIndex >= getRenderedChildCount(tabSet)) {
        	selectedIndex = 0;
        }

        
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
	        .entry("selectedIndex", selectedIndex).endMap().toString())
        .append(");");
       
////        //Initialize client side tab as soon as they inserted to the DOM
////        if (isClientSide) {
            ScriptWriter.insertScript(facesContext, uiComponent, call.toString());
////        } else {
////        	//initialize server side tab lazily
////            writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, call.toString(), HTML.ONMOUSEOVER_ATTR);
////        }
        
        //encode animation if any 
        final StringBuilder effect = new StringBuilder();
        Utils.iterateEffects(new AnimationBehavior.Iterator(uiComponent) {
			public void next(String event, AnimationBehavior effectBehavior) {
				effectBehavior.encodeBegin(FacesContext.getCurrentInstance(), tabSet);
				effect.append(effectBehavior.getScript(new ClientBehaviorContextImpl(this.getUIComponent(), "transition"), false));	
			}
		});
        
        //encode animation call if any
        if (effect.toString().length()> 0) {
        	ScriptWriter.insertScript(facesContext, uiComponent, effect.toString());
        }
        writer.endElement(HTML.DIV_ELEM);  
    }    

   /*
    * renders tab headers 
    */
    private void renderTabNav(FacesContext facesContext, TabSet tabSet, UIComponent tab, int index) throws IOException {

    	String clientId = tab.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.LI_ELEM, tab);
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.PRESENTATION_ROLE, ARIA.ROLE_ATTR);  
        }
        writer.writeAttribute(HTML.ID_ATTR, clientId+ "li"+ index, HTML.ID_ATTR);
        UIComponent labelFacet = ((TabPane)tab).getLabelFacet();
////        //if a server side tabset? then apply selected tab class
////        if (!tabSet.isClientSide()) {
////	        if (tabSet.getSelectedIndex() == index) {
////	            writer.writeAttribute(HTML.CLASS_ATTR, "selected", HTML.CLASS_ATTR);
////	        }
////        }
        if (tabSet.isDisabled() || ((TabPane) tab).isDisabled()) {
            writer.writeAttribute(HTML.CLASS_ATTR, "disabled", HTML.CLASS_ATTR);
        }
        writer.startElement(HTML.DIV_ELEM, tab);  
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute(ARIA.ROLE_ATTR, ARIA.TAB_ROLE, ARIA.ROLE_ATTR);  
        }
        //Server side tab initializes lazily on mouse hover, here we are covering lazy initialization on focus
////        if (!tabSet.isClientSide()) {
////        	writer.writeAttribute(HTML.ONFOCUS_ATTR, "this.parentNode.parentNode.parentNode.onmouseover()", HTML.ONFOCUS_ATTR);
////        }
        writer.writeAttribute(HTML.ID_ATTR, clientId+ "tab"+ index, HTML.ID_ATTR); 
        writer.writeAttribute(HTML.TABINDEX_ATTR, "0", HTML.TABINDEX_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "yui-navdiv", HTML.CLASS_ATTR);           
        writer.startElement("em", tab);
        writer.writeAttribute(HTML.ID_ATTR, clientId+ "Lbl", HTML.ID_ATTR); 
        //tab header can have input elements, we don't want tab to consume any event that was initiated by an input 
        writer.writeAttribute(HTML.ONCLICK_ATTR, "if(Ice.isEventSourceInputElement(event)) event.cancelBubble = true;", HTML.ONCLICK_ATTR);            
        
        if (labelFacet!= null)
            Utils.renderChild(facesContext, ((TabPane)tab).getLabelFacet());
        else
            writer.write(String.valueOf(tab.getAttributes().get("label")));
        writer.endElement("em");
        writer.endElement(HTML.DIV_ELEM);        
   
        //this is to making a tab focusable.
        writer.startElement(HTML.ANCHOR_ELEM, tab);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", HTML.STYLE_ATTR); 
        writer.endElement(HTML.ANCHOR_ELEM);               
        
        writer.endElement(HTML.LI_ELEM);
    }    
    
    /*
     * Renders tab contents/body
     */
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
        //Render all tabs and their contents for clientSide tabset 
        if (isClientSide) {
            Utils.renderChild(facesContext, tab);
        } else {
        	//Render selected tab only for server side tab
            if (tabSet.getSelectedIndex() == index) {
                final StringBuilder style = new StringBuilder();
                //apply style on a tab body if it was modified by an animation 
                Utils.iterateEffects(new AnimationBehavior.Iterator(tabSet) {
        			public void next(String name, AnimationBehavior effectBehavior) {
        		        if (effectBehavior.getStyle() != null) {
        		        	style.append(effectBehavior.getStyle());
        		        	style.append(";");
        		        }
        			}
        		});
                if (style.toString().length() > 0) {
                	writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
                }
                Utils.renderChild(facesContext, tab);
            } else {
            	//add hidden class to tabs that are not selected. 
                writer.writeAttribute(HTML.CLASS_ATTR, "yui-hidden iceOutConStatActv", HTML.CLASS_ATTR);
                writer.write(HTML.NBSP_ENTITY);
            }
        }
        writer.endElement(HTML.DIV_ELEM);
    }
    
    /*
     * Render children of tabset component
     */
    private void renderTab(FacesContext facesContext, UIComponent uiComponent, boolean isLabel) throws IOException{
    	TabSet tabSet = (TabSet) uiComponent;
        Iterator children = tabSet.getChildren().iterator();
        int index = -1;
        while (children.hasNext()) {
            UIComponent child = (UIComponent)children.next();
            //render tabpane component
            if (child instanceof TabPane) {
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
                
                //if tabs component found, iterate through its modal and render all child. 
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
    
    private int getRenderedChildCount(UIComponent uiComponent) {
    	int count = 0;
    	for (UIComponent component: uiComponent.getChildren()) {
    		if (component instanceof TabPane && component.isRendered()) {
    			count++;
    		}
    	}
    	return count;
    }
}   
