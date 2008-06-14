/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.icefaces.samples.showcase.navigation;
import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Destroy;
import javax.persistence.EntityManager;

import com.icesoft.icefaces.samples.showcase.common.MResource;

import org.icefaces.application.showcase.facelets.navigation.NavigationBean;

/**
 * <p>The NavigationBean class is responsible for storing the state of the two
 * panel stacks which display dynamic content.  </p>
 *
 * @since 0.3.0
 */
@Scope(ScopeType.CONVERSATION)
@Name("navigation")
public class SeamNavigationBean extends NavigationBean implements Serializable{

	@Logger Log log;
	
	@Out
	@In
	private String msgKey;
	private String viewPage="";
	
	private final String prefixPath="/WEB-INF/includes/examples/custom/";

	
//	@RequestParameter
//	String selectedIncludePath;
	private String titleLookup="node.splashPage.title";
	private String labelLookup="node.splashPage.title";
	   
	   
	@In
	EntityManager entityManager;
	   	
	@Create
	public void init(){
		log.info("creating navigation bean");
		if (msgKey==null)msgKey="splashPage";
		else log.info("msgKey="+msgKey);
	}
	

	@Override
	@Begin(join=true)
	public void navigationPathChange(ActionEvent event){
	  super.navigationPathChange(event);
	  log.info("old way selectedPath = "+super.getSelectedIncludePath());
	  getMsgBundleKey(this.getSelectedIncludePath());
	}
	
	public void navigationKeyChange(){
		if (!msgKey.equals("gmap")){
			log.info("have to look it up!");
	        FacesContext context = FacesContext.getCurrentInstance();
	        Map map = context.getExternalContext().getRequestParameterMap();
	        msgKey = (String) map.get("msgKey");
	        log.info("looked it up and is="+msgKey);
	        this.setSelectedIncludePath(findPagePath(msgKey));
		}
	}
	
	public String navigationPathChange(String slinkValue){
		log.info("looking for selected path for "+slinkValue);
		this.msgKey = slinkValue;
		String temp = findPagePath(slinkValue);
		log.info("temp page is "+temp);
		this.setSelectedIncludePath(temp);
		return "";
	}
	
	protected String findPagePath(String slinkValue){
		  Object o = entityManager.createQuery("SELECT m FROM MResource m WHERE m.lookup=:keyLookup")
             .setParameter("keyLookup",msgKey).getSingleResult();
		  if (o==null){
			  log.info("didn't get the dang thing!!");
			  return "";
		  }
		  else{
			  MResource mresource = (MResource)o;
			  log.info("got it and page jspx is = "+mresource.getPgName());
			  return this.prefixPath+mresource.getPgName(); 
		  }		
	}
	
	protected void getMsgBundleKey(String includePath){
	  if (includePath!=null && !includePath.equals("")){
		  log.info("parse lookup string");
		  String expr = includePath;
		  String[] tokens = expr.split("/");
		  for (int i=0; i<tokens.length; i++){
			  if (tokens[i].contains(".jspx")){
				  log.info("found jspx page");
				  this.viewPage = tokens[i];
				  log.info("viewPage="+viewPage);
			  }else log.info("not viewPage :-"+tokens[i]);
		  }
		  Object o = entityManager.createQuery("SELECT m FROM MResource m WHERE m.pgName=:pageName")
		                 .setParameter("pageName",viewPage).getSingleResult();
		  if (o==null)log.info("didn't get the dang thing!!");
		  else{
			  MResource mresource = (MResource)o;
			  log.info("got it and mresource = "+mresource.getLookup());
			  this.msgKey = mresource.getLookup();
		  }
		  
	  }
	}

	public String getMsgKey(){
		return this.msgKey;
	}
	public void setMsgKey(String msgKey){
		this.msgKey=msgKey;
	}
	
	public String getTitleLookup(){
		return "node."+msgKey+".title";
	}
	
	@Destroy
	public void destroy(){
		log.info("conversation has ended or timed out for navigation bean");
	}
	
}
