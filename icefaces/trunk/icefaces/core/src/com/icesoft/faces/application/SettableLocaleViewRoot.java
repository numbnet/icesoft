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
        int setLocaleIndex = -1;
        int lifeCycleRestoreViewIndex = -1;
        StackTraceElement[] ste = (new RuntimeException()).getStackTrace();
        for(int i = 0; (i < 10 && i < ste.length); i++) {
            String className = ste[i].getClassName().toLowerCase();
            String methodName = ste[i].getMethodName().toLowerCase();
            if( setLocaleIndex == -1 &&
                methodName.indexOf("setlocale") >= 0 )  {
                setLocaleIndex = i;
            }

            if ( lifeCycleRestoreViewIndex == -1 &&
                 className.indexOf("lifecycle") >= 0 &&
                 (className.indexOf("restoreview") >= 0 ||
                  methodName.indexOf("restoreview") >= 0) ) {

                lifeCycleRestoreViewIndex = i;
                break;
        }
            }
        boolean ignore =
                setLocaleIndex >= 0 &&
                lifeCycleRestoreViewIndex >= 0 &&
                setLocaleIndex + 1 == lifeCycleRestoreViewIndex;
        if (!ignore) {
            super.setLocale(locale);
        } 
    }
}
