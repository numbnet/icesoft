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

package org.icefaces.sample.portlet.chat.resources;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The ResourceUtil is used to retrieve localised messages and such from the resource
 * bundle.  It can also add localized FacesMessages to the chat page.
 */
public class ResourceUtil {

    private static final String BUNDLE = "org.icefaces.sample.portlet.chat.resources.messages";

    public static void addLocalizedMessage(String messagePatternKey) {
        String[] messageArgs = {};
        addLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static void addLocalizedMessage(String messagePatternKey, String message) {
        String[] messageArgs = {message};
        addLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static void addLocalizedMessage(String messagePatternKey, String[] messageArgs) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String localizedMessage = getLocalizedMessage(messagePatternKey, messageArgs);
        facesContext.addMessage(null,new FacesMessage(localizedMessage));
    }

    public static String getLocalizedMessage(String messagePatternKey) {
        String[] messageArgs = {};
        return getLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static String getLocalizedMessage(String messagePatternKey, String message) {
        String[] messageArgs = {message};
        return getLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static String getLocalizedMessage(String messagePatternKey, String[] messageArgs){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot root = facesContext.getViewRoot();
        Locale locale = root.getLocale();
        String localizedPattern = ResourceUtil.getI18NString(locale,messagePatternKey);
        return MessageFormat.format(localizedPattern,(Object[])messageArgs);
    }

    public static String getI18NString(Locale locale, String key) {

        String text;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, locale);
            text = bundle.getString(key);
        } catch (Exception e) {
            text = "?UNKNOWN?";
        }
        return text;
    }


}
