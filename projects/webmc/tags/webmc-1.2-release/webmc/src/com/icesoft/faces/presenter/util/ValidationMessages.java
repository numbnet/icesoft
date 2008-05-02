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
package com.icesoft.faces.presenter.util;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Code required for custom validators. See Core JavaServer Faces page 231
 */
public class ValidationMessages {
    public static final String MESSAGE_RESOURCES =
            "com.icesoft.faces.presenter.resource.messages";

    /**
     * Method to get a generic FacesMessage based on the various parameters
     *
     * @param bundleName to use
     * @param resourceId related to the message
     * @param params     list
     * @return constructed messages
     */
    public static FacesMessage getMessage(String bundleName, String resourceId,
                                          Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        String appBundle = app.getMessageBundle();
        Locale locale = ValidationMessages.getLocale(context);
        ClassLoader loader = ValidationMessages.getClassLoader();
        String summary = ValidationMessages.getString(appBundle, bundleName,
                                                      resourceId, locale,
                                                      loader, params);

        if (summary == null) {
            summary = "???" + resourceId + "???";
        }

        String detail = ValidationMessages.getString(appBundle, bundleName,
                                                     resourceId, locale,
                                                     loader, params);

        return new FacesMessage(summary, detail);
    }

    /**
     * Method to get a String after passing it through a bundle
     *
     * @param bundle     to use
     * @param resourceId related to the string
     * @param params     list
     * @return result of bundle pass
     */
    public static String getString(String bundle, String resourceId,
                                   Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        String appBundle = app.getMessageBundle();
        Locale locale = ValidationMessages.getLocale(context);
        ClassLoader loader = ValidationMessages.getClassLoader();

        return ValidationMessages.getString(appBundle, bundle, resourceId,
                                            locale, loader, params);
    }

    /**
     * Method to get a String after passing it through a series of bundles and
     * locales
     *
     * @param bundle1    name
     * @param bundle2    name
     * @param resourceId related to the string
     * @param locale     package to use
     * @param loader     as needed
     * @param params     list
     * @return resulting string
     */
    public static String getString(String bundle1, String bundle2,
                                   String resourceId, Locale locale,
                                   ClassLoader loader, Object[] params) {
        String resource = null;
        ResourceBundle bundle;

        if (bundle1 != null) {
            bundle = ResourceBundle.getBundle(bundle1, locale, loader);
            if (bundle != null) {
                try {
                    resource = bundle.getString(resourceId);
                }catch (MissingResourceException missingError) {
                    missingError.printStackTrace();
                }
            }
        }

        if (resource == null) {
            bundle = ResourceBundle.getBundle(bundle2, locale, loader);
            if (bundle != null) {
                try {
                    resource = bundle.getString(resourceId);
                }catch (MissingResourceException missingError) {
                    missingError.printStackTrace();
                }
            }
        }

        if (resource == null) {
            return null;
        }

        if (params == null) {
            return resource;
        }

        MessageFormat formatter = new MessageFormat(resource, locale);

        return formatter.format(params);
    }

    /**
     * Method to get a locale package from the passed faces context
     *
     * @param context to use
     * @return retrieved locale
     */
    public static Locale getLocale(FacesContext context) {
        Locale locale = null;
        UIViewRoot viewRoot = context.getViewRoot();

        if (viewRoot != null) {
            locale = viewRoot.getLocale();
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }

        return locale;
    }

    /**
     * Method to get a class loader (either from this thread or the default
     * system loader)
     *
     * @return resulting class loader
     */
    public static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        return loader;
    }
}
