package org.icesoft.testclient.event;

import org.icesoft.testclient.client.Client;

/**
 * An Event is an attempt to encapsulate the request parameters that are
 * common to a particular components interaction. All Buttons, for example, have
 * the same number of paramters and only the fields based on form and id actually
 * change between instances.  
 */
public interface Event {

    public String encodeEvent(Client controller);  


}
