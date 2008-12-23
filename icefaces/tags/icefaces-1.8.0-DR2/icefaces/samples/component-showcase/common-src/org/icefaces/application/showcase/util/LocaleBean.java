package org.icefaces.application.showcase.util;

import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Igor
 * Date: 5-Dec-2008
 * Time: 11:25:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocaleBean {
    private String currentLanguage;
    private Locale usedLocale;

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

    public Locale getUsedLocale() {
        return usedLocale;
    }

    public void setUsedLocale(Locale usedLocale) {
        this.usedLocale = usedLocale;
    }

    public LocaleBean() {
        //ResourceBundle bdl = ResourceBundle.getBundle("org.icefaces.application.showcase.view.resources.messages");
        //currentLanguage = bdl.getLocale().getLanguage();
        if (currentLanguage == null) {
            currentLanguage = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        }
        availableLocales = new ArrayList();
        availableLocales.add(new SelectItem("en","English"));
        availableLocales.add(new SelectItem("es","Spanish"));
        usedLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public void changeLanguage(ValueChangeEvent e) throws Exception{
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale locale = ctx.getViewRoot().getLocale();
        if ("en".equals(e.getNewValue()))  {
            currentLanguage = "en";
            if (!"en".equals(locale.getLanguage())) {
                usedLocale = new Locale("en");
                ctx.getViewRoot().setLocale(usedLocale);
            }
        }
        else if ("es".equals(e.getNewValue())) {
            currentLanguage="es";
            if (!"es".equals(locale.getLanguage())) {
                usedLocale = new Locale("es");
                ctx.getViewRoot().setLocale(usedLocale);
            }
        }
        //Not all versions have application builder. We need to check if it's there and reflect at runtime
        Object appBuilder = ctx.getApplication().getVariableResolver().resolveVariable(ctx, "applicationBuilder");
        if (appBuilder != null) {
            Class klazz = Class.forName("org.icefaces.application.showcase.view.builder.ApplicationBuilder");
            Method method = klazz.getMethod("loadMetaData", new Class[0]);
            method.invoke(appBuilder, new Object[0]);
        }

        MessageBundleLoader.init();
    }
}
