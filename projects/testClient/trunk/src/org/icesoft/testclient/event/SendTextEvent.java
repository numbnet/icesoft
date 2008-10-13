package org.icesoft.testclient.event;

import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

/**
 * This Event sends the loginEvent necessary for logging in by prssing a button.
 * Hopefully, this is generic enough to enable logon to a lot of applications.
 */
public class SendTextEvent extends MouseClickEvent {

    // componentId from superClass contains form and button id. This is necessary
    // for submits
    protected String componentValue;

    protected String sendForm; 

    // The container for the message
    protected String textfieldId;

    protected String baseMessage;

    DateFormat df = DateFormat.getTimeInstance();

    public String encodeEvent(Client controller) {

    // get the click event info and pad it with ours
        StringBuffer data = new StringBuffer( super.encodeEvent(controller) );

        try {

            data.append("&").append(URLEncoder.encode(componentId, "UTF-8")).append("=");

            data.append(URLEncoder.encode(componentValue, "UTF-8")).append("&");


            String message = baseMessage + "-" + df.format(new Date(System.currentTimeMillis()));

            data.append(URLEncoder.encode(sendForm + ":" + textfieldId, "UTF-8")).append("=");
            data.append(URLEncoder.encode(message, "UTF-8")).append("&");

            data.append(URLEncoder.encode(sendForm , "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.focus", "UTF-8")).append("=");
            data.append(URLEncoder.encode(componentId, "UTF-8")).append("&");



        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toString();
    }


    public void setBaseMessage(String baseMessage) {
        this.baseMessage = baseMessage;
    }

    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public void setSendForm(String sendForm) {
        this.sendForm = sendForm;
    }

    public void setTextfieldId(String textfieldId) {
        this.textfieldId = textfieldId;
    }
}
