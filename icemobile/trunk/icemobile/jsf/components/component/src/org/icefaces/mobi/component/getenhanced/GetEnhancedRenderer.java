package org.icefaces.mobi.component.getenhanced;

/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.mobi.utils.Utils.DeviceType;


public class GetEnhancedRenderer extends CoreRenderer {
    private static Logger log = Logger.getLogger(GetEnhancedRenderer.class.getName());
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        GetEnhanced getEnhanced = (GetEnhanced) uiComponent;
        
        DeviceType device = Utils.getDeviceTypeWithoutDefault(facesContext);
		
		if( device != null && !Utils.isICEmobileContainerOrHasSX()){
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.writeAttribute(HTML.ID_ATTR, clientId, null);
			String styleClass = getEnhanced.getStyleClass();
			if( styleClass != null ){
				styleClass = GetEnhanced.CSS_CLASS + " " + styleClass;
			}
			else{
				styleClass = GetEnhanced.CSS_CLASS;
			}
			writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
			String style = getEnhanced.getStyle();
			if( style != null ){
				writer.writeAttribute(HTML.STYLE_ATTR, style, null);
			}
			boolean disabled = getEnhanced.isDisabled();
			if( disabled ){
				writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
			}
			
			
			String msg = GetEnhanced.INFO_MSG; //default msg
			String link = null;
			switch(device){
				case ANDROID_PHONE: 	
				case ANDROID_TABLET:	
					link = GetEnhanced.ANDROID_LINK; 
					String androidMsg = getEnhanced.getAndroidMsg();
					if( androidMsg != null ){
						msg = androidMsg;
					}
					break;
				case IPHONE:
				case IPAD: 		
					link = GetEnhanced.IOS_LINK; 
					String iosMsg = getEnhanced.getIosMsg();
					if( iosMsg != null ){
						msg = iosMsg;
					}
					break;
				case BLACKBERRY: 	
					link = GetEnhanced.BLACKBERRY_LINK; 
					String blackberryMsg = getEnhanced.getBlackberryMsg();
					if( blackberryMsg != null ){
						msg = blackberryMsg;
					}
					break;
			}
			writer.writeText(msg, null);
			
			if( getEnhanced.isIncludeLink() ){
				writer.startElement(HTML.ANCHOR_ELEM,null);
				writer.writeAttribute(HTML.HREF_ATTR, link, null);
				writer.writeAttribute(HTML.CLASS_ATTR, "mobi-button mobi-button-important", null);
				writer.writeText(GetEnhanced.DOWNLOAD, null);
				writer.endElement(null);
			}
			
			writer.endElement(null);//span
		}
    }


}
