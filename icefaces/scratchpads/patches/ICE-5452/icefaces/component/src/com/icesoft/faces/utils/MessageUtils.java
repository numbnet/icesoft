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

package com.icesoft.faces.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * This class has been designed, so the custom components can get 
 * facesMessages either from the icefaces' ResourceBundle or an 
 * application's resourceBundle. The location of ice's messages.properties
 * is under com.icesoft.faces.resources package. 
 */

public class MessageUtils {
    private static Log log = LogFactory.getLog(MessageUtils.class);
    private static String DETAIL_SUFFIX = "_detail";
    private static int SUMMARY = 0;
    private static int DETAIL = 1;
    private static String ICE_MESSAGES_BUNDLE = "com.icesoft.faces.resources.messages";
    public static FacesMessage getMessage(FacesContext context, 
            String messageId) {
        return getMessage(context, messageId, null);
    }
    
    public static FacesMessage getMessage(FacesContext facesContext, 
            String messageId, Object params[]) {
        String messageInfo[] = new String[2];
        
        Locale locale = facesContext.getViewRoot().getLocale();
        String bundleName = facesContext.getApplication().getMessageBundle();
        //see if the message has been overridden by the application
        if (bundleName != null) {
            try {
                loadMessageInfo(bundleName, locale, messageId, messageInfo);
            } catch (Exception e)  {
                if(log.isWarnEnabled())
                    log.warn(e + ", using " + ICE_MESSAGES_BUNDLE);
            }
        }
        
        //TODO Use defered evaluation of the parameters, like how
        // JSF 1.2's javax.faces.component.MessageFactory.
        // BindingFacesMessage does. ICE-2290.
        
        //if not overridden then check in Icefaces message bundle.
        if (messageInfo[SUMMARY] == null && messageInfo[DETAIL]== null) {
            loadMessageInfo(ICE_MESSAGES_BUNDLE, locale, messageId, messageInfo);
        }
        if (params != null) {
            MessageFormat format;
            for (int i= 0; i <messageInfo.length; i++) {
                if (messageInfo[i] != null) {
                    format = new MessageFormat(messageInfo[i], locale);
                    messageInfo[i] = format.format(params);
                }
            }
        }
        return new FacesMessage(messageInfo[SUMMARY], messageInfo[DETAIL]);
    }
    
    private static void loadMessageInfo(String bundleName, 
                                Locale locale,
                                String messageId,  
                                String[] messageInfo) {
        ResourceBundle bundle = ResourceBundle.
                    getBundle(bundleName, locale, getClassLoader(bundleName));
        try {
            messageInfo[SUMMARY] = bundle.getString(messageId);
            messageInfo[DETAIL] = bundle.getString(messageId + DETAIL_SUFFIX);
        } catch (MissingResourceException e) {         
        }
    }
    
   
    public static ClassLoader getClassLoader(Object fallback) {
        ClassLoader classLoader = Thread.currentThread()
                                    .getContextClassLoader();
        if (classLoader == null) {
            classLoader = fallback.getClass().getClassLoader();
        }
        return classLoader;
    }
    
    public static String getResource(FacesContext facesContext, String messageId) {
        String ret = null;
        Locale locale = facesContext.getViewRoot().getLocale();
        String bundleName = facesContext.getApplication().getMessageBundle();
        //see if the message has been overridden by the application
        if (bundleName != null) {
            ret = getResource(bundleName, locale, messageId);
        }
        if(ret == null) {
            ret = getResource(ICE_MESSAGES_BUNDLE, locale, messageId);
        }
        return ret;
    }
    
    protected static String getResource(
        String bundleName, Locale locale, String messageId)
    {
        ResourceBundle bundle = ResourceBundle.getBundle(
            bundleName, locale, getClassLoader(bundleName));
        String ret = null;
        try {
            ret = bundle.getString(messageId);
        } catch(Exception e) {}
        return ret;
    }
    
    public static Object getComponentLabel(
        FacesContext context, UIComponent comp)
    {
        Object label = comp.getAttributes().get("label");
        if(nullOrEmptyString(label)) {
            //TODO When doing that MessageFactory stuff, uncomment this. ICE-2290.
            ////ValueBinding vb = comp.getValueBinding("label");
            ////if(vb != null) {
            ////    label = vb;
            ////}
            ////else {
                label = comp.getClientId(context);
            ////}
        }
        return label;
    }
    
    private static boolean nullOrEmptyString(Object ob) {
        return ( (ob == null) ||
                 ((ob instanceof String) && (ob.toString().length() == 0)) );
    }
}
