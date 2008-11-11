package com.icesoft.faces.application;

import javax.faces.component.UIViewRoot;
import java.util.Locale;

/**
 * Needs to be publicly instantiable by JSF via state restoration
 */
public class SettableLocaleViewRoot extends UIViewRoot {

    public void setLocale(Locale locale) {
        //ignore locale set by RestoreViewPhase since it is using the first locale in the Accept-Language list,
        //instead it should calculate the locale
        StackTraceElement[] ste = (new RuntimeException()).getStackTrace();

        for (int i = 0; i < ste.length; i++) {
            String className = ste[i].getClassName().toLowerCase();
            String methodName = ste[i].getMethodName().toLowerCase();

            if (className.contains("restoreviewphase") && methodName.contains("execute")) {
                return;
            }

            if (className.contains("viewtag") && methodName.contains("setproperties")) {
                return;
            }
        }

        super.setLocale(locale);
    }
}
