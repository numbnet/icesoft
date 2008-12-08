package org.icefaces.application.showcase.util;

import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Igor
 * Date: 5-Dec-2008
 * Time: 11:25:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocaleBean {
    private String currentLanguage;

    private ArrayList availableLocales;

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public ArrayList getAvailableLocales() {
        return availableLocales;
    }

    public void setAvailableLocales(ArrayList availableLocales) {
        this.availableLocales = availableLocales;
    }

    public LocaleBean() {
        ResourceBundle bdl = ResourceBundle.getBundle("org.icefaces.application.showcase.view.resources.messages");
        currentLanguage = bdl.getLocale().getLanguage();
        availableLocales = new ArrayList();
        availableLocales.add(new SelectItem("en","English"));
        availableLocales.add(new SelectItem("es","Spanish"));
    }

    public void changeLanguage(ValueChangeEvent e) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale locale = ctx.getViewRoot().getLocale();
        if ("en".equals(e.getNewValue()))  {
            currentLanguage = "en";
            if (!"en".equals(locale.getLanguage())) {
                ctx.getViewRoot().setLocale(new Locale("en"));
            }
        }
        else if ("es".equals(e.getNewValue())) {
            currentLanguage="es";
            if (!"es".equals(locale.getLanguage())) {
                ctx.getViewRoot().setLocale(new Locale("es"));
            }
        }
    }
}
