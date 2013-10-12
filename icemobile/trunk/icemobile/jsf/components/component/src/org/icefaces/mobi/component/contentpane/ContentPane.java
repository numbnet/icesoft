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
package org.icefaces.mobi.component.contentpane;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.ClientDescriptor;

@SuppressWarnings({"rawtypes","unchecked"})
public class ContentPane extends ContentPaneBase{
    
    private static final Logger logger =
            Logger.getLogger(ContentPane.class.toString());
    
    public static final String CONTENT_SELECTED = "mobi-contentpane ui-body-c ";
    public static final String CONTENT_LEFT_SELECTED = "mobi-contentpane-left ui-body-c";
    public static final String CONTENT_HIDDEN = "mobi-contentpane-hidden ";
    public static final String CONTENT_LEFT_HIDDEN = "mobi-contentpane-left-hidden";
    public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ui-bar-b");
    public static final String TABSET_SPAN_CLASS = "mobitab";
    public static final String TABSET_HIDDEN_PAGECLASS = "mobi-tabpage-hidden";
    public static final String TABSET_ACTIVE_CONTENT_CLASS= "mobi-tabpage";
    
    private transient Boolean first;

    @Override
    public void setClient(boolean client) {
        ValueExpression ve = getValueExpression(PropertyKeys.client.name() );
        if (ve != null) {
            // map of style values per clientId
            ve.setValue(getFacesContext().getELContext(), client );
        } else {
            StateHelper sh = getStateHelper();
            if (isDisconnected(this))  {
                String defaultKey = PropertyKeys.client.name() + "_defaultValues";
                Map clientDefaults = (Map) sh.get(defaultKey);
                if (clientDefaults == null) {
                    clientDefaults = new HashMap();
                    clientDefaults.put("defValue",client);
                    sh.put(defaultKey, clientDefaults);
                }
            }
        }
    }

    @Override
    public boolean isClient() {
        java.lang.Boolean retVal = false;
        ValueExpression ve = getValueExpression( PropertyKeys.client.name() );
        if (ve != null) {
            Object o = ve.getValue( getFacesContext().getELContext() );
            if (o != null) {
                retVal = (java.lang.Boolean) o;
            }
        } else {
            StateHelper sh = getStateHelper();
            String defaultKey = PropertyKeys.client.name() + "_defaultValues";
            Map defaultValues = (Map) sh.get(defaultKey);
            if (defaultValues != null) {
                if (defaultValues.containsKey("defValue" )) {
                    retVal = (java.lang.Boolean) defaultValues.get("defValue");
                }
            }
        }
        return retVal;
    }
	private static boolean isDisconnected(UIComponent component) {
		UIComponent parent = component.getParent();
		if (parent != null && parent instanceof UIViewRoot) {
			return false;
		} else if (parent != null) {
			return isDisconnected(parent);
		} else {
			return true;
		}
	}
    
    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }
    
    public boolean isFirstPane(){
        if( first == null ){
            if( this.getParent().getChildren().get(0).equals(this)){
                first = Boolean.TRUE;
            }
            else{
                first = Boolean.FALSE;
            }
        }
        return first.booleanValue();
    }
    
    public boolean isSelected(){
        UIComponent parent = this.getParent();
        String selectedId= null;
        if (parent instanceof ContentPaneController){
            ContentPaneController paneController = (ContentPaneController)parent;
            selectedId = paneController.getSelectedId();
            
            if (null == selectedId){
                UIComponent pComp = (UIComponent)parent;
                logger.warning("Parent controller of contentPane must have value for selectedId="+pComp.getClientId());
                return false;
            }
        }
        else {
            logger.warning("Parent must implement ContentPaneController-> has instead="+parent.getClass().getName());
            return false;
        }
        String id = this.getId();
        if( id == null ){
            return false;
        }
        else{
            return (id.equals(selectedId));
        }
        
    }

    @Override
    public String toString() {
        return "ContentPane [first=" + first + ", getId()=" + getId() + "]";
    }
    
    
    
    
  }
