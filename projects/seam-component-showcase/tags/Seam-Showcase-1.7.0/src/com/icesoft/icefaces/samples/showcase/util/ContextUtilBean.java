package com.icesoft.icefaces.samples.showcase.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.util.HashMap;

/**
 * A utility bean coded to solve ICE-2714.  It's used to generate the iframe markup
 * for the Description and Source tab panels.  It adds the context path of the application
 * dynamically in front of the src attribute to ensure that the resource can be found when
 * running in both portlet and plain servlet environments.
 */
@Scope(ScopeType.PAGE)
@Name("ctxtUtil")
public class ContextUtilBean extends HashMap {

    private static final String IFRAME_PREFIX = "<iframe src=\"";
    private static final String IFRAME_SUFFIX = "\"class=\"includeIframe\" width=\"100%\"></iframe>";

    public String getContextPath() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        return ec.getRequestContextPath();
    }

    public Object get(Object source) {
        return generateMarkup(getContextPath() + source);
    }

    private static String generateMarkup(String source) {
        StringBuffer markup = new StringBuffer();
        markup.append(IFRAME_PREFIX);
        markup.append(source);
        markup.append(IFRAME_SUFFIX);
        return markup.toString();
    }
}
