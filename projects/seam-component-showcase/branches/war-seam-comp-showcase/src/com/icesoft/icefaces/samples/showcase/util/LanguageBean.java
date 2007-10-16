package com.icesoft.icefaces.samples.showcase.util;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.icesoft.faces.webapp.xmlhttp.RenderingException;

import static org.jboss.seam.ScopeType.SESSION;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.Serializable;


/**
 * <p>The LanguageBean class is the backing bean which manages the demonstrations'
 * language locale selector.  There are currently two languages supported by the bean; 
 * en and fr </p>
 * <p/>
 * <p>The different messages properties files are managed by changing the localeSelector
 * in the UIViewRoot.  </p>
 *
 * @since 0.4.0
 */
@Scope(SESSION)
@Name("languageBean")
public class LanguageBean implements Serializable{
	@In(required=false)
	private Locale locale;
    private enum Pages {home,page1};
    private Pages current = Pages.home;
    
	private static Log log =
           LogFactory.getLog(LanguageBean.class);	
    
    private Map messages;
    
//    @In(required=false)
    private ResourceBundle resourceBundle;
    
    private List supportedLocales;
 	
    public List getSupportedLocales() {
		return supportedLocales;
	}

	public void setSupportedLocales(List supportedLocales) {
		this.supportedLocales = supportedLocales;
	}
    /**
     * Creates a new instance of the LanguageBean.
     */
    public LanguageBean() {
        // the list
    	log.info("constructor before setLocale()");
    	setLocale( FacesContext.getCurrentInstance().getViewRoot().getLocale());
    	this.supportedLocales = new ArrayList();
    	Iterator iter = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
    	while( iter.hasNext() ){
    		supportedLocales.add(new SelectItem(iter.next()));
    	}
    	log.info("number supported locales="+supportedLocales.size());
    }
    
//    @Begin
    public void setLocale(Locale locale) {
    	System.out.println("setLocal to new locale: " + locale.toString());
    	if( this.locale == null ||  ! locale.toString().equals( this.locale.toString() ) ){
    		Iterator iter = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
    		Locale supportedLocale = null;
    		while( iter.hasNext() ){
    			supportedLocale = (Locale)iter.next();
    			if (supportedLocale !=null){
    				log.info("supported locale: " + supportedLocale.toString());
    				if( locale.toString().equals( supportedLocale.toString() )){
    					this.locale = locale;
    					FacesContext.getCurrentInstance().getViewRoot().setLocale(locale); 
    					log.info("before setting resourceBundle");
    					resourceBundle = ResourceBundle.getBundle("messages", locale);
    					log.info("before init ResourceBundleMap");
    					initializeResourceBundleMap();
    					log.info("changed locale");
    					return;
    				}
    			}else log.info("supportedLocale is null");
    		}
    		if( supportedLocale == null ){
    			log.info("could not initialize locale!!!");
    		}
    		
    	}
    }
    public String getLocale() {
    	log.info("getLocale has language "+locale.getLanguage());
        return locale.getLanguage();
    }
 
 /*   public void setLanguage(ActionEvent event) {
        log.info("setLanguage");
        String oldLocale = locale == null ? "" : locale.getLanguage();
        String id = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lang");
        log.info("new id is "+id);    
        this.setLocale( new Locale(id) );
        //renderer.requestRender();
        log.info("event id: " + id + ", locale=" + oldLocale + "->" + locale + ", " + FacesContext.getCurrentInstance().getViewRoot().getLocale().getDisplayLanguage());
    //    return "reload";
    } */
    
      
    public void renderingException(RenderingException renderingException) {
       renderingException.printStackTrace();        
    }

	public Map getMessages() {
		return messages;
	}
	
	public void setMessages(Map msgs){
		this.messages = msgs;
	}
//	@End
	private void initializeResourceBundleMap(){
		log.info("initializeResourceBundleMap for locale = "+this.locale.getLanguage());
		setMessages(new HashMap());
		Enumeration enumer = resourceBundle.getKeys();
		while( enumer.hasMoreElements() ){
			String key = (String)enumer.nextElement();
			messages.put(key, resourceBundle.getString(key));
		//	log.info(key + "=" + resourceBundle.getString(key));
		}
		log.info("checking resource bundle title is "+ messages.get("menuDisplayText.componentSuiteMenuGroup"));
	}
	public void setLocaleFromSelection(ValueChangeEvent event){
		log.info("setting locale from selection");
	//	this.setLocale( (Locale)event.getNewValue());
		this.setLocale(new Locale("fr"));
	}

}
