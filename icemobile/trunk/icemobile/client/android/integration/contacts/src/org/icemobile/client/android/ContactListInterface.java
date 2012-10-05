package org.icemobile.client.android;


import java.net.URI;
import java.util.Arrays;
import java.util.StringTokenizer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import org.icemobile.client.android.AttributeExtractor;
import org.icemobile.client.android.JavascriptInterface;
import org.icemobile.client.android.UtilInterface;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Base64;
import android.util.Log;


/**
 *
 */
@TargetApi(8)
public class ContactListInterface implements JavascriptInterface {


	private Context mContext; 
	private ContentResolver mResolver; 
	
	private boolean[] mCheckedArray;
	private CharSequence[] mContactArray;	
	private TempContactData[] mContacts; 
	
    private final String SELECT_TYPE_ARG = "select";
    private final String FIELDS_ARG = "fields"; 
    private final String CONTACT_FIELD = "contact"; 
    private final String EMAIL_FIELD = "email"; 
    private final String PHONE_FIELD = "phone"; 
    
    private UtilInterface mInterface; 
    private boolean mMultipleSelect;
    private boolean mFetchContact; 
    private boolean mFetchEmail; 
    private boolean mFetchPhone; 
    
    
    private int mSelectedCount; 

    
    
    public ContactListInterface(UtilInterface util, Context context, ContentResolver cr) {

    	mContext = context; 
    	mResolver = cr; 
    	mInterface = util;
    }

    
    
    public void fetchContacts(String id, String attr) {
       
        Log.d("ICEmobileContainer", "Value of attr string: " + attr);
       
        AttributeExtractor attributes = new AttributeExtractor(attr);
        mMultipleSelect = false; 
        String selectionType = attributes.getAttribute(SELECT_TYPE_ARG, null);
        if (selectionType != null) { 
        	mMultipleSelect = "multiple".equalsIgnoreCase(selectionType);
        }
        
        String selectedFields = attributes.getAttribute( FIELDS_ARG, "fields=contact");
        processFields(selectedFields); 
       
        String presetFilter = attributes.getAttribute("pattern", null);
        Log.d("ICEmobileContainer", "Value of preset filter: " + presetFilter); 
        Log.d("IcemobileContainer", "Value of multipleSelect: " + mMultipleSelect);
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);        
        
        Cursor cur = null;
        String[] projection = { ContactsContract.Contacts.DISPLAY_NAME, 
    			ContactsContract.CommonDataKinds.Email.ADDRESS, 
    			ContactsContract.CommonDataKinds.Phone.NUMBER };  
        
        try { 
        if (presetFilter != null) {
        	presetFilter = "%"+ presetFilter + "%";
        	String selection = ContactsContract.Contacts.DISPLAY_NAME + " like ?";        	
        	cur = mResolver.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, selection, new String[] {presetFilter}, null);
        	
        } else { 
        	cur = mResolver.query( ContactsContract.RawContacts.CONTENT_URI, projection, null, null, null);
        }
        
        } catch (Exception e) { 
        	Log.e("ICEmobileContainer", "Exception doing query: " + e); 
        	return;
        }
        
        int rowCount = cur.getCount();
        int rowIdx = 0; 
        
        mContacts = new TempContactData[rowCount]; 
        
        mCheckedArray = new boolean[ rowCount ];
        mContactArray = new CharSequence[ rowCount ];
        mSelectedCount = 0; 

        if ( rowCount > 0 ) { 
        	cur.moveToFirst();
        	
        	int displayCol =  cur.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        	int emailCol = cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        	int phoneCol = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER ); 
        	
        	String contact; 
        	while (rowIdx < rowCount) { 
        		
        		 contact =  cur.getString( displayCol ); 
        		 mContacts[rowIdx] = new TempContactData(); 
        		 mContacts[rowIdx].mEmail = cur.getString( emailCol ); 
        		 mContacts[rowIdx].mPhoneNumber = cur.getString( phoneCol );
        		 mContacts[rowIdx].mPrimaryContact = contact;
        		 
        		 mContactArray[rowIdx] = contact.subSequence(0, contact.length());  
        		 cur.moveToNext();
        		 rowIdx++;
        	}          	
        	cur.close(); 
        }
        
        builder.setTitle("Select from Contact List "); 
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    		}
    	});
        
        if (mMultipleSelect) { 
        
        	builder.setMultiChoiceItems(mContactArray,  mCheckedArray, new MultiSelectListener () );
        	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			uploadContactInfo();
        		}		
        	});
        	
        } else { 
        	mCheckedArray[0] = true; 
        	builder.setSingleChoiceItems(mContactArray, 0 , new SingleSelectListener() );
        	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			uploadContactInfo();
        		}		
        	});
        }
        
        builder.create().show(); 
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
    		} else if (CONTACT_FIELD.equalsIgnoreCase(t)) { 
    			mFetchContact = true; 
    		} else if (PHONE_FIELD.equalsIgnoreCase(t)) { 
    			mFetchPhone = true; 
    		}
    	}    	
    }

    /**
     * Upload the contact info to the hidden fields in the javascript layer via the UtilInterface.loadURL method. 
     */
    private void uploadContactInfo() {
    	
    	StringBuilder contactList = new StringBuilder(); 
    	for (int idx = 0; idx < mContacts.length; idx ++) { 
    		if (mCheckedArray[idx] ) { 
    			mSelectedCount ++; 
    		}
    	}
    	
    	int contactCount = 0; 
    	for (int idx = 0; idx < mContacts.length; idx ++ ) { 
    		if (mCheckedArray[idx]) { 
    			if (mFetchContact) { 
    				contactList.append( mContacts [ idx ].mPrimaryContact);
    			}
    			if (mFetchPhone) { 
    				contactList.append(":").append(mContacts[idx].mPhoneNumber );
    			} 
    			if (mFetchEmail) { 
    				contactList.append(":").append(mContacts[idx].mEmail); 
    			}
    			
    			if (!mMultipleSelect) { 
    				break; 
    			}
    			
    			if (mSelectedCount > 1 && ++contactCount < mSelectedCount) { 
    				contactList.append(", "); 
    			}    		
    		} 
    	}
    	
    	String base64String = Base64.encodeToString(contactList.toString().getBytes(), Base64.DEFAULT);
    	
//    	mInterface.loadURL(
//        	  "javascript:ice.addHidden(ice.currentContactId, ice.currentContactId, '" +  base64String + "'); ");
    	
    	mInterface.loadURL(
          	  "javascript:ice.addHidden(ice.currentContactId, ice.currentContactId, '" +  contactList.toString() + "'); ");
    }
        
       
    class MultiSelectListener implements DialogInterface.OnMultiChoiceClickListener {
    	public void onClick(DialogInterface dialog, int which, boolean isChecked) {    		
    		mCheckedArray[which] = isChecked;    		
    	} 
    }
    
    class SingleSelectListener implements DialogInterface.OnClickListener {
    	public void onClick(DialogInterface dialog, int which) {
    		
    		Arrays.fill(mCheckedArray,  false); 
    		mCheckedArray[which] = true;
    	} 
    }
    
    
    class TempContactData { 
    	public String mPrimaryContact;
    	public String mEmail; 
    	public String mPhoneNumber; 
    }
}
