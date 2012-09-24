package org.icemobile.jsp.tags;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.icemobile.jsp.util.HTML;

public class ViewSwitcherTag extends BaseSimpleTag{
	
	public static final String CSS_CLASS = "mobi-view-switcher";
	public static final String COOKIE_NAME = "mobi-view-pref";
	public static final String VIEW_PARAM = "view";
	private static final String SETCOOKIEM = "ice.mobi.setCookie('mobi-view-pref','m','/');";
	private static final String SETCOOKIET = "ice.mobi.setCookie('mobi-view-pref','t','/');";
	private static final String SETCOOKIED = "ice.mobi.setCookie('mobi-view-pref','d','/');";
	private static final String UNSETCOOKIE = "ice.mobi.setCookie('mobi-view-pref',null,'/');";
	
	public enum VIEW_TYPE{ 
		MOBILE("m"), TABLET("t"), DESKTOP("d");
		public String param;
		VIEW_TYPE(String param){ this.param = param; }
		public static VIEW_TYPE valueOfParam(String param){
			if( MOBILE.param.equals(param) ){ return MOBILE; }
			if( TABLET.param.equals(param) ){ return TABLET; }
			if( DESKTOP.param.equals(param) ){ return DESKTOP; }
			else{ return null; }
		}
	};
	
	private String tabletView;
	private String mobileView;
	private String desktopView;
	
	
	private static Logger log = Logger.getLogger(ViewSwitcherTag.class.getName());
	
	public void doTag() throws IOException {
		
		Cookie cookie = getCookie(COOKIE_NAME);
		
		//if response has already been committed render the links
		if( getResponse().isCommitted()){	
			renderLinks();
		}
		else{
			//follow redirect logic
			String currentPath = getCurrentPath();
			String target = null;
			VIEW_TYPE view = null;
			
			//if requested view does not equal target view, redirect with param
			if( cookie != null ){
				view = VIEW_TYPE.valueOfParam(cookie.getValue());
			}			
			//request without cookie or param
			//redirect with param
			else{
				view = detectViewType();
			}
			target = getTargetViewByViewType(view);	
			if( !currentPath.equals(target)){
				getResponse().sendRedirect(target);
			}
			
		}
	}
	
	private String getCurrentPath(){
		HttpServletRequest request = getRequest();
		//check forward path first, as this will be the url in spring mvc
		String path = (String)request.getAttribute("javax.servlet.forward.servlet_path");
		if( path == null ){
			path = request.getPathInfo();
		}
		else{
			path = request.getRequestURI().replace(request.getContextPath(), "");
		}
		if( path == null ){
			path = "/";
		}
		return path;
	}
	
	private void setCookie(VIEW_TYPE view){
		//detect mobile, tablet, or desktop
		Cookie cookie = new Cookie(COOKIE_NAME, view.param);
		cookie.setHttpOnly(false);
		cookie.setPath("/");
		getResponse().addCookie(cookie);
	}
	
	private void renderLinks() throws IOException{
		//only write out links if we have at least one view specified
		if( mobileView != null || tabletView != null || desktopView != null ){
			TagWriter writer = new TagWriter(getContext());
			
			writer.startElement(HTML.SPAN_ELEM);
			writer.writeStyleClassWithBase(styleClass, CSS_CLASS);
			writer.writeStyle(style);
			
			if( mobileView != null ){
				writer.startElement(HTML.ANCHOR_ELEM);
				writer.writeAttribute(HTML.ONCLICK_ATTR, SETCOOKIEM);
				writer.writeDisabled(disabled);
				writer.writeAttribute(HTML.HREF_ATTR, mobileView);
				writer.writeTextNode("Mobile");
				writer.endElement();
			}
			if( tabletView != null ){
				if( mobileView != null ){
					writer.writeTextNode(" | ");
				}
				writer.startElement(HTML.ANCHOR_ELEM);
				writer.writeAttribute(HTML.ONCLICK_ATTR, SETCOOKIET);
				writer.writeDisabled(disabled);
				writer.writeAttribute(HTML.HREF_ATTR, tabletView);
				writer.writeTextNode("Tablet");
				writer.endElement();	
			}
			if( desktopView != null ){
				if( tabletView != null || mobileView != null ){
					writer.writeTextNode(" | ");
				}
				writer.startElement(HTML.ANCHOR_ELEM);
				writer.writeDisabled(disabled);
				writer.writeAttribute(HTML.ONCLICK_ATTR, SETCOOKIED);
				writer.writeAttribute(HTML.HREF_ATTR, desktopView);
				writer.writeTextNode("Desktop");
				writer.endElement();
			}
			if( tabletView != null || mobileView != null || desktopView != null ){
				writer.writeTextNode(" | ");
			}
			writer.startElement(HTML.ANCHOR_ELEM);
			writer.writeDisabled(disabled);
			writer.writeAttribute(HTML.ONCLICK_ATTR, UNSETCOOKIE);
			writer.writeAttribute(HTML.HREF_ATTR, desktopView);
			writer.writeTextNode("Auto");
			writer.endElement();
		}
	}
	
	public VIEW_TYPE detectViewType(){
		VIEW_TYPE view = null;
		PageContext pageContext = (PageContext) getJspContext();
		String userAgent = TagUtil.getUserAgent(pageContext);
		if( TagUtil.isMobileBrowser(userAgent)){
			view = VIEW_TYPE.MOBILE;
		}
		else if( TagUtil.isTabletBrowser(userAgent)){
			view = VIEW_TYPE.TABLET;
		}
		else{
			view = VIEW_TYPE.DESKTOP;
		}
		return view;
	}
	
	public String getTargetViewByViewType(VIEW_TYPE view){
		String redirect = null;
		if( VIEW_TYPE.MOBILE == view ){
			if( mobileView != null ){
				redirect = mobileView;
			}
			else if( tabletView != null ){
				redirect = tabletView;
			}
			else{
				redirect = desktopView;
			}
		}
		else if( VIEW_TYPE.TABLET == view ){
			if( tabletView != null ){
				redirect = tabletView;
			}
			else if( mobileView != null ){
				redirect = mobileView;
			}			 
			else{
				redirect = desktopView;
			}
		}
		else if( VIEW_TYPE.DESKTOP == view ){
			if( desktopView != null ){
				redirect = desktopView;
			}
			else if( tabletView != null ){
				redirect = tabletView;
			}			 
			else{
				redirect = mobileView;
			}
		}
		return redirect;
	}
	
	

	public String getTabletView() {
		return tabletView;
	}

	public void setTabletView(String tabletView) {
		this.tabletView = tabletView;
	}

	public String getMobileView() {
		return mobileView;
	}

	public void setMobileView(String mobileView) {
		this.mobileView = mobileView;
	}

	public String getDesktopView() {
		return desktopView;
	}

	public void setDesktopView(String desktopView) {
		this.desktopView = desktopView;
	}

}
