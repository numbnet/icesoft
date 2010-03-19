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

package com.icesoft.faces.component.ext;


import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.context.effects.JavascriptContext;

public class OutputBody extends javax.faces.component.UIComponentBase{
    private String alink;
    private String background;
    private String bgcolor;
    private String link;
    private String style;
    private String styleClass;
    private String text;
    private String vlink;
    private String focus;
    transient private String previousFocus;
    
    public OutputBody() {
        super();
        setRendererType("com.icesoft.faces.OutputBody");
    }
    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.OutputBody";
    }

    public String getAlink() {
        return (String) getAttribute("alink", alink, null);
    }

    public void setAlink(String alink) {
        this.alink = alink;
    }

    public String getBackground() {
        return (String) getAttribute("background", background, null);
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBgcolor() {
        return (String) getAttribute("bgcolor", bgcolor, null);
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getLink() {
        return (String) getAttribute("link", link, null);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStyle() {
        return (String) getAttribute("style", style, null);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return (String) getAttribute("styleClass", styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getText() {
        return (String) getAttribute("text", text, null);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVlink() {
        return (String) getAttribute("vlink", vlink, null);
    }

    public void setVlink(String vlink) {
        this.vlink = vlink;
    }
    
    public String getFocus(){
        return (String)getAttribute("focus",focus,null);
    }
    
    public void setFocus(String focus){
        this.focus = focus;
    }

    private Object getAttribute(String name, Object localValue, Object defaultValue) {
        if (localValue != null) return localValue;
        ValueBinding vb = getValueBinding(name);
        if (vb == null) return defaultValue;
        Object value = vb.getValue(getFacesContext());
        if (value == null) return defaultValue;
        return value;
    }
    
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                alink,
                background,
                bgcolor,
                link,
                style,
                styleClass,
                text,
                vlink,
                focus
        };
    }

    public void restoreState(FacesContext context, Object state) {
        String[] attrNames = {
                "alink",
                "background",
                "bgcolor",
                "link",
                "style",
                "styleClass",
                "text",
                "vlink",
                "focus"
        };
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        for (int i = 0; i < attrNames.length; i++) {
            getAttributes().put(attrNames[i], values[i + 1]);
        }
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
        String focus = getFocus();
        if( focus != null ){
            setFocus(focus, context);
        } 
    }
 
    public void setFocus(String focus, FacesContext fc){
        UIComponent target = null;
        if( focus.indexOf(':') > -1){
            applyFocus(fc,focus);
          
        } else {
            target = D2DViewHandler.findComponentInView(this,focus);
            if( target != null){
                applyFocus(fc,target.getClientId(fc));            }            
        }
    }
    
    void applyFocus(FacesContext facesContext, String focus) {
        if (!focus.equals(previousFocus)){
            JavascriptContext.applicationFocus(facesContext,focus);
            previousFocus = focus;
        }
    }
}

