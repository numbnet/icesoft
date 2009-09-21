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
package org.icefaces.component.tab;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import com.icesoft.util.pooling.ELPool;

/**
 * <p>Auto-generated component tag class.
 * Do <strong>NOT</strong> modify; all changes
 * <strong>will</strong> be lost!</p>
 */

public class TabSetTag extends UIComponentTag {

    /**
     * <p>Return the requested component type.</p>
     */
    public String getComponentType() {
        return "com.icesoft.faces.TabSet";
    }

    /**
     * <p>Return the requested renderer type.</p>
     */
    public String getRendererType() {
        return "com.icesoft.faces.TabSetRenderer";
    }

    /**
     * <p>Release any allocated tag handler attributes.</p>
     */
    public void release() {
        super.release();
        clientSide = null;
        tabIndex = null;
        hideId = null;
        orientation = null;
        immediate = null;
        onupdate = null;
        style = null;
        styleClass = null;
        tabChangeListener = null;        
    }

    /**
     * <p>Transfer tag attributes to component properties.</p>
     */
    protected void setProperties(UIComponent _component) {
        try{
            super.setProperties(_component);
            if (clientSide != null) {
                if (isValueReference(clientSide)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(clientSide));
                    _component.setValueBinding("clientSide", _vb);
                } else {
                    _component.getAttributes().put("clientSide", Boolean.valueOf(clientSide));
                }
            }
            if (hideId != null) {
                if (isValueReference(hideId)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(hideId));
                    _component.setValueBinding("hideId", _vb);
                } else {
                    _component.getAttributes().put("hideId", hideId);
                }
            }
            if (orientation != null) {
                if (isValueReference(orientation)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(orientation));
                    _component.setValueBinding("orientation", _vb);
                } else {
                    _component.getAttributes().put("orientation", orientation);
                }
            }            
            if (tabIndex != null) {
                if (isValueReference(tabIndex)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(tabIndex));
                    _component.setValueBinding("tabIndex", _vb);
                } else {
                    _component.getAttributes().put("tabIndex", Integer.valueOf(tabIndex));
                }
            }
            if (immediate != null) {
                if (isValueReference(immediate)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(immediate));
                    _component.setValueBinding("immediate", _vb);
                } else {
                    _component.getAttributes().put("immediate", Boolean.valueOf(immediate));
                }
            }  
            if (partialSubmit != null) {
                if (isValueReference(partialSubmit)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(partialSubmit));
                    _component.setValueBinding("partialSubmit", _vb);
                } else {
                    _component.getAttributes().put("partialSubmit", Boolean.valueOf(partialSubmit));
                }
            }  
            if (onupdate != null) {
                if (isValueReference(onupdate)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(onupdate));
                    _component.setValueBinding("onupdate", _vb);
                } else {
                    _component.getAttributes().put("onupdate", onupdate);
                }
            } 
            if (style != null) {
                if (isValueReference(style)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(style));
                    _component.setValueBinding("style", _vb);
                } else {
                    _component.getAttributes().put("style", style);
                }
            }
            if (styleClass != null) {
                if (isValueReference(styleClass)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(styleClass));
                    _component.setValueBinding("styleClass", _vb);
                } else {
                    _component.getAttributes().put("styleClass", styleClass);
                }
            }
            if (tabChangeListener != null) {
                if (isValueReference(tabChangeListener)) {
                    MethodBinding _mb = getFacesContext().getApplication().createMethodBinding(ELPool.get(tabChangeListener), tabChangeListenerArgs);
                    _component.getAttributes().put("tabChangeListener", _mb);
                } else {
                    throw new IllegalArgumentException(tabChangeListener);
                }
            }            
       }catch(Exception e1){e1.printStackTrace();throw new RuntimeException(e1);}
    }

    // renderedOnUserRole
    private String clientSide = null;
    public void setClientSide(String clientSide) {
        this.clientSide = clientSide;
    }

    private String tabIndex = null;
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }
    
    private String hideId = null;
    public void setHideId(String hideId) {
        this.hideId = hideId;
    }
    
    private String orientation = null;
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    } 
   
    private String immediate = null;
    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }    
    
    private String partialSubmit = null;
    public void setPartialSubmit(String partialSubmit) {
        this.partialSubmit = partialSubmit;
    }  
    
    private String onupdate = null;
    public void setOnupdate(String onupdate) {
        this.onupdate = onupdate;
    }    

    private String style = null;
    public void setStyle(String style) {
        this.style = style;
    }  
    
    private String styleClass = null;
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
    // valueChangeListener
    private String tabChangeListener = null;
    public void setTabChangeListener(String tabChangeListener) {
        this.tabChangeListener = tabChangeListener;
    }    
    
    private static Class actionArgs[] = new Class[0];
    private static Class actionListenerArgs[] = { javax.faces.event.ActionEvent.class };
    private static Class validatorArgs[] = { FacesContext.class, UIComponent.class, Object.class };
    private static Class tabChangeListenerArgs[] = { javax.faces.event.ValueChangeEvent.class };


    // 
    // Methods From TagSupport
    // 

    public int doStartTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doStartTag();
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
        return rc;
    }


    public int doEndTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doEndTag();
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
        return rc;
    }

}
