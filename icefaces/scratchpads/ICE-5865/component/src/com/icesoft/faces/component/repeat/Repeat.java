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

package com.icesoft.faces.component.repeat;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import com.icesoft.faces.component.panelseries.UISeries;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

public class Repeat extends UISeries{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Repeat";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.Repeat";
    public Repeat() {
        setRendererType(null);        
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public boolean getRendersChildren() {
        return true;
    }
    
    public void encodeChildren(FacesContext context) throws IOException {
        int rowIndex = getFirst();
       
        int numberOfRowsToDisplay = getRows();
        int countOfRowsDisplayed = 0;
        while (  ( numberOfRowsToDisplay == 0 ) ||
                 ( (numberOfRowsToDisplay > 0) &&
                   (countOfRowsDisplayed < numberOfRowsToDisplay) )  )
        {
             setRowIndex(rowIndex);
             if(!isRowAvailable()){
                break;
            }
            Iterator childs;
            if (getChildCount() > 0) {
                childs = getChildren().iterator();
                while (childs.hasNext()) {
                    UIComponent nextChild = (UIComponent) childs.next();
                    if (nextChild.isRendered()) {
                        DomBasicRenderer.encodeParentAndChildren(context, nextChild);
                    }
                }
            }
            rowIndex++;
            countOfRowsDisplayed++;
        }
        setRowIndex(-1);        
    }
    
    /*
     *  (non-Javadoc)
     * @see com.icesoft.faces.component.panelseries.UISeries#restoreChild(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    protected void restoreChild(FacesContext facesContext,
                                UIComponent uiComponent) {
        super.restoreChild(facesContext, uiComponent);
        if (uiComponent instanceof UIData) {
            String clientId = uiComponent.getClientId(facesContext);
            Object value = savedChildren.get(clientId);
            ((UIData) uiComponent).setValue(value);
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see com.icesoft.faces.component.panelseries.UISeries#saveChild(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    protected void saveChild(FacesContext facesContext,
                             UIComponent uiComponent) {
        super.saveChild(facesContext, uiComponent);
        if (uiComponent instanceof UIData) {
            String clientId = uiComponent.getClientId(facesContext);
            savedChildren.put(clientId, ((UIData) uiComponent).getValue());
        }
    }

}
