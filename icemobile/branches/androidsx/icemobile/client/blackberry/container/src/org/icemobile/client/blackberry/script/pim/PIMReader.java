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

package org.icemobile.client.blackberry.script.pim;



import java.util.Hashtable;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMItem;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.blackberry.api.pdap.BlackBerryContactList;
import net.rim.device.api.script.ScriptableFunction;

public class PIMReader extends ScriptableFunction {

	private final String FETCH_NAME = "name"; 
	private final String FETCH_EMAIL = "email"; 
	private final String FETCH_PHONE = "phone"; 

	private ContainerController mContainer;
	private boolean mFetchName; 
	private boolean mFetchPhone;
	private boolean mFetchEmail; 
	private String mClientId; 

	public PIMReader(ContainerController controller) { 
		mContainer = controller;      
	}   

	/**
	 * Read the PIM database 
	 * 
	 */
	public  Object invoke( Object thiz, Object[] args) { 

		Hashtable params = new Hashtable();

		NameValuePair fields = null; 

		if (args.length == 2) {
			Logger.DEBUG("arg[0] = " + args[0]);
			mClientId = (String) args[0];
			
			mFetchName = false; 
			mFetchEmail = false; 
			mFetchPhone = false;
			
			Logger.DEBUG("arg[1] = " + args[1]);
			if (args[1] != null) { 
				params = UploadUtilities.getNameValuePairHash((String)args[1], "=", "&");
				if (params != null) { 
					fields = (NameValuePair) params.get("fields"); 
					if (fields != null) { 
						parseFields(fields); 
					}
				}
			} 
		}
		if ( !(mFetchName | mFetchEmail | mFetchPhone)) { 
			mFetchName = true; 
		}
		
		BlackBerryContactList contactList = null;
		try { 
			contactList = (BlackBerryContactList) PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
			StringBuffer contactsString = new StringBuffer();
			
			PIMItem contact = contactList.choose(); 
			if (contact != null ) { 
				if (mFetchName) { 
					String[] names = contact.getStringArray(Contact.NAME, 0); 
					contactsString.append("name=").append( names[Contact.NAME_FAMILY] )
								  .append(", ").append( names[Contact.NAME_GIVEN]); 
				} else { 
					contactsString.append("name="); 
				}
				if (mFetchEmail) { 
					contactsString.append("&email=").append(contact.getString(Contact.EMAIL, 0));
				} 
				if (mFetchPhone) { 
					contactsString.append("&phone=").append(contact.getString(Contact.TEL, 0)); 
				} 			
				URLEncodedPostData urlEncoder = new URLEncodedPostData("UTF-8", false); 
				urlEncoder.setData(contactsString.toString());
				mContainer.insertHiddenFieldUntyped(mClientId, urlEncoder.toString() );
			} else { 
				contactsString.append("No Contact chosen"); 
			} 

		} catch (Exception e) { 
			Logger.ERROR("ice.PIMReader - Exception reading contact list: " + e); 
		}

		return Boolean.TRUE;
	} 

	private void parseFields(NameValuePair fields) { 

		

		String[] tokens = UploadUtilities.split(fields.getValue(), ",");
		for (int idx = 0; idx < tokens.length; idx ++ ) { 
			if (tokens[idx].equals(FETCH_NAME) ) { 
				mFetchName = true; 
			} else if (tokens[idx].equals(FETCH_EMAIL)) { 
				mFetchEmail = true;   			
			} else if (tokens[idx].equalsIgnoreCase(FETCH_PHONE) ) { 
				mFetchPhone = true; 
			} 
		}
	}
}
