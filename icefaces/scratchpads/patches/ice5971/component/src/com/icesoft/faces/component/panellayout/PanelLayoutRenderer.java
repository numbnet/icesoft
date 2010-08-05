/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

/*
 * 
 * AbsoluteLayout allow placement of components in absolute positions.
 * A flow layout arranges components in relative alignment.
 */
package com.icesoft.faces.component.panellayout;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class PanelLayoutRenderer extends Renderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("div", uiComponent);
        
        if(uiComponent instanceof PanelLayout){
            PanelLayout panelLayout = (PanelLayout)uiComponent;
            String modifiedStyle = getLayoutStyle(panelLayout.getStyle(), getLayoutMode(panelLayout.getLayout()));            
            writer.writeAttribute("style", modifiedStyle, "style");
            String clientId = uiComponent.getClientId(facesContext);
            writer.writeAttribute("id", clientId, "id");
            
            String styleClass = panelLayout.getStyleClass();
            if(styleClass != null && styleClass.length() > 0){
                writer.writeAttribute("class", styleClass, "styleClass");
            }
        }                    
    }

    private String getLayoutStyle(String style, int layoutMode){
        
        StringBuffer prefixStyle = new StringBuffer(" ");
        switch(layoutMode){
            case 1: 
                 prefixStyle.append("position:relative;");
                 break;
            case 2:
                 prefixStyle.append("position:absolute;");  
                 break;
        }
        
        if(style != null && style.length() > 0){
            prefixStyle.append(style);
        }
        
        return prefixStyle.toString();
    } 
    
    private int getLayoutMode(String layout){
        
        if(layout.equals(PanelLayout.FLOWLAYOUT)){
            return 1;
        }else if(layout.equals(PanelLayout.ABSOLUATELAYOUT)){
            return 2;
        }
        
        return 2;
    }
    
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        if (!component.isRendered()) return;

        com.icesoft.faces.component.util.CustomComponentUtils.renderChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }

    public boolean getRendersChildren() {
        return true;
    }
}
