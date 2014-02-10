/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.renderkit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.icemobile.component.IAccordion;
import org.icemobile.component.IMobiComponent;
import static org.icemobile.util.HTML.*;
import org.icemobile.util.Utils;

public class AccordionCoreRenderer extends BaseCoreRenderer implements IRenderer{
    
    private static final Logger logger =
            Logger.getLogger(AccordionCoreRenderer.class.toString());
    
    public void encodeBegin(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        
        IAccordion accordion = (IAccordion)component;
        String clientId = accordion.getClientId();
        
        /* write out root tag.  For current incarnation html5 semantic markup is ignored */
        writer.startElement(DIV_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId);
        writer.startElement(DIV_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId+"_acc");
        StringBuilder styleClass = new StringBuilder(IAccordion.ACCORDION_CLASS);
        String userDefinedClass = accordion.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
                styleClass.append(" ").append(userDefinedClass);
        }
        writer.writeAttribute(CLASS_ATTR, styleClass.toString());
        writer.writeAttribute(STYLE_ATTR, accordion.getStyle());
    }
    
    public void encodeEnd(IMobiComponent component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        IAccordion accordion = (IAccordion)component;
        String clientId=accordion.getClientId();
        String openedPaneId = accordion.getOpenedPaneClientId();
        writer.endElement(DIV_ELEM);  //end of _acc div
        writer.startElement(DIV_ELEM, accordion); //start of div for hidden and script
        writer.writeAttribute(CLASS_ATTR, "mobi-hidden");
        writer.writeAttribute(ID_ATTR, clientId+"_hid");
        if (null != openedPaneId){
            super.writeHiddenInput(writer, accordion, openedPaneId);
        }else{
             super.writeHiddenInput(writer,accordion);
        }
        writer.startElement(SPAN_ELEM, accordion);
        writer.writeAttribute(ID_ATTR, clientId + "_script");
        writer.startElement(SCRIPT_ELEM, null);
        writer.writeAttribute(INPUT_TYPE_TEXT, "text/javascript");
        StringBuilder jsCall = new StringBuilder("ice.mobi.accordionController.initClient('");
        jsCall.append(clientId).append("', ");
        jsCall.append("{singleSubmit: true");
        if (null!=openedPaneId){
            jsCall.append(", opened: '").append(openedPaneId).append("'");
        }
        boolean autoheight = accordion.isAutoHeight();
        int hashcode = Utils.generateHashCode(openedPaneId);
        if (null != accordion.getHashVal()){
            hashcode = Utils.generateHashCode(accordion.getHashVal());
        }
        jsCall.append(", hash: ").append(hashcode);
        jsCall.append(", autoHeight: ").append(autoheight);
        if (accordion.isDisabled()){
            jsCall.append(", disabled: ").append(accordion.isDisabled());
        }
        String fixedHeight = accordion.getHeight();
        if (fixedHeight!=null && !autoheight && fixedHeight.length()>0){
            int fixedHtVal = -1;
            StringBuffer numbers = new StringBuffer();
            for(char c : fixedHeight.toCharArray()){
               if(Character.isDigit(c)) {
                   numbers.append(c);
               }
            }
            try {
               fixedHtVal = Integer.valueOf(numbers.toString());
            }   catch (NumberFormatException nfe){
                if (logger.isLoggable(Level.WARNING)){
                    logger.warning(" could not parse int value of fixedHeight="+fixedHeight);
                }
            }
            jsCall.append(", fixedHeight: '").append(fixedHeight).append("'");
         /*   if (accordion.isScrollablePaneContent()){
                jsCall.append(", scp: true");
            }  */
            if (fixedHtVal > 30) {
               jsCall.append(", fHtVal: ").append(fixedHtVal);
            }
        }
        jsCall.append("});");
         //just have to add behaviors if we are going to use them.
      //  writer.writeText(jsCall.toString());
        writer.writeText("setTimeout(function () {"+jsCall.toString()+"}, 200);");
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
        writer.endElement(DIV_ELEM);
        writer.endElement(DIV_ELEM);
    }

}