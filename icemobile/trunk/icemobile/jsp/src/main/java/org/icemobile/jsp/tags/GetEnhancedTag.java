package org.icemobile.jsp.tags;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.PageContext;

import org.icemobile.jsp.tags.TagUtil.DeviceType;

public class GetEnhancedTag extends BaseSimpleTag{
	
	public static final String CSS_CLASS = "mobi-getenhanced";
	public static final String INFO_MSG = "ICEmobile container was not detected, native device integration will not be available.&nbsp;";
	public static final String DOWNLOAD = "Download";
	public static final String DOWNLOAD_MSG = "&nbsp;the ICEmobile Container.";
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
		
		DeviceType device = TagUtil.getDeviceTypeNoDefault(pageContext);
		
		if( device != null && !TagUtil.isEnhancedBrowser(pageContext)){
			TagWriter writer = new TagWriter(pageContext);
			
			writer.startSpan();
			writer.writeStyleClassWithBase(styleClass, CSS_CLASS);
			writer.writeStyle(style);
			writer.writeDisabled(disabled);
			
			String msg = INFO_MSG; //default msg
			String link = null;
			switch(device){
				case android: 	
				case honeycomb:	
					link = ANDROID_LINK; 
					if( androidMsg != null ){
						msg = androidMsg;
					}
					break;
				case iphone:
				case ipad: 		
					link = IOS_LINK; 
					if( iosMsg != null ){
						msg = iosMsg;
					}
					break;
				case bberry: 	
					link = BLACKBERRY_LINK; 
					if( blackberryMsg != null ){
						msg = blackberryMsg;
					}
					break;
			}
			writer.writeTextNode(msg);
			
			if( includeLink ){
				writer.startElement("a");
				writer.writeAttribute("href", link);
				writer.writeTextNode(DOWNLOAD);
				writer.endElement();
				writer.writeTextNode(DOWNLOAD_MSG);
			}
			
			writer.endElement();//span
		}
		
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
