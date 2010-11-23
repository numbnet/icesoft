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
 
package org.icefaces.application.showcase.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.Locale;
import java.util.ArrayList;
import java.io.Serializable;


/**
 * <p>The LocaleBean is responsible for keeping track of the current application
 * locale.  The locale of the application can be changed using the a
 * selectOneMenu located in the languageThemeControl.jspx.  The application
 * currently support English, German and Spanish</p>
 *
 * 
 * @since 1.8
 *
 */
public class LocaleBean implements Serializable {

    private static final Log logger =
            LogFactory.getLog(LocaleBean.class);

    // current language selection
    private String currentLanguage;
    // current local
    private Locale usedLocale;

    // available locals to choose from.
    private static final ArrayList AVAILABLE_LOCALES = new ArrayList(3);
    static{
        // setup our list of supported languages.
        AVAILABLE_LOCALES.add(new SelectItem("en","English"));
        AVAILABLE_LOCALES.add(new SelectItem("de","German"));
        AVAILABLE_LOCALES.add(new SelectItem("es","Spanish"));
    }

    /**
     * Creates an instance of the LocalBean.  The default language type is
     * specified by the initial request.  The availableLocales is
     */
    public LocaleBean() {
        // get default language type.
        currentLanguage = FacesContext.getCurrentInstance().getViewRoot()
                .getLocale().getLanguage();
        usedLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    /**
     * Changes the view roots language type.
     * @param event jsf value change event.
     */
    public void changeLanguage(ValueChangeEvent event){
        FacesContext ctx = FacesContext.getCurrentInstance();
        // find out view roots current local
        Locale locale = ctx.getViewRoot().getLocale();
        String newLanguage = (String) event.getNewValue();
        // see if matches any of our translations.
        if ("en".equals(newLanguage))  {
            currentLanguage = "en";
        }
        else if ("es".equals(newLanguage)) {
            currentLanguage="es";
        }
        else if ("de".equals(newLanguage)) {
            currentLanguage="de";
        }
        // finally change the local for the view root.
        if (!currentLanguage.equals(locale.getLanguage())) {
            usedLocale = new Locale(currentLanguage);
            ctx.getViewRoot().setLocale(usedLocale);
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public ArrayList getAvailableLocales() {
        return AVAILABLE_LOCALES;
    }

    public Locale getUsedLocale() {
        return usedLocale;
    }
}
