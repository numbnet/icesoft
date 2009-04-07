package org.icesoft.testclient.event;

import org.icesoft.testclient.client.Client;

/**
 *  
 *
 * @author ICEsoft Technologies, Inc.
 */
public class ButtonPressEvent extends MouseClickEvent {

    protected String buttonValue; 

    // This isn't quite workable yet.

    public String encodeString(Client controller)  {
        StringBuffer returnVal = new StringBuffer(super.encodeEvent(controller));

        return returnVal.toString();

    }
    
}
