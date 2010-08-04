/**
 * 
 */
package com.x.jsf;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name="demo")
//@RequestScoped
//@ViewScoped
@SessionScoped
//@CustomScoped(value="#{window}")
public class DemoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5395176310517774113L;
	//private static Logger log=LoggerFactory.getLogger(DemoBean.class);
	private static Logger log = Logger.getLogger(DemoBean.class.getName());

	private static String INPUT_PAGE="/input.xhtml";
	private static String OUTPUT_PAGE="/output.xhtml";
	private static String MSG="Hello, ";
	private String name=null;
	private String path=INPUT_PAGE;
	
	public DemoBean() {
		super();
		log.info("DemoBean created");
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		log.info("Returning path to facelet: "+this.path);
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String close() {
		this.path = INPUT_PAGE;
		log.info("Page Path is now: "+this.path);
		return null;
	}
	public String doClick() {
		String msg = MSG + name +"!";
		FacesMessage fm = new FacesMessage(msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, fm);
		this.path = OUTPUT_PAGE;
		log.info("Page Path is now: "+this.path);
		return null;
	}
}
