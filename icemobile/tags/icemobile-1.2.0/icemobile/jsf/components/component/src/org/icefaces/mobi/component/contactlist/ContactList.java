package org.icefaces.mobi.component.contactlist;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IContactList;
import org.icemobile.util.ClientDescriptor;

public class ContactList extends ContactListBase implements IContactList{


    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    public String getScript(String id, boolean isSX)  {
       return MobiJSFUtils.getICEmobileSXScript(
                "fetchContacts", null, this);
    }

    public String getPostURL()  {
       return MobiJSFUtils.getPostURL();
    }

    public String getSessionId()  {
       return MobiJSFUtils.getSessionIdCookie();
    }

}
