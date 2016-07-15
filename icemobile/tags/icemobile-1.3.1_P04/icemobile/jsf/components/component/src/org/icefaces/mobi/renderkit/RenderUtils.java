/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.renderkit;

import static org.icefaces.mobi.utils.HTML.BUTTON_ELEM;
import static org.icefaces.mobi.utils.HTML.CLASS_ATTR;
import static org.icefaces.mobi.utils.HTML.DISABLED_ATTR;
import static org.icefaces.mobi.utils.HTML.ID_ATTR;
import static org.icefaces.mobi.utils.HTML.NAME_ATTR;
import static org.icefaces.mobi.utils.HTML.STYLE_ATTR;
import static org.icefaces.mobi.utils.HTML.TYPE_ATTR;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

public class RenderUtils {
    
    public static void writeStyle(UIComponent uicomp, ResponseWriter writer)
        throws IOException{
        String style = (String)uicomp.getAttributes().get(STYLE_ATTR);
        if (style != null){
            writer.writeAttribute(STYLE_ATTR, style, STYLE_ATTR);
        }
        
    }
    
    public static void writeStyleClass(UIComponent uicomp, ResponseWriter writer)
        throws IOException{
        String styleClass = (String)uicomp.getAttributes().get("styleClass");
        if (styleClass != null){
            writer.writeAttribute(STYLE_ATTR, styleClass, STYLE_ATTR);
        }
    }
    
    public static void writeStyleClassAndBase(UIComponent uicomp, ResponseWriter writer, String baseClass)
        throws IOException{
        String styleClass = (String)uicomp.getAttributes().get("styleClass");
        String classList = baseClass;
        if (styleClass != null){
            classList += " " + styleClass;
        }
        writer.writeAttribute(CLASS_ATTR, classList, "styleClass");
    }
    
    public static void writeDisabled(UIComponent uicomp, ResponseWriter writer)
        throws IOException{
        Boolean disabled = (Boolean)uicomp.getAttributes().get(DISABLED_ATTR);
        if ( disabled != null && disabled.booleanValue()){
            writer.writeAttribute(DISABLED_ATTR, DISABLED_ATTR, DISABLED_ATTR);
        }
    }
    
    public static void writeTabIndex(UIComponent uicomp, ResponseWriter writer)
        throws IOException{
        Integer tabIndex = (Integer)uicomp.getAttributes().get("tabindex");
        if (tabIndex != null){
            writer.writeAttribute("tabindex", tabIndex.intValue(), "tabIndex");
        }
    }
    
    public static void startButtonElem(UIComponent uicomp, ResponseWriter writer)
        throws IOException{
        String clientId = uicomp.getClientId();
        writer.startElement(BUTTON_ELEM, uicomp);
        writer.writeAttribute(ID_ATTR, clientId, ID_ATTR);
        writer.writeAttribute(NAME_ATTR, clientId + "_button", NAME_ATTR);
        writer.writeAttribute(TYPE_ATTR, "button", null);
    }
    
    

}
