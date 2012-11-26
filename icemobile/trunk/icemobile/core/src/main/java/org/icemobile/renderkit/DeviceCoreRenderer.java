package org.icemobile.renderkit;

import java.io.IOException;

import org.icemobile.component.IDevice;
import org.icemobile.util.ClientDescriptor;

import java.lang.StringBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.icemobile.util.HTML.*;
import javax.servlet.http.HttpSession;

public class DeviceCoreRenderer extends BaseCoreRenderer{
    private static final Logger logger =
            Logger.getLogger(DeviceCoreRenderer.class.toString());
    public void encode(IDevice component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        String clientId = component.getClientId();
        String baseClass = IDevice.CSS_CLASS;
        String comptype = component.getComponentType();
        if (comptype.equals("scan") || (comptype.equals("aug"))){
    	    baseClass= "";
        }
        boolean disabled = component.isDisabled();
        if (component.getStyleClass() !=null){
            baseClass+= component.getStyleClass();
        }
        if (disabled){
            baseClass+=" "+IDevice.DISABLED_STYLE_CLASS;
        }
        ClientDescriptor cd = component.getClient();
        boolean isEnhanced = cd.isICEmobileContainer()  || cd.isSXRegistered();
    //    logger.info("is SX="+cd.isSXRegistered()+" and useCookie="+ component.isUseCookie());
        if (cd.isICEmobileContainer() || (cd.isSXRegistered() && !isJSP)){
            // button element
            writer.startElement(BUTTON_ELEM, component);
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId + "_button");
            writer.writeAttribute(TYPE_ATTR, "button");
            writeStandardAttributes(writer, component, IDevice.CSS_CLASS, IDevice.DISABLED_STYLE_CLASS);
            //default value of unset in params is Integer.MIN_VALUE
            String script = component.getScript(clientId, cd.isSXRegistered());
      //     logger.info("script = "+script);
            writer.writeAttribute(ONCLICK_ATTR, script);
            writer.writeText(component.getButtonLabel());
            writer.endElement(BUTTON_ELEM);
        }
        else if (isJSP && cd.isSXRegistered()){
      //      logger.info(" SX and using Cookie");
            //for iOS until we can store the ICEmobile-SX registration
            //without a session (likely a cookie)  for JSP
            writer.startElement(BUTTON_ELEM, component);
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId+"_button");
            writer.writeAttribute(TYPE_ATTR, "button");
            writer.writeAttribute("data-id", component.getClientId());
        /*   Ithink this is only used for augmented reality..must check */
            if (null != component.getParams())  {
                writer.writeAttribute("data-params", component.getParams());
            }

            if (null != component.getSessionId())  {
                writer.writeAttribute("data-jsessionid", component.getSessionId());
            }
            writeStandardAttributes(writer, component, baseClass, IDevice.DISABLED_STYLE_CLASS);
            writer.writeAttribute("data-command", component.getComponentType());
            writer.writeAttribute(ONCLICK_ATTR, "ice.mobilesx(this)");
            writer.writeText(component.getButtonLabel());
            writer.endElement(BUTTON_ELEM);
        }
        /** use html5 input type of file as default */
        // else  (!isEnhanced || component.isUseNative()){
        else {
      //      logger.info("comptype="+comptype);
            writer.startElement(INPUT_ELEM, component);
            if (comptype.equals("scan") || comptype.equals("aug")){
                writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_TEXT);
            }else {
                writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_FILE);
            }
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId);
            writeStandardAttributes(writer, component, "", "");
            if (comptype.equals("camera")){
                writer.writeAttribute("accept", "image/*");
            }
            if (comptype.equals("camcorder")){
                writer.writeAttribute("accept", "video/*");
            }
            if (comptype.equals("microphone")){
                writer.writeAttribute("accept", "audio/*");
            }
            writer.endElement(INPUT_ELEM);
        }
    }

}
