/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.menubutton;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.JSFUtils;
import org.icemobile.component.IMenuButton;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.MenuButtonCoreRenderer;


public class MenuButtonRenderer extends BaseLayoutRenderer {
    private static final Logger logger = Logger.getLogger(MenuButtonRenderer.class.getName());


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        MenuButton menu = (MenuButton) uiComponent;
     	UIComponent form = JSFUtils.findParentForm(uiComponent);
   	    if(form == null) {
	        throw new FacesException("MenuButton : \"" + clientId + "\" must be inside a form element");
		}
         // root element
        IMenuButton imenu = (IMenuButton)uiComponent;
        IResponseWriter writer2 = new ResponseWriterWrapper(facesContext.getResponseWriter());
        MenuButtonCoreRenderer renderer = new MenuButtonCoreRenderer();
        renderer.encodeBegin(imenu, writer2) ;
        if (null != imenu.getSelectTitle()) {
            renderer.encodeSelectTitle(imenu, writer2);
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

        }  else {
             //doing it with indiv menuButtonItem tag's
             renderChildren(facesContext, menu);

        }
        renderer.encodeEnd(imenu, writer2);
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
