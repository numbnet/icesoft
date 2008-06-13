package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import org.icefaces.application.showcase.view.bean.examples.component.inputFile.InputFileController;


import org.jboss.seam.annotations.Destroy;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import org.jboss.seam.faces.FacesMessages;



import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.Serializable;

import java.util.EventObject;


/**
 * <p>The InputFileBean class is the backing bean for the inputfile showcase
 * demonstration. It is used to store the state of the uploaded file.</p>
 *
 * @since 0.3.0
 */



//import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.ScopeType;

@Scope(ScopeType.SESSION)
@Name("inputFileController")
public class SeamInputFileController extends InputFileController{


}



