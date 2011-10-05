/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * This class has been designed, so the components can get FacesMessages 
 * either from their own ResourceBundle or an application's ResourceBundle. 
 * The component's ResourceBundle package is: org.icefaces.ace.resources
 */

public class MessageUtils {
    private static String DETAIL_SUFFIX = "_detail";
    private static int SUMMARY = 0;
    private static int DETAIL = 1;
    private static String ICE_MESSAGES_BUNDLE =
        "org.icefaces.ace.resources.messages";
    
    public static FacesMessage getMessage(FacesContext facesContext, 
            FacesMessage.Severity sev, String messageId, Object[] params) {
//System.out.println("MessageUtils.getMessage()  messageId: " + messageId);
        String messageInfo[] = new String[2];
        Locale locale = facesContext.getViewRoot().getLocale();
//System.out.println("MessageUtils.getMessage()    locale: " + locale);
        
        String bundleName = facesContext.getApplication().getMessageBundle();
//System.out.println("MessageUtils.getMessage()    application bundleName: " + bundleName);
        //see if the message has been overridden by the application
        if (bundleName != null) {
            try {
                loadMessageInfo(bundleName, locale, messageId, messageInfo);
            } catch (Exception e)  {
//System.out.println("MessageUtils.getMessage()    application bundle exception: " + e);
            }
        }
        
        //if not overridden then check in Icefaces message bundle.
        if (messageInfo[SUMMARY] == null && messageInfo[DETAIL]== null) {
            try {
            loadMessageInfo(ICE_MESSAGES_BUNDLE, locale, messageId, messageInfo);
            }
            catch(Exception e) {
//System.out.println("MessageUtils.getMessage()    EXCEPTION  e: " + e);
            }
        }
        
        return getMessage(
            locale, sev, messageInfo[SUMMARY], messageInfo[DETAIL], params);
    }
    
    public static FacesMessage getMessage(Locale locale, 
            FacesMessage.Severity sev, String summary, String detail,
            Object[] params) {
//System.out.println("MessageUtils.getMessage()      summary  BEFORE: " + summary);
        summary = formatString(locale, summary, params);
//System.out.println("MessageUtils.getMessage()      summary   AFTER: " + summary);
//System.out.println("MessageUtils.getMessage()      detail   BEFORE: " + summary);
        detail = formatString(locale, detail, params);
//System.out.println("MessageUtils.getMessage()      detail    AFTER: " + summary);
        return new FacesMessage(sev, summary, detail);
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
//System.out.println("MessageUtils.loadMessageInfo()  MISSING  bundleName: " + bundleName + "  locale: " + locale + "  messageId: " + messageId);
        }
    }
    
    private static String formatString(Locale locale, String pattern, Object[] params) {
        if (pattern != null && params != null) {
            MessageFormat format = new MessageFormat(pattern, locale);
            pattern = format.format(params);
        }
        return pattern;
    }
    
    public static ClassLoader getClassLoader(Object fallback) {
        ClassLoader classLoader = Thread.currentThread()
                                    .getContextClassLoader();
        if (classLoader == null) {
            classLoader = fallback.getClass().getClassLoader();
        }
        return classLoader;
    }
}
