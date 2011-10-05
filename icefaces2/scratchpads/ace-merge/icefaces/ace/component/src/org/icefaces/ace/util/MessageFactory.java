/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class MessageFactory {

	private static String DEFAULT_BUNDLE_BASENAME = "javax.faces.Messages";
	private static String ACE_BUNDLE_BASENAME = "org.icefaces.ace.resources.messages";
	private static String DEFAULT_DETAIL_SUFFIX = "_detail";

	private MessageFactory() {}
	
	public static FacesMessage getMessage(String messageId, FacesMessage.Severity severity, Object[] params) {
		FacesMessage facesMessage = getMessage(getLocale(), messageId, params);
		facesMessage.setSeverity(severity);
		
		return facesMessage;
	}
	
	public static FacesMessage getMessage(Locale locale, String messageId, Object params[]) {
		String summary = null;
		String detail = null;
		String userBundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        ResourceBundle bundle = null;
        
        //try user defined bundle first
        if(userBundleName != null) {
        	 try {
                 bundle = ResourceBundle.getBundle(userBundleName, locale, getCurrentClassLoader(userBundleName));
                 summary = bundle.getString(messageId);
             }
             catch (MissingResourceException e) {
            	 // No Op
             }
        }
        
        if(summary == null) {
        	try {
				bundle = ResourceBundle.getBundle(ACE_BUNDLE_BASENAME, locale, getCurrentClassLoader(ACE_BUNDLE_BASENAME));
				if (bundle == null) {
					throw new NullPointerException();
				}
				summary = bundle.getString(messageId);
			} catch (MissingResourceException e) {
				 // No Op
			}
		}
        
        //fallback to default jsf bundle
        if(summary == null) {
        	try {
				bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_BASENAME, locale, getCurrentClassLoader(DEFAULT_BUNDLE_BASENAME));
				if (bundle == null) {
					throw new NullPointerException();
				}
				summary = bundle.getString(messageId);
			} catch (MissingResourceException e) {
				 // No Op
			}
		}
        
        summary = getFormattedText(locale, summary, params);
        
        try
        {
            detail = getFormattedText(locale, bundle.getString(messageId + DEFAULT_DETAIL_SUFFIX), params);
        }
        catch(MissingResourceException e) {
            // NoOp
        }
        
        return new FacesMessage(summary, detail);
    }
	
	public static String getFormattedText(Locale locale, String message, Object params[]) {
		MessageFormat messageFormat = null;
		
		if(params == null || message == null)
			return message;
		
		if(locale != null)
			messageFormat = new MessageFormat(message, locale);
		else
			messageFormat = new MessageFormat(message);
		
		return messageFormat.format(params);
	}
	
	public static Object getLabel(FacesContext facesContext, UIComponent component) {
		String label = (String) component.getAttributes().get("label");

		if(label == null) {
			label = component.getClientId(facesContext);
        }
		
		return label;
	}
	
	protected static ClassLoader getCurrentClassLoader(Object clazz) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
        if (loader == null) {
            loader = clazz.getClass().getClassLoader();
        }
        return loader;
    }
	
	protected static Locale getLocale() {
		Locale locale = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if(facesContext != null && facesContext.getViewRoot() != null) {
            locale = facesContext.getViewRoot().getLocale();
            
            if(locale == null)
                locale = Locale.getDefault();
        } 
        else {
            locale = Locale.getDefault();
        }
        
        return locale;
	}
}

