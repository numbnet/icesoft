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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.KeyEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.menubar.MenuItem;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;

import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

public class SecretRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.SecretRenderer {
    
    
    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 HashSet excludes) {
        
        //exclude following events
        excludes.add("onkeypress");
        excludes.add("onfocus");
        excludes.add("onblur");
        
        String onkeypress = ((HtmlInputSecret)uiComponent).getOnkeypress() != null ? ((HtmlInputSecret)uiComponent).getOnkeypress() : "";
        String onfocus = ((HtmlInputSecret)uiComponent).getOnfocus() != null ? ((HtmlInputSecret)uiComponent).getOnfocus() : "";
        String onblur = ((HtmlInputSecret)uiComponent).getOnblur() != null ? ((HtmlInputSecret)uiComponent).getOnblur() : "";
                
        //Add the enter key behavior by default
        root.setAttribute("onkeypress", onkeypress + this.ICESUBMIT);
        // set the focus id
        root.setAttribute("onfocus", onfocus + "setFocus(this.id);");
        // clear focus id
        root.setAttribute("onblur", onblur + "setFocus('');");
        
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            root.setAttribute("onblur", onblur + "setFocus('');" + 
                                        "iceSubmitPartial(form,this,event); return false;");
        }
        
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        HtmlInputSecret inputSecret = (HtmlInputSecret)uiComponent;
        // check if we are processing a partial submit
        Map requestParameterMap =
            facesContext.getExternalContext().getRequestParameterMap();
        String partial = "partial";
        boolean test = Boolean.valueOf( 
            (String) requestParameterMap.get(partial)).booleanValue();
        if (test) {
            // force the redisplay for partialSubmit enabled inputSecret components
            if (!inputSecret.isRedisplay()) {
                inputSecret.setRedisplay(true);
            }
        } 
        
        super.decode(facesContext, uiComponent);        
        if (Util.isEventSource(facesContext, uiComponent)) {
            queueEventIfEnterKeyPressed(facesContext, uiComponent);
        }
        
    }


    public void queueEventIfEnterKeyPressed(FacesContext facesContext,
                                            UIComponent uiComponent) {
        try {
            KeyEvent keyEvent =
                new KeyEvent(uiComponent, facesContext.getExternalContext().getRequestParameterMap());
            if (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param uiComponent
     * @return boolean
     */
    private boolean redisplayAttributeIsTrue(UIComponent uiComponent, FacesContext facesContext) {
        if (Util.isEventSource(facesContext,uiComponent)) {
            return true;
        }
        Object redisplayAttribute =
                uiComponent.getAttributes().get("redisplay");
        return redisplayAttribute != null
               && redisplayAttribute.toString().toLowerCase().equals("true");
    }
    
}