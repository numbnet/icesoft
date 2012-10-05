package org.icemobile.jsp.tags;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.PageContext;

import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Constants;
import org.icemobile.util.SXUtils;

import static org.icemobile.util.HTML.*;

public class GetEnhancedTag extends BaseSimpleTag{
	
	public static final String CSS_CLASS = "mobi-getenhanced";
	public static final String INFO_MSG = "ICEmobile container was not detected.&nbsp;";
	public static final String DOWNLOAD = "Download ICEmobile";
	public static final String IOS_LINK = "http://itunes.apple.com/us/app/icemobile-sx/id485908934?mt=8";
	public static final String ANDROID_LINK = "https://play.google.com/store/apps/details?id=org.icemobile.client.android";
	public static final String BLACKBERRY_LINK = "http://appworld.blackberry.com/webstore/content/59555/";
	
	private boolean includeLink = true;
	private String androidMsg;
	private String iosMsg;
	private String blackberryMsg;
	
	private static Logger log = Logger.getLogger(GetEnhancedTag.class.getName());
	
	public void doTag() throws IOException {
		PageContext pageContext = getContext();
		
		if( Boolean.TRUE.equals(pageContext.getAttribute(Constants.IOS_SMART_APP_BANNER_KEY)) ){
			return;
		}
		
		ClientDescriptor client = getClient();		
		if( client.isDesktopBrowser() || TagUtil.isEnhancedBrowser(pageContext)
            || SXUtils.isSXRegistered(getRequest()) ){
			return;
		}
		
		TagWriter writer = new TagWriter(pageContext);
		
		writer.startSpan();
		writer.writeStyleClassWithBase(styleClass, CSS_CLASS);
		writer.writeStyle(style);
		writer.writeDisabled(disabled);
		
		String msg = INFO_MSG; //default msg
		String link = null;
		if( client.isAndroidOS()){
		    link = ANDROID_LINK; 
            if( androidMsg != null ){
                msg = androidMsg;
            }
		}
		else if( client.isIOS()){
		    link = IOS_LINK; 
            if( iosMsg != null ){
                msg = iosMsg;
            }
		}
		else if( client.isBlackBerryOS()){
		    link = BLACKBERRY_LINK; 
            if( blackberryMsg != null ){
                msg = blackberryMsg;
            }
		}
		writer.writeTextNode(msg);
		
		if( client.isIOS() ){
			writer.startElement(ANCHOR_ELEM);
			writer.writeStyleClass("mobi-button mobi-button-important");
			writer.writeAttribute(ONCLICK_ATTR, 
			        SXUtils.getICEmobileRegisterSXScript(getRequest(),MobiJspConstants.SX_UPLOAD_PATH));
			writer.writeTextNode("Enable ICEmobile");
			writer.endElement();
		}
		
		if( includeLink ){
			writer.startElement("a");
			writer.writeAttribute("href", link);
			writer.writeStyleClass("mobi-button mobi-button-important");
			writer.writeTextNode(DOWNLOAD);
			writer.endElement();
		}
		
		writer.endElement();//span
		
	}

	public boolean isIncludeLink() {
		return includeLink;
	}

	public void setIncludeLink(boolean includeLink) {
		this.includeLink = includeLink;
	}

	public String getAndroidMsg() {
		return androidMsg;
	}

	public void setAndroidMsg(String androidMsg) {
		this.androidMsg = androidMsg;
	}

	public String getIosMsg() {
		return iosMsg;
	}

	public void setIosMsg(String iosMsg) {
		this.iosMsg = iosMsg;
	}

	public String getBlackberryMsg() {
		return blackberryMsg;
	}

	public void setBlackberryMsg(String blackberryMsg) {
		this.blackberryMsg = blackberryMsg;
	}
	

}
