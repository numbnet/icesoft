package org.icesoft.testclient.event;

import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;
import java.awt.*;

/**
 * MouseClickEvent is an implementation of Event that does all the encoding
 * of parameters for a mouseClick. Construct this object and use the API to
 * define variations on the basic theme such as alt, ctrl, right mouse, etc,
 * and this object uses the Client to take care of encoding itself to the URLConnection.
 *
 * The general purpose ICEsoft parameters
 *
 *
 */
public class MouseClickEvent implements Event {

    protected String componentId = "";

    protected String partialSubmit = "false";

    protected Point p = new Point(0,0);

    protected boolean leftButton = true;
    protected boolean rightButton;
    
    protected boolean altMod;
    protected boolean ctrlMod;
    protected boolean shiftMod;
    protected boolean metaMod;

    protected String eventType = "onclick";

    /**
     * 
     * @param controller
     * @return
     */
    public String encodeEvent(Client controller) {
        StringBuffer data = new StringBuffer(); 

        try {
            data.append(URLEncoder.encode("ice.submit.partial", "UTF-8")).append("=");
            data.append(URLEncoder.encode(partialSubmit, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.target", "UTF-8")).append("=");
            data.append(URLEncoder.encode(componentId, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.captured", "UTF-8")).append("=");
            data.append(URLEncoder.encode(componentId, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.type", "UTF-8")).append("=");
            data.append(URLEncoder.encode(eventType, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.alt", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(altMod), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.ctrl", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(ctrlMod), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.shift", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(shiftMod), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.meta", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(metaMod), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.x", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Integer.toString(p.x), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.y", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Integer.toString(p.y), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.left", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(leftButton), "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.right", "UTF-8")).append("=");
            data.append(URLEncoder.encode(Boolean.toString(rightButton), "UTF-8"));

           

        } catch (Exception e) {
            System.out.println("Exception encoding MouseEvent: " + e);
        }
        return data.toString();
    }


    public void setAltMod(boolean altMod) {
        this.altMod = altMod;
    }

    public void setCtrlMod(boolean ctrlMod) {
        this.ctrlMod = ctrlMod;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setLeftButton(boolean leftButton) {
        this.leftButton = leftButton;
    }

    public void setMetaMod(boolean metaMod) {
        this.metaMod = metaMod;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public void setPartialSubmit(String partialSubmit) {
        this.partialSubmit = partialSubmit;
    }

    public void setRightButton(boolean rightButton) {
        this.rightButton = rightButton;
    }

    public void setShiftMod(boolean shiftMod) {
        this.shiftMod = shiftMod;
    }


    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
}
