package com.icesoft.icefaces.samples.showcase.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;

import org.icefaces.application.showcase.util.ContextUtilBean;
import java.io.Serializable;

/**
 * A utility bean coded to solve ICE-2714.  It's used to generate the iframe markup
 * for the Description and Source tab panels.  It adds the context path of the application
 * dynamically in front of the src attribute to ensure that the resource can be found when
 * running in both portlet and plain servlet environments.
 */
@Scope(ScopeType.APPLICATION)
@Synchronized
@Name("ctxtUtil")
public class SeamContextUtilBean extends ContextUtilBean implements Serializable {

    
	
}
