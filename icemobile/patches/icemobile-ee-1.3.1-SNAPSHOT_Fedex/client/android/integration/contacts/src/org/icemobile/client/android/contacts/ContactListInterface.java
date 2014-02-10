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

package org.icemobile.client.android.contacts;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.BaseColumns;
import org.icemobile.client.android.util.AttributeExtractor;
import org.icemobile.client.android.util.JavascriptInterface;
import org.icemobile.client.android.util.UtilInterface;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.net.Uri;

/**
 *
 */
@TargetApi(8)
    public class ContactListInterface implements JavascriptInterface {

	private Context mContext; 
	private ContentResolver mResolver; 

	private final String SELECT_TYPE_ARG = "select";
	private final String FIELDS_ARG = "fields"; 
	private final String NAME_FIELD = "name"; 
	private final String EMAIL_FIELD = "email"; 
	private final String PHONE_FIELD = "phone"; 
	private UtilInterface mInterface; 
	private boolean mFetchContact; 
	private boolean mFetchEmail; 
	private boolean mFetchPhone;   
	private int selected;
	private int contactCode;

	public ContactListInterface(UtilInterface util, Context context, ContentResolver cr, int contact_code) {
	    mContext = context; 
	    mResolver = cr; 
	    mInterface = util;
	    contactCode = contact_code;
	}

	public void fetchContact(String id, String attr) {

	    Log.d("ICEmobileContainer", "Value of attr string: " + attr);

	    selected = -1;
	    AttributeExtractor attributes = new AttributeExtractor(attr);
	    String selectedFields = attributes.getAttribute( FIELDS_ARG, NAME_FIELD);
	    processFields(selectedFields); 

	    String presetFilter = attributes.getAttribute("pattern", null);
	    Log.d("ICEmobileContainer", "Value of preset filter: " + presetFilter); 
	    Intent i=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

	    ((Activity)mContext).startActivityForResult(i, contactCode);	    
	}

	public void gotContact(Uri contactUri) {
	    if (contactUri != null) {
		uploadContactInfo(contactUri);
	    }
	}

	private void processFields(String fields) { 

	    mFetchContact = false; 
	    mFetchEmail = false; 
	    mFetchPhone = false; 
	    if (fields == null) { 
		return; 
	    }
	    StringTokenizer st = new StringTokenizer(fields, ",");
	    String t;
	    while (st.hasMoreElements()) { 
		t = st.nextToken().trim(); 
		if (EMAIL_FIELD.equalsIgnoreCase(t)) {
		    mFetchEmail = true; 
		} else if (NAME_FIELD.equalsIgnoreCase(t)) { 
		    mFetchContact = true; 
		} else if (PHONE_FIELD.equalsIgnoreCase(t)) { 
		    mFetchPhone = true; 
		}
	    }    	
	}

	/**
	 * Upload the contact info to the hidden fields in the javascript layer via the UtilInterface.loadURL method. 
	 */
	private void uploadContactInfo(Uri contactUri) {
	    String encodedContactList = getEncodedContactList(contactUri); 
	    try { 
		Log.d("ICEcontacts", "Encoded Contact = " + encodedContactList);
		mInterface.loadURL("javascript:ice.addHidden(ice.currentContactId, ice.currentContactId, '" +  encodedContactList + "'); ");
	    } catch (Exception e) { 
		Log.e("ICEmobile", "Exception encoding contact information: " + e); 
	    }
	}
    
	public String getEncodedContactList(Uri contactUri) {

	    String[] projection = { ContactsContract.Contacts._ID,
				    ContactsContract.Contacts.DISPLAY_NAME};
	    Cursor dataCur;
	    String phone;
	    String email;
            Cursor cursor = mResolver.query(contactUri, projection, null, null, null);
            cursor.moveToFirst();
		
	    // Get name and id;
            int column = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String displayName = cursor.getString(column);
            column = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String id = cursor.getString(column);
	    cursor.close();

	    StringBuilder contactList = new StringBuilder(); 
	    if (mFetchContact) { 
		contactList.append( NAME_FIELD + "=" + displayName + "&");
	    }

	    // Get phone;
	    if (mFetchPhone) { 
		projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
		dataCur = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					  projection,ContactsContract.CommonDataKinds.Email.CONTACT_ID + 						     " = " + id, null, null);
		if (dataCur.moveToFirst()) {
		    contactList.append(PHONE_FIELD + "=" + dataCur.getString(dataCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "&");
		    dataCur.close();
		}
	    }

	    // Get email;
	    if (mFetchEmail) { 
		projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };  //should be ContactsContract.CommonDataKinds.Email.ADDRESS but not available until API 11
		dataCur = mResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
					  projection,ContactsContract.CommonDataKinds.Email.CONTACT_ID + 						     " = " + id, null, null);
		if (dataCur.moveToFirst()) {
		    contactList.append(EMAIL_FIELD + "=" + dataCur.getString(dataCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "&");
		    dataCur.close();
		}
	    }

	    Log.d("ICEcontact", "Contact = " + contactList.toString());
	    String encodedContactList = null;
	    try {
		encodedContactList = URLEncoder.encode(contactList.toString(), "utf-8");
	    } catch (Exception e)  {
		Log.e("ICEmobile", "Exception encoding contact information: " + e); 
	    }

	    return encodedContactList;
	}

	class SingleSelectListener implements DialogInterface.OnClickListener {
	    public void onClick(DialogInterface dialog, int which) {
		selected = which;
	    } 
	}

    }
