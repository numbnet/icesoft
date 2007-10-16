package com.icesoft.icefaces.samples.showcase.util;


import javax.faces.convert.*;
import javax.faces.context.*;
import javax.faces.component.*;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Name;
import static org.jboss.seam.ScopeType.EVENT;

import java.util.*;

//@Scope(EVENT)
@Name("localeConverter")
public class LocaleConverter implements Converter {
	 
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) throws ConverterException {
        System.out.println("entered getAsObject() string="+s);
    	if (s != null) {
            String[] aux = s.split("_");
            switch (aux.length) {
                case 1:
                    return new Locale(aux[0]);
                case 2:
                    return new Locale(aux[0], aux[1]);
                default:
                    return null;
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) throws ConverterException {
        System.out.println("entered getAsString()");
    	if (o instanceof Locale) {
            return ((Locale) o).toString();
        } else {
            return null;
        }
    }
}