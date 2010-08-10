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

package com.icesoft.faces.component.jseventlistener;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;

public class JSEventListener extends UICommand{
    public static final String COMPONENT_TYPE =
        "com.icesoft.faces.JSEventListener";
    public static final String RENDERER_TYPE = "com.icesoft.faces.JSEventListenerRenderer";
    private String events;
    private String handler;
    private String style;
    private String styleClass;
    
    public JSEventListener() {
        setRendererType(RENDERER_TYPE);
    }
    
    public String getRendererType() {
        return RENDERER_TYPE;
    }
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public boolean getRendersChildren() {
        return true;
    } 
    
    public void setEvents(String events) {
        this.events = events;
    }
    
    public String getEvents() {
        if (events != null) {
            return events;
        }
        ValueBinding vb = getValueBinding("events");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getHandler() {
        if (handler != null) {
            return handler;
        }
        ValueBinding vb = getValueBinding("handler");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    } 
    
    /**
     * <p>Set the value of the <code>style</code> property.</p>
     *
     * @param style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     *
     * @return String style
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }    
    
    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     *
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     *
     * @return String styleClass
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass,
                CSS_DEFAULT.JSEVENT_LISTENER_DEFAULT_STYLE_CLASS,
                "styleClass");
                                             
    }    
    
    public Object saveState(FacesContext context) {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = events;
        values[2] = handler;
        values[3] = style;
        values[4] = styleClass;        
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        events = (String) values[1];
        handler = (String) values[2];
        style = (String) values[3];
        styleClass = (String) values[4];        
    }    
}
